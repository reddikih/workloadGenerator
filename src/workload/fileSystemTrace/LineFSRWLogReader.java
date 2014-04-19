package workload.fileSystemTrace;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import java.io.EOFException;

public class LineFSRWLogReader extends LogReaderAbstract {

	private LineFSRWLog currentLog;

	public LineFSRWLogReader(String input) throws FileNotFoundException {

		super(input);
		currentLog = null;
	}

	/**
     * HP���������
     * <dl>
     * <li> 1: time -- \d+\.\d+
     * <li> 2: pid -- \d+
     * <li> 3: output status -- -*\d+
     * <li> 4: output error -- \d+
     * <li> 5: command name -- \w+
     * <li> 6: command information -- .+
     * </dl>
     */

    private static Pattern pt = Pattern.compile(
      "^(\\d+\\.\\d+):\\s+(\\d+)\\s+(-*\\d+)\\s+(\\d+)\\s(\\w+):\\s+(.+)$");

    /**
     * HP���������
     * <dl>
     * <li> 1: time -- \d+\.\d+
     * <li> 2: pid -- \d+
     * <li> 3: output status -- -*\d+
     * <li> 4: output error -- \d+
     * <li> 5: request type -- [READ|WRITE]
     * <li> 6: device -- \w+
     * <li> 7: file descriptor -- \d+
     * <li> 8: bytes -- \d+
     * </dl>
     */
    private static Pattern ptREAD = Pattern.compile(
    "^(\\d+\\.\\d+):\\s+(\\d+)\\s+(-*\\d+)\\s+(\\d+)\\s+(READ):\\s.\\w+\\s+(\\w+)\\S+\\s+\\w+\\s+" +
    "(\\d+)\\S+\\s+\\w+\\s+(\\d+)$");

    private static Pattern ptWRITE = Pattern.compile(
    "^(\\d+\\.\\d+):\\s+(\\d+)\\s+(-*\\d+)\\s+(\\d+)\\s+(WRITE):\\s.\\w+\\s+(\\w+)\\S+\\s+\\w+\\s+" +
    "(\\d+)\\S+\\s+\\w+\\s+(\\d+)$");

    private static Pattern ptREADorWRITE = Pattern.compile(
    	    "^(\\d+\\.\\d+):\\s+(\\d+)\\s+(-*\\d+)\\s+(\\d+)\\s+(READ|WRITE):\\s.\\w+\\s+(\\w+)\\S+\\s+\\w+\\s+" +
    	    "(\\d+)\\S+\\s+\\w+\\s+(\\d+)$");

    private static Pattern vforkpt = Pattern.compile(
    	"^(\\d+\\.\\d+):\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)\\s+VFORK:\\s+$");

    @Override
	protected Pattern getPattern() {
		return ptREADorWRITE;
	}
    public LineFSRWLog getNext() throws Exception{

    	Matcher mt = checkMatch(currentLogLine);
        if(mt != null){
        	currentLog = makeLog(mt);

        	//currentLog.printLine(currentLog);
            return currentLog;
        }else{
        	return null;
        }

    }

    public LineFSRWLog makeLog(Matcher mt){
        return new LineFSRWLog(
                mt.group(1) , //arrival time
                mt.group(2), //requestCall_id
                mt.group(3), //output status
                mt.group(4), //output error
                mt.group(5), //device
                mt.group(6), //file descriptor
                mt.group(7), //bytes
                mt.group(8)// function information
        );
    }

    public static void main(String[] args) {
    	String str = "0.000092: 27133 83 0 WRITE:	dev 0/000000, FD 1, bytes 83";
    	System.out.println(ptREAD.matcher(str).matches() || ptWRITE.matcher(str).matches());
    	System.out.println(ptREADorWRITE.matcher(str).matches());

    }

}
