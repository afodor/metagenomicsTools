package scripts.IanOctober2015;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

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
		for( int x=2; x <=6; x++)
			pivotALevel(x);
	}
	
	private static void pivotALevel(int x) throws Exception
	{
		System.out.println("Level " + x);
		HashMap<Integer, PivotToMetadata> map = getMap();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File( 
			ConfigReader.getIanOct2015Dir() + File.separator + 
			"Level_" + x +  "_asColumns.txt")));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(		
				ConfigReader.getIanOct2015Dir() + File.separator + 
				"Level_" + x +  "_asColumnsWithMetadata.txt")));
		
		writer.write("sample\tpatientID\ttimepoint\tcalorimetryData1\tcalorimetryData2");
		
		String[] topSplits = reader.readLine().split("\t");
		
		for( int y=1; y < topSplits.length; y++)
			writer.write("\t" + topSplits[y]);
		
		writer.write("\n");
	
		for(String s= reader.readLine(); s != null && s.trim().length() > 0 ; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			
			writer.write(splits[0] + "\t");
			
			int key = Integer.parseInt(splits[0].replace("ad", "").replace("dis", ""));
			
			writer.write(key + "\t");
			
			String timepoint = splits[0].replace("" + key, "");
			
			if( timepoint.length() == 0 )
				timepoint = "NA";
			
			writer.write(timepoint + "\t");
			
			PivotToMetadata ptm = map.get(key);
			
			if( ptm == null)
			{
				writer.write("NA\tNA");
			}
			else
			{
				writer.write(ptm.calorimetryData1 + "\t" + ptm.getCalorimetryData2());
			}	
				
			for( int y=1; y < splits.length ; y++)
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
			
		}
		
		reader.close();
		return map;
	}
	
	
}
