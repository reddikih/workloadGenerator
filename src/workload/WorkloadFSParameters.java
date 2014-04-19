package workload;

public class WorkloadFSParameters {

	public static final String DBHOST = "jdbc:postgresql://delphinium/file_system_trace_HP";

	public static final String BEGIN_DAY = "2000-12-02";

	public static final String BEGIN_HOUR = "00";

	public static final String END_DAY = "2000-12-02";

	public static final String END_HOUR = "03";					//have to be greater than begin_hour

	/*
	* @TODO: Make TABLENAME flexible change corresponding to input file
	*/
	public static String TABLENAME = "fsTrace_raw_20001202";

	public static String TABLENAME_TEST = "HPLogtest";

	public static String OUT_DIR = "./";

	public static String IN_DIR = "./resources/Trace/FileTrace/trace/raw";

	public static String OUT_FILE_NAME = "WorkloadFSTraceFrom".concat(BEGIN_DAY).concat("-").
	concat(BEGIN_HOUR)+ "To".concat(END_DAY).concat("-").concat(END_HOUR)+".csv";

	public static String OUT_FILE_NAME_DATAID = "DataIDListForWorkloadFSTraceFrom".concat(BEGIN_DAY).
	concat("-").concat(BEGIN_HOUR)+ "To".concat(END_DAY).concat("-").concat(END_HOUR)+".csv";

	public static long FILESIZE = 32 * 1024; //32KB
}
