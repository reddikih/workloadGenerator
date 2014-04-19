/**
 *Created on 2010/5/26
 */
package workload.syntheticWorkload;


import java.util.Arrays;

import workload.WorkloadParameters;

/**
 * @author hanhlh
 *
 */
public class UniformTimingGenerator extends SyntheticTimingGeneratorAbstract{

	public UniformTimingGenerator(int seed, double lambda) {
		super(seed, lambda,0);
	}

	public UniformTimingGenerator(int seed, double lambda, long starttimeMilliseconds) {
		super(seed, lambda, starttimeMilliseconds);
	}
	@Override
	protected double calcNextArrivalRelativeTime() {
		return 1.0/lambda;
	}

	@Override
	public void outputXML() {

	}

	public void init(long num) {
		// Do nothing
	}
	public double[] getArrivalTimeList(SyntheticTimingGeneratorAbstract stg,
			int seed, double lambda, long requestNum) {
		int count = 0;
		//double tm = 0.0;

		//long requestNum = WorkloadParameters.SYNTHETIC_REQUESTNUM;

		double[] arrivalTimeList = new double[(int) requestNum];
		Arrays.fill(arrivalTimeList, 0);

		//SyntheticTimingGeneratorAbstract stg = new UniformTimingGenerator(seed, lambda);

		for(int i=0; i<requestNum; i++){
			arrivalTimeList[i] = stg.getNextAbsoluteTime();
			count++;
			//System.out.println(tm);
		}

		return arrivalTimeList;

	}
	/**
	 * For Test
	 * Calculate the duration time according to exponential distribution for next arrival
	 * @param numberOfRequests will be determined from System Parameters
	 */
	public static void main(String[] args){
		int count = 0;
		double tm= 0.0;

		long requestNum = WorkloadParameters.SYNTHETIC_REQUESTNUM;

		SyntheticTimingGeneratorAbstract stg = new UniformTimingGenerator(1, 100);

		for(int i=0; i<requestNum; i++){
			//System.out.println("Current Time="+stg.getCurrentTime());
			tm = stg.getNextAbsoluteTime();
			count++;
			System.out.println(tm);
		}
		//System.out.println(tm);
		//System.out.println(count);
		System.out.println("avgreq/sec=" + (count/tm * 1000));

	}


}
