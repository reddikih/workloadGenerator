/**
 *Created on 2010/05/26

 *
 * keyword:
 *
 * f(x) = l * exp(-l*x)    (x>0)
 * F(x) = 1-exp(-l*x)    (x>0)
 */

package workload.syntheticWorkload;

import java.util.Arrays;

import workload.WorkloadParameters;
 /**
 * f(x) = l * exp(-l*x)    (x>0)
 *  F(x) = 1-exp(-l*x)    (x>0)
 * @author hanhlh
 *
 */
public class PoissonTimingGenerator extends SyntheticTimingGeneratorAbstract{

	public PoissonTimingGenerator(int seed, double lambda){
		super(seed,lambda, 0);
	}

	/**
	 * @param lambda  number of requests per second (eq/sec)
	 */
	public PoissonTimingGenerator(int seed, double lambda, long starttimeMillis){
		super(seed, lambda, starttimeMillis);
	}
	/**
	 * Calculate the duration time according to exponential distribution for next arrival
	 * @return
	 */
		double tmp = 1.0-rand.nextDouble();
		//System.out.println("1.0-Random=" + tmp);
		protected double calcNextArrivalRelativeTime(){
		//System.out.println("lambda=" + lambda);
		//System.out.println("NextArrivalRelativeTime="+ Math.log(1.0-rand.nextDouble())/(-lambda));
		return Math.log(1.0-rand.nextDouble())/(-lambda);

	}

	public void init(long num) {
		// Do nothing
	}
	public double[] getArrivalTimeList(SyntheticTimingGeneratorAbstract stg,
			int seed, double lambda, long requestNum) {

		double[] arrivalTimeList = new double[(int) requestNum];
		Arrays.fill(arrivalTimeList, 0);

		for(int i=0; i<requestNum; i++){
			arrivalTimeList[i] = stg.getNextAbsoluteTime();
		}

		return arrivalTimeList;

	}
	/// static method ////////////////////////////////////////////////////////

	/**
	 * For Test
	 * Calculate the duration time according to exponential distribution for next arrival
	 * @param numberOfRequests will be determined from System Parameters
	 */
	public static void main(String[] args){
		int count = 0;
		double tm= 0.0;

		long requestNum = WorkloadParameters.SYNTHETIC_REQUESTNUM;
		SyntheticTimingGeneratorAbstract stg = new PoissonTimingGenerator(1, 100);

		for(int i=0; i<requestNum; i++){
			//System.out.println("Current Time="+stg.getCurrentTime());
			tm = stg.getNextAbsoluteTime();
			count++;
			System.out.println(tm);
		}
		System.out.println(tm);
		System.out.println(count);
		System.out.println("avgreq/sec=" + (count/tm * 1000));

	}

	@Override
	public void outputXML() {

	}

}
