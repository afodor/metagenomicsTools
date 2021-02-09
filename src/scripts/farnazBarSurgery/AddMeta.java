package scripts.farnazBarSurgery;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.StringTokenizer;

import parsers.OtuWrapper;

public class AddMeta
{
	public static final String[] LEVELS = {  "phylum", "class", "order" , "family", "genus" };
	
	public static void main(String[] args) throws Exception
	{
		for(String s : LEVELS)
		{
			addALevel(s);
		}
	}
	
	private static void addALevel(String level)  throws Exception
	{
		HashMap<String, MetaParser1> metaMap1 = MetaParser1.getMetaMap1();
		HashMap<String, String> typeOfSurgerymap = MetaParser1.getSurgeryType();
		HashMap<String, MetaParser2> metaMap2 = MetaParser2.getMeta2Map();
		
		OtuWrapper unnormalizedWrapper = new OtuWrapper(
				"C:\\BariatricSurgery_Analyses2021-main\\input\\none-normalized\\bariatricSurgery_Sep152020_2_2020Sep15_taxaCount_" + level +  ".tsv");
				 
		
		@SuppressWarnings("resource")
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				"C:\\BariatricSurgery_Analyses2021-main\\input\\bariatricSurgery_Sep152020_2_2020Sep15_taxaCount_norm_Log10_" + level + ".tsv")));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter( new File(
				"C:\\BariatricSurgery_Analyses2021-main\\input\\AF_Merged\\mergedMeta_" + level + ".txt") ));
		
		String[] topSplits = reader.readLine().split("\t");
		
		writer.write("sampleID\tshannonDiversity\ttimepoint\ttypeOfSurgery\tpatientId\tsite\tsampleType\t");
		
		writer.write("age\tblWeightInPounds\toneMonthWeightInPounds\tsixMonthWeightInPoinds\t" + 
				"twelveMonthWeightInPounds\t" + "percentChangeBLOneWeight\tpercentChangeBLSixWeight\tpercentChangeBLTwelveWeight\t" + 
				"percentChangeOneSixWeight\tpercentChangeOneTwelveWeight\tpercentChangeSixTwelveWeek");
		
		for( int x=1; x < topSplits.length; x++)
			writer.write("\t" + topSplits[x]);
		
		writer.write("\n");
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			String sampleId = new StringTokenizer(splits[0], "_").nextToken();
			writer.write(sampleId + "\t");
			writer.write(unnormalizedWrapper.getShannonEntropy(splits[0]) + "\t");
			
			if(  splits[0].startsWith("BS"))
			{
				StringTokenizer sToken =  new StringTokenizer(splits[0], "_");
				sampleId = sToken.nextToken() + "_" + sToken.nextToken();
			}
			
			if( sampleId.startsWith("1") )
			{
				//System.out.println(sampleId);
				StringTokenizer sToken = new StringTokenizer(sampleId, "-");
				String firstToken = sToken.nextToken();
				String secondToken = sToken.nextToken();
				String thirdToken = sToken.nextToken();
				
				if( thirdToken.charAt(0) == '0' && thirdToken.length() > 1)
					thirdToken = "" + thirdToken.charAt(1);
				
				sampleId = firstToken + "-" + secondToken+ "-" + thirdToken;
			}
			
			if( ! metaMap1.containsKey(sampleId))
			{
				System.out.println("Could not find " + sampleId + " for meta " );
				
				for( int x=0; x < 15; x++)
					writer.write("NA\t");
				
			}
			else
			{
				MetaParser1 mp1 = metaMap1.get(sampleId);
				
				String patientID = mp1.getPatientId();
				
				writer.write( mp1.getTimepoint() +"\t" );
				
				String shortPatientID = patientID.substring(0, patientID.lastIndexOf("-"));
				
				if( ! typeOfSurgerymap.containsKey(shortPatientID))
				{
					System.out.println("COuld not find " + shortPatientID+ " for surgery " + patientID );
					writer.write("NA\t");
				}
				else
				{
					writer.write(typeOfSurgerymap.get(shortPatientID) + "\t");
				}
				
				writer.write(shortPatientID + "\t" + mp1.getSite() + "\t" + mp1.getSampleType());
				
				
				if( ! metaMap2.containsKey(shortPatientID))
				{
					System.out.println("Could not find " + shortPatientID + " for metamap2 " + patientID);
					
					for( int x=0; x < 11; x++)
						writer.write("\t" + "NA");
					
				}
				else
				{
					MetaParser2 mp2 = metaMap2.get(shortPatientID);
					
					writer.write("\t"+ mp2.getAge() + "\t" +  mp2.getBlWeightInPounds() + "\t" + getValOrNA( mp2.getOneMonthWeightInPounds() )+ "\t"+ 
								getValOrNA(	mp2.getSixMonthWeightInPounds()) + "\t" + getValOrNA( mp2.getTwelveMonthWeightInPounds()) + "\t" + 
								getPercentWeightChange(mp2.getBlWeightInPounds(),mp2.getOneMonthWeightInPounds()) + "\t" + 
								getPercentWeightChange(mp2.getBlWeightInPounds(),mp2.getSixMonthWeightInPounds()) + "\t" + 
								getPercentWeightChange(mp2.getBlWeightInPounds(),mp2.getTwelveMonthWeightInPounds()) + "\t" + 
											getPercentWeightChange(mp2.getOneMonthWeightInPounds(),mp2.getSixMonthWeightInPounds()) 
												+"\t" + getPercentWeightChange(mp2.getOneMonthWeightInPounds(),mp2.getTwelveMonthWeightInPounds())
												+ "\t" + getPercentWeightChange(mp2.getSixMonthWeightInPounds(),mp2.getTwelveMonthWeightInPounds())
							);		
				}
			}
				
			for(int x=1; x < splits.length; x++)
				writer.write("\t" + splits[x]);
			
			writer.write("\n");
		}
		
		writer.flush();  writer.close();
		reader.close();
	}
	
	private static String getValOrNA(Double d)
	{
		if( d== null)
			return "NA";
		
		return "" + d;
	}
	
	private static String getPercentWeightChange(Double early, Double late)
	{
		if( early == null || late == null)
			return "NA";
		
		return "" + ( 100  * ( late-early )/ early );
	}

}
