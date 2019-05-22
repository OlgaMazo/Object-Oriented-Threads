import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * LineCounter
 * This class represents a Thread that count how many rows there are in one file
 * @author Olga and Daniel
 */
public class LineCounter extends Thread {
	
	private String file_name;
	private int rows = 0;
	
	/**
	 * LineCounter
	 * constructor that receive file's name
	 * @param file_name - file's name 
	 */
	public LineCounter(String file_name) {
		
		this.file_name = file_name;
	}
	
	/**
	 * getRows
	 * @return - how many rows
	 */
	public int getRows() {
		return rows;
	}
	
	/**
	 * run
	 * This function will be enabled at the beginning of the thread (start command)
	 * It calls to function that calculate how many rows in the file
	 */
	public void run() {
		try {
			num_of_rows();
		} catch (IOException e) {
		}
	}
	
	/**
	 * num_of_rows
	 * This function will be activated when the Thread is initiated, it calculate how many rows there are in
	 * this file
	 * source - https://stackoverflow.com/questions/453018/number-of-lines-in-a-file-in-java
	 * @throws IOException - I/O exception
	 */
	private void num_of_rows() throws IOException {
		
		//create an object that represent a stream in order to read the data from the file
		InputStream is = new BufferedInputStream(new FileInputStream(file_name));
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
	        if(count == 0 && !empty)
	        	rows = 1;
	        else
	        	rows = count;
	    //close the file    
	    } finally {
	        is.close();
	    }
	}
}
