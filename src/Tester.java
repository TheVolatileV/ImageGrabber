import java.io.IOException;
import java.util.Scanner;


public class Tester {

	
	public static void main(String[] args) throws IOException
	{
		Scanner scanner = new Scanner(System.in);
		System.out.println("Please enter the manga's name. (with hyphens as in the link on www.mangareader.net");
		String name = scanner.nextLine();
		System.out.println("Please wait.");
		MangareaderDownloader g = new MangareaderDownloader(name);
	}
}
