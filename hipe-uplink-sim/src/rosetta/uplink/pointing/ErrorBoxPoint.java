package rosetta.uplink.pointing;

public class ErrorBoxPoint {
	Float eta;
	Float zeta;
	Float epsilon;
	String name;
	public ErrorBoxPoint(Float eta,Float zeta,Float epsilon){
		this.eta=eta;
		this.zeta=zeta;
		this.epsilon=epsilon;
		name=null;
	}
	public Float getEta(){
		return eta;
	}
	public Float getZeta(){
		return zeta;
	}
	public Float getEpsilon(){
		return epsilon;
	}
	public String getName(){
		if (name==null){
			return "ETA="+getEta()+" ZETA="+getZeta()+" EPSILON="+getEpsilon();
		}
		else return name;
	}
	public void setName(String name){
		this.name=name;
	}
}
