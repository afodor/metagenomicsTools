package scripts.IanLongitudinal_Dec2015;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import parsers.FastQ;
import utils.ConfigReader;

public class QuickCount
{
	public static void main(String[] args) throws Exception
	{
		HashMap<String, String> map = getBarcodeToSampleMap();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File( 
			ConfigReader.getIanLongitudnalDec2015Dir() + File.separator + 
			"BEHAVBUGS_NoIndex_L001_R2_001.fastq")));
		
		long numAssigned = 0;
		long numNotAssigned =0;
		for(FastQ fq = FastQ.readOneOrNull(reader); fq != null; fq = FastQ.readOneOrNull(reader))
		{
			boolean gotOne = false;
			
			for(String s : map.keySet())
				if( fq.getSequence().indexOf(s) != -1)
					gotOne = true;
			
			if( gotOne)
				numAssigned++;
			else
				numNotAssigned++;
			
			if( numNotAssigned % 1000 ==0 )
				System.out.println( numAssigned + "  "+ numNotAssigned + " " + 
										((double)numAssigned / (numNotAssigned+numAssigned)));
			
			
		}
	
		System.out.println( numAssigned + "  "+ numNotAssigned + " " + 
								((double)numAssigned / (numNotAssigned+numAssigned)));
	}
	
	private static HashMap<String, String> getBarcodeToSampleMap() throws Exception
	{
		HashMap<String,String> map = new HashMap<String,String>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
			ConfigReader.getIanLongitudnalDec2015Dir() + File.separator + 
				"SeriesBarcode.txt")));
		
		reader.readLine();
		
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			
			if(splits.length != 3)
				throw new Exception("No");
			
			if( map.containsKey(splits[2]))
				throw new Exception("No");
			
			if( map.containsValue(splits[1]))
				throw new Exception("No");
			
			map.put(splits[2], splits[1]);
		}
		
		return map;
	}
}
