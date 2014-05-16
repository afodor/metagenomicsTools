package kleb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import utils.ConfigReader;
import utils.TabReader;

public class StrainMetadataFileLine
{
	private final int strainID;
	private final String dateString;
	private final String hospital;
	
	public int getStrainID()
	{
		return strainID;
	}

	public String getDateString()
	{
		return dateString;
	}

	public String getHospital()
	{
		return hospital;
	}

	private StrainMetadataFileLine(String s) throws Exception
	{
		TabReader tReader = new TabReader(s);
		
		for( int x=0; x < 4; x++)
			tReader.nextToken();
		
		this.dateString = tReader.nextToken();
		
		tReader.nextToken();
		this.strainID = Integer.parseInt(tReader.nextToken());
		this.hospital = tReader.nextToken();
	}
	
	@Override
	public String toString()
	{
		return this.strainID + " " + this.dateString + " " +
				this.hospital;
	}
	
	
	public String getColorStringByLocation() throws Exception
	{
		if( this.hospital.equals("CMC Main"))
			return "<color><red>255</red><green>0</green><blue>0</blue></color>";
		
		if( this.hospital.equals("Rehab"))
			return "<color><red>0</red><green>255</green><blue>0</blue></color>";
		
		if( this.hospital.equals("Mercy"))
			return "<color><red>0</red><green>0</green><blue>255</blue></color>";
		
		if( this.hospital.equals("Outpatient"))
			return "<color><red>128</red><green>128</green><blue>128</blue></color>";
		
		if( this.hospital.equals("University"))
			return "<color><red>255</red><green>255</green><blue>0</blue></color>";
			
		return "";
	}
	
	public static HashMap<Integer,StrainMetadataFileLine> parseMetadata() throws Exception
	{
		HashMap<Integer,StrainMetadataFileLine>  map = new HashMap<Integer, StrainMetadataFileLine>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getKlebDir() + File.separator + 
				"Corrected_SpreadsheetSamplesSentToBroadWithFacilities_5-16-14_AF.txt")));
		
		reader.readLine(); reader.readLine();
		
		for(String s = reader.readLine(); s != null && s.trim().length() > 0; s= reader.readLine())
		{
			StrainMetadataFileLine mfl = new StrainMetadataFileLine(s);
				
				if( map.containsKey(mfl.strainID))
					throw new Exception("Parsing error " + mfl.strainID);
				
				map.put(mfl.strainID, mfl);

		}
		return map;
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<Integer, StrainMetadataFileLine> map = parseMetadata();
		
		for( Integer key :map.keySet())
			System.out.println(map.get(key));
	}
}
