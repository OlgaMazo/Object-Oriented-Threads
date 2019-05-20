import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.util.Random;

public class Ex3B {

	public static void main(String[] args) {
		
		countLinesThreads(6);
		
//		String[] filesNames1 = createFiles(4);
//		
//		for(int i=0; i<filesNames1.length; i++) {
//			System.out.println(filesNames1[i]);
//		}
//		
//		deleteFiles(filesNames1);
	}

	public static String[] createFiles(int n) {

		String[] filesNames = new String[n];
		Random r = new Random(123);
		int numLines = 0;
		String fileName = "File_";

		for (int i = 1; i <= n; i++) {

			numLines = r.nextInt(1000);
			fileName = fileName + i;
			File file = new File(fileName);
			filesNames[i-1] = fileName;

			try {

				if (!file.exists()) {
					file.createNewFile();
				}

				PrintWriter pw = new PrintWriter(file);

				for (int j = 0; j < numLines; j++) {
					pw.println("Hello World");
				}
				
				pw.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
			
			fileName = "File_";
		}
		return filesNames;
	}
	
	public static void deleteFiles(String[] fileNames) {
		
		final String dir = System.getProperty("user.dir");
		
		for(int i=0; i<fileNames.length; i++) {
			File file = new File(dir + "\\" + fileNames[i]);		}
	}
	
	public static void countLinesThreads(int numFiles) {
		
		String[] filesNames = createFiles(numFiles);
		int totalRows = 0;
		
		
		
		for(int i=0; i<filesNames.length; i++) {
			
			long startTime = System.nanoTime();
			LineCounter lc = new LineCounter(filesNames[i]);
			lc.start();
			long endTime = System.nanoTime();
			
			try {
				lc.join();
			} catch (InterruptedException e) {}
			
			totalRows = totalRows + lc.rows;

			System.out.println("Thread running time: " + (endTime-startTime));
			}
		
		System.out.println("Total rows in all the files: " + totalRows);
		
		deleteFiles(filesNames);
	}
}
