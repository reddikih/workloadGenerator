/**
 * Created on 2010/05/26
 */
package workload.syntheticWorkload;

import workload.WorkloadGenerator;
/**
 * @author hanhlh
 *
 */
public interface TrafficGeneratorInterface {

	/**
	 * Return the id of next accessed data
	 * */
	public long getNext(long datasize);


    /**
     * Return the total number of access data
     * @param num
     */
    public void init(long requestNum);

    public void close();

    /*
     * Return the name of traffic
     * */
    public String getName();

    //public abstract long[] getAccessDataList();

	public abstract long[] getAccessDataList(TrafficGeneratorAbstract utf, long datasize,
			long requestNum);

}
