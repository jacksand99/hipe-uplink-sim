package vega.hipe.pds.pds4;

/**
 * The Table Base class defines a heterogeneous repeating record of scalars. The Table Base class is the parent class for all heterogeneous repeating record of scalars.
 * @author jarenas
 *
 */
public interface TableBase {
	public String toXml();
	public String toXml(int indent);
}
