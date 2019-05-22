import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.concurrent.Callable;

/**
 *LineCounter2
 *This class implements the interface Callable.
 *When the thread start and the function call is on - it calculate and returns how many rows there are in a file
 * @author Olga and Daniel
 */
public class LineCounter2 implements Callable <Integer>{
	
	private String file_name;
	
	/**
	 * LineCounter2
	 * constructor that receive file's name
	 * @param file_name - file's name 
	 */
	public LineCounter2(String file_name) {
		
		this.file_name = file_name;
	}

	/**
	 *call
	 *This function is working when we turn on the thread
	 *It calculate and return how many word in a file.
	 *source - https://stackoverflow.com/questions/453018/number-of-lines-in-a-file-in-java
	 *@return - how many words in the file 
	 */
	public Integer call() throws Exception {
		
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
	        return (count == 0 && !empty) ? 1 : count;
	        
	    } finally {
	    	//close the file
	        is.close();
	    }
	}
	
}
