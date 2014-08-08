package vega.uplink.pointing;

import java.util.Date;

//import rosetta.uplink.pointing.SolarAspectAngle;

public class SolarAspectAngle implements Comparable<SolarAspectAngle>{
private Date time;
private float saa_yp;
private float saa_yn;
private float saa_xaxis;
private float saa_yaxis;
private float saa_zaxis;

public SolarAspectAngle (Date time,float yp,float yn,float xaxis,float yaxis,float zaxis){
	this.time=time;
	this.saa_yp=yp;
	this.saa_yn=yn;
	this.saa_xaxis=xaxis;
	this.saa_yaxis=yaxis;
	this.saa_zaxis=zaxis;

}

@Override
public int compareTo(SolarAspectAngle o) {
	// TODO Auto-generated method stub
	return this.time.compareTo(o.time);
}

public String toString(){
	java.text.SimpleDateFormat dateFormat=new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
	dateFormat.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
	return dateFormat.format(time)+" "+saa_yp+" "+saa_yn+" "+saa_xaxis+" "+saa_yaxis+" "+saa_zaxis;
}

public Date getTime(){
	return time;
}

public float getSaaYPositivePanel(){
	return saa_yp;
}

public float getSaaYNegativePanel(){
	return saa_yn;
}

public float getSCAnglePositiveXAxis(){
	return saa_xaxis; 
}
public float getPositiveXFaceIlumination(){
	return Math.abs(90-getSCAnglePositiveXAxis());
}
public float getSCAngleNegativeXAxis(){
	return 180-saa_xaxis; 
}
public float getNegativeXFaceIlumination(){
	return Math.abs(90-getSCAngleNegativeXAxis());
}


public float getSCAnglePositiveYAxis(){
	return saa_yaxis; 
}
public float getPositiveYFaceIlumination(){
	return Math.abs(90-getSCAnglePositiveYAxis());
}

public float getSCAngleNegativeYAxis(){
	return 180-saa_yaxis; 
}
public float getNegativeYFaceIlumination(){
	return Math.abs(90-getSCAngleNegativeYAxis());
}


public float getSCAnglePositiveZAxis(){
	return saa_zaxis; 
}
public float getPositiveZFaceIlumination(){
	return Math.abs(90-getSCAnglePositiveZAxis());
}


public float getSCAngleNegativeZAxis(){
	return 180-saa_zaxis; 
}
public float getNegativeZFaceIlumination(){
	return Math.abs(90-getSCAngleNegativeZAxis());
}



}
