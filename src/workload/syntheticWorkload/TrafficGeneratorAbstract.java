package workload.syntheticWorkload;

import java.util.Arrays;

public abstract class TrafficGeneratorAbstract implements
		TrafficGeneratorInterface {

	@Override
	public void close() {

	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public long getNext(long datasize) {
		return 0;
	}

	@Override
	public void init(long requestNum) {
	}
	@Override
	public abstract long[] getAccessDataList(TrafficGeneratorAbstract utf, long datasize,
			long requestNum);
}