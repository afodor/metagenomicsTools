package scripts.KeHospitalPivot;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*
 * Run WriteBrayCurtisDistnace first
 */
public class WriteDistanceFromBaseline
{
	public static void main(String[] args) throws Exception
	{
		HashMap<String, Double> brayMap = getBrayDistanceMap();
		
		List<MetaMapFileLine> preSamples = getBaselineSamples();
	}
	
	private static List<MetaMapFileLine> getBaselineSamples() throws Exception
	{
		HashMap<String, MetaMapFileLine> metaMap = MetaMapFileLine.getMetaMap();
		
		HashMap<String, MetaMapFileLine> patientToMetaMap = new HashMap<>();
		
		for(String s : metaMap.keySet())
		{
			MetaMapFileLine mfl = metaMap.get(s);
			
			if( mfl.getBin().equals("PRE"))
			{
				MetaMapFileLine candidate = patientToMetaMap.get(mfl.getPatientID());
				
				if( candidate == null)
				{
					patientToMetaMap.put(mfl.getPatientID(), mfl);
				}
				else
				{
					throw new Exception("Dupicate PRE");
				}
			}
		}
		
		List<MetaMapFileLine> list = new ArrayList<>();
		
		for(MetaMapFileLine mfl : patientToMetaMap.values())
			list.add(mfl);
		
		return list;
	}
	
	
	@SuppressWarnings("resource")
	private static HashMap<String, Double> getBrayDistanceMap() throws Exception
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
