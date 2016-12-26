package scripts.topeOneAtATime;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import utils.ConfigReader;
import utils.TabReader;

public class Nov2016MetadataParser
{
	//studyid	waist	tics_count	age	sex	bmi	whr	wbo	BMI_Cat

	private final Integer waist;
	private final Integer ticsCount;
	private final Integer age;
	private final Integer sex;
	private final Double bmi;
	private final Double whr;
	private final String wbo;
	private final String bmi_CAT;
	
	private Nov2016MetadataParser(String s )
	{
		TabReader tReader = new TabReader(s);
		
		tReader.nextToken();
		this.waist = getIntOrNull(tReader);
		this.ticsCount = getIntOrNull(tReader);
		this.age = getIntOrNull(tReader);
		this.sex = getIntOrNull(tReader);
		this.bmi = getDoubleOrNull(tReader);
		this.whr = getDoubleOrNull(tReader);
		this.wbo = getStringOrNull(tReader);
		this.bmi_CAT = getStringOrNull(tReader);
	}
	
	public Integer getWaist()
	{
		return waist;
	}

	public Integer getTicsCount()
	{
		return ticsCount;
	}

	public Integer getAge()
	{
		return age;
	}

	public Integer getSex()
	{
		return sex;
	}

	public Double getBmi()
	{
		return bmi;
	}

	public Double getWhr()
	{
		return whr;
	}

	public String getWbo()
	{
		return wbo;
	}

	public String getBmi_CAT()
	{
		return bmi_CAT;
	}

	private Double getDoubleOrNull(TabReader tReader)
	{
		String next = tReader.getNext();
		
		if( next.length() == 0)
			return null;
		
		return new Double(next);
	}
	
	private String getStringOrNull(TabReader tReader)
	{
		String next = tReader.getNext();
		
		if( next.length() == 0)
			return null;
		
		return next;
	}
	
	private Integer getIntOrNull(TabReader tReader)
	{
		String next = tReader.getNext();
		
		if( next.length() == 0)
			return null;
		
		return new Integer(next);
	}
	
	public static HashMap<String, Nov2016MetadataParser> getMetaMap() throws Exception
	{
		HashMap<String, Nov2016MetadataParser> map = new HashMap<String, Nov2016MetadataParser>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getTopeOneAtATimeDir() + File.separator + 
					"tk_out_22Nov2016.txt")));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			if(map.containsKey(splits[0]))
				throw new Exception("Duplicate");
			
			map.put(splits[0], new Nov2016MetadataParser(s));
		}
		
		return map;
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, Nov2016MetadataParser> map = getMetaMap();
		
		for(String s : map.keySet())
			System.out.println(s + " " + map.get(s).bmi + " " + map.get(s).bmi_CAT);
	}
}
