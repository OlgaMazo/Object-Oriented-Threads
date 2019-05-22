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
 * In this class we can do some actions on files: create files, delete files,
 * count the number of rows in each file - this is done by threads
 * @author Olga and Daniel
 */
public class Ex3B {

	/**
	 * createFiles 
	 * This function creates files, the number of files will be according to the number it received.
	 * @param n - how many files
	 * @return - array of String's with the file's names
	 */
	public static String[] createFiles(int n) {

		String[] filesNames = new String[n];
		Random r = new Random(123);
		int numLines = 0;
		String fileName = "File_"; //the files name will be File_i

		//create n files
		for (int i = 1; i <= n; i++) {

			//dragging the number of rows in a file
			numLines = r.nextInt(1000);
			fileName = fileName + i;
			File file = new File(fileName);
			filesNames[i - 1] = fileName;

			try {
				if (!file.exists()) {
					file.createNewFile(); //create the file
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
	 * deleteFiles 
	 * This function receives an array with file names and deletes them.
	 * @param fileNames - the file's names that will be deleted
	 */
	public static void deleteFiles(String[] fileNames) {

		//create routing of the folder in which the files are stored
		final String dir = System.getProperty("user.dir");

		//go through all the files and delete them
		for (int i = 0; i < fileNames.length; i++) {
			File file = new File(dir + "\\" + fileNames[i]);
			file.delete();
		}
	}

	/**
	 * countLinesThreads 
	 * The function receives multiple files as an argument. The
	 * function creates files, triggers for each file the thread and prints the
	 * total number of rows in all files. It also prints the runtime of the threads
	 * and eventually deletes all the files
	 * @param numFiles - the number of files we need to create
	 */
	public static void countLinesThreads(int numFiles) {

		String[] filesNames = createFiles(numFiles); //create numFiles
		int totalRows = 0;
		long totalTime = 0;

		//go through all the files
		for (int i = 0; i < filesNames.length; i++) {

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
			} catch (InterruptedException e) {
			}

			//sum all the rows
			totalRows += lc.get_rows();

			//total running time of the threads
			totalTime = totalTime + (endTime - startTime);
		}

		System.out.println("Total running time (countLinesThreads): " + totalTime + " nano-seconds");
		System.out.println("Total rows in all the files: " + totalRows);

		//delete all the files
		deleteFiles(filesNames);
	}

	/**
	 * countLinesThreadPool
	 * The function creates files, triggers for each file the thread that C=created in TreadPool 
	 * and prints the run time and total number of rows in all files.
	 * @param num - the files number
	 */
	public static void countLinesThreadPool(int num) {

		//create numFiles
		String[] filesNames = createFiles(num);
		int totalRows = 0;
		long totalTime = 0;

		//with this object we have static services to create Thread repositories
		ExecutorService executor = Executors.newFixedThreadPool(num);

		//go through all the files
		for (int i = 0; i < filesNames.length; i++) {

			//the start time of the thread
			long startTime = System.nanoTime();

			//create LineCounter2 object to calculate how many rows in the file
			LineCounter2 lc = new LineCounter2(filesNames[i]);

			//perform "call" function, the answer is to be saved in the future object
			Future<Integer> future = executor.submit(lc);

			//the end time of the thread
			long endTime = System.nanoTime();

			//sum all the rows
			try {
				totalRows = totalRows + future.get();
			} catch (Exception e) {
				e.printStackTrace();
			}

			//total running time of the threads
			totalTime = totalTime + (endTime - startTime);
		}

		System.out.println("Total running time (countLinesThreadPool): " + totalTime + " nano-seconds");
		System.out.println("Total rows in all the files: " + totalRows);

		//delete all the files
		deleteFiles(filesNames);

		//shutdown the executor service object
		executor.shutdown();
	}

	/**
	 * countLinesOneProcess
	 * The function creates files, reads files one by one, prints the total number of rows and the run time without using threads.
	 * @param numFiles -  - the files number
	 */
	public static void countLinesOneProcess(int numFiles) {

		//create numFiles
		String[] filesNames = createFiles(numFiles);
		int totalRows = 0;
		long totalTime = 0;
		int rows = 0;

		//go through all the files
		for (int i = 0; i < filesNames.length; i++) {

			//the start time of the thread
			long startTime = System.nanoTime();

			//calculate the file rows by "count_rows" function
			try {
				rows = count_rows(filesNames[i]);
			} catch (IOException e) {
				e.printStackTrace();
			}

			//the end time of the thread
			long endTime = System.nanoTime();

			//sum all the rows
			totalRows = totalRows + rows;

			//total running time of the threads
			totalTime = totalTime + (endTime - startTime);
		}

		System.out
				.println("Total running time of the threads (countLinesOneProcess): " + totalTime + " nano-seconds");
		System.out.println("Total rows in all the files: " + totalRows);

		//delete all the files
		deleteFiles(filesNames);
	}

	/**
	 * count_rows
	 * count the file rows
	 * @param fileName - the file name
	 * @return the file rows
	 * @throws IOException - input/output exception
	 */
	private static int count_rows(String fileName) throws IOException {

		//create input stream for the file in order to read the data from it
		InputStream is = new BufferedInputStream(new FileInputStream(fileName));

		try {
			byte[] c = new byte[1024]; //byte array in order to read the chars in the lines
			int count = 0;
			int readChars = 0;
			boolean empty = true; //if the file is empty

			//while the file has more lines
			while ((readChars = is.read(c)) != -1) {
				empty = false;

				//read this line until we reach a line drop
				for (int i = 0; i < readChars; ++i) {
					if (c[i] == '\n') {
						++count;
					}
				}
			}

			//if there is one line in the file
			if (count == 0 && !empty) {
				return 1;
			} else {
				return count;
			}

			//close the input stream
		} finally {
			is.close();
		}
	}
}
