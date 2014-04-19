package workload.fileSystemTrace;


public class LineFSVForkLog {

	String arrivalTime;
	String p_id1;
	String p_id2;
	String requestType;

	public LineFSVForkLog (String arrivalTime_, String p_id1_, String p_id2_, String requestType_) {
		if(!requestType.equalsIgnoreCase("VFORK")){
			throw new RuntimeException("only vfork supported.");
		}
		this.arrivalTime = arrivalTime_;
		this.p_id1 = p_id1_;
		this.p_id2 = p_id2_;
		this.requestType = requestType_;
	}

		public String getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(String arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public String getP_id1() {
		return p_id1;
	}

	public void setP_id1(String p_id1) {
		this.p_id1 = p_id1;
	}

	public String getP_id2() {
		return p_id2;
	}

	public void setP_id2(String p_id2) {
		this.p_id2 = p_id2;
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public void printLine (LineFSVForkLog line) {
	System.out.println(line.getArrivalTime() + ", " +
			line.getP_id1() + ", " +
			line.getP_id2() + ", " +
			line.getRequestType());
	}


}
