package scripts.KeHospitalPivot;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

/*
 * Run WriteBrayCurtisDistnace first
 */
public class WriteDistanceFromBaseline
{
	public static void main(String[] args) throws Exception
	{
		HashMap<String, Double> brayMap = getBrayDistanceMap();
		for(String s: brayMap.keySet())
			System.out.println(s + " " + brayMap.get(s));
	}
	
	@SuppressWarnings("resource")
	public static HashMap<String, Double> getBrayDistanceMap() throws Exception
	{
		HashMap<String,Double> map = new HashMap<>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File("C:\\Ke_Hospital\\brayCurtisDistances.txt")));
		
		reader.readLine();
		
		for(String s = reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			String key = splits[0] + "@" + splits[1];
			
			if( map.containsKey(key))
				throw new Exception("Duplicate " + key);
			
			map.put(key, Double.parseDouble(splits[2]));
		}
		
		reader.close();
		return map;
	}
}
