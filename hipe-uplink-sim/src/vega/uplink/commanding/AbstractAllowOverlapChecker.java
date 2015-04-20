package vega.uplink.commanding;

public  abstract class AbstractAllowOverlapChecker {
	/**
	 * Check if 2 sequences are allowed to overlap in execution
	 * @param seq1
	 * @param seq2
	 * @return True if the sequences are allowed to overlap in execution
	 */
	public abstract boolean allowOverlap(AbstractSequence seq1,AbstractSequence seq2);
}
