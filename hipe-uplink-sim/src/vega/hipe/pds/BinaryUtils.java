package vega.hipe.pds;

public class BinaryUtils {
	public static String toBinary( byte[] bytes )
	{
	    StringBuilder sb = new StringBuilder(bytes.length * Byte.SIZE);
	    for( int i = 0; i < Byte.SIZE * bytes.length; i++ )
	        sb.append((bytes[i / Byte.SIZE] << i % Byte.SIZE & 0x80) == 0 ? '0' : '1');
	    return sb.toString();
	}
	
	public static String toBinary(byte b){
		byte[] arg = new byte[1];
		arg[0]=b;
		return toBinary(arg);
	}

	public static byte[] fromBinary( String s )
	{
	    int sLen = s.length();
	    byte[] toReturn = new byte[(sLen + Byte.SIZE - 1) / Byte.SIZE];
	    char c;
	    for( int i = 0; i < sLen; i++ )
	        if( (c = s.charAt(i)) == '1' )
	            toReturn[i / Byte.SIZE] = (byte) (toReturn[i / Byte.SIZE] | (0x80 >>> (i % Byte.SIZE)));
	        else if ( c != '0' )
	            throw new IllegalArgumentException();
	    return toReturn;
	}
	public static byte fromBinaryByte(String s){
		return fromBinary(s)[0];
	}
	
	public static  byte setBit(byte b,int index,boolean newValue){
		String binary = toBinary(b);
		String part1="";
		String part2="";
		if (index==0){
			part1="";
		}else{
			part1=binary.substring(0,index-1);
		}
		if (index==7){
			part2="";
		}else{
			part2=binary.substring(index+1);
		}
		if (newValue){
			return fromBinaryByte(part1+"1"+part2);
		}else{
			return fromBinaryByte(part1+"0"+part2);
		}
	}
	
	public static boolean getBit(byte b,int index){
		String binary = toBinary(b);
		char[] ca = binary.toCharArray();
		char s=ca[index];
		
		if (s=='0') return false;
		else return true;

	}
	public static byte[] pcRealToFloatBinary(byte[] bytes){
		String sPcReal = toBinary(bytes);
		char e0 = sPcReal.charAt(16);
		char sign = sPcReal.charAt(24);
		//String mantisa = sPcReal.substring(0,15)+sPcReal.substring(17,23);
		String mantisa = sPcReal.substring(17,23)+sPcReal.substring(8,15)+sPcReal.substring(0,7);
		String e = sPcReal.substring(25)+e0;
		String iee=sign+e+mantisa;
		return fromBinary(iee);
	}
	
	public static float pcRealToFloat(byte[] pcRealBytes){
		byte[] bytes = pcRealToFloatBinary(pcRealBytes);
		int asInt = (bytes[3] & 0xFF) 
	            | ((bytes[2] & 0xFF) << 8) 
	            | ((bytes[1] & 0xFF) << 16) 
	            | ((bytes[0] & 0xFF) << 24);
		
		return Float.intBitsToFloat(asInt);
	}
	
	public static void main (String[] args){
		String number="11111101111101111101111011101111";
		byte[] array=fromBinary(number);
		byte[] fl=pcRealToFloatBinary(array);
		System.out.println(toBinary(fl));
		System.out.println(pcRealToFloat(array));
		/*for (int i=0;i<array.length;i++){
			System.out.println("byte "+i);
			System.out.println(array[i]);
		}
		
		String b="10110111";
		byte by = fromBinaryByte(b);
		by=setBit(by,0,false);
		System.out.println(toBinary(by));
		System.out.println(getBit(by,1));*/
	}
}
