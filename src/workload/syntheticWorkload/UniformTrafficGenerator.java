/**
 *
 */
package workload.syntheticWorkload;

import java.util.Arrays;
import java.util.Random;

import workload.WorkloadParameters;


/**
 * @author hanhlh
 *
 */
public class UniformTrafficGenerator extends TrafficGeneratorAbstract{

	// total number of stream traffic
	private long requestNum=WorkloadParameters.UNIFORM_TRAFFIC_REQUESTNUM;

	// random arguments
	private int seed;
	private Random rand;


	public UniformTrafficGenerator() {
		super();
		this.rand = new Random();
	}

	public UniformTrafficGenerator(int seed) {
		super();
		this.seed = seed;
		this.rand = new Random(seed);
	}

	public int getSeed() {
		return seed;
	}
	@Override
	public void close() {

	}

	@Override
	public String getName() {
		return "UniformTraffic";
	}

	@Override
	public synchronized long getNext(long datasize) {
		long next_id = rand.nextLong() % datasize;
		while (next_id < 0) {
			next_id += datasize;
		}
		return next_id;
	}

	@Override
	public void init(long requestNum) {
		this.requestNum = requestNum;
	}

	@Override

	public long[] getAccessDataList(TrafficGeneratorAbstract utf, long datasize, long requestNum) {
		//UniformTrafficGenerator utf = new UniformTrafficGenerator();
		//long requestNum = WorkloadParameters.UNIFORM_TRAFFIC_REQUESTNUM;
		utf.init(requestNum);
		long[] accessDataList = new long[(int) requestNum];
		Arrays.fill(accessDataList, 0);

		for (int i=0; i<requestNum; i++) {
			accessDataList[i]=utf.getNext(datasize);
		}

		return accessDataList;
	}
	//For Test
	/*
	public static void main(String[] args) {

		UniformTrafficGenerator utf = new UniformTrafficGenerator();
		long streamNum = WorkloadParameters.UNIFORM_TRAFFIC_REQUESTNUM;
		long datasize = WorkloadParameters.UNIFORM_TRAFFIC_DATASIZE;
		utf.init(streamNum);

		for (int i=0; i<streamNum; i++) {
			System.out.print("Stream["+i+"]=");
			System.out.println(utf.getNext(datasize));
		}
	}
	*/


}

