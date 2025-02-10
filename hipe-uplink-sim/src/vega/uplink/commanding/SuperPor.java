package vega.uplink.commanding;

import herschel.ia.dataset.Product;
import herschel.ia.pal.ProductRef;
import herschel.share.fltdyn.time.FineTime;
import herschel.share.interpreter.InterpreterUtil;

import java.security.GeneralSecurityException;
import java.util.Iterator;


public class SuperPor extends Por {
	private boolean recalculateValidity=true;
	   public SuperPor(boolean rValidity){
	        super();
	        recalculateValidity=false;
	    }
	public SuperPor(){
		super();
	}
	
	public void setPors(Por[] PORs){
		for (int i=0;i<PORs.length;i++){
			setProduct(PORs[i].getName(),PORs[i]);
		}
		super.calculateValidity();
	}
	
	public boolean hasPors(){
		Iterator<ProductRef> it = this.getRefs().values().iterator();
		boolean result=false;
		while (it.hasNext()){
			if (InterpreterUtil.isInstance(SuperPor.class, it.next())){
				result=true;
			}
		}
		return result;
	}
	
	public Por toSinglePor(){
		Por result=new Por();
		result.regenerate(this);
		result.setName(this.getName());
		result.setPath(this.getPath());
		result.setSequences(this.getInternalSequences());
		return result;
		
	}
	public Por getPorByName(String Name) {
	    try {
	        return (Por) this.getRefs().get(Name).getProduct();
	    }catch (Exception gse) {
	        gse.printStackTrace();
	        return null;
	    }
	}
	
	public Por[] getPors(){
		java.util.Vector<Por> vector=new java.util.Vector<Por>();
		Iterator<ProductRef> it = this.getRefs().values().iterator();
		while (it.hasNext()){
			try {
				Product product = it.next().getProduct();
				if (InterpreterUtil.isInstance(SuperPor.class, product)){
					vector.add((SuperPor) product);
				}else{

					if (InterpreterUtil.isInstance(Por.class, product)){
						vector.add((Por) product);
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Por[] result=new Por[vector.size()];
		result=vector.toArray(result);
		return result;

	}
	
	public void addPor(Por POR){
		
		if (InterpreterUtil.isInstance(SuperPor.class, POR)){
			setProduct(POR.getName(),(SuperPor)POR);
		}else{
			setProduct(POR.getName(),POR);
		}
		calculateValidity();
		this.getInitModes().append(POR.getInitModes());
		this.getInitMS().append(POR.getInitMS());
		this.getInitMemory().append(POR.getInitMemory());
		this.getInitDataStore().append(POR.getInitDataStore());

	}
	
	public AbstractSequence[] getSequences(){
		java.util.Vector<AbstractSequence> vector=new java.util.Vector<AbstractSequence>();
		AbstractSequence[] internal = getInternalSequences();
		for (int j=0;j<internal.length;j++){
			vector.add(internal[j]);
		}
		Por[] pors = getPors();
		for(int i=0; i < pors.length;i++){
			AbstractSequence[] seqs=pors[i].getSequences();
			for (int j=0;j<seqs.length;j++){
				vector.add(seqs[j]);
			}
		}
		Sequence[] result = new Sequence[vector.size()];
		result=vector.toArray(result);
		return result;
	}
	public AbstractSequence[] getInternalSequences(){
		return super.getSequences();
	}
	public AbstractSequence[] getOrderedInternalSequences(){
		OrderedSequences ordered = new OrderedSequences();
		AbstractSequence[] arraySeq=getInternalSequences();
		for (int i=0;i<arraySeq.length;i++){
			ordered.put(arraySeq[i].getExecutionDate(), arraySeq[i]);
		}
		return ordered.toSequenceArray();
		
	}
	protected void calculateValidity(){
	    if (this.calculateValidity==false) {
	        if (this.getStartDate()!=null && this.getEndDate()!=null) return;
	    }
		Por[] pors = getPors();
		int size=pors.length;
		java.util.Date lower=null;
		java.util.Date higher=null;
		
		for (int i=0;i<size;i++){
			java.util.Date exDate=pors[i].getStartDate().toDate();
			java.util.Date endDate=pors[i].getEndDate().toDate();;
			if (lower==null) lower=exDate;
			if (higher==null) higher=endDate;
			if (exDate.before(lower)) lower=exDate;
			if (endDate.after(higher)) higher=endDate;
		}
		if (lower!=null)  this.setStartDate(new FineTime(lower));

		if (higher!=null){
			this.setEndDate(new FineTime(higher));
		}

	}
	


}
