package workload.syntheticWorkload;

import workload.WorkloadGenerator;

public interface TimingGeneratorInterface{

	/**
	 * @return
	 */
	public double getNextAbsoluteTime();

	/**
	 * @param time (msec)
	 */
	public void setCurrentTime(double timeMilliseconds);

	public void outputXML();
}
