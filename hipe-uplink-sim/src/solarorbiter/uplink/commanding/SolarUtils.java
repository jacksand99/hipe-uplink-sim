package solarorbiter.uplink.commanding;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

import vega.uplink.commanding.Por;
import vega.uplink.commanding.PorUtils;
import vega.uplink.commanding.SuperPor;

public class SolarUtils {
    public static void mergeSSMMPors(String path,String stp,String version) throws IOException {
        String original_porg_file = path+"PORG_SSGS_S"+stp+"_E_0000"+version+".ZIP";
        SuperPor porg = PorUtils.readPORGfromFile(original_porg_file,false);
        Por[] original_pors = porg.getPors();
        Por[] pors_to_modify=Arrays.copyOfRange(original_pors, 14, original_pors.length);
        String name_of_final_por = pors_to_modify[0].getName();
        System.out.println( "merging following files into "+name_of_final_por);
        for (Por p : pors_to_modify) {
            System.out.println(p.getName());
        }
        Date startDate = pors_to_modify[0].getValidityDates()[0];
        Date endDate = pors_to_modify[pors_to_modify.length-1].getValidityDates()[1];
        SuperPor sPor = new SuperPor();
        for (Por p : pors_to_modify) {
            sPor.addPor(p);
        }
        Por newPor = sPor.toSinglePor();
        Date[] validityDates={startDate,endDate};
        newPor.setValidityDates(validityDates);
        newPor.setName(name_of_final_por);
        SuperPor porgD = new SuperPor();
        
        Por[] pors_to_add_untouched = Arrays.copyOfRange(original_pors, 0, 14);
        for (Por p : pors_to_add_untouched) {
            porgD.addPor(p);
        }
        porgD.addPor(newPor);
        PorUtils.writePORGtofile(path+"/PORG_SSGS_S"+stp+"_E_0000"+version+"-modified.ZIP",porgD,"MAN__SSGS_S"+stp+"_E_0000"+version+".SOL");
        System.out.println(path+"/PORG_SSGS_S"+stp+"_E_0000"+version+"-modified.ZIP created");

    }
    public static void main(String[] args) {
        if (args.length==0) {
            printUsage();
            System.exit(0);
        }
        if (args[0]=="mergeSSMMPors") {
            if (args.length!=4) {
                printUsage();
                System.exit(0);
            }
            
        }else {
            try {
                mergeSSMMPors(args[1],args[2],args[3]);
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
    }
    private static void printUsage() {
        System.out.println("Usage:");
        System.out.println("\tmergeSSMMPors [path_to_original_por] [stp] [version_of_the_por]");
        System.out.println("\t\texample: mergeSSMMPors /Users/john/Downloads/ 346 1");
        System.out.println("\t\tto merge /Users/john/Downloads/PORG_SSGS_S346_E_00001.ZIP");
    }

}
