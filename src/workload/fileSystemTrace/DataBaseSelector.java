package workload.fileSystemTrace;

import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

import workload.WorkloadFSParameters;

public class DataBaseSelector {

	private static String TABLENAME = WorkloadFSParameters.TABLENAME;

	//private static String TABLENAMETEST = WorkloadFSParameters.TABLENAME_TEST;

	String day;

	String hour;

	public static void close(Connection connection, String tablename){

	}

	public DataBaseSelector(String day_, String hour_) {

		//this.tableName = tableName_;
		this.day = day_;
		this.hour = hour_;

	}

	public ResultSet selectDatabase (PreparedStatement pst) throws SQLException {

		//System.out.println(pst.toString());

		pst.setString(1, day);
		pst.setString(2, hour);

		System.out.println(pst.toString());

		return pst.executeQuery();


	}

	public static String getSelectAllSQL (){
		return ("SELECT * from " + TABLENAME +
				" WHERE (day = ? and hour = ? and (request_type = 'READ' or" +
				" request_type= 'WRITE'));");
	}

	public static String getSelectRequestDetailSQL (){
		return ("SELECT request_detail from " + TABLENAME +
				" WHERE (day = ? and hour = ? and (request_type = 'READ' or" +
				" request_type= 'WRITE'));");
	}

}

