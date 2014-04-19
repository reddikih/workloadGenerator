package workload.fileSystemTrace;

public class LineFSLogRecord {

	String date;
	String hour;
	String arrivalTime;
	String requestCall_id;
	String outputStatus;
	String outputError;
	String requestType;
	String requestDetail;

	public LineFSLogRecord (String date_, String hour_, String arrivalTime_, String requestCall_id_, String outputStatus_,
			String outputError_, String requestType_, String requestDetail_) {
		this.date = date_;
		this.hour = hour_;
		this.arrivalTime = arrivalTime_;
		this.requestCall_id = requestCall_id_;
		this.outputStatus = outputStatus_;
		this.outputError = outputError_;
		this.requestType = requestType_;
		this.requestDetail = requestDetail_;

	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getHour() {
		return hour;
	}
	public void setHour(String hour) {
		this.hour = hour;
	}
	public String getArrivalTime() {
		return arrivalTime;
	}
	public void setArrivalTime(String arrivalTime) {
		this.arrivalTime = arrivalTime;
	}
	public String getRequestCall_id() {
		return requestCall_id;
	}
	public void setRequestCall_id(String requestCallId) {
		requestCall_id = requestCallId;
	}
	public String getOutputStatus() {
		return outputStatus;
	}
	public void setOutputStatus(String outputStatus) {
		this.outputStatus = outputStatus;
	}
	public String getOutputError() {
		return outputError;
	}
	public void setOutputError(String outputError) {
		this.outputError = outputError;
	}
	public String getRequestType() {
		return requestType;
	}
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}
	public String getRequestDetail() {
		return requestDetail;
	}
	public void setRequestDetail(String requestDetail) {
		this.requestDetail = requestDetail;
	}
	public void printLine (LineFSLogRecord line) {
	System.out.println(line.getArrivalTime() + ", " +
			line.getRequestCall_id() + ", " +
			line.getOutputStatus() + ", " +
			line.getOutputError() + ", " +
			line.getRequestType() + ", " +
			line.getRequestDetail());
	}

}
