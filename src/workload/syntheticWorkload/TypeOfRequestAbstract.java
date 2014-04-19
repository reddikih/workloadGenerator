/**
 *
 */
package workload.syntheticWorkload;

import java.util.ArrayList;

/**
 * @author hanhlh
 *
 */
public abstract class TypeOfRequestAbstract implements TypeOfRequestInterface {

	//protected double readRatio;
	protected static int seed;

	public abstract String getName();

	public TypeOfRequestAbstract (){
		this(seed);
	}

	public TypeOfRequestAbstract (int seed) {

	}
	public void init(long requestNum) {

	}

	public abstract long getNext(long datasize);

	public abstract ArrayList getTypeOfRequestList(double readRatio, long datasize);
}
