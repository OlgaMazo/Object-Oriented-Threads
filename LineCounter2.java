import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.concurrent.Callable;

/**
 * This class represents the thread (callable) that calculates the line number of the file.
 * @author Daniel and Olga
 */
public class LineCounter2 implements Callable<Integer>{
	
	private String fileName;

	/**
	 * LineCounter2 constractor
	 * @param fileName - the file name
	 */
	public LineCounter2(String fileName) {
		this.fileName = fileName;
	}

	/**
	 *call
	 *This function is working when we turn on the thread
	 *It calculate and return how many word in a file.
	 *source - https://stackoverflow.com/questions/453018/number-of-lines-in-a-file-in-java
	 *@return - how many words in the file 
	 */
	public Integer call() throws Exception {
		
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
