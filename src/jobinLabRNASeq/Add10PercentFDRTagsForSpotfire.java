package jobinLabRNASeq;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashSet;

import utils.ConfigReader;

public class Add10PercentFDRTagsForSpotfire
{
	private static HashSet<String> getTenPercentSet(String filePath) throws Exception
	{
		HashSet<String> set = new HashSet<String>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(filePath)));
		
		reader.readLine();
		
		for(String s= reader.readLine();
					s != null;
						s = reader.readLine())
		{
			String[] splits = s.split("\t");
			
			if( Double.parseDouble(splits[3]) <= 0.10)
				set.add(splits[0]);
		}
		
		reader.close();
		return set;
	}
	
	public static void main(String[] args) throws Exception
	{
		HashSet<String> twoDayVs12WeekSet = getTenPercentSet(ConfigReader.getJobinLabRNASeqDir() + File.separator + 
				"2DayVs12WeeksPlusAnnotation.txt" );
		
		HashSet<String> twoDayVs18WeekSet = getTenPercentSet(ConfigReader.getJobinLabRNASeqDir() + File.separator + 
				"2DayVs18WeeksPlusAnnotation.txt" );
		
		HashSet<String> twelveWeekVs18WeekSet = getTenPercentSet(ConfigReader.getJobinLabRNASeqDir() + File.separator + 
				"12WeeksVs18WeeksPlusAnnotation.txt" );
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(ConfigReader.getJobinLabRNASeqDir() + File.separator + 
				"pivotedSamplesAsColumnsR1Only.txt")));
		
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(ConfigReader.getJobinLabRNASeqDir() + File.separator + 
				"pivotedSamplesAsColumnsR1OnlyPlusDisposition.txt")));
		
		writer.write(reader.readLine() + "\tfate\n");
		
		for(String s = reader.readLine(); 
				s != null;
				 s= reader.readLine())
		{
			writer.write(s + "\t");
			
			String fate = "";
			
			String key = s.split("\t")[0];
			
			if( twoDayVs12WeekSet.contains(key) )
				fate = fate + "2Vs12";
			
			if( twoDayVs18WeekSet.contains(key))
				fate = fate + "2Vs18";
			
			if(  twelveWeekVs18WeekSet.contains(key) )
				fate = fate + "12Vs18";
			
			if( fate.length() == 0 )
				fate = "NS";
			
			writer.write(fate + "\n");
		}
		
		writer.flush();  writer.close();
		
		reader.close();
		
	}
}
