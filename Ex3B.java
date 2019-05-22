import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

/**
 * Ex3B
 * In this class we can do some actions on files: create files, delete files, 
 * count the number of rows in each file - this is done by threads
 * @author Olga and Daniel
 */
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

	/**
	 *createFiles
	 *This function creates files. The number of files will be according to the number it received.
	 * @param n - how many files
	 * @return - array of String's with the file's names
	 */
	public static String[] createFiles(int n) {

		String[] filesNames = new String[n];
		Random r = new Random(123);
		int numLines = 0;
		//the files name will be File_i
		String fileName = "File_";

		//creating n files
		for (int i = 1; i <= n; i++) {

			//dragging the number of rows in a file
			numLines = r.nextInt(1000);
			fileName = fileName + i;			
			File file = new File(fileName);
			filesNames[i-1] = fileName;

			try {

				if (!file.exists()) {
					//create the file
					file.createNewFile();
				}

				//with this variable we can write in the file
				PrintWriter pw = new PrintWriter(file);

				//go through the entire file and write in each line "Hello World"
				for (int j = 0; j < numLines; j++) {
					pw.println("Hello World");
				}
				
				//close the print writer
				pw.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
			
			fileName = "File_";
		}
		//return array with the file's name
		return filesNames;
	}
	
	/**
	 *deleteFiles
	 *This function receives an array with file names and deletes them
	 *@param fileNames - the file's names that will be deleted
	 */
	public static void deleteFiles(String[] fileNames) {
		
		//create routing of the folder in which the files are stored
		final String dir = System.getProperty("user.dir");
		
		//go through all the files and delete them
		for(int i=0; i<fileNames.length; i++) {
			File file = new File(dir + "\\" + fileNames[i]);
			file.delete();
		}
	}
	
	/**
	 *countLinesThreads
	 *The function receives multiple files as an argument. The function creates files, triggers for each file the
	 *thread and prints the total number of rows in all files. It also prints the runtime of the threads and eventually deletes all the files
	 *@param numFiles - the number of files we need to create
	 */
	public static void countLinesThreads(int numFiles) {
		
		//create numFiles
		String[] filesNames = createFiles(numFiles);
		int totalRows = 0;
		
		//go through all the files
		for(int i=0; i<filesNames.length; i++) {
		
			//the start time of the thread
			long startTime = System.nanoTime();
			//create LineCounter to calculate how many rows in the file
			LineCounter lc = new LineCounter(filesNames[i]);
			//start the thread
			lc.start();
			//the end time of the thread
			long endTime = System.nanoTime();
			
			try {
				lc.join();
			} catch (InterruptedException e) {}
			
			//sum all the rows
			totalRows += lc.rows;

			System.out.println("Thread running time: " + (endTime-startTime));
			}
		
		System.out.println("Total rows in all the files: " + totalRows);
		
		//delete all the files
		deleteFiles(filesNames);
	}
}
