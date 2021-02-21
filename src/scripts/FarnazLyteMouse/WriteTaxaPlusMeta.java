package scripts.FarnazLyteMouse;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WriteTaxaPlusMeta
{
	public static void main(String[] args) throws Exception
	{
		String[] levels = { "Phylum", "Class", "Order", "Family", "Genus" };
		
		HashMap<String, MetadataParser> metaMap = MetadataParser.getMetaMap();
		
		for( String level : levels)
		{
			BufferedReader reader = new BufferedReader(new FileReader(
					"C:\\LyteManuscriptInPieces\\MouseStressStudy_BeefSupplement2020-main\\input\\" + 
							level+  "_table.txt"));
			
			String[] topSplits = reader.readLine().split("\t");
			
			List<String> taxaNames = new ArrayList<String>();
			
			boolean foundId = false;
			int index=0;
			
			while( ! foundId)
			{
				taxaNames.add(topSplits[index]);
				
				index++;
				
				if( topSplits[index].equals("Argonne Sequence Number"))
					foundId =true;
			}
			
			for(String s= reader.readLine(); s != null; s = reader.readLine())
			{
				String[] splits = s.split("\t");
				
				String key = splits[0];
				
				MetadataParser mp = metaMap.get(key);
				
				if( mp == null)
					throw new Exception("Could not find " + key);
			}
		}
		
		
		
	}
}
