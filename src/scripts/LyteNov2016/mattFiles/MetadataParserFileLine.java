package scripts.LyteNov2016.mattFiles;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.StringTokenizer;

import utils.ConfigReader;

public class MetadataParserFileLine
{
	//Sample #	Source	Animal Nu	Exp or Ctrl	Experiment/ Sample info	Cage
	private final String sample;
	private final String source;
	private final int animal;
	private final String expControl;
	private final String date;
	private final String sex;
	private final String cage;
	
	public String getSample()
	{
		return sample;
	}

	public String getSource()
	{
		return source;
	}

	public int getAnimal()
	{
		return animal;
	}

	public String getExpControl()
	{
		return expControl;
	}

	public String getDate()
	{
		return date;
	}

	public String getSex()
	{
		return sex;
	}

	public String getCage()
	{
		return cage;
	}

	private MetadataParserFileLine(String s)
	{
		String[] splits = s.split("\t");
		this.sample = splits[0];
		this.source = splits[1];
		this.animal = Integer.parseInt(splits[2]);
		this.expControl = splits[3];
		StringTokenizer sToken = new StringTokenizer(splits[4]);
		this.date = sToken.nextToken();
		this.sex = sToken.nextToken();
		this.cage = splits[5];
		
	}
	
	public static HashMap<String, MetadataParserFileLine> getMetaMap() throws Exception
	{
		HashMap<String, MetadataParserFileLine> map = new HashMap<String, MetadataParserFileLine>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getLyteNov2016Dir() + File.separator + 
				"mattFiles" + File.separator +  "ArgonneMetadata.txt")));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			MetadataParserFileLine mpfl = new MetadataParserFileLine(s);
			
			if( map.containsKey(mpfl.sample))
				throw new Exception("Duplicate");
			
			map.put(mpfl.sample, mpfl);
		}
		
		reader.close();
		
		return map;
	}
}
