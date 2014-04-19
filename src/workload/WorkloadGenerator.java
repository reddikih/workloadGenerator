package workload;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.lang.Double;
import java.lang.Long;

import workload.WorkloadParameters;
import workload.WorkloadParameters.WorkloadType;
import workload.syntheticWorkload.PoissonTimingGenerator;
import workload.syntheticWorkload.SyntheticTimingGeneratorAbstract;
import workload.syntheticWorkload.TrafficGeneratorAbstract;
import workload.syntheticWorkload.TypeOfRequestAbstract;
import workload.syntheticWorkload.UniformTimingGenerator;
import workload.syntheticWorkload.UniformTrafficGenerator;
import workload.syntheticWorkload.UniformTypeOfRequestGenerator;
import workload.syntheticWorkload.ZipfTrafficGenerator;

public class WorkloadGenerator {

	private double[] arrivalTime;

	private long[] accessedDataId;

	private ArrayList typeOfRequest;

	private double readRatio;

	private long fileSize;

	private WorkloadType workloadType;

	private String dataType; //initial data or normal data

	/**
	 * @param args
	 */
	public double[] getArrivalTime() {
		return arrivalTime;
	}

	public long[] getAccessedDataId() {
		return accessedDataId;
	}

	public ArrayList<?> getTypeOfRequest() {
		return typeOfRequest;
	}
	public double getReadRatio() {
		return readRatio	;
	}

	public long getFileSize() {
		return fileSize;
	}

	public String getDataType() {
		return dataType;
	}

	public void setArrivalTime(SyntheticTimingGeneratorAbstract stg, int seed, double lambda, long requestNum){
		this.arrivalTime= stg.getArrivalTimeList(stg, seed, lambda, requestNum) ;
	}

	public void setAccessedDataTraffic (TrafficGeneratorAbstract tg, long datasize, long requestNum) {
		this.accessedDataId = tg.getAccessDataList(tg, datasize, requestNum);
	}


	public void setTypeOfRequest (TypeOfRequestAbstract tor, double readRatio, long requestNum) {
		this.typeOfRequest = tor.getTypeOfRequestList(readRatio, requestNum);
	}

	public void setReadRatio (double readRatio) {
		this.readRatio = readRatio;
	}

	public void setFileSize (long filesize) {
		this.fileSize = filesize;
	}

	public void setDataType (String dataType) {
		this.dataType = dataType;
	}

	public void setWorkload(WorkloadGenerator wg, WorkloadType workloadType, double readRatio,
			long datasize, long filesize, long	requestNum, int seed, double lambda) {
		switch (workloadType) {
		case UNIFORM_TIMING_UNIFORM_TRAFFIC:
			SyntheticTimingGeneratorAbstract utg_uu = new UniformTimingGenerator(seed, lambda);
			TrafficGeneratorAbstract utf_uu = new UniformTrafficGenerator(seed);
			TypeOfRequestAbstract tor_uu = new UniformTypeOfRequestGenerator();
			wg.setFileSize(filesize);
			wg.setReadRatio(readRatio);
			wg.setArrivalTime(utg_uu, seed, lambda, requestNum);
			wg.setAccessedDataTraffic(utf_uu, datasize, requestNum);
			wg.setTypeOfRequest(tor_uu, readRatio, requestNum);

		case UNIFORM_TIMING_ZIPF_TRAFFIC:
			SyntheticTimingGeneratorAbstract utg_uz = new UniformTimingGenerator(seed, lambda);
			TrafficGeneratorAbstract ztg_uz = new ZipfTrafficGenerator();
			TypeOfRequestAbstract tor_uz = new UniformTypeOfRequestGenerator();
			wg.setFileSize(filesize);
			wg.setReadRatio(readRatio);
			wg.setArrivalTime(utg_uz, seed, lambda, requestNum);
			wg.setAccessedDataTraffic(ztg_uz, datasize, requestNum);
			wg.setTypeOfRequest(tor_uz, readRatio, requestNum);

		case POISSON_TIMING_UNIFORM_TRAFFIC:
			SyntheticTimingGeneratorAbstract ptg_pu = new PoissonTimingGenerator(seed, lambda);
			TrafficGeneratorAbstract ztg_pu = new UniformTrafficGenerator(seed);
			TypeOfRequestAbstract tor_pu = new UniformTypeOfRequestGenerator();
			wg.setFileSize(filesize);
			wg.setReadRatio(readRatio);
			wg.setArrivalTime(ptg_pu, seed, lambda, requestNum);
			wg.setAccessedDataTraffic(ztg_pu, datasize, requestNum);
			wg.setTypeOfRequest(tor_pu, readRatio, requestNum);

		case POISSON_TIMING_ZIPF_TRAFFIC:
			SyntheticTimingGeneratorAbstract ptg_pz = new PoissonTimingGenerator(seed, lambda);
			TrafficGeneratorAbstract ztg_pz = new ZipfTrafficGenerator();
			TypeOfRequestAbstract tor_pz = new UniformTypeOfRequestGenerator();
			wg.setFileSize(filesize);
			wg.setReadRatio(readRatio);
			wg.setArrivalTime(ptg_pz, seed, lambda, requestNum);
			wg.setAccessedDataTraffic(ztg_pz, datasize, requestNum);
			wg.setTypeOfRequest(tor_pz, readRatio, requestNum);

		default:
		}
	}

