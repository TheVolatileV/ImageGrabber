import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import javax.imageio.ImageIO;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.apache.commons.io.FileUtils;

/**
 * Obsolete.
 */
public class grabber
{	
	protected final static String PATH = "C:\\Users\\hurseyen\\workspace\\ImageGrabber\\images";
	protected final static String VALIDATE = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) "
			+ "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36";
	protected final static String SOURCE = "http://www.mangareader.net/";
	protected final static String MANGANAME = "its-my-life";
	public static void main(String[] args) throws IOException
	{
		clean();
		getManga(SOURCE + MANGANAME);
	}
	
	public static void getManga(String url) throws IOException
	{
		Document doc = null;
		Elements imports = null;
		int chapter = 7;
		boolean mangaDone = false;
		try {
			while (!mangaDone)
			{
				getChapter(doc, imports, url, chapter);
				chapter++;
			}
		} catch (HttpStatusException e) {
			mangaDone = true;
		}

	}
	
	public static void getChapter(Document doc, Elements imports, String url, int chapter) throws IOException
	{
		int page = 1;
		url += "/" + chapter + "/" + page;
		boolean chapterDone = false;
		try {
			//clean(chapter);
			while (!chapterDone)
			{
				doc = Jsoup.connect(url).get();
				imports = doc.select("img[name=img]");
				for(Element link : imports)
				{
					System.out.println(link.getAllElements());
				}
				//getPage(imports, chapter, page);
				page++;
				url = SOURCE + MANGANAME + "/" + chapter + "/" + page;
				imports.empty();
			}		
		} catch(HttpStatusException e) {
			throw e;
		}
	}
	
	public static void getPage(Elements imports, int chapter, int page)
	{
		for(Element link : imports) 
		{
			System.out.println(link.attr("abs:src"));
			storeImageIntoFS(link.attr("abs:src"), "page" + page + ".jpg", chapter);
		}
	}
	
	public static void clean() throws IOException
	{
		FileUtils.cleanDirectory(new File(PATH));
	}
	public static void clean(int chapter) throws IOException
	{
		FileUtils.deleteDirectory(new File(PATH + "\\" + MANGANAME + "\\Manga" + chapter + "\\"));
	}
	
	
	public static String storeImageIntoFS(String imageUrl, String fileName, int chapter) {
	    String imagePath = null;
	    try {
	        byte[] bytes = Jsoup.connect(imageUrl)
	        		.userAgent(VALIDATE)
	        				.ignoreContentType(true).execute().bodyAsBytes();
	        ByteBuffer buffer = ByteBuffer.wrap(bytes);
	        String rootTargetDirectory = PATH + "\\" + MANGANAME + "\\" + "Manga" + chapter + "\\";
	        imagePath = rootTargetDirectory + fileName;
	        System.out.println(imagePath);
	        saveByteBufferImage(buffer, rootTargetDirectory, fileName);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    return imagePath;
	}
	
	public static void saveByteBufferImage(ByteBuffer imageDataBytes, String rootTargetDirectory, String savedFileName) {
		   String uploadInputFile = rootTargetDirectory + "/"+savedFileName;

		   File rootTargetDir = new File(rootTargetDirectory);
		   if (!rootTargetDir.exists()) {
		       boolean created = rootTargetDir.mkdirs();
		       if (!created) {
		           System.out.println("Error while creating directory for location- "+rootTargetDirectory);
		       }
		   }
		   String[] fileNameParts = savedFileName.split("\\.");
		   String format = fileNameParts[fileNameParts.length-1];

		   File file = new File(uploadInputFile);
		   BufferedImage bufferedImage;

		   InputStream in = new ByteArrayInputStream(imageDataBytes.array());
		   try {
		       bufferedImage = ImageIO.read(in);
		       ImageIO.write(bufferedImage, format, file);
		   } catch (IOException e) {
		       e.printStackTrace();
		   }
	}
}
