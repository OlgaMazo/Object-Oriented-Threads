import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Ex3B
 * In this class we can do some actions on files: create files, delete files, 
 * count the number of rows in each file - this is done by threads
 * @author Olga and Daniel
 */
public class Ex3B {
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
	 *The function receives several files to be created. The function creates files, triggers for each file the
	 *thread and prints the total number of rows in all files. It also prints the runtime of the threads and eventually deletes all the files
	 *@param numFiles - the number of files we need to create
	 */
	public static void countLinesThreads(int numFiles) {
		
		//create numFiles
		String[] filesNames = createFiles(numFiles);
		int totalRows = 0;
		long totalTime = 0;
		
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
			
			//total time
			totalTime = totalTime + (endTime-startTime);
			
			try {
				lc.join();
			} catch (InterruptedException e) {}
			
			//sum all the rows
			totalRows += lc.getRows();
			}
		
		System.out.println("Total run time of the Threads in seconds: " + totalTime/1000);
		System.out.println("Total rows in all the files: " + totalRows);
		
		//delete all the files
		deleteFiles(filesNames);
	}
	
	/**
	 *countLinesThreadPool
	 *The function receives several files to be created, triggering for each file the thread created in TreadPool
	 *and prints the total number of rows in all files. It also prints the runtime of the threads and eventually deletes all the files
	 *@param num - the number of files we need to create
	 */
	public static void countLinesThreadPool(int num) {
		
		//create num files
		String[] filesNames = createFiles(num);
		int totalRows = 0;
		long totalTime = 0;
		//get ExecutorService from Executors utility class, thread pool
		ExecutorService executor = Executors.newFixedThreadPool(num);
		
		//go through all the files
		for(int i=0; i<filesNames.length; i++) {

			//start time
			long startTime = System.nanoTime();
			LineCounter2 lc = new LineCounter2(filesNames[i]);
			//the number of rows will be in future
			Future<Integer> future = executor.submit(lc);
			//end time
			long endTime = System.nanoTime();	
			//total time
			totalTime = totalTime + (endTime-startTime);
						
			try {
				totalRows += future.get();
			} catch (Exception e) {
				
				e.printStackTrace();
			} 
		}
		
		System.out.println("Total run time of the Threads in seconds: " + totalTime/1000);
		System.out.println("Total rows in all the files: " + totalRows);
		
		//delete all files
		deleteFiles(filesNames);
		//stop the thread
		executor.shutdown();
		}
	
	
	/**
	 *countLinesOneProcess
	 *The function receives a number of files to create, creates, calculates and prints a few lines in total
	 *It also prints the runtime of the threads and eventually deletes all the files
	 *@param numFiles - the number of files we need to create
	 */
	public static void countLinesOneProcess(int numFiles) {
		
		//create numFiles
		String[] filesNames = createFiles(numFiles);
		int totalRows = 0;
		long totalTime = 0;
		int rows = 0;
		
		//go through all the files
		for(int i=0; i<filesNames.length; i++) {
			
			//start time
			long startTime = System.nanoTime();
			try {
				//check how many rows there are in the file
				rows = numOfRows(filesNames[i]);
			} catch (IOException e) {
			}
			
			//end time
			long endTime = System.nanoTime();
			totalTime = totalTime + (endTime-startTime);
			totalRows += rows;	
		}
		
		System.out.println("Total run time of the Threads in seconds: " + totalTime/1000);
		System.out.println("Total rows in all the files: " + totalRows);
		
		//delete all files
		deleteFiles(filesNames);
	}
	
	/**
	 * numOfRows
	 * This function calculate how many rows there are in a file
	 * source - https://stackoverflow.com/questions/453018/number-of-lines-in-a-file-in-java
	 * @param filesNames - the file that we check
	 * @throws IOException - I/O exception
	 */
	private static int numOfRows(String filesNames) throws IOException {
		//create an object that represent a stream in order to read the data from the file
	    InputStream is = new BufferedInputStream(new FileInputStream(filesNames));
	    try {
	    	//byte array in order to read the chars from lines
	        byte[] c = new byte[1024];
	        int count = 0;
	        int readChars = 0;
	        //flag for the first line
	        boolean empty = true;
	        
	        //while the file has more lines
	        while ((readChars = is.read(c)) != -1) {
	            empty = false;
	            //go over all the line
	            for (int i = 0; i < readChars; ++i) {
	            	//if we got the end of the line
	                if (c[i] == '\n') {
	                    ++count;
	                }
	            }
	        }     
	        return (count == 0 && !empty) ? 1 : count;
	        
	    } finally {
	    	//close the file
	        is.close();
	    }
	}
}
