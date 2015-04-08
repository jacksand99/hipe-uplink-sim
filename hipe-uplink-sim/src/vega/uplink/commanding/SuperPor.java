package vega.uplink.commanding;

import herschel.ia.dataset.Product;
import herschel.ia.numeric.String1d;
import herschel.ia.pal.ProductRef;
import herschel.share.fltdyn.time.FineTime;
import herschel.share.interpreter.InterpreterUtil;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
//import java.util.Collection;
import java.util.Iterator;

import vega.uplink.commanding.Por.OrderedSequences;

public class SuperPor extends Por {
	//Por[] pors;
	
	public SuperPor(){
		super();
		//pors = new Por[0];
	}
	
	public void setPors(Por[] PORs){
		for (int i=0;i<PORs.length;i++){
			setProduct(PORs[i].getName(),PORs[i]);
		}
		//setPors(PORs);
		//pors=PORs;
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
	
	public Por[] getPors(){
		//Por[] result=new Por[this.getRefs().size()];
		java.util.Vector<Por> vector=new java.util.Vector<Por>();
		Iterator<ProductRef> it = this.getRefs().values().iterator();
		//int locator=0;
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
				//result[locator]=(Por) product;
				//locator++;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//locator++;
		}
		Por[] result=new Por[vector.size()];
		result=vector.toArray(result);
		return result;

		//return pors;
	}
	
	public void addPor(Por POR){
		/*Por[] newArr=new Por[pors.length+1];
		for (int i=0;i<pors.length;i++){
			newArr[i]=pors[i];
		}
		newArr[pors.length]=POR;
		setPors(newArr);*/
		//String name=POR.getName(name);
		
		if (InterpreterUtil.isInstance(SuperPor.class, POR)){
			setProduct(POR.getName(),(SuperPor)POR);
		}else{
			setProduct(POR.getName(),POR);
		}
		//setProduct("")
		//addProduct()
		//super.calculateValidity();
		calculateValidity();
		this.getInitModes().append(POR.getInitModes());
		this.getInitMS().append(POR.getInitMS());
		this.getInitMemory().append(POR.getInitMemory());
		this.getInitDataStore().append(POR.getInitDataStore());

	}
	
	public AbstractSequence[] getSequences(){
		java.util.Vector<AbstractSequence> vector=new java.util.Vector<AbstractSequence>();
		//int size=0;
		//Sequence[] result = new Sequence[0];
		AbstractSequence[] internal = getInternalSequences();
		//AbstractSequence[] seqs=pors[i].getSequences();
		for (int j=0;j<internal.length;j++){
			//result=super.insertSequenceAt(result, seqs[j], result.length);
			//size=size+1;
			vector.add(internal[j]);
		}
		Por[] pors = getPors();
		for(int i=0; i < pors.length;i++){
			AbstractSequence[] seqs=pors[i].getSequences();
			for (int j=0;j<seqs.length;j++){
				//result=super.insertSequenceAt(result, seqs[j], result.length);
				//size=size+1;
				vector.add(seqs[j]);
			}
		}
		Sequence[] result = new Sequence[vector.size()];
		//vector.copyInto(result);
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
		
		/*AbstractSequence[] arraySeq=getInternalSequences();
		if (arraySeq==null) return new Sequence[0];
		java.util.Vector<AbstractSequence> result =new java.util.Vector<AbstractSequence>();
		long[] times;
		java.util.Set<Long> set = new HashSet<Long>();
		Long newTime;
		for (int i=0;i<arraySeq.length;i++){
			newTime=new Long(arraySeq[i].getExecutionDate().getTime());
			set.add(newTime);
		}
		times=new long[set.size()];
		Iterator<Long> it = set.iterator();
		int counter=0;
		while(it.hasNext()) {
		  times[counter]=(it.next()).longValue();
		  counter++;
		}
		Arrays.sort(times);
		for (int i=0;i<times.length;i++){
			AbstractSequence[] seqDate = this.getSequencesForDate(new java.util.Date(times[i]));
			for (int j=0;j<seqDate.length;j++) result.add(seqDate[j]);
		}
		Sequence[] ret=new Sequence[result.size()];
		result.toArray(ret);
		return ret;*/
	}
	protected void calculateValidity(){
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
			//higher
			this.setEndDate(new FineTime(higher));
		}

	}
	


}
