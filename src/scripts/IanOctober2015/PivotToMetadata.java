package scripts.IanOctober2015;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

import parsers.NewRDPParserFileLine;
import utils.ConfigReader;

public class PivotToMetadata
{
	private final int patientID;
	private Double calorimetryData1 = null;
	private Double calorimetryData2 = null;
	
	public int getPatientID()
	{
		return patientID;
	}
	
	public Double getCalorimetryData1()
	{
		return calorimetryData1;
	}
	
	public Double getCalorimetryData2()
	{
		return calorimetryData2;
	}
	
	private PivotToMetadata(String s) throws Exception
	{
		String[] splits = s.split("\t");
		this.patientID  = Integer.parseInt(splits[0]);
		setCalorimetery(s);
	}
	
	private void setCalorimetery(String s ) throws Exception
	{

		String[] splits = s.split("\t");
	
		if( splits.length != 3)
			throw new Exception("No " + splits);
		
		if( splits[1].equals("1") && this.calorimetryData1 != null)
			throw new Exception("No");
		
		if( splits[1].equals("2") && this.calorimetryData2 != null)
			throw new Exception("No");
		
		if( splits[1].equals("1"))
			this.calorimetryData1 = Double.parseDouble(splits[2]);
		else if( splits[1].equals("2"))
			this.calorimetryData2 = Double.parseDouble(splits[2]);
		else 
			throw new Exception("No");
	}
	
	public static void main(String[] args) throws Exception
	{

		for( int x=1; x < NewRDPParserFileLine.TAXA_ARRAY.length; x++)
			pivotALevel(NewRDPParserFileLine.TAXA_ARRAY[x]);
	}
	
	private static void pivotALevel(String level) throws Exception
	{
		System.out.println("Level " + level);
		HashMap<Integer, PivotToMetadata> map = getMap();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
					ConfigReader.getIanOct2015Dir() + 
				File.separator + "spreadsheetAs" + level + ".txt")));
		
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(		
				ConfigReader.getIanOct2015Dir() + File.separator + 
				level +  "_asColumnsWithMetadata.txt")));
		
		writer.write("patientID\ttimepoint\tcalorimetryData\tshannonDiversity");
		
		String[] topSplits = reader.readLine().split("\t");
		
		for( int y=2; y < topSplits.length; y++)
			writer.write("\t" + topSplits[y]);
		
		writer.write("\n");
	
		for(String s= reader.readLine(); s != null && s.trim().length() > 0 ; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			
			writer.write(splits[0] + "\t");
			
			int key = Integer.parseInt(splits[0].replace("ad", "").replace("dis", ""));
			
			int timepoint = Integer.parseInt(splits[1]);
			writer.write(timepoint + "\t");
			
			PivotToMetadata ptm = map.get(key);
			
			if( ptm == null)
			{
				writer.write("NA\t");
			}
			else
			{
				if( timepoint == 1)
				{
					writer.write(ptm.calorimetryData1 == null ? "NA\t"  : ptm.calorimetryData1 + "\t");
				}
				else if (timepoint == 2)
				{
					writer.write(ptm.calorimetryData2 == null ? "NA\t"  : ptm.calorimetryData2 + "\t");
				}
				else throw new Exception("No");
			}
			
			double sum = 0;
			
			for (int y=3; y < splits.length; y++)
			{
				double aVal = Double.parseDouble(splits[y]);
				
				if( aVal > 0 )
					sum += aVal * Math.log(aVal);
			}
			
			writer.write("" + -sum);
		
			for( int y=2; y < splits.length ; y++)
				writer.write("\t" + splits[y]);
			
			writer.write("\n");
		}
		
		writer.flush(); writer.close();
		
		reader.close();
	}
	
	private static HashMap<Integer, PivotToMetadata> getMap() throws Exception
	{
		HashMap<Integer, PivotToMetadata> map = new HashMap<Integer, PivotToMetadata>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(ConfigReader.getIanOct2015Dir()
				+ File.separator + "CalormiteryData.txt")));
		
		reader.readLine();
		
		for(String s = reader.readLine(); s != null && s.trim().length() > 0 ; s = reader.readLine())
		{
			
			PivotToMetadata ptm= map.get( Integer.parseInt(s.split("\t")[0]));
			
			if( ptm== null)
			{
				ptm = new PivotToMetadata(s);
				map.put(ptm.patientID, ptm);
			}
			else
			{
				ptm.setCalorimetery(s);
			}
			
		}
		
		reader.close();
		return map;
	}
	
	
}
