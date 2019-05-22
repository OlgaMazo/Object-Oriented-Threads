import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * This class represents the thread that calculates the line number of the file.
 * @author Daniel and Olga
 */
public class LineCounter extends Thread {

	private String fileName;
	private int rows = 0;

	/**
	 * LineCounter constractor
	 * @param fileName - the file name
	 */
	public LineCounter(String fileName) {
		this.fileName = fileName;
	}
	
	/**
	 * get_rows
	 * @return the number of rows in a file
	 */
	public int get_rows() {
		return rows;
	}

	/**
	 * run
	 * This function will be enabled at the beginning of the thread (start command).
	 */
	public void run() {
		try {
			num_of_rows();
		} catch (IOException e) {
		}
	}

	/**
	 * num_of_rows
	 * Calculates the number of rows in the file.
	 * source: https://stackoverflow.com/questions/453018/number-of-lines-in-a-file-in-java
	 * @throws IOException - I/O exception
	 */
	private void num_of_rows() throws IOException {

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
				rows = 1;
			} else {
				rows = count;
			}

			//close the input stream
		} finally {
			is.close();
		}
	}
}
