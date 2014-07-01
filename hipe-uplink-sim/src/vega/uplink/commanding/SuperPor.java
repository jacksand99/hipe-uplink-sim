package vega.uplink.commanding;

public class SuperPor extends Por {
	Por[] pors;
	
	public SuperPor(){
		super();
		pors = new Por[0];
	}
	
	public void setPors(Por[] PORs){
		pors=PORs;
		super.calculateValidity();
	}
	
	public Por[] getPors(){
		return pors;
	}
	
	public void addPor(Por POR){
		Por[] newArr=new Por[pors.length+1];
		for (int i=0;i<pors.length;i++){
			newArr[i]=pors[i];
		}
		newArr[pors.length]=POR;
		setPors(newArr);
		//String name=POR.getName(name);
		setProduct(POR.getName(),POR);
		//setProduct("")
		//addProduct()
		super.calculateValidity();
	}
	
	public Sequence[] getSequences(){
		java.util.Vector<Sequence> vector=new java.util.Vector<Sequence>();
		int size=0;
		//Sequence[] result = new Sequence[0];
		for(int i=0; i < pors.length;i++){
			Sequence[] seqs=pors[i].getSequences();
			for (int j=0;j<seqs.length;j++){
				//result=super.insertSequenceAt(result, seqs[j], result.length);
				size=size+1;
				vector.add(seqs[j]);
			}
		}
		Sequence[] result = new Sequence[size];
		vector.copyInto(result);
		return result;
	}
}
