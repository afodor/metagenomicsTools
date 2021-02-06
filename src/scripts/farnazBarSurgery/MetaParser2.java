package scripts.farnazBarSurgery;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.LinkedHashMap;

import utils.TabReader;

public class MetaParser2
{
	private final String patientID;
	private final double site;
	private final double age;
	private final double blWeightInPounds;
	private final Double oneMonthWeightInPounds;
	private final Double sixMonthWeightInPoinds;
	private final Double twelveMonthWeightInPounds;
	
	public double getSite()
	{
		return site;
	}

	public double getAge()
	{
		return age;
	}

	public double getBlWeightInPounds()
	{
		return blWeightInPounds;
	}

	public Double getOneMonthWeightInPounds()
	{
		return oneMonthWeightInPounds;
	}

	public Double getSixMonthWeightInPoinds()
	{
		return sixMonthWeightInPoinds;
	}

	public Double getTwelveMonthWeightInPounds()
	{
		return twelveMonthWeightInPounds;
	}

	public String getPatientID()
	{
		return patientID;
	}
	
	private static Double getValOrNull(String s )
	{
		if( s.equals("#NULL!"))
			return null;
		
		return Double.parseDouble(s);
	}
	
	private MetaParser2(String s) throws Exception
	{
		String[] splits= s.split("\t");
		this.patientID =splits[0];
		this.site = Double.parseDouble(splits[1]);
		this.age = Double.parseDouble(splits[2]);
		this.blWeightInPounds = Double.parseDouble( TabReader.getTokenAtIndex(s, 12));
		this.oneMonthWeightInPounds = getValOrNull(TabReader.getTokenAtIndex(s, 19));
		this.sixMonthWeightInPoinds =getValOrNull(TabReader.getTokenAtIndex(s, 26));
		this.twelveMonthWeightInPounds = getValOrNull(TabReader.getTokenAtIndex(s, 31));
	}
	
	public static HashMap<String, MetaParser2> getMeta2Map() throws Exception
	{
		HashMap<String, MetaParser2> map = new LinkedHashMap<String, MetaParser2>();
		
		@SuppressWarnings("resource")
		BufferedReader reader = new BufferedReader(new FileReader("C:\\BariatricSurgery_Analyses2021-main\\input\\Metadata\\reexternalredatarequest\\MASTER Microbiome paper_FF.txt"));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			
			MetaParser2 mp2 = new MetaParser2(s);
			
			if( map.containsKey(mp2.getPatientID()))
				throw new Exception("Duplicate");
			
			map.put(mp2.getPatientID(), mp2);
		}
		
		
		return map;
	}
}
