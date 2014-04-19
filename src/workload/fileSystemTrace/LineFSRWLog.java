package workload.fileSystemTrace;

public class LineFSRWLog {

	String arrivalTime;
	String requestCall_id;
	String outputStatus;
	String outputError;
	String requestType;
	String device;
	String fileDescriptor;
	String bytes;

	public LineFSRWLog (String arrivalTime_, String requestCall_id_, String outputStatus_,
			String outputError_, String requestType_, String device_, String fileDescriptor_,
			String bytes_) {
		this.arrivalTime = arrivalTime_;
		this.requestCall_id = requestCall_id_;
		this.outputStatus = outputStatus_;
		this.outputError = outputError_;
		this.requestType = requestType_;
		this.device = device_;
		this.fileDescriptor = fileDescriptor_;
		this.bytes = bytes_;

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
	public String getDevice() {
		return device;
	}
	public void setDevice(String device) {
		this.device = device;
	}
	public String getFileDescriptor() {
		return fileDescriptor;
	}
	public void setFileDescriptor(String fileDescriptor) {
		this.fileDescriptor = fileDescriptor;
	}
	public String getBytes() {
		return bytes;
	}
	public void setBytes(String bytes) {
		this.bytes = bytes;
	}

	public void printLine (LineFSRWLog line) {
		System.out.println(line.getArrivalTime() + ", " +
				line.getRequestCall_id() + ", " +
				line.getOutputStatus() + ", " +
				line.getOutputError() + ", " +
				line.getRequestType() + ", " +
				line.getDevice() + ", " +
				line.getFileDescriptor() + ", " +
				line.getBytes());
	}

}
