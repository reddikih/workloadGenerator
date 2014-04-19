package workload.fileSystemTrace;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.io.EOFException;


public abstract class LogReaderAbstract {

	protected int lineNo;

	    protected String currentLogLine = "";

	    protected boolean matched = false;

		/**
		 * @throws FileNotFoundException
		 */
	    public LogReaderAbstract(String line) throws FileNotFoundException {
			super();

		}


	    /**
	     * @return
	     * @throws IOException
	     * @throws Exception
	     */

	    protected Matcher checkMatch(String line) throws IOException{
    		//System.out.println("start checkMatch");
    		//String line = br.readLine();
    		//System.out.println("line: "+ line);
	        if (line !=null) {

	        	this.currentLogLine = line;

	        	lineNo++;						//#line matched

		         Matcher mt = getPattern().matcher(line);

		         if(mt.find()){
		        	 matched = true;
		        	 //System.out.println("end checkMatch true");
		        	 return mt;

		         }

	        } else {
	        	matched = false;
	        	//System.out.println("end checkMatch false");
	        	return null;
	        }



	        if(matched == false){
	    		System.err.println("unmatch line is found(" + lineNo + "): " + currentLogLine);
	    		System.out.println(currentLogLine);

	    	}

	        return null;

    }


	    public void close() throws Exception{
	          //br.close();
	    }

	    /**
	     * @return
	     */
	    protected abstract Pattern getPattern();
}
