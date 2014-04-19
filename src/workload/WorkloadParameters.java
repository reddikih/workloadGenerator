package workload;

public class WorkloadParameters {

    public static String TRAFFIC_OUTDIR = "./bin/sim/resources/out/";

    public static String RESOURCE_DIR_PATH = "./";

    //lambda Number of request per second
    public static int  LAMBDA = 5;
    // public static int  LAMBDA = 10;

    /*Type of Workload*/
    public static enum WorkloadType {
        UNIFORM_TIMING_UNIFORM_TRAFFIC,
            UNIFORM_TIMING_ZIPF_TRAFFIC,
            POISSON_TIMING_UNIFORM_TRAFFIC,
            POISSON_TIMING_ZIPF_TRAFFIC,
            //TRACE,
            }
    //Number of requests during simulation
    // public static long REQUESTNUM = 100;
//  public static long REQUESTNUM = LAMBDA * 3600 * 6;
    public static long REQUESTNUM = LAMBDA * 60 * 10;
     
    //Number of data during simulation
    public static int DATASIZE = 10000;
//    public static int DATASIZE = 100;
    
    public static long OBJECT_SIZE = 32L * 1024; //32KB
//    public static long OBJECT_SIZE = 1L * 1024 * 1024; //1MB

    /* Size of File and Number of File in Synthetic Workload */
//    public static  long SYNTHETIC_OBJECT_SIZE = OBJECT_SIZE;
    public static  long SYNTHETIC_REQUESTNUM = REQUESTNUM;

    /*Uniform Traffic*/
    public static long UNIFORM_TRAFFIC_REQUESTNUM = REQUESTNUM;
    public static long UNIFORM_TRAFFIC_DATASIZE = DATASIZE;

    /*Zipf Traffic*/
    public static long ZIPF_TRAFFIC_REQUESTNUM = REQUESTNUM;
    public static int ZIPF_TRAFFIC_DATASIZE = DATASIZE;
    public static double ZIPF_TRAFFIC_THETA = 1.2;
    public static double ZIPF_TRAFFIC_ACCESSED_DATA_RATE = 1.0;
    public static int ZIPF_TRAFFIC_SHUFFLE_SEED = 13;

    public static double READ_RATIO = 0.7;

}
