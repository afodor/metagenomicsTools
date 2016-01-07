package creOrthologs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import utils.ConfigReader;

public class MeanFileLine
{
	private final float meanRes;
	private final float meanSuc;
	private final float meanCar;
	
	private MeanFileLine(String s) throws Exception
	{
		String[] splits = s.split("\t");
		
		if(splits.length != 4)
			throw new Exception("No");
		
		this.meanRes = Float.parseFloat(splits[1]);
		this.meanSuc = Float.parseFloat(splits[2]);
		this.meanCar = Float.parseFloat(splits[3]);
	}
	
	public float getMeanRes()
	{
		return meanRes;
	}

	public float getMeanSuc()
	{
		return meanSuc;
	}

	public float getMeanCar()
	{
		return meanCar;
	}

	public static HashMap<String, MeanFileLine> getMeans() throws Exception
	{
		HashMap<String, MeanFileLine> map = 
				new HashMap<String, MeanFileLine>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getCREOrthologsDir() + File.separator + 
					"means.txt")));
		
		reader.readLine();
		
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			
			String key = splits[0].replaceAll("\"", "");
			
			if( map.containsKey(key))
				throw new Exception("No");
			
			map.put(key, new MeanFileLine(s));
		}
		
		return map;
	}
}
