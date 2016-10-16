import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.jsoup.Jsoup;

/**
 * This class accepts a page object and downloads the image at the link to the file system.
 */
public class FileMaker {
	private static String VALIDATOR = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) "
			+ "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36";
    protected final static String PATH = "/Users/elijahhursey/Dropbox/workspace/ImageGrabber/images/";
	public static String storeImageIntoFS(Page page) {
	    String imagePath = null;
	    try {
	        byte[] bytes = Jsoup.connect(page.getUrl())
	        		.userAgent(VALIDATOR)
	        				.ignoreContentType(true).execute().bodyAsBytes();
	        ByteBuffer buffer = ByteBuffer.wrap(bytes);
	        String rootTargetDirectory = PATH + page.getMangaName() + "/"
                    + "Chapter" + page.getChapterNum();
	        imagePath = rootTargetDirectory + "/Page_" + page.getPage() + ".jpg";
	        System.out.println(imagePath);
	        saveByteBufferImage(buffer, rootTargetDirectory, "Page_" + page.getPage() + ".jpg");
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
