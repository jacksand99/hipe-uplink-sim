package vega.uplink.commanding;



public interface SequenceTimelineInterface {
		
		
		public void setName(String name);
		
		public String getName();
		
		public void addSequence(SequenceInterface sequence);
		
		
		public SequenceInterface[] getSequencesForDate(java.util.Date date);
		
		
		public void setSequences(SequenceInterface[] porSequences);
		
		public SequenceInterface[] getSequences();

		
		public String toXml();
		
		public SequenceInterface[] getOrderedSequences();
		
		
		public SequenceInterface getSequenceBefore(java.util.Date date);
		

}
