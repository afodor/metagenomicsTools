package mbqc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import utils.ConfigReader;

public class RawDesignMatrixParser
{
	private final String id;
	private final String extractionWetlab;
	private final String sequecingWetlab;
	private final String mbqcID;
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, RawDesignMatrixParser> map = getByFullId();
		
		System.out.println("Got map with " + map.size());
	}
	
	public static HashMap<String, RawDesignMatrixParser> getByFullId() throws Exception
	{
		HashMap<String, RawDesignMatrixParser> map = new HashMap<String, RawDesignMatrixParser>();
		
		BufferedReader reader = new BufferedReader(new FileReader(ConfigReader.getMbqcDir() + File.separator + 
				"dropbox" + File.separator + 
				"raw_design_matrix.txt"));
		
		reader.readLine();
		
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			
			if (map.containsKey(splits[0]))
				throw new Exception("No");
			
			map.put(splits[0], new RawDesignMatrixParser(splits));
		}
		
		return map;
	}
	
	private RawDesignMatrixParser(String[] splits)
	{
		this.id = new String(splits[0]);
		this.extractionWetlab = new String(splits[1]);
		this.sequecingWetlab = new String(splits[2]);
		this.mbqcID = new String(splits[3]);
	}

	public String getId()
	{
		return id;
	}

	public String getExtractionWetlab()
	{
		return extractionWetlab;
	}

	public String getSequecingWetlab()
	{
		return sequecingWetlab;
	}

	public String getMbqcID()
	{
		return mbqcID;
	}
}
