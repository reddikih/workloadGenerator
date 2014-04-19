package workload.fileSystemTrace;

import java.io.FileNotFoundException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class LineFSLogReader extends LogReaderAbstract {

	private LineFSLog currentLog;

	private String currentLogInString;

	private double timeOffset;

	public LineFSLogReader(String line) throws FileNotFoundException {

		super(line);
		currentLog = null;
		this.timeOffset = 0.0;
		currentLogInString = line;
	}

	public LineFSLogReader(String input, double timeOffset) throws FileNotFoundException {

		super(input);
		currentLog = null;
		this.timeOffset = timeOffset;
	}

	/**
     * HP
     * <dl>
     * <li> 1: time -- \d+\.\d+
     * <li> 2: request call id -- \d+
     * <li> 3: output status -- -*\d+
     * <li> 4: output error -- \d+
     * <li> 5: request name -- \w+
     * <li> 6: request detail -- .*     //For VFORK COMMAND
     * </dl>
     */

    public static Pattern pt = Pattern.compile(
      "^(\\d+\\.\\d+):\\s+(\\d+)\\s+(-*\\d+)\\s+(\\d+)\\s(\\w+):\\s+(.*)$");


    private static Pattern vforkpt = Pattern.compile(
    	"^(\\d+\\.\\d+):\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)\\s+VFORK:\\s+$");

    public LineFSLog makeLog(Matcher mt){

    	double arrivalTimeUpdate = Double.parseDouble(mt.group(1)) + timeOffset;

        return new LineFSLog(
                Double.toString(arrivalTimeUpdate), //arrival time
                mt.group(2), //requestCall_id
                mt.group(3), //output status
                mt.group(4), //output error
                mt.group(5), //request type
                mt.group(6) //request detail

        );
    }
    @Override
	protected Pattern getPattern() {
		return pt;
	}
    public LineFSLog getNext() throws Exception{

    	//Matcher mt = grepNext();
    	Matcher mt = checkMatch(currentLogInString);

    	if(mt != null){
           	currentLog = makeLog(mt);
           	return currentLog;
    	}
         //  	currentLog.printLine(currentLog);

        	//return currentLog;

    	else return null;
    }

    public LineFSVForkLog getVFORK() {
    	Matcher mt = vforkpt.matcher(currentLogLine);
    	if(mt.find()){
    		return new LineFSVForkLog(
    				mt.group(1),
    				mt.group(2),
    				mt.group(3),
    				"VFORK");

    	} else {
    		return null;
    	}
    }

    public static void main(String[] args) {
    	String str = "0.000092: 27133 11549 0 VFORK: ";
    	System.out.println(pt.matcher(str).matches());
    }

}
