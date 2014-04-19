/**
 *
 */
package workload.syntheticWorkload;

import java.util.ArrayList;
import java.util.Random;

/**
 * @author hanhlh
 *
 */
public class UniformTypeOfRequestGenerator extends TypeOfRequestAbstract{
	private Random rand;
	public UniformTypeOfRequestGenerator () {
		super();
		this.rand = new Random();
	}
	public UniformTypeOfRequestGenerator (int seed, double readRatio) {
		super();
	}
	@Override
	//@para datasize = Number of data in traffic
	public long getNext(long requestNum) {
		long nextId = rand.nextLong() % requestNum;
		while (nextId < 0) {
			nextId += requestNum;
		}
		return nextId;
	}
	@Override
	public ArrayList getTypeOfRequestList(double readRatio, long requestNum) {
		long readRequestNum = (long) Math.floor(readRatio * requestNum);
		//long[] typeOfRequestList = new long[(int) readRequestNum];
		ArrayList typeOfRequestList = new ArrayList();
		long countRead = 0;

		UniformTypeOfRequestGenerator utr = new UniformTypeOfRequestGenerator();

//		typeOfRequestList.add(utr.getNext(requestNum));

		while (countRead<readRequestNum) {
			long tmp=utr.getNext(requestNum);
			if (!typeOfRequestList.contains(tmp)) {
				typeOfRequestList.add(tmp);
				countRead++;
			}
		}


		//ArrayList.sort(typeOfRequestList);
		return typeOfRequestList;
	}

	public int checkExist (long[] array, long value) {
		int result=0;
		int i=0;
		while (i<array.length) {
			if (array[i]==value)
				result=1;
		}
		return result;
	}
	@Override
	public String getName() {
		return "Uniform Type of Request Generator";
	}


}
