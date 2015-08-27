package scripts.markSeqsAug2015;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import utils.ConfigReader;

public class MetadataParserFileLine
{
	//#SampleID	SampleName	IndexPrimer5	Indexprimer7	sex	treat	cage	exp	batch
	private final String sampleID;
	private final String sampleName;
	private final String acuteOrChronic;
	private final String sex;
	private final String treatment;
	private final String cage;
	private final String expriment;
	private final String batch;
	
	
	
	public String getSampleID()
	{
		return sampleID;
	}

	public String getSampleName()
	{
		return sampleName;
	}

	public String getAcuteOrChronic()
	{
		return acuteOrChronic;
	}

	public String getSex()
	{
		return sex;
	}

	public String getTreatment()
	{
		return treatment;
	}

	public String getCage()
	{
		return cage;
	}

	public String getExpriment()
	{
		return expriment;
	}

	public String getBatch()
	{
		return batch;
	}

	private MetadataParserFileLine(String s) throws Exception
	{
		String[] splits = s.split("\t");
		
		this.sampleID = splits[0];
		this.sampleName = splits[1];
		
		if( splits[2].length() == 2)
		{
			this.acuteOrChronic = "" + splits[2].charAt(0);
			this.sex = "" + splits[2].charAt(1);
		}
		else
		{
			this.acuteOrChronic = splits[2];
			this.sex = splits[2];
		}
		
		this.treatment = splits[5];
		this.cage = splits[6];
		this.expriment  = splits[7];
		this.batch = splits[8];
		
		if ( splits.length != 9 )
			throw new Exception("NO");
	}
	
	public static HashMap<String, MetadataParserFileLine> getMap() throws Exception
	{
		HashMap<String, MetadataParserFileLine> map = new HashMap<String, MetadataParserFileLine>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(ConfigReader.getMarkAug2015Batch1Dir()+
				File.separator + "batch2_map.txt")));

		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s = reader.readLine())
		{
			String key = s.split("\t")[0];
			
			if( map.containsKey(key))
				throw new Exception("No");
			
			map.put(key, new MetadataParserFileLine(s));
		}
		
		reader.close();
		
		return map;
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, MetadataParserFileLine> map =getMap();
		
		for(String s : map.keySet())
			System.out.println(s + " "  + map.get(s).getCage());
	}
}
