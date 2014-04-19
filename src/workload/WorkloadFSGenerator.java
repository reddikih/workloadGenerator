package workload;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import workload.WorkloadFSParameters;
import workload.fileSystemTrace.DataBaseSelector;

/*created by hanhlh
 *
 * 1. Determine the period for workload in WorkloadFSParameters through Year, Month, Day, Hour
 * 2. Initialize the workload by writing all data id and size in the period by scanning DB for the
 * 1st time. They are appeared on top of the file with the marking I and arrival time is 0.
 * 3. Generate the workload by scanning the DB for the 2nd time.
 * */
public class WorkloadFSGenerator {

	private static double cummulativeArrivalTime = 0.0;

	/**
	 * TODO
	 * @param args
	 * @throws Exception
	 */
	private Connection connection;

	private PreparedStatement pstAll;

	private PreparedStatement pstRequestDetail;

	private long selectCount = 0;

	//private static String tableName = WorkloadFSParameters.TABLENAME_TEST;

	//private static String tableName = WorkloadFSParameters.TABLENAME;


	private static String beginDay = WorkloadFSParameters.BEGIN_DAY;
	private static String endDay = WorkloadFSParameters.END_DAY;
	private static String beginHour = WorkloadFSParameters.BEGIN_HOUR;
	private static String endHour = WorkloadFSParameters.END_HOUR;

	private static String outDir = WorkloadFSParameters.OUT_DIR;

	private static String outFilename = WorkloadFSParameters.OUT_FILE_NAME;

	private static String ourFilename_dataIdList = WorkloadFSParameters.OUT_FILE_NAME_DATAID;

	private static FileOutputStream fos;

	private static OutputStreamWriter osw;

	private static BufferedWriter bw;

	private static ArrayList<String> dateList = new ArrayList<String>();

	private static ArrayList<String> hourList = new ArrayList<String>();

	private static ArrayList<String> dateHourList = new ArrayList<String>();

	//private static ArrayList<Long> dataIDList_tmp = new ArrayList<Long>();

	//private static ArrayList<String> deviceIDList_tmp = new ArrayList<String>();

	private static ArrayList<Long> dataIDList = new ArrayList<Long>();

	private static ArrayList<Long> dataSizeList = new ArrayList<Long>();

	//private static ArrayList<String> deviceIDList = new ArrayList<String>();

	//private static ArrayList <String> requestDetailList = new ArrayList<String>();



	//private static ArrayList<String> dateListHourList = new ArrayList<String>();

	public WorkloadFSGenerator() throws SQLException, ClassNotFoundException, FileNotFoundException,
	UnsupportedEncodingException {
		initWriterForWorkload();
		initDB();
	}


	private void initWriterForWorkload() throws FileNotFoundException, UnsupportedEncodingException {

		fos = new FileOutputStream(outDir.concat(outFilename), true); //append to existing file
		osw = new OutputStreamWriter(fos,"ASCII");
		bw = new BufferedWriter (osw);

	}

	private void initDB() throws SQLException, ClassNotFoundException {
		this.connection = setupDB();
		this.pstAll = connection.prepareStatement(DataBaseSelector.getSelectAllSQL());
		this.pstRequestDetail = connection.prepareStatement(DataBaseSelector.getSelectRequestDetailSQL());
	}

