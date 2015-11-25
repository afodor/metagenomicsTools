package jobinLabRnaSeqHMMs;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import jobinLabRNASeq.TimeAnnotations;

import utils.ConfigReader;

public class FractionrRNA
{
	public static final String INTER_GENIC = "INTER_GENIC";
	public static final String ECNC101_r07204 = "ECNC101_r07204";
	public static final String ECNC101_r07200 = "ECNC101_r07200";

	
	private static class Holder
	{
		private String name;
		private float numInterGenic=0;
		private float numECNC101_r07204=0;  //23S rRNA
		private float ECNC101_r07200 =0; // 16S rRNA
		
		private float totalNum =0;
	}
	
	public static void main(String[] args) throws Exception
	{
		writeResults(getList());
	}
	
	private static void writeResults(List<Holder> list) throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(ConfigReader.getJobinLabRNASeqDir() + File.separator + 
				"rRNASummary.txt")));
		
		writer.write("name\ttotalNum\tratioIntergenic\tratio23S\tratio16S\ttotalRNA\tannotation\n");
		
		for( Holder h : list)
		{
			String label = new StringTokenizer(h.name, "MM").nextToken();
			writer.write( (label.startsWith("10") ||label.startsWith("11")  ? "" : "0") +  label+ "MM\t");
			writer.write(h.totalNum + "\t");
			writer.write(h.numInterGenic / h.totalNum + "\t");
			writer.write(h.numECNC101_r07204 / h.totalNum + "\t");
			writer.write(h.ECNC101_r07200 / h.totalNum + "\t");
			writer.write( (h.numECNC101_r07204 + h.ECNC101_r07200) / h.totalNum + "\t");
			writer.write(TimeAnnotations.getAnnotation(h.name) + "\n");
		}
		
		writer.flush();
		writer.close();
	}
	
	private static List<Holder> getList() throws Exception
	{
		List<Holder> list = new ArrayList<Holder>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File( 
				ConfigReader.getJobinLabRNASeqDir() + File.separator + "pivotedSamplesAsColumns.txt")));
		
		String top =reader.readLine();
		
		StringTokenizer sToken = new StringTokenizer(top);
		
		sToken.nextToken();
		
		while(sToken.hasMoreTokens())
		{
			Holder h = new Holder();
			h.name = sToken.nextToken();
			
			list.add(h);
			
		}
		
		for(String s= reader.readLine();
					s != null;
						s = reader.readLine())
		{
			sToken =new StringTokenizer(s);
			
			String gene = sToken.nextToken();
			
			for( Holder h : list)
			{
				float num = Float.parseFloat(sToken.nextToken());
				h.totalNum += num;
				
				if( gene.equals(INTER_GENIC) )
				{
					if( h.numInterGenic != 0)
						throw new Exception("No");
					
					h.numInterGenic = num;
				}
				
				if( gene.equals(ECNC101_r07200) )
				{
					if( h.ECNC101_r07200!= 0)
						throw new Exception("No");
					
					h.ECNC101_r07200= num;
				}
				
				if( gene.equals(ECNC101_r07204) )
				{
					if( h.numECNC101_r07204!= 0)
						throw new Exception("No");
					
					h.numECNC101_r07204= num;
				}
			}
		}
		
		
		return list;
	}
}