	public String getWorkloadTypeName(WorkloadGenerator wg) {
		return wg.workloadType.toString();
	}
	
	public void generateOutput(WorkloadGenerator wg, String outdir, String filename)
		throws Exception{

		FileOutputStream fos = new FileOutputStream(filename);
		OutputStreamWriter osw = new OutputStreamWriter(fos,"ASCII");
		BufferedWriter bw = new BufferedWriter (osw);

		long requestNum = WorkloadParameters.REQUESTNUM;
		long fileSize = WorkloadParameters.OBJECT_SIZE;

		String fileSize_str = Long.toString(fileSize);

		double[] arrivalTimeList = wg.getArrivalTime();
		long[] accessDataList = wg.getAccessedDataId();

		ArrayList<?> typeOfRequestList = wg.getTypeOfRequest();

		// header
		bw.write("[header]\n");
		bw.write(String.format("numfiles %d\n", WorkloadParameters.DATASIZE));
		bw.write(String.format("lower %d\n", fileSize));
		bw.write(String.format("upper %d\n", fileSize));
		bw.write("\n");
		bw.write("# key, size, arrival, action\n");

		for (long i=0; i<requestNum; i++) {
			String temp = Double.toString(arrivalTimeList[(int) i]/1000); //in second unit
//			bw.write(i+",");
			bw.write(accessDataList[(int) i]+",");
			bw.write(fileSize_str+",");
			bw.write(temp+",");
			if (!(typeOfRequestList.contains(i))) {
				bw.write("write\n");
			} else bw.write("read\n");
		}
		bw.close();
		osw.close();
		fos.close();
	}
	
	private static final String baseName = "workloads";
	
	private static String getOutputFileName(String name, int seqNo) {
		File file = new File(name);
		if (!file.exists()) {
			return name;
		}
		
		int pos = name.indexOf("_"); 
		if (pos >= 0) {
			if (pos == name.length() - 1) {
				name = name + seqNo;
			} else {
				name = name.substring(0, pos + 1) + seqNo;
			}
		} else {
			name = name + "_" + seqNo;
		}
		seqNo++;
		return getOutputFileName(name, seqNo);
	}
	

	public static void main(String[] args) throws Exception {
		int seed = 1;
		int lambda = WorkloadParameters.LAMBDA;
		long requestNum = WorkloadParameters.REQUESTNUM;
		long numFiles = WorkloadParameters.DATASIZE;
		long fileSize = WorkloadParameters.OBJECT_SIZE;
		double readRatio = WorkloadParameters.READ_RATIO;
		String outdir = WorkloadParameters.TRAFFIC_OUTDIR;

		WorkloadGenerator wg = new WorkloadGenerator();

		System.out.println("Workload Generation start ...");
		long start = System.currentTimeMillis();

// hikida add start
		String fileName = getOutputFileName(baseName, 1);

		wg.setWorkload(wg, WorkloadType.POISSON_TIMING_ZIPF_TRAFFIC, readRatio, numFiles, fileSize,
			requestNum,	seed, lambda);
		wg.generateOutput(wg, outdir, fileName);

        long end = System.currentTimeMillis();
        System.out.println("Workload Generation end ...");
        System.out.println("Elapsed time : " + (end - start) + "ms");
// hikida add end
	}

}