	private Connection setupDB() throws ClassNotFoundException, SQLException {
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

	public void generateDateHourList() {

		String beginYear = beginDay.substring(0, 4);
		String beginMonth = beginDay.substring(5, 7);
		String beginDate = beginDay.substring(8, 10);

		String endYear = endDay.substring(0, 4);
		String endMonth = endDay.substring(5, 7);
		String endDate = endDay.substring(8, 10);

		int beginDayInt = Integer.parseInt(beginDate);
		int endDayInt = Integer.parseInt(endDate);
		int beginHourInt = Integer.parseInt(beginHour);
		int endHourInt = Integer.parseInt(endHour);
		int beginMonthInt = Integer.parseInt(beginMonth);
		int endMonthInt = Integer.parseInt(endMonth);

		if (beginMonthInt == 11) {
			if ((endMonthInt == 11) && (beginDayInt > endDayInt)) {
				System.err.println("Check Day begin and end - 11");
			} else if ((endMonthInt == 11) && (beginHourInt >= endHourInt)) {
				System.err.println("Check Hour begin and end");
			} else if (((endMonthInt == 11) && (beginHourInt < endHourInt))) {

				//System.out.println("CALL");

				int hour_tmp = beginHourInt;
				int date_tmp = beginDayInt;
				int month_tmp = beginMonthInt;

				while ((hour_tmp < endHourInt) && (date_tmp == endDayInt)) {
					if (hour_tmp < 10) {
						dateHourList.add(String.format("2000-%d-%d-0%d", month_tmp, date_tmp, hour_tmp));
					} else dateHourList.add(String.format("2000-%d-%d-%d", month_tmp, date_tmp, hour_tmp));

					if (hour_tmp < 23) {
						hour_tmp++;

					} else if (hour_tmp >= 23) {
						System.err.println("Wrong end day input");
					}
				}

			} else if (endMonthInt == 12) {


				int hour_tmp = beginHourInt;
				int date_tmp = beginDayInt;
				int month_tmp = beginMonthInt;

				while ((month_tmp < 12) || ((month_tmp == 12) && (date_tmp < endDayInt)) ||
						((hour_tmp < endHourInt) && (date_tmp == endDayInt))) {

					if (month_tmp == 11) {
						if (hour_tmp < 10) {
							dateHourList.add(String.format("2000-%d-%d-0%d", month_tmp, date_tmp, hour_tmp));
						} else dateHourList.add(String.format("2000-%d-%d-%d", month_tmp, date_tmp, hour_tmp));
					} else if (month_tmp == 12) {
						if (hour_tmp < 10) {
							dateHourList.add(String.format("2000-%d-0%d-0%d", month_tmp, date_tmp, hour_tmp));
						} else dateHourList.add(String.format("2000-%d-0%d-%d", month_tmp, date_tmp, hour_tmp));
					}

					if (hour_tmp < 23) {

						hour_tmp++;

					} else if ((hour_tmp >= 23) && (date_tmp == 30)) {
						hour_tmp = 0;
						month_tmp = 12;
						date_tmp = 1;
					} else if ((hour_tmp >= 23) && (date_tmp != 30 )) {
						hour_tmp = 0;
						month_tmp = 12;
						date_tmp++;
					}
				}
			}

		} else if (beginMonthInt == 12) {
		    if ((beginDayInt > endDayInt)) {
				System.err.println("Check Day begin and end");
			} else if (beginHourInt >= endHourInt) {
				System.err.println("Check Hour begin and end");
			} else if (beginHourInt < endHourInt) {
				int hour_tmp = beginHourInt;
				int date_tmp = beginDayInt;
				int month_tmp = beginMonthInt;

				while ((date_tmp < endDayInt) || (hour_tmp < endHourInt) && (date_tmp == endDayInt)) {
					if (hour_tmp < 10) {
						dateHourList.add(String.format("2000-%d-0%d-0%d", month_tmp, date_tmp, hour_tmp));
					} else dateHourList.add(String.format("2000-%d-0%d-%d", month_tmp, date_tmp, hour_tmp));

					if (hour_tmp < 23) {
						hour_tmp++;

					} else if (hour_tmp >= 23) {
						hour_tmp = 0;
						} else date_tmp++;
					}

				}
			}

		for (int i=0; i<dateHourList.size(); i++) {
			System.out.println(i+ ". " + dateHourList.get(i));
		}
		//System.out.println(dateHourList.size());
	}

	/*
	* DateHour format:
	*
	* 2000-11-30-01
	*
	*/

	static Pattern ptDateHour = Pattern.compile(
	"^(\\d{4}-\\d{2}-\\d{2})-(\\d{2}).*$");

	public void generateDateListHourList() {
		for (int i=0; i<dateHourList.size(); i++) {
			Matcher mt = ptDateHour.matcher(dateHourList.get(i));
			//System.out.println(mt.matches());

			if (mt.matches()) {
				dateList.add(mt.group(1));
				hourList.add(mt.group(2));
			} else continue;
		}
	}
	/*
	* RequestDetail format:
	*
	* dev deviceName, FD dataID, bytes 96360
	*
	* There is a case that deviceName = NO_DEV. In this case, output as -1.
	*/

	static Pattern ptRequestDetail = Pattern.compile(
	"^dev\\s(\\d+/\\d+\\w+\\d+),\\sFD\\s(\\d+),\\sbytes\\s(\\d+).*$");


	public String generateDeviceId(String requestDetail) {

		Matcher mt = ptRequestDetail.matcher(requestDetail);
		//System.out.println(mt.matches());

		if (mt.matches()) {
			return mt.group(1);
		} else return "NO_DEV"; //NOT MATCH

	}

	public long generateAccessedDataId(String requestDetail) {
		//System.out.println(requestDetail);

		Matcher mt = ptRequestDetail.matcher(requestDetail);

		//System.out.println(mt.matches());

		if (mt.matches()) {
			return Long.parseLong(mt.group(2));
		} else {}
			return -1; //NO_DEV

	}

	public long generateDataSize(String requestDetail) {

		Matcher mt = ptRequestDetail.matcher(requestDetail);
		//System.out.println(mt.matches());

		if (mt.matches()) {
			return Long.parseLong(mt.group(3));
		} else return -1; //NOT MATCH

	}

	/*
	 * Generate distinct DataID and DataSize List for initial data
	 */

	public void generateDataIDAndDataSizeList(ResultSet rs) throws Exception{


		boolean existed = false;

		boolean no_device = false; 					//Filter out NO_DEV error

		int i = 0;

		if (generateAccessedDataId(rs.getString(1)) == -1) {
			no_device = true;
		}

		while (i<dataIDList.size()) {

			/*
			 * The 1st if clause is for setting fixed size to initial data
			 * The 2nd if clause is for setting data size as in raw file system trace
			 * Make sure to keep the consistency in function generateInitialData()
			 */

			if (dataIDList.get(i) == generateAccessedDataId(rs.getString(1))) {


			//if ((dataIDList.get(i) == generateAccessedDataId(rs.getString(1))) &&
			//	(dataSizeList.get(i) == generateDataSize(rs.getString(1)))) {
				existed = true;
				break;
			}
			i++;

			}

		if ((existed==false) && (no_device==false)) {
			dataIDList.add(generateAccessedDataId(rs.getString(1)));
			dataSizeList.add(generateDataSize(rs.getString(1)));
		}

	}


	public static void  closeWriterForWorkload() throws IOException {
		bw.close();
		osw.close();
		fos.close();

	}


		public void selectFromDBForDataList(String day, String hour) throws Exception {

		DataBaseSelector dbs = new DataBaseSelector(day, hour);

		ResultSet rs = dbs.selectDatabase(pstRequestDetail);

		System.out.println("Scan DB for generating Data ID and Data Size ...");
		while (rs.next()) {

			generateDataIDAndDataSizeList(rs);

		}


	}

		public void generateDataIDAndDeviceIDList(ResultSet rs) throws SQLException {

			boolean existed = false;

			int i = 0;

			while (i<dataIDList.size()) {
				if (dataIDList.get(i) == generateAccessedDataId(rs.getString(1))) {
					existed = true;
					break;
				}
				i++;

			}

			if (existed==false) {
				dataIDList.add(generateAccessedDataId(rs.getString(1)));
			}
		}

	// For Initialization Data

	public void generateInitialData() throws IOException {

		System.out.println("Output initial data to file ...");
		for (int i=0; i<dataIDList.size();i++) {
			bw.write("0,");									//RequestId

			bw.write(dataIDList.get(i) + ",");				//DataId

			double arrivalTime = 0.0;

			bw.write(arrivalTime + ",");					//Arrival Time

			//DataSize: Choose 1 of 2 from next two bw.write lines

			bw.write(WorkloadFSParameters.FILESIZE + ",");	//Fixed DataSize
			//bw.write(dataSizeList.get(i)+",");				//DataSize

			bw.write("WRITE,");								//Request_Type READ or WRITE
			bw.write("I\n"); 								//Initial Data

		}
	}
	public void generateOutput(ResultSet rs, long request_id)
															throws Exception{

		//String filename = outdir.concat(wg.getWorkloadTypeName(wg)) + ".csv";


		//ArrayList<?> typeOfRequestList = wg.getTypeOfRequest();

		bw.write(request_id + ",");									//RequestId
		bw.write(generateAccessedDataId(rs.getString(8)) + ",");	//DataId

		double arrivalTime = Double.parseDouble(rs.getString(3)) + cummulativeArrivalTime;

		bw.write(arrivalTime + ",");								//Arrival Time
		bw.write(generateDataSize(rs.getString(8)) + ",");			//DataSize
		bw.write(rs.getString(7) + ",");							//Request_Type READ or WRITE
		bw.write("N\n"); 												//Normal load


	}


	public void selectFromDBForNormalWorkload(String day, String hour) throws Exception {

		DataBaseSelector dbs = new DataBaseSelector(day, hour);

		ResultSet rs = dbs.selectDatabase(pstAll);

		System.out.println("Output workload data to file ...");

		while (rs.next()) {
			selectCount++;
			generateOutput(rs, selectCount);
			if (rs.isLast()) {
				cummulativeArrivalTime += Double.parseDouble(rs.getString(3));
			}
		}
		//System.out.println(cummulativeArrivalTime);

	}


	public static void main(String[] args) throws Exception {


		WorkloadFSGenerator wg = new WorkloadFSGenerator();

		System.out.println("Generate Data and Hour.");
		wg.generateDateHourList();

		wg.generateDateListHourList();

		for (int i=0; i<dateHourList.size(); i++) {
			wg.selectFromDBForDataList(dateList.get(i), hourList.get(i));

		}

		System.out.println("Generate Initial Data");

		wg.generateInitialData();

		System.out.println("Generate Normal Workload");
		for (int i=0; i<dateHourList.size(); i++) {
			wg.selectFromDBForNormalWorkload(dateList.get(i), hourList.get(i));

		}


		closeWriterForWorkload();
		System.out.println("Workload Generated.");	}
}
