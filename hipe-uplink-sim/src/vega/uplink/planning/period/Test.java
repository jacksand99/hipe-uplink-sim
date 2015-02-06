package vega.uplink.planning.period;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//Plan plan = new Plan();
		//System.out.println(plan.toString());
		Plan plan=Plan.readFromFile("/Users/jarenas 1//OPS/ROS_SGS/PLANNING/LTP005/LTP005H/ros_mtp_stp_vstp_LTP005H.def");
		System.out.println(plan.toXml());
	}

}
