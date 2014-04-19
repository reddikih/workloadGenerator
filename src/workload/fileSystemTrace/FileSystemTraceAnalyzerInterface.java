package workload.fileSystemTrace;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;


/** From ftrace.h
 *
 * struct ftraceHeader {
 *   unsigned secs;	/* seconds from start of trace
 *   unsigned microSecs;	/* microseconds
 *   unsigned pid;	/* pid making call
 *   unsigned type;	/* one of FT_ above
 *   int errno;		/* error status
 *   int status;		/* return status
 *   unsigned size;	/* size of remaining record
 * };
 * struct ftraceRead {
 *   int device;		/* device file is on
 *   int fd;		/* file descriptor
 *   unsigned bytes;	/* size of request
 *	};

 * @author hanhlh
 *
 */

public interface FileSystemTraceAnalyzerInterface {


	public void rawFSTraceReader(String fileName) throws Exception;


	public void storeToDB(String fileName, LineFSLog lfsl) throws Exception;

}
