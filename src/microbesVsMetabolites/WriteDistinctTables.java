package microbesVsMetabolites;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import utils.ConfigReader;
import microbesVsMetabolites.WriteTrialsForSVMLight.MetaboliteClass;

public class WriteDistinctTables
{
	private static String removeAnnoyingCharacters(String s)
	{
		StringBuffer buff = new StringBuffer();
		
		for( int x=0; x < s.length(); x++)
		{
			char c = s.charAt(x);
			
			if( c != '\"' && c != ',')
				buff.append(c);
			else
				buff.append("_");
		}
		
		return buff.toString();
	}
	
	
	private static void writeMetabolitesFile(MetaboliteClass mc) throws Exception
	{
		HashMap<Integer, List<Double>> metMap = WriteTrialsForSVMLight.getMetabolites(mc, false);
		List<String> names = PivotMetabolitesPhylaIntoOne.getNames(mc);
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(ConfigReader.getMicrboesVsMetabolitesDir()+
				File.separator + mc + "AsColumns.txt")));
		
		writer.write("sample");
		
		for(int x=0;x  < names.size(); x++)
			writer.write("\t" + removeAnnoyingCharacters(names.get(x)));
		
		writer.write("\n");
		
		List<Integer> samples = new ArrayList<Integer>(metMap.keySet());
		Collections.sort(samples);
		
		for( int i : samples)
		{
			writer.write("" + i);
			
			List<Double> list = metMap.get(i);
			
			if( list.size() != names.size())
				throw new Exception("No");
			
			for( Double d : list)
				writer.write("\t" + d);
			
			writer.write("\n");
		}
		
		writer.flush();  writer.close();
	}
	
	public static void main(String[] args) throws Exception
	{
		writeMetabolitesFile( MetaboliteClass.PLASMA);
		writeMetabolitesFile( MetaboliteClass.URINE);
	}
}
