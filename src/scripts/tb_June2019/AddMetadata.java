package scripts.tb_June2019;

import java.io.File;
import java.util.HashSet;

import parsers.NewRDPParserFileLine;
import parsers.OtuWrapper;
import utils.ConfigReader;

public class AddMetadata
{
	public static void main(String[] args) throws Exception
	{
		for(int x=1; x < NewRDPParserFileLine.TAXA_ARRAY.length; x++)
		{
			String level = NewRDPParserFileLine.TAXA_ARRAY[x];
			System.out.println(level);
			
			OtuWrapper wrapper = new OtuWrapper(ConfigReader.getTb_June_2019_Dir() + File.separator + 
					"spreadsheets" + File.separator + "rdp_" + level + ".txt");
			
			for( int y=0; y < wrapper.getSampleNames().size(); y++)
			{
				String sampleName = wrapper.getSampleNames().get(y);
				String category = getCategory(sampleName);
				System.out.println(category);
			}
		}
	}
	
	public static String getCategory(String s ) throws Exception
	{
		Integer anInt = Integer.parseInt(s);
		
		if( anInt >= 1 && anInt <= 10)
			return "healthy";
		
		HashSet<Integer> moderate = new HashSet<>();
		
		moderate.add(22);
		moderate.add(23);
		moderate.add(26);
		moderate.add(28);
		moderate.add(32);
		moderate.add(33);
		moderate.add(36);
		moderate.add(37);
		moderate.add(39);
		moderate.add(42);
		moderate.add(49);
		
		HashSet<Integer> severe = new HashSet<>();
		
		int[] severes = {12,
				13,
				18,
				21,
				31,
				34,
				38,
				15,
				25,
				27,
				30,
				41,
				45,
				46,
				51};

		for(int i : severes)
		{
			if( severe.contains(i) && moderate.contains(i))
				throw new Exception("Duplicate");
			
			severe.add(i);
		}

		if( moderate.contains(anInt))
			return "moderate";
		
		if( severe.contains(anInt))
			return "severe";
		
		System.out.println("Could not find " + anInt);
		return null;
	}
}
