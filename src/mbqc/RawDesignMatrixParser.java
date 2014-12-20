package mbqc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.StringTokenizer;

import utils.ConfigReader;

public class RawDesignMatrixParser
{
	private final String id;
	private final String extractionWetlab;
	private final String sequecingWetlab;
	private final String mbqcID;
	private final List<Double> taxaVals;
	
	public static final List<String> getTaxaIds() throws Exception
	{
		List<String> list = new ArrayList<String>();
		
		BufferedReader reader = new BufferedReader(new FileReader(ConfigReader.getMbqcDir() + File.separator + 
				"dropbox" + File.separator + 
				"raw_design_matrix.txt"));
		
		String[] splits = reader.readLine().split("\t");
		
		int x=4;
		
		while(splits[x].startsWith("k__"))
		{
			StringTokenizer sToken = new StringTokenizer(splits[x], ".");
			sToken.nextToken();
			
			list.add(sToken.nextToken().replace("p__", ""));
			x++;
		}
		
		reader.close();
		
		return list;
	}
	
	public List<Double> getTaxaVals()
	{
		return taxaVals;
	}
	
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, List<RawDesignMatrixParser>> map = getByLastTwoTokens();
		
		System.out.println("Got map with " + map.size());
		
		int numDups =0;
		
		for(String s : map.keySet())
		{
			if( map.get(s).size() > 1)
				numDups++;
		
			System.out.println(map.get(s).size());
		}
			
		System.out.println(numDups);
		
	}
	
	public static HashMap<String, List<RawDesignMatrixParser>> getByLastTwoTokens() throws Exception
	{
		List<String> headers = getTaxaIds();
		HashMap<String, List<RawDesignMatrixParser>> map = new HashMap<String, List<RawDesignMatrixParser>>();
		
		BufferedReader reader = new BufferedReader(new FileReader(ConfigReader.getMbqcDir() + File.separator + 
				"dropbox" + File.separator + 
				"raw_design_matrix.txt"));
		
		reader.readLine();
		
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
		
			StringTokenizer sToken = new StringTokenizer(splits[0], ".");
			sToken.nextToken();
			String key = sToken.nextToken() + "." + sToken.nextToken();
			
			List<RawDesignMatrixParser> list = map.get(key);
			
			if( list == null)
			{
				list= new ArrayList<RawDesignMatrixParser>();
				map.put(key, list);
			}
			
			list.add(new RawDesignMatrixParser(splits,headers));
			
		}
		
		for(String s : map.keySet())
		{
			List<RawDesignMatrixParser> list = map.get(s);
			String mbqcID = null;
			String wetlabSequencigID = null;
			
			for(RawDesignMatrixParser rdmp : list)
			{
				if(mbqcID ==null)
					mbqcID = rdmp.mbqcID;
				
				if( wetlabSequencigID == null)
					wetlabSequencigID = rdmp.extractionWetlab;
				
				if( ! mbqcID.equals(rdmp.mbqcID))
					throw new Exception("No");
				
				if( ! wetlabSequencigID.equals(rdmp.extractionWetlab))
					throw new Exception("No");
			}
		}
		
		return map;
	}
	
	public static List<String> getAllBioinformaticsIDs(HashMap<String, RawDesignMatrixParser> map) throws Exception
	{
		HashSet<String> set = new HashSet<String>();
		
		for(String s : map.keySet())
		{
			set.add( new StringTokenizer(s.split("\t")[0], ".").nextToken());
		}
		
		List<String> list = new ArrayList<String>(set);
		Collections.sort(list);
		
		return list;
	}
	
	public static HashMap<String, RawDesignMatrixParser> getByFullId() throws Exception
	{
		List<String> headers = getTaxaIds();
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
			
			map.put(splits[0], new RawDesignMatrixParser(splits, headers));
		}
		
		return map;
	}
	
	private RawDesignMatrixParser(String[] splits, List<String> headers)
	{
		this.id = new String(splits[0]);
		this.extractionWetlab = new String(splits[1]);
		this.sequecingWetlab = new String(splits[2]);
		this.mbqcID = new String(splits[3]);
		
		List<Double> list = new ArrayList<Double>();
		
		for( int x=0; x < headers.size(); x++)
		{
			String aVal = splits[x+4];
			
			if( aVal.equals("NA"))
				list.add(null);
			else
				list.add(Double.parseDouble(aVal));
		}
		
		this.taxaVals = Collections.unmodifiableList(list);
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
