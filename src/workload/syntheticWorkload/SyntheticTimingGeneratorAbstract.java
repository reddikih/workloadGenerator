/**
 *
 */
package workload.syntheticWorkload;

import java.util.Random;
/**
 * @author hanhlh
 *
 */
public abstract class SyntheticTimingGeneratorAbstract implements
		TimingGeneratorInterface {

	protected double lambda;

	protected Random rand;

	protected double currentTimeMilliseconds;

	public SyntheticTimingGeneratorAbstract(int seed, double lambda) {
		this(seed, lambda, 0);
	}
	public SyntheticTimingGeneratorAbstract(int seed, double lambda, long starttimeMilliseconds) {
		//this(seed, lambda, starttimeMilliseconds);
		this.lambda = lambda/1000; // to msec
		rand = new Random(seed);

	}


	/// instance method ///////////////////////////////////////////////////////

	protected abstract double calcNextArrivalRelativeTime();

	public void setLambda(double lm){
		this.lambda = lm/1000;
	}


	public double getLambda(){
		return this.lambda * 1000;
	}

	@Override

	public double getNextAbsoluteTime() {
		currentTimeMilliseconds += calcNextArrivalRelativeTime();
		return currentTimeMilliseconds;
	}
	public void setCurrentTime(double timeMilliseconds) {
		this.currentTimeMilliseconds = timeMilliseconds;
	}

	public double getCurrentTime() {
		return currentTimeMilliseconds;
	}

	public abstract double[] getArrivalTimeList(SyntheticTimingGeneratorAbstract stg,
			int seed, double lambda, long requestNum);
}
