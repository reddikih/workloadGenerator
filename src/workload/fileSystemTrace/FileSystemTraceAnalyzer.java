package workload.fileSystemTrace;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.FileReader;
import java.io.Reader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import workload.WorkloadFSParameters;
import workload.fileSystemTrace.LineFSLogReader;

public class FileSystemTraceAnalyzer implements
		FileSystemTraceAnalyzerInterface {

	private Connection connection;

	private PreparedStatement pst;

	private int insertCount = 0;

	//private String tableName = WorkloadFSParameters.TABLENAME_TEST;

	private String tableName = WorkloadFSParameters.TABLENAME;

	private String[] filenames = null;

	private String hour;

	private String day;

	public FileSystemTraceAnalyzer () throws SQLException, ClassNotFoundException {
		initDB();
	}

	public FileSystemTraceAnalyzer (String[] filenames) throws SQLException, ClassNotFoundException {
		this.filenames = filenames;
		initDB();
	}
	/*
	 * File name: "./2000-11-30-00%3A00%3A01.fs"
	 * */
	private static Pattern ptFileName = Pattern.compile(
    "^.+(\\d{4}-\\d{2}-\\d{2}+)-(\\d{2}+)\\%3A\\d{2}\\%3A\\d.+$");
	//
	private static String makeDay(String fileName, Matcher mt) {
		//System.out.println(ptFileName.matcher(fileName).matches());
		return mt.group(1);
	}

	private String makeHour(String fileName, Matcher mt) {
		//System.out.println(fileName);
		return mt.group(2);
	}


	private String LineFSLogToString (LineFSRWLog line) {
		String tmp = new String();

		String lineFSLogToString_bf = tmp.concat(line.getArrivalTime() + " ")
		.concat(line.getRequestCall_id() + " ")
		.concat(line.getOutputStatus() + " ")
		.concat(line.getOutputError() + " ")
		.concat(line.getRequestType() + " ")
		.concat(line.getDevice()+ " ")
		.concat(line.getFileDescriptor() + " ")
		.concat(line.getBytes() + "\n");

		return lineFSLogToString_bf;

	}


		@Override
	public void rawFSTraceReader(String fileName) throws Exception {
		// TODO ��ư�������줿�᥽�åɡ�������

			Matcher mtFileName = ptFileName.matcher(fileName);

			if (mtFileName.matches()) {
				day = makeDay(fileName, mtFileName);

				hour = makeHour(fileName, mtFileName);
			} else {
				System.err.println("File name not match");
			}

			BufferedReader br = new BufferedReader(new FileReader (fileName));
			String line ;
			int readLine = 0;
			while ((line=br.readLine())!=null) {
				//System.out.println(line);

				LineFSLogReader lfslr = new LineFSLogReader(fileName);
				Matcher mt = lfslr.checkMatch(line);
				if (mt!=null) {
					LineFSLog lfsl = lfslr.makeLog(mt);
					System.out.println(".");
					storeToDB(fileName, lfsl);
					readLine++;
				} else {
					continue;
				}

				//lfsl.printLine(lfsl);

			}
			try {
				pst.executeBatch();
			} catch (SQLException sqle) {
				sqle.getNextException();
			}
			//System.out.println(readLine);
			br.close();
	}

	@Override
	public void storeToDB(String fileName, LineFSLog lfsl) throws Exception {

		DataBaseInserter dbi = new DataBaseInserter(
				fileName,
				tableName,
				day,
				hour,
				lfsl.getArrivalTime(),
				lfsl.getRequestCall_id(),
				lfsl.getOutputStatus(),
				lfsl.getOutputError(),
				lfsl.getRequestType(),
				lfsl.getRequestDetail());


		//dbi.insertDatabaseNormal(connection);
		//System.out.println(pst.toString());

		insertCount++;

		//System.out.println("Insert " + insertCount);
		try {
			if(insertCount % 10000 == 0){
				pst.executeBatch();
				System.out.println("current " + insertCount + " lines are batched.");
			}
		} catch (SQLException sqle) {
			sqle.getNextException();
		}
		dbi.insertDatabase(pst);
	}

	private void initDB() throws SQLException, ClassNotFoundException {
		this.connection = setupDB();
		DataBaseInserter.createTable(connection);
		this.pst = connection.prepareStatement(DataBaseInserter.getInsertSQL(tableName));


	}
	public Connection setupDB() throws SQLException, ClassNotFoundException{
    	Connection connection = null;

			// open connection to db
			Class.forName("org.postgresql.Driver");
			//connection = DriverManager.getConnection("jdbc:postgresql://cattleya/disk_simulator",
			//		"hanhlh",
			//		"");
			connection = DriverManager.getConnection(WorkloadFSParameters.DBHOST,
					"hanhlh",
					"");

			//connection.setAutoCommit(false);

			return connection;
	}

	public static void main(String[] args) throws Exception {
		// TODO ��ư�������줿�᥽�åɡ�������
		String[] ftrace = new String[24];
		String dir = WorkloadFSParameters.IN_DIR;
		/*
		ftrace[0] = "/2000-11-30/2000-11-30-00%3A00%3A01.fs";
		ftrace[1] = "/2000-11-30/2000-11-30-01%3A00%3A00.fs";
		ftrace[2] = "/2000-11-30/2000-11-30-02%3A00%3A01.fs";
		ftrace[3] = "/2000-11-30/2000-11-30-03%3A00%3A00.fs";
		ftrace[4] = "/2000-11-30/2000-11-30-04%3A00%3A01.fs";
		ftrace[5] = "/2000-11-30/2000-11-30-05%3A00%3A00.fs";
		ftrace[6] = "/2000-11-30/2000-11-30-06%3A00%3A00.fs";
		ftrace[7] = "/2000-11-30/2000-11-30-07%3A00%3A00.fs";
		ftrace[8] = "/2000-11-30/2000-11-30-08%3A00%3A01.fs";
		ftrace[9] = "/2000-11-30/2000-11-30-09%3A00%3A00.fs";
		ftrace[10] = "/2000-11-30/2000-11-30-10%3A00%3A00.fs";
		ftrace[11] = "/2000-11-30/2000-11-30-11%3A00%3A00.fs";
		ftrace[12] = "/2000-11-30/2000-11-30-12%3A00%3A01.fs";
		ftrace[13] = "/2000-11-30/2000-11-30-13%3A00%3A00.fs";
		ftrace[14] = "/2000-11-30/2000-11-30-14%3A00%3A00.fs";
		ftrace[15] = "/2000-11-30/2000-11-30-15%3A00%3A00.fs";
		ftrace[16] = "/2000-11-30/2000-11-30-16%3A00%3A00.fs";
		ftrace[17] = "/2000-11-30/2000-11-30-17%3A00%3A01.fs";
		ftrace[18] = "/2000-11-30/2000-11-30-18%3A00%3A00.fs";
		ftrace[19] = "/2000-11-30/2000-11-30-19%3A00%3A00.fs";
		ftrace[20] = "/2000-11-30/2000-11-30-20%3A00%3A00.fs";
		ftrace[21] = "/2000-11-30/2000-11-30-21%3A00%3A00.fs";
		ftrace[22] = "/2000-11-30/2000-11-30-22%3A00%3A01.fs";
		ftrace[23] = "/2000-11-30/2000-11-30-23%3A00%3A00.fs";
		*/


		ftrace[0] = "/2000-12-01/2000-12-01-00%3A00%3A01.fs";
		ftrace[1] = "/2000-12-01/2000-12-01-01%3A00%3A00.fs";
		ftrace[2] = "/2000-12-01/2000-12-01-02%3A00%3A01.fs";
		ftrace[3] = "/2000-12-01/2000-12-01-03%3A00%3A00.fs";
		ftrace[4] = "/2000-12-01/2000-12-01-04%3A00%3A00.fs";
		ftrace[5] = "/2000-12-01/2000-12-01-05%3A00%3A00.fs";
		ftrace[6] = "/2000-12-01/2000-12-01-06%3A00%3A01.fs";
		ftrace[7] = "/2000-12-01/2000-12-01-07%3A00%3A00.fs";
		ftrace[8] = "/2000-12-01/2000-12-01-08%3A00%3A00.fs";
		ftrace[9] = "/2000-12-01/2000-12-01-09%3A00%3A00.fs";
		ftrace[10] = "/2000-12-01/2000-12-01-10%3A00%3A00.fs";
		ftrace[11] = "/2000-12-01/2000-12-01-11%3A00%3A01.fs";
		ftrace[12] = "/2000-12-01/2000-12-01-12%3A00%3A00.fs";
		ftrace[13] = "/2000-12-01/2000-12-01-13%3A00%3A00.fs";
		ftrace[14] = "/2000-12-01/2000-12-01-14%3A00%3A00.fs";
		ftrace[15] = "/2000-12-01/2000-12-01-15%3A00%3A01.fs";
		ftrace[16] = "/2000-12-01/2000-12-01-16%3A00%3A00.fs";
		ftrace[17] = "/2000-12-01/2000-12-01-17%3A00%3A00.fs";
		ftrace[18] = "/2000-12-01/2000-12-01-18%3A00%3A00.fs";
		ftrace[19] = "/2000-12-01/2000-12-01-19%3A00%3A01.fs";
		ftrace[20] = "/2000-12-01/2000-12-01-20%3A00%3A00.fs";
		ftrace[21] = "/2000-12-01/2000-12-01-21%3A00%3A00.fs";
		ftrace[22] = "/2000-12-01/2000-12-01-22%3A00%3A00.fs";
		ftrace[23] = "/2000-12-01/2000-12-01-23%3A00%3A00.fs";

		/*
		ftrace[0] = "/2000-12-02/2000-12-02-00%3A00%3A01.fs";
		ftrace[1] = "/2000-12-02/2000-12-02-01%3A00%3A00.fs";
		ftrace[2] = "/2000-12-02/2000-12-02-02%3A00%3A01.fs";
		ftrace[3] = "/2000-12-02/2000-12-02-03%3A00%3A00.fs";
		ftrace[4] = "/2000-12-02/2000-12-02-04%3A00%3A01.fs";
		ftrace[5] = "/2000-12-02/2000-12-02-05%3A00%3A01.fs";
		ftrace[6] = "/2000-12-02/2000-12-02-06%3A00%3A00.fs";
		ftrace[7] = "/2000-12-02/2000-12-02-07%3A00%3A00.fs";
		ftrace[8] = "/2000-12-02/2000-12-02-08%3A00%3A01.fs";
		ftrace[9] = "/2000-12-02/2000-12-02-09%3A00%3A00.fs";
		ftrace[10] = "/2000-12-02/2000-12-02-10%3A00%3A00.fs";
		ftrace[11] = "/2000-12-02/2000-12-02-11%3A00%3A01.fs";
		ftrace[12] = "/2000-12-02/2000-12-02-12%3A00%3A00.fs";
		ftrace[13] = "/2000-12-02/2000-12-02-13%3A00%3A00.fs";
		ftrace[14] = "/2000-12-02/2000-12-02-14%3A00%3A01.fs";
		ftrace[15] = "/2000-12-02/2000-12-02-15%3A00%3A00.fs";
		ftrace[16] = "/2000-12-02/2000-12-02-16%3A00%3A00.fs";
		ftrace[17] = "/2000-12-02/2000-12-02-17%3A00%3A01.fs";
		ftrace[18] = "/2000-12-02/2000-12-02-18%3A00%3A00.fs";
		ftrace[19] = "/2000-12-02/2000-12-02-19%3A00%3A00.fs";
		ftrace[20] = "/2000-12-02/2000-12-02-20%3A00%3A01.fs";
		ftrace[21] = "/2000-12-02/2000-12-02-21%3A00%3A00.fs";
		ftrace[22] = "/2000-12-02/2000-12-02-22%3A00%3A01.fs";
		ftrace[23] = "/2000-12-02/2000-12-02-23%3A00%3A00.fs";
		*/


		String test0 = "/2000-11-30/hptest1.log";
		String test1 = "/2000-11-30/hptest2.log";

		//String ftraceFileNameComplete0 = dir.concat(ftrace0);
		//String ftraceFileNameComplete1 = dir.concat(ftrace1);

		String[] fileList = new String[24];

		for (int i = 0; i < 24; i++) {
			fileList[i] = dir.concat(ftrace[i]);
		}
		/*
		for (int i=0; i<24 ; i++) {
			System.out.println(fileList[i]);
		}*/
		//String ftraceFileNameComplete0 = dir.concat(test0);
		//String ftraceFileNameComplete1 = dir.concat(test1);




			FileSystemTraceAnalyzer fsta = new FileSystemTraceAnalyzer();

			for (int i=0; i<24; i++) {
			//String temp = dir.concat("/2000-11-30/hptest".concat(String.valueOf(i)).concat(".log"));
			//System.out.println(temp);
				System.out.println(i);
				fsta.rawFSTraceReader(fileList[i]);

			}

			//fsta.rawFSTraceReader(ftraceFileNameComplete1);
		System.out.println("End");
	}




}
