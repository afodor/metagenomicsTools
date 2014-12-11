package scripts.vanderbilt.kraken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.StringTokenizer;
import java.util.zip.GZIPInputStream;

import utils.ConfigReader;

public class NumberSequencesHumanStandardDB
{
	private static class Holder
	{
		HashMap<String, Boolean> foundInHuman = new HashMap<String, Boolean>();
		HashMap<String, Boolean> foundInReference = new HashMap<String, Boolean>();
		
		int numFoundInBoth() throws Exception
		{
			int x=0;
			
			// why is this throwing???
			//if( ! foundInHuman.keySet().equals(foundInReference.keySet()))
				//throw new Exception("No");
			
			for(String s : foundInHuman.keySet())
				if( foundInHuman.get(s) && foundInReference.get(s) )
					x++;
			
			return x;
		}
		
		int numFoundInNeither() throws Exception
		{
			int x=0;
			
			///
			// why is this throwing???
		//	if( ! foundInHuman.keySet().equals(foundInReference.keySet()))
			//	throw new Exception("No");
			
			for(String s : foundInHuman.keySet())
				if( !foundInHuman.get(s) && !foundInReference.get(s) )
					x++;
			
			return x;
		}
	}
	
	private static void addToMap(File file, HashMap<String, Holder> map) throws Exception
	{
		System.out.println(file.getAbsolutePath());
		boolean human = file.getName().indexOf("Human") != -1;
		
		String[] splits = file.getName().split("_");
		String fileName = splits[1] + "_" + splits[2];
		
		Holder h = map.get(fileName);
		
		if( h ==  null)
		{
			h = new Holder();
			map.put(fileName, h);
		}
		
		HashMap<String, Boolean> foundMap = human ? h.foundInHuman : h.foundInReference;
		
		if( foundMap.size() != 0)
			throw new Exception("Map not empty");
		
		BufferedReader reader =new BufferedReader(new InputStreamReader( new GZIPInputStream( new FileInputStream(  file))));
		
		for(String s= reader.readLine(); s != null; s = reader.readLine())
		{
			StringTokenizer sToken = new StringTokenizer(s);
			
			String firstToken = sToken.nextToken();
			
			if( firstToken.length() != 1)
				throw new Exception("Unexpected " + firstToken);
			
			String seqName = new String( sToken.nextToken());
			
			if( foundMap.containsKey(seqName))
				throw new Exception("Duplicate sequence " + seqName);
			
			if( firstToken.equals("U"))
				foundMap.put(seqName, false);
			else if ( firstToken.equals("C"))
				foundMap.put(seqName, true);
			else throw new Exception("Unexpected first token " + s);
		}
		
		reader.close();
		
		
	}
	
	private static int getNumTrue(HashMap<String, Boolean> map ) 
	{
		int x=0;
		
		for(Boolean b : map.values())
			if( b)
				x++;
		
		return x;
	}
	
	private static void writeResults(HashMap<String, Holder> map) throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(ConfigReader.getVanderbiltDir() + File.separator + 
				"humanVsReferenceFromKraken.txt")));
		
		writer.write("fullSampleName\tsampleID\tstoolVsSwab\trunNum\tnumClassifiedRef\tnumNotClassifiedRef\t" + 
						"totalRef\tnumClassifiedHuman\tnumNotClassifeidHuman\ttotalHuman\tnumNeither\tnumBoth\tabsoluteInHuman\tabsoluteInRef\n");
		
		for(String s : map.keySet())
		{
			System.out.println("Writing " + s);
			writer.write(s + "\t");
			String[] splits = s.split("_");
			writer.write( splits[0] + "\t" );
			if( s.startsWith("ST"))
					writer.write("stool\t");
			else if (s.startsWith("SW"))
					writer.write("swab\t");
			else throw new Exception("No");
			
			Holder h = map.get(s);
			double totalNum = h.foundInHuman.size();
			
			writer.write(splits[1] + "\t");

			int numClassifiedRef = getNumTrue(h.foundInReference);
			writer.write(numClassifiedRef / totalNum + "\t");
			writer.write((h.foundInReference.size() - numClassifiedRef)/totalNum + "\t");
			writer.write(h.foundInReference.size()/totalNum + "\t");
			
			int numClassifiedHuman = getNumTrue(h.foundInHuman);
			writer.write(numClassifiedHuman /totalNum+ "\t");
			writer.write((h.foundInHuman.size() - numClassifiedHuman) /totalNum + "\t");
			writer.write(h.foundInHuman.size()/totalNum+"\t");
			writer.write(h.numFoundInBoth() /totalNum + "\t");
			writer.write(h.numFoundInNeither() /totalNum + "\t");
			writer.write(h.foundInHuman.size() + "\t");
			writer.write(h.foundInReference.size() + "\n");
			
		}
		
		
		
		
		writer.flush();  writer.close();
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, Holder> map = new LinkedHashMap<String, Holder>();
		
		File topDir = new File(ConfigReader.getVanderbiltDir() + File.separator + "krakenOut");
		
		for(String s: topDir.list())
			if( s.startsWith("Sample"))
				addToMap(new File(topDir.getAbsoluteFile() + File.separator + s), map);
		
		writeResults(map);
		
		
	}
}
