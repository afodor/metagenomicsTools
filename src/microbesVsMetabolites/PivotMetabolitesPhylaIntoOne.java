package microbesVsMetabolites;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import microbesVsMetabolites.WriteTrialsForSVMLight.MetaboliteClass;
import parsers.OtuWrapper;
import utils.ConfigReader;

public class PivotMetabolitesPhylaIntoOne
{
	public static void main(String[] args) throws Exception
	{

		OtuWrapper wrapper = new OtuWrapper(ConfigReader.getMicrboesVsMetabolitesDir() + File.separator + "phylaAsColumns.txt");
		MetaboliteClass mc = MetaboliteClass.PLASMA;
		
		List<String> metaboliteNames = getNames(mc);
		
		HashMap<Integer, List<Double>> metMap = WriteTrialsForSVMLight.getMetabolites(mc, false);
		
		if( wrapper.getSampleNames().size() != metMap.size())
			throw new Exception("No " + wrapper.getSampleNames().size() + " " + metMap.size());
		
		for(Integer i : metMap.keySet())
			if( metMap.get(i).size() != metaboliteNames.size())
				throw new Exception("No");
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(ConfigReader.getMicrboesVsMetabolitesDir() + File.separator + 
				"mergedPhyla_" + mc + "_AsColumnsLogNorm.txt")));
		
		writer.write("sample");
		
		for(String s : wrapper.getOtuNames())
			writer.write("\t" + s);
		
		for(String s : metaboliteNames)
			writer.write("\t" + s);
		
		writer.write("\n");
		
		for(String sample : wrapper.getSampleNames())
		{
			writer.write(sample);
			
			int sampleID = wrapper.getIndexForSampleName(sample);
			
			for( int x=0; x < wrapper.getOtuNames().size(); x++)
				writer.write("\t" + wrapper.getDataPointsNormalizedThenLogged().get(sampleID).get(x));
			
			List<Double> list = metMap.get(Integer.parseInt(sample.replace("sample", "")));
			
			for( int x=0; x < list.size(); x++)
				writer.write("\t" + list.get(x));
			
			writer.write("\n");
		}
		
		writer.flush();  writer.close();
	
	}
	
	private static List<String> getNames(MetaboliteClass metaboliteClass) throws Exception
	{
		List<String> list = new ArrayList<String>();
		BufferedReader reader = null;
		
		if( metaboliteClass.equals(MetaboliteClass.URINE))
		{
			reader = 
					new BufferedReader(new FileReader(new File( 
							ConfigReader.getMicrboesVsMetabolitesDir() + File.separator + 
							"urine_metabolites_data.txt")));
					
		}
		else if ( metaboliteClass.equals(MetaboliteClass.PLASMA))
		{
			reader = 
					new BufferedReader(new FileReader(new File( 
							ConfigReader.getMicrboesVsMetabolitesDir() + File.separator + 
							"plasmaMetabolites.txt")));
		}
		
		reader.readLine();
		
		for(String s = reader.readLine() ; s != null; s = reader.readLine())
		{
			StringTokenizer sToken = new StringTokenizer(s);
			list.add(sToken.nextToken());
		}
		
		return list;
	}
}
