package vega.uplink.commanding;



/**
 * Interface to be implemented by classes containing a list of sequences (PORs, PDORs, ITLs, Observations, etc)
 * @author jarenas
 *
 */
public interface SequenceTimelineInterface {
		
		
		/**
		 * Set the name of the timeline
		 * @param name
		 */
		public void setName(String name);
		
		/**
		 * Get the name of the timeline
		 * @return
		 */
		public String getName();
		
		/**
		 * Add a sequence to the timeline
		 * @param sequence
		 */
		public void addSequence(SequenceInterface sequence);
		
		
		/**
		 * Get the sequences with a given execution date 
		 * @param date
		 * @return
		 */
		public SequenceInterface[] getSequencesForDate(java.util.Date date);
		
		
		/**
		 * Remove any sequences to this timeline and add the given sequences
		 * @param porSequences
		 */
		public void setSequences(SequenceInterface[] porSequences);
		
		/**
		 * Get the sequences in this timeline
		 * @return
		 */
		public SequenceInterface[] getSequences();

		
		/**
		 * Get a XML representation of this timeline
		 * @return
		 */
		public String toXml();
		
		/**
		 * Get all sequences in this timeline in execution time order
		 * @return
		 */
		public SequenceInterface[] getOrderedSequences();
		
		
		/**
		 * Get all sequences before a date
		 * @param date
		 * @return
		 */
		public SequenceInterface getSequenceBefore(java.util.Date date);
		

}
