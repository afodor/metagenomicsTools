package scripts.ratSach2.newNancyCodes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import utils.ConfigReader;

public class NewNancyCodesMetaline
{
	private final String jobinID;
	private final String condition;
	private final int time;
	private final int batch;
	private final int line;
	
	public String getJobinID()
	{
		return jobinID;
	}

	public String getCondition()
	{
		return condition;
	}

	public int getTime()
	{
		return time;
	}

	public int getBatch()
	{
		return batch;
	}

	public int getLine()
	{
		return line;
	}

	private NewNancyCodesMetaline(String s) throws Exception
	{
		String[] splits = s.split("\t");
		
		int asNum = Integer.parseInt(splits[0]);
		
		String asString = "" + asNum;
		
		while(asString.length() < 4)
			asString = "0" + asString;
		
		this.jobinID = "sample" + asString;
		this.condition = splits[3];
		this.time = Integer.parseInt(splits[5]);
		this.batch = Integer.parseInt(splits[6]);
		this.line = Integer.parseInt(splits[7]);
		
	}
	
	public static HashMap<String, NewNancyCodesMetaline> getMetaMap() throws Exception
	{
		HashMap<String, NewNancyCodesMetaline> map = new HashMap<String, NewNancyCodesMetaline>();
		
		BufferedReader reader =new BufferedReader(new FileReader(new File(
				ConfigReader.getRachSachReanalysisDir() + File.separator + "codes from Anthony and NKD.txt"	)));
			
		reader.readLine();
		reader.readLine();
		
		for(String s = reader.readLine(); s != null && s.trim().length() > 0 ; s= reader.readLine())
		{
			NewNancyCodesMetaline nncm = new NewNancyCodesMetaline(s);
			
			if( map.containsKey(nncm.jobinID))
				throw new Exception("No");
			
			map.put(nncm.jobinID, nncm);
		}
		
		return map;
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, NewNancyCodesMetaline> map = getMetaMap();
		
		for(String s : map.keySet())
			System.out.println(s);
		
		System.out.println(map.size());
	}
}
