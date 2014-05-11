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
	private final String tissue;
	private final String dateString;
	private final String hospital;
	
	public int getStrainID()
	{
		return strainID;
	}

	public String getTissue()
	{
		return tissue;
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
		tReader.nextToken();
		this.strainID = Integer.parseInt(tReader.nextToken());
		
		for( int x=0; x < 7; x++)
			tReader.nextToken();
		
		this.tissue = tReader.nextToken().toLowerCase();
		this.dateString = tReader.nextToken();
		this.hospital = tReader.nextToken().toLowerCase();
	}
	
	@Override
	public String toString()
	{
		return this.strainID + " " + this.dateString + " " + this.tissue+ " " + 
				this.hospital;
	}
	
	public String getColorStringByLocation() throws Exception
	{
		if( this.hospital.equals("metro"))
			return "<color><red>255</red><green>0</green><blue>0</blue></color>";
		
		if( this.hospital.equals("reg"))
			return "<color><red>0</red><green>255</green><blue>0</blue></color>";
		
		if( this.hospital.equals("op"))
			return "<color><red>0</red><green>0</green><blue>255</blue></color>";
		
		throw new Exception("Unknown " + this.hospital );
	}
	
	public static HashMap<Integer,StrainMetadataFileLine> parseMetadata() throws Exception
	{
		HashMap<Integer,StrainMetadataFileLine>  map = new HashMap<Integer, StrainMetadataFileLine>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getKlebDir() + File.separator + 
				"cre_for_broad_final_OrdByKlebTree_5-7-14.txt")));
		
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
