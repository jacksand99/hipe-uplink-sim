package vega.uplink.commanding;

public interface SequenceTimeline {
	public void addSequence(Sequence sequence);
	
	public void setSequences(Sequence[] porSequences);
	
	public Sequence[] getSequences();
	
	public String toXml();
}
