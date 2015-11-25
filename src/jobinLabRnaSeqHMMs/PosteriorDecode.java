package jobinLabRnaSeqHMMs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import utils.ConfigReader;

public class PosteriorDecode
{
	private static class Holder implements Comparable<Holder>
	{
		@SuppressWarnings("unused")
		String geneId;
		@SuppressWarnings("unused")
		double foldChange;
		double pVal;
		@SuppressWarnings("unused")
		double pValAdjusted;
		@SuppressWarnings("unused")
		String annotation;
		@SuppressWarnings("unused")
		String gffFile;
		int position;
		@SuppressWarnings("unused")
		String cogID;
		@SuppressWarnings("unused")
		String cogAnnotation;
		@SuppressWarnings("unused")
		String cogFunction;
		@SuppressWarnings("unused")
		String cogFunctionalAnnotation;
		String fileLine;
		
		@Override
		public int compareTo(Holder arg0)
		{
			return this.position - arg0.position;
		}
	}
	
	private static HashMap<String, List<Holder>> getMap(String filePath) throws Exception
	{
		HashMap<String, List<Holder>> map = new HashMap<String, List<Holder>>();
		BufferedReader reader = new BufferedReader(new FileReader(new File( 
			filePath	)));
		
		reader.readLine();
		
		for(String s = reader.readLine();
					s != null;
						s = reader.readLine())
		{
			String[] splits = s.split("\t");
			Holder h = new Holder();
			h.fileLine = s;
			h.geneId = splits[0];
			h.foldChange = Double.parseDouble(splits[1]);
			h.pVal = Double.parseDouble(splits[2]);
			h.pValAdjusted = Double.parseDouble(splits[3]);
			h.annotation = splits[4];
			String gffString = splits[5];
			h.gffFile = gffString;
			h.position = Integer.parseInt(splits[6]);
			h.cogID = splits[7];
			h.cogAnnotation = splits[8];
			h.cogFunction = splits[9];
			h.cogFunctionalAnnotation = splits[10];
			List<Holder> list = map.get(gffString);
			
			if( list == null)
			{
				list = new ArrayList<Holder>();
				map.put(gffString, list);
			}
			
			
			list.add(h);
		}
		
		reader.close();
		
		for( String s : map.keySet())
			Collections.sort(map.get(s));
		
		return map;
	}
	
	/*
	 * Requires the output of jobinLabRNASeq.AddNamesToROutput
	 */
	public static void main(String[] args) throws Exception
	{
		
		HashMap<String, List<Holder>> dataMap = 
				getMap(ConfigReader.getJobinLabRNASeqDir() + File.separator + "12WeeksVs18WeeksPlusAnnotation.txt");
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(ConfigReader.getJobinLabRNASeqDir() + File.separator + 
				"12WeeksVs18WeeksPlusAnnotationDecoded.txt"));
		
		writer.write("id\tfoldChange\tlnpval\tpValAdjusted\tannotation\tgffFile\tstartPos\t");
		writer.write("cogID\tcogAnnotation\tcogFunctionalChar\tcogFunctionalAnnotation\t");
		writer.write("downProb\tequalProb\tupProb\n");
		
		for(String s : dataMap.keySet())
		{
			List<Holder> list = dataMap.get(s);
			
			double[] emissions = new double[list.size()];
			double[] foldChanges = new double[list.size()];
			
			int index =0;
			for( Holder h : list)
			{
				emissions[index] = h.pVal;
				foldChanges[index] = h.pVal;
				index++;
			}
			
			MarkovModel mm = new DiffExpressMarkovModel();
			ForwardAlgorithm fa = new ForwardAlgorithm(mm, emissions, foldChanges);
			BackwardsAlgorithm ba = new BackwardsAlgorithm(mm, emissions, foldChanges);
			
			index=0;
			for(Holder h : list)
			{
				writer.write(h.fileLine);
			
				double sum = ForwardAlgorithm.logPAddedToQ(ba.getLogProbs()[0][index] + fa.getLogProbs()[0][index], 
						ba.getLogProbs()[1][index] + fa.getLogProbs()[1][index]);
				sum = ForwardAlgorithm.logPAddedToQ(sum, ba.getLogProbs()[2][index] + fa.getLogProbs()[2][index]);
				
				for( int y=0; y <= 2; y++)
				{
					double fTimesB = ba.getLogProbs()[y][index] + fa.getLogProbs()[y][index]; 
					
					if( Double.isInfinite(fTimesB) || Double.isNaN(fTimesB))
						writer.write("\t" + 0);
					else
						writer.write("\t" + Math.exp(fTimesB - sum));
				}
				
				writer.write("\n");
				index++;
			}
			
			writer.flush(); 
		}
		
		writer.flush();  writer.close();
	}
}
