package vega.hipe.pds.pds4;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import vega.hipe.logging.VegaLog;

/**
 * The observation area consists of attributes that provide information about the circumstances under which the data were collected.
 * @author jarenas
 *
 */
public class ObservationArea {
	private String comment;
	private TimeCoordinates timeCoordinates;
	private PrimaryResultSummary primaryResultSummary;
	private InvestigationArea investigationArea;
	private ObservingSystem observingSystem;
	private TargetIdentification targetIdentification;
	private MissionArea missionArea;
	private DisciplineArea disciplineArea;
	public static String COMMENT="comment";
	public static String OBSERVING_AREA="Observation_Area";
	
	public ObservationArea(){
		comment="";
		timeCoordinates=new TimeCoordinates();
		primaryResultSummary=new PrimaryResultSummary();
		investigationArea=new InvestigationArea();
		observingSystem=new ObservingSystem();
		targetIdentification=new TargetIdentification();
		missionArea=new MissionArea();
		disciplineArea=new DisciplineArea();
	}
	
	public String toXml(){
		return toXml(0);
	}
	public static ObservationArea getFromNode(Node node){
		ObservationArea result=new ObservationArea();
		Element poElement = (Element) node;
		try{
			result.setComment(((Element) node).getElementsByTagName(COMMENT).item(0).getTextContent().trim());
		}catch (Exception e){
			VegaLog.info("Could not get "+COMMENT);
		}
		try{
			Node timeCoordinatesNode = poElement.getElementsByTagName(TimeCoordinates.TIME_COORDINATES).item(0);
			result.setTimeCoordinates(TimeCoordinates.getFromNode(timeCoordinatesNode));
		}catch (Exception e){
			VegaLog.info("Could not get "+TimeCoordinates.TIME_COORDINATES);
		}
		try{
			Node primaryResultSummaryNode = poElement.getElementsByTagName(PrimaryResultSummary.PRIMARY_RESULT_SUMMARY).item(0);
			result.setPrimaryResultSummary(PrimaryResultSummary.getFromNode(primaryResultSummaryNode));
		}catch (Exception e){
			VegaLog.info("Could not get "+PrimaryResultSummary.PRIMARY_RESULT_SUMMARY);
		}
		
		try{
			Node investigationAreaNode = poElement.getElementsByTagName(InvestigationArea.INVESTIGATION_AREA).item(0);
			result.setInvestigationArea(InvestigationArea.getFromNode(investigationAreaNode));
		}catch (Exception e){
			VegaLog.info("Could not get "+InvestigationArea.INVESTIGATION_AREA);
		}
		
		try{
			Node observingSystemNode = poElement.getElementsByTagName(ObservingSystem.OBSERVING_SYSTEM).item(0);
			result.setObservingSystem(ObservingSystem.getFromNode(observingSystemNode));
		}catch (Exception e){
			VegaLog.info("Could not get "+InvestigationArea.INVESTIGATION_AREA);
		}
		
		try{
			Node targetIdentificationNode = poElement.getElementsByTagName(TargetIdentification.TARGET_IDENTIFICATION).item(0);
			result.setTargetIdentification(TargetIdentification.getFromNode(targetIdentificationNode));
		}catch (Exception e){
			VegaLog.info("Could not get "+TargetIdentification.TARGET_IDENTIFICATION);
		}
		
		try{
			Node missionAreaNode = poElement.getElementsByTagName(MissionArea.MISSION_AREA).item(0);
			result.setMissionArea(MissionArea.getFromNode(missionAreaNode));
		}catch (Exception e){
			VegaLog.info("Could not get "+MissionArea.MISSION_AREA);
		}
		
		try{
			Node disciplineArenaNode = poElement.getElementsByTagName(DisciplineArea.DISCIPLINE_AREA).item(0);
			result.setDisciplineArea(DisciplineArea.getFromNode(disciplineArenaNode));
		}catch (Exception e){
			VegaLog.info("Could not get "+DisciplineArea.DISCIPLINE_AREA);
		}
		

			return result;
	}
	public String toXml(int indent){
		String id="";
		for (int i=0;i<indent;i++){
			id=id+"\t";
		}
		String result="";
		result=result+id+"<Observation_Area>\n";
		result=result+id+"\t<comment>"+comment+"</comment>\n";

		result=result+timeCoordinates.toXml(indent+1);
		result=result+primaryResultSummary.toXml(indent+1);
		result=result+investigationArea.toXml(indent+1);
		result=result+observingSystem.toXml(indent+1);
		result=result+targetIdentification.toXml(indent+1);
		result=result+missionArea.toXml(indent+1);
		result=result+disciplineArea.toXml(indent+1);
		
		result=result+id+"</Observation_Area>\n";
		
		return result;
	}
	
	public String getComment(){
		return comment;
	}
	
	public void setComment(String newComment){
		comment=newComment;
	}
	
	public TimeCoordinates getTimeCoordinates(){
		return timeCoordinates;
	}
	
	public void setTimeCoordinates(TimeCoordinates newTimeCoordinates){
		timeCoordinates=newTimeCoordinates;
	}
	
	public PrimaryResultSummary getPrimaryResultSummary(){
		return primaryResultSummary;
	}
	
	public void setPrimaryResultSummary(PrimaryResultSummary newPrimaryResultSummary){
		primaryResultSummary=newPrimaryResultSummary;
	}
	
	public InvestigationArea getInvestigationArea(){
		return investigationArea;
	}
	public void setInvestigationArea(InvestigationArea newInvestigationArea){
		investigationArea=newInvestigationArea;
	}
	
	public ObservingSystem getObservingSystem(){
		return observingSystem;
	}
	
	public void setObservingSystem(ObservingSystem newObservingSystem){
		observingSystem=newObservingSystem;
	}
	
	public TargetIdentification getTargetIdentification(){
		return targetIdentification;
	}
	
	public void setTargetIdentification (TargetIdentification newTargetIdentification){
		targetIdentification=newTargetIdentification;
	}
	
	public MissionArea getMissionArea(){
		return missionArea;
	}
	
	public void setMissionArea(MissionArea newMissionArea){
		missionArea=newMissionArea;
	}
	
	public DisciplineArea getDisciplineArea(){
		return disciplineArea;
	}
	
	public void setDisciplineArea (DisciplineArea newDisciplineArea){
		disciplineArea=newDisciplineArea;
	}
	
	
	
}
