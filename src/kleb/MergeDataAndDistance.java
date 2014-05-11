package kleb;

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
import java.util.StringTokenizer;

import utils.ConfigReader;

public class MergeDataAndDistance
{
	public static void main(String[] args) throws Exception
	{
		List<StrainMetadataFileLine> metaList = 
				new ArrayList<StrainMetadataFileLine>(StrainMetadataFileLine.parseMetadata().values());
		
		HashMap<String, Double> distanceMap = getDistances();
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File( 
				ConfigReader.getKlebDir() + File.separator + "distanceVsTime.txt")));
		
		writer.write("xGenome\tyGenome\ttimeDifference\tgenomicDistance\n");
		SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy");
		
		for(int x=0; x < metaList.size() -1; x++)
		{
			StrainMetadataFileLine xMeta = metaList.get(x);
			
			if( xMeta.getDateString().trim().length() > 0)
			{
				Date xDate = sdf.parse(xMeta.getDateString());
						
				for( int y=x+1; y < metaList.size(); y++)
				{
					StrainMetadataFileLine yMeta = metaList.get(y);
					
					if( yMeta.getDateString().trim().length() > 0 )
					{
						Date yDate = sdf.parse(yMeta.getDateString());
						String key = makeTwoChars(xMeta.getStrainID())
								+ "_" + makeTwoChars(yMeta.getStrainID());
						
						if( distanceMap.containsKey(key))
						{
							writer.write(xMeta.getStrainID() + "\t");
							writer.write(yMeta.getStrainID() + "\t");
							writer.write(Math.abs(xDate.getTime() - yDate.getTime()) + "\t");
							writer.write(distanceMap.get(key) + "\n");
						}
						else
							System.out.println("Could not find " + key);
							
					}
							
				}
			}
			
			
		}
		
		writer.flush(); writer.close();
	}
	
	private static String makeTwoChars(int val) throws Exception
	{
		if( val < 1)
			throw new Exception("Unexpected identifier");
		
		if( val < 10)
			return "0" + val;
		
		return "" + val;
	}
	
	private static HashMap<String, Double> getDistances() throws Exception
	{
		HashMap<String, Double> map = new HashMap<String, Double>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(ConfigReader.getKlebDir()+
				File.separator + "distances.txt")));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			StringTokenizer sToken = new StringTokenizer(s, "\t");
			String key = sToken.nextToken() + "_" + sToken.nextToken();
			System.out.println(key);
			
			if( map.containsKey(key))
				throw new Exception("Parsing error");
			
			map.put(key, Double.parseDouble(sToken.nextToken()));
		}
		
		return map;
	}
}
