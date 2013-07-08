package scripts.sequenceScripts;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;

import utils.ConfigReader;

import coPhylog.CoPhylogBinaryFileReader;
import coPhylog.ContextCount;

public class GenerateDistances 
{
	private static class Holder
	{
		private long aLong;
		private ContextCount cc1;
		private ContextCount cc2;
	}
	
	public static void main(String[] args) throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(  
				ConfigReader.getBurkholderiaDir()+ File.separator + "distances" +
						File.separator + "techReps1.txt")));
		
		HashMap<Long, ContextCount> map1 = 
				CoPhylogBinaryFileReader.readBinaryFile(new File(ConfigReader.getBurkholderiaDir() +
						File.separator + "results" + File.separator + 
						"AS130-2_ATCACG_s_2_1_sequence.txt.gz_CO_PhylogBin.gz"));
		
		System.out.println("got map1 " + map1.size());
		
		HashMap<Long, ContextCount> map2 = 
				CoPhylogBinaryFileReader.readBinaryFile(new File(ConfigReader.getBurkholderiaDir() +
						File.separator + "results" + File.separator + 
						"AS130-2_ATCACG_s_2_2_sequence.txt.gz_CO_PhylogBin.gz"));
		
		System.out.println("got map2 " + map2.size());
		
		int numSearched =0;
		int numFound =0;
		int numDiff =0;
		for(Long l1 : map1.keySet())
		{
			ContextCount cc1 = map1.get(l1);
			numSearched++;
			
			ContextCount cc2 = map2.get(l1);
			
			if( cc2 != null)
			{
				numFound++;
				
				map2.remove(cc2);
				
				if( cc1.isDifferentInHighest(cc2))
					numDiff++;
			}
			
			if( numSearched % 1000==0)
				System.out.println(numSearched + " " + numFound + " "+ numDiff);
		}
		

		System.out.println(numSearched + " " + numFound + " "+ numDiff + " " + map2.size());
		
		
	}
}
