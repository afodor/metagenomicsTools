package scripts.tb_June2019;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
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
			
			File outFile = new File( ConfigReader.getTb_June_2019_Dir() + File.separator + 
					"spreadsheets" + File.separator + "rdp_" + level + "logNormPlusMeta.txt" );
			
			writeLogNormalizedMeta(wrapper, outFile);
		} 
	}
	
	private static void writeLogNormalizedMeta(OtuWrapper wrapper, File outFile) throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
		
		writer.write("id\tdiseaseStatus\tshannonDiversity");
		
		for(int x=0; x < wrapper.getOtuNames().size(); x++)
			writer.write("\t" + wrapper.getOtuNames().get(x));
		
		writer.write("\n");
		
		for( int x=0; x < wrapper.getSampleNames().size(); x++)
		{
			String sampleId = wrapper.getSampleNames().get(x);
			
			writer.write(sampleId + "\t");
			writer.write(getCategory(sampleId) );
			writer.write("\t" + wrapper.getShannonEntropy(sampleId));
			
			for( int y =0; y < wrapper.getOtuNames().size(); y++)
				writer.write("\t" + wrapper.getDataPointsNormalizedThenLogged().get(x).get(y));
			
			writer.write("\n");
		}
		
		writer.flush();  writer.close();
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
		return "NA";
	}
}
