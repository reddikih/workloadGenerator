/**
 *Created on 2010/05/26
 */
package workload.syntheticWorkload;

import java.util.Arrays;
import java.util.Random;

import workload.WorkloadParameters;


/**
 * @author hanhlh
 *
 */
public class ZipfTrafficGenerator extends TrafficGeneratorAbstract{

	private long requestNum = WorkloadParameters.ZIPF_TRAFFIC_REQUESTNUM;

	private long datasize = WorkloadParameters.ZIPF_TRAFFIC_DATASIZE;

	private static double accessedDataRate = WorkloadParameters.ZIPF_TRAFFIC_ACCESSED_DATA_RATE;

	private Random rand;

	//Skew parameter t
	private static double theta;

	private long[] shuffle;

	//seed for random shuffle data id in Zipf traffic again
	private int shuffleSeed;

	public ZipfTrafficGenerator() {
		this(WorkloadParameters.ZIPF_TRAFFIC_THETA);
	}

	/*
	 * Generate traffic according to Zipf distribution with given parameter theta
	 * */
	public ZipfTrafficGenerator(double theta) {
		this.theta=theta;
		this.rand = new Random();
		this.shuffleSeed = WorkloadParameters.ZIPF_TRAFFIC_SHUFFLE_SEED;
	}


	@Override
	public void close() {

	}

	@Override
	public String getName() {
		return "Zipf" + theta;
	}

	@Override
	public void init(long num) {
		this.requestNum=num;
		reshuffle();
	}

	public void reshuffle() {
		long num = this.requestNum;
    	Random rand_sh = new Random(this.shuffleSeed++);
		//Random rand_sh = new Random();
    	this.shuffle = new long[(int) num];
    	for(int i=0; i<shuffle.length; i++){
    		shuffle[i] = i;
    	}

    	for(int i=0; i< num; i++){
    		int r = rand_sh.nextInt((int) (num-i)) + i;
    		long temp = shuffle[i];
    		shuffle[i] = shuffle[r];
    		shuffle[r] = temp;
    	}
    }

	@Override
	public long getNext(long datasize) {
		return this.nextLong(requestNum);
	}

	public long nextLong(long num) {
		// Return the id of next data
		long id;
		long nused = (long) (num * accessedDataRate);

		double id_tmp = Math.pow(1+ rand.nextDouble() * (Math.pow(nused, 1-theta) - 1),
							  (1/(1-theta)));

		//System.out.println("id_tmp="+id_tmp);
        id = (long) Math.floor(id_tmp);

      if(id >= nused)  id = (nused-1);
      //System.out.println("shuffle="+ shuffle[(int) id]);
      return shuffle[(int) id];
	}


	public long[] getAccessDataList (TrafficGeneratorAbstract ztg, long datasize, long requestNum) {
		//long n=WorkloadParameters.ZIPF_TRAFFIC_DATASIZE;
//		double theta=WorkloadParameters.ZIPF_TRAFFIC_THETA;
		//long requestNum = WorkloadParameters.ZIPF_TRAFFIC_REQUESTNUM;
		int reshuffleNum = 4;

    	long[] accessDataList = new long[(int) requestNum];

    	long[] countList = new long [(int) datasize];
    	Arrays.fill(countList, 0);

    	//ZipfTrafficGenerator ztg = new ZipfTrafficGenerator(theta);
    	ztg.init(datasize);

    	for(int j=0; j<reshuffleNum; j++){
    		for(int i=0; i<requestNum/reshuffleNum; i++){
    			long index = i + j*requestNum/reshuffleNum;
	    		long value = (long) ztg.getNext(datasize);

	    		if(value < 0 || value > datasize){
	    			throw new RuntimeException("illegal value:" + value);
	    		}else{
	    			countList[(int) value]++;
	    			//System.out.println("Value=" + value);
	    			accessDataList[(int) index]=value;
	    		}
	    	}
	    	((ZipfTrafficGenerator) ztg).reshuffle();
    	}
		return accessDataList;
	}

	public static void main(String[] args) {

		//n Number of Data;
		long n=WorkloadParameters.ZIPF_TRAFFIC_DATASIZE;
		double th=WorkloadParameters.ZIPF_TRAFFIC_THETA;
		//loop Number of Request;
		long loop=WorkloadParameters.ZIPF_TRAFFIC_REQUESTNUM;

		//long[] requestList =
	}

}
