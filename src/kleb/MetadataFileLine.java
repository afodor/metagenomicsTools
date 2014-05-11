package kleb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import utils.ConfigReader;
import utils.TabReader;

public class MetadataFileLine
{
	private final int treeNumber;
	private final String tissue;
	private final String dateString;
	private final String hospital;
	
	public int getTreeNumber()
	{
		return treeNumber;
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

	private MetadataFileLine(String s) throws Exception
	{
		TabReader tReader = new TabReader(s);
		tReader.nextToken();
		this.treeNumber = Integer.parseInt(tReader.nextToken());
		
		for( int x=0; x < 7; x++)
			tReader.nextToken();
		
		this.tissue = tReader.nextToken().toLowerCase();
		this.dateString = tReader.nextToken();
		this.hospital = tReader.nextToken().toLowerCase();
	}
	
	@Override
	public String toString()
	{
		return this.treeNumber + " " + this.dateString + " " + this.tissue+ " " + 
				this.hospital;
	}
	
	public static HashMap<Integer,MetadataFileLine> parseMetadata() throws Exception
	{
		HashMap<Integer,MetadataFileLine>  map = new HashMap<Integer, MetadataFileLine>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getKlebDir() + File.separator + 
				"cre_for_broad_final_OrdByKlebTree_5-7-14.txt")));
		
		reader.readLine(); reader.readLine();
		
		for(String s = reader.readLine(); s != null && s.trim().length() > 0; s= reader.readLine())
		{
			MetadataFileLine mfl = new MetadataFileLine(s);
				
				if( map.containsKey(mfl.treeNumber))
					throw new Exception("Parsing error " + mfl.treeNumber);
				
				map.put(mfl.treeNumber, mfl);

		}
		return map;
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<Integer, MetadataFileLine> map = parseMetadata();
		
		for( Integer key :map.keySet())
			System.out.println(map.get(key));
	}
}
