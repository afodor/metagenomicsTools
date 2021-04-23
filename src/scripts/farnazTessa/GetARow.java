package scripts.farnazTessa;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class GetARow
{
	public static void main(String[] args) throws Exception
	{
		List<String> sampleNames = new ArrayList<String>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File("C:\\HCT_Probiotic2020-main\\input\\taxonomy_matrices_classified_only\\bracken_species_percentage.txt")));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File("C:\\HCT_Probiotic2020-main\\AF\\AM42_10AC.txt")));
		
		HashMap<String, MetadataParser> map = MetadataParser.getMetaMap();
		
		
		writer.write("sample\tfraction\tdate\n");
		
		String[] topLine =reader.readLine().split("\t");
		
		for(String s : topLine)
			sampleNames.add(s);
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits =s.split("\t");
			
			if( splits[0].indexOf("Ruminococcus sp. AM42-10AC") != -1)
				for( int x=1; x < splits.length; x++)
				{
					String timeString = "NA";
					
					MetadataParser mp = map.get(sampleNames.get(x-1));
					
					if( mp != null)
						timeString = "" + mp.getDate();
					
					writer.write( sampleNames.get(x-1) + "\t" + splits[x] + "\t" + timeString  + "\n" );
				}
		}
		
		reader.close();
		writer.flush();  writer.close();
			
	}
}
