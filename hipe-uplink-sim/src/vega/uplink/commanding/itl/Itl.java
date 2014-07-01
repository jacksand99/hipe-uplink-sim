package vega.uplink.commanding.itl;

public class Itl {
	private int version;
	private java.util.Date startDate;
	private java.util.Date endDate;
	
	public Itl(){
		version=1;
		startDate=new java.util.Date();
		endDate=new java.util.Date();
		
	}
	
	public int getVersion(){
		return version;
	}
	
	public java.util.Date getStartTime(){
		return startDate;
	}
	
	public java.util.Date getEndTime(){
		return endDate;
	}
	
	public void setVersion(int newVersion){
		version=newVersion;
	}
	
	public void setStartTime(java.util.Date time){
		startDate=time;
	}
	
	public void setEndTime(java.util.Date time){
		endDate=time;
	}
}
