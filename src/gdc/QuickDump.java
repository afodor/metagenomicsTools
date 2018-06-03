package gdc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;
public class QuickDump
{
	public static void main(String[] args) throws Exception
	{
		File topDir = new File("C:\\gdcClient");
		
		String[] files =topDir.list();

		HashMap<String, Integer> map = new HashMap<>();
		
		for( String f : files)
		{
			File aDir = new File(topDir.getAbsolutePath() + File.separator + f);
			
			if( aDir.isDirectory())
			{
				String[] subFiles = aDir.list();
				
				for(String s : subFiles)
				{
					if( s.endsWith("xml"))
					{
						File subFile = new File(aDir.getAbsolutePath() + File.separator + s);
						String val = printALine("clin_shared:tumor_tissue_site", subFile);
						
						if( val != null)
						{
							Integer count =map.get(val);
							
							if (count == null)
								count =0;
							
							count++;
							
							map.put(val, count);
						}
							
					}
				}
			}
			
		}
		System.out.println(map);
	}
	
	private static String printALine(String include, File file) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(file));
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			if( s.indexOf(include) != -1)
			{
				//System.out.println(s );
				
				try
				{
					s =s.substring(s.indexOf(">") + 1, s.lastIndexOf("<"));
					s = s.trim();
					//System.out.println(s );
					return s;
				}
				catch(Exception ex)
				{
					
				}
				
			}
		}
					
		return null;
	}
}
