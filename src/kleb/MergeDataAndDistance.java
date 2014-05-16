package kleb;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.StringTokenizer;

import utils.ConfigReader;

public class MergeDataAndDistance
{
	static HashSet<Integer> getOutbreakGroup()
	{
		int[] a1 =
		{3, 12, 23, 25, 26, 27, 28, 30, 32, 34, 36, 37, 38, 39, 41, 42, 53, 62, 64, 71, 72, 73, 75};
		
		int[] a2 = 
			{2, 5, 6, 7, 13, 14, 16, 17, 18, 19, 29, 33, 35, 45, 50, 54, 57, 58, 59, 60, 61, 63, 65, 70, 74};
		
		HashSet<Integer> set = new HashSet<Integer>();
		
		for( Integer i : a1)
			set.add(i);
		
		for( Integer i : a2)
			set.add(i);
		
		return set;
	}
	
	static GregorianCalendar getGregorianCalendar(String s)
	{
		System.out.println(s);
		StringTokenizer sToken = new StringTokenizer(s, "/");
		int month = Integer.parseInt(sToken.nextToken()) -1;
		int day = Integer.parseInt(sToken.nextToken());
		int year = Integer.parseInt(sToken.nextToken());
		return new GregorianCalendar(year, month,day);
	}
	
	public static void main(String[] args) throws Exception
	{
		long aDay = 1000 * 60 *60 * 24;
		
		List<StrainMetadataFileLine> metaList = 
				new ArrayList<StrainMetadataFileLine>(StrainMetadataFileLine.parseMetadata().values());
		
		HashMap<String, Double> distanceMap = getDistances();
		HashSet<Integer> outbreakGroup = getOutbreakGroup();
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File( 
				ConfigReader.getKlebDir() + File.separator + "distanceVsTimeCorrected.txt")));
		
		writer.write("xGenome\tyGenome\txDateString\tyDateString\txLocation\tyLocation\tsameLocation\t" 
				+ "timeDifference\tgenomicDistance\tinOutbreakGroup\n");
		
		for(int x=0; x < metaList.size() -1; x++)
		{
			StrainMetadataFileLine xMeta = metaList.get(x);
			
			if( ! xMeta.getDateString().equals("NA"))
			{
				GregorianCalendar xCal = getGregorianCalendar(xMeta.getDateString());
						
				for( int y=x+1; y < metaList.size(); y++)
				{
					StrainMetadataFileLine yMeta = metaList.get(y);
					
					if( ! yMeta.getDateString().equals("NA"))
					{
						GregorianCalendar yCal = getGregorianCalendar(yMeta.getDateString());
						String key = makeTwoChars(xMeta.getStrainID())
								+ "_" + makeTwoChars(yMeta.getStrainID());
						
						if( distanceMap.containsKey(key))
						{
							writer.write(xMeta.getStrainID() + "\t");
							writer.write(yMeta.getStrainID() + "\t");
							writer.write(xMeta.getDateString() + "\t");
							writer.write(yMeta.getDateString() + "\t");
							writer.write(xMeta.getHospital() + "\t");
							writer.write(yMeta.getHospital() + "\t");
							writer.write(xMeta.getHospital().equals(yMeta.getHospital()) + "\t");
							writer.write(Math.abs(xCal.getTime().getTime() - yCal.getTime().getTime())/aDay + "\t");
							writer.write(distanceMap.get(key) + "\t");
							
							boolean inOutbreakGroup = outbreakGroup.contains(xMeta.getStrainID()) &&
										outbreakGroup.contains(yMeta.getStrainID());
							writer.write( inOutbreakGroup + "\n" );
						}
						else
							System.out.println("Could not find " + key);
							
					}
							
				}
			}
			
			
		}
		
		writer.flush(); writer.close();
	}
	
	static String makeTwoChars(int val) throws Exception
	{
		if( val < 1)
			throw new Exception("Unexpected identifier");
		
		if( val < 10)
			return "0" + val;
		
		return "" + val;
	}
	
	static HashMap<String, Double> getDistances() throws Exception
	{
		HashMap<String, Double> map = new HashMap<String, Double>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(ConfigReader.getKlebDir()+
				File.separator + "distances.txt")));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			StringTokenizer sToken = new StringTokenizer(s, "\t");
			String key = sToken.nextToken() + "_" + sToken.nextToken();
			//System.out.println(key);
			
			if( map.containsKey(key))
				throw new Exception("Parsing error");
			
			map.put(key, Double.parseDouble(sToken.nextToken()));
		}
		
		return map;
	}
}
