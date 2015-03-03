package vega.uplink.planning.period;

import java.io.IOException;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//Plan plan = new Plan();
		//System.out.println(plan.toString());
		Plan plan=Plan.readFromFile("/Users/jarenas 1//OPS/ROS_SGS/PLANNING/LTP005/LTP005H/ros_mtp_stp_vstp_LTP005H.def");
		try {
			Plan.saveToXmlFile("/Users/jarenas 1//Rosetta/pp.xml", plan);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			Plan p2 = Plan.readPlanFromXmlFile("/Users/jarenas 1//Rosetta/pp.xml");
			System.out.println(p2.toXml());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println(plan.toXml());
	}

}
