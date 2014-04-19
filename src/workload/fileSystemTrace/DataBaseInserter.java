package workload.fileSystemTrace;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

import workload.WorkloadFSParameters;

public class DataBaseInserter {

	/*CREATE TABLE fstrace_rawlog
	 *(day varchar(10) NOT NULL,
	 *hour varchar(2) NOT NULL,
	 *arrival_time varchar NOT NULL,
	 *request_call varchar(10) NOT NULLL,
	 *output_status varchar(10) NOT NULL,
	 *output_error varchar(4) NOT NULL,
	 *request_type varchar(5) NOT NULL,
	 *request_detail text)
	 *WITHOUT OIDS;
	 */



	//private static String TABLENAME = WorkloadFSParameters.TABLENAME_TEST;

	private static String TABLENAME = WorkloadFSParameters.TABLENAME;

	//private static String TABLENAME = "HPLogTest";

	public static void createTable() throws ClassNotFoundException, SQLException{
		Connection connection = null;
		System.out.println("CREATE TABLE: " + TABLENAME);
			if(connection == null) {
				// open connection to db
				Class.forName("org.postgresql.Driver");

				//connection = DriverManager.getConnection (
				//		"jdbc:postgresql://cattleya/disk_simulator",
				//		"hanhlh",
				//		"");
				connection = DriverManager.getConnection("jdbc:postgresql://delphinium/file_system_trace_HP",
						"hanhlh",
						"");

				if(connection == null) {
					throw new RuntimeException("Database connection is null");
				}
				connection.setAutoCommit(false);
			}

			createTable(connection);

	}

	public static void createTable(Connection connection)  {
		try {
			System.out.println("CREATE TABLE: " + TABLENAME);
			if(connection == null) {
				throw new RuntimeException("Null connection");
			}
			String sql =
				"CREATE TABLE " + TABLENAME +
				" (day varchar(15) NOT NULL," +
				"hour varchar(2) NOT NULL," +
				"arrival_time varchar (20) NOT NULL," +
				"request_call varchar(10) NOT NULL," +
				"output_status varchar (5) NOT NULL," +
				"output_error varchar(4) NOT NULL," +
				"request_type varchar(10) NOT NULL," +
				"request_detail text" +
				") WITH OIDS;";

			System.out.println(sql);
			connection.createStatement().execute(sql);

			// CHANGE USER
			/*
			sql = "ALTER TABLE " + TABLENAME +
			" OWNER TO " + Parameters.TR_DBUSER + ";";
			System.out.println(sql);
			connection.createStatement().executeUpdate(sql);
			 */
			System.out.println("CREATE TABLE: DONE");

		} catch (SQLException sqle) {
			SQLException ne = sqle.getNextException();
			while(ne != null){
				ne.printStackTrace();
				ne = ne.getNextException();
			}
		}

	}
	public static void close(Connection connection, String tablename){

	}

	String fileName;
	String tableName;
	String day;
	String hour;
	String arrivalTime;
	String requestCall;
	String outputStatus;
	String outputError;
	String requestType;
	String requestDetail;

	public DataBaseInserter(String fileName, String tableName, String day, String hour,
	String arrivalTime, String requestCall,	String outputStatus, String outputError,
	String requestType,	String requestDetail) {

		this.fileName = fileName;
		this.tableName = tableName;
		this.day = day;
		this.hour = hour;
		this.arrivalTime = arrivalTime;
		this.requestCall = requestCall;
		this.outputStatus = outputStatus;
		this.outputError = outputError;
		this.requestType = requestType;
		this.requestDetail = requestDetail;

	}

	public void insertDatabase (PreparedStatement pst) throws SQLException {

		pst.setString(1, day);
		pst.setString(2, hour);
		pst.setString(3, arrivalTime);
		pst.setString(4, requestCall);
		pst.setString(5, outputStatus);
		pst.setString(6, outputError);
		pst.setString(7, requestType);
		pst.setString(8, requestDetail);

		pst.addBatch();

	}

	public void insertDatabaseNormal (Connection connection) throws SQLException {
	try{
		if(connection == null) {
			throw new RuntimeException("Null connection");
		}
		String insertSql =
			"INSERT INTO TABLE " + TABLENAME +
			" (day ,hour, arrival_time, request_call, output_status, output_error, request_type," +
			"request_detai) VALUES ("
			+ day + "," +
			hour + "," +
			arrivalTime + "," +
			requestCall + "," +
			outputStatus + "," +
			outputError + "," +
			requestType + ","+
			requestDetail + ");";

		System.out.println(insertSql);
		System.out.println(connection);
		connection.createStatement().execute(insertSql);

		System.out.println("INSERT INTO TABLE: DONE");

	} catch (SQLException sqle) {
		SQLException ne = sqle.getNextException();
		while(ne != null){
			ne.printStackTrace();
			ne = ne.getNextException();
		}
	}

	}



	public static String getInsertSQL (String tableName){
		return ("INSERT INTO " + TABLENAME +
		" (day, hour, arrival_time, request_call, " +
		"output_status, output_error, request_type, request_detail)" +
		" VALUES ( ?, ?, ?, ?, ?, ?, ?, ? );");
	}

}

