package scripts.KeHospitalPivot;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.StringTokenizer;

/*
 * Run WriteBrayCurtisDistnace first
 */
public class WriteDistanceFromBaseline
{
	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception
	{
		HashMap<String, MetaMapFileLine> metaMap = MetaMapFileLine.getMetaMap();
		
		HashMap<String, Double> brayMap = getBrayDistanceMap();
		
		List<MetaMapFileLine> preSamples = getBaselineSamples();
		
		//arbitrarily toss duplicates; key is bin@patientID
		HashSet<String> included = new HashSet<>();
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				"C:\\Ke_Hospital\\distancesFromPre.txt")));
		
		writer.write("patientID\tinOut\tDonor\tday\tbin\tdistance\n");
		
		for( MetaMapFileLine preMML : preSamples )
		{
			String prePatient = preMML.getPatientID();
			
			for(String s : brayMap.keySet())
			{
				StringTokenizer sToken = new StringTokenizer(s, "@");
				String firstPatient = sToken.nextToken();
				String secondPatient= sToken.nextToken();
				
				MetaMapFileLine mfl1 = metaMap.get(firstPatient);
				MetaMapFileLine mfl2 = metaMap.get(secondPatient);
				
				if( mfl1 != null && mfl2 != null && mfl1.getPatientID().equals(prePatient) && mfl2.getPatientID().equals(prePatient)
							&& mfl1.getBin().equals("PRE") && mfl2.getTimepoint() >= 0)
				{
				
					if( mfl1.getTimepoint() >= 0)
						throw new Exception("Logic error");
					
					if( ! mfl1.getDonor().equals(mfl2.getDonor()))
						throw new Exception("Logic error");
					
					if( ! mfl1.getPatientInOut().equals(mfl2.getPatientInOut()))
						throw new Exception("Logic error");
					
					String key = mfl2.getBin() + "@" + prePatient;
					
					if( ! included.contains(key) )
					{
						writer.write( prePatient + "\t" + mfl1.getPatientInOut() + "\t" + mfl1.getDonor() + 
								"\t" + mfl2.getTimepoint() + "\t" + mfl2.getBin() + "\t" +  brayMap.get(s) + "\n");
						
						included.add(key);
						
					}
					else
					{
						System.out.println("Excluding duplicate " + key);
					}
				}
			}
		
		}
			
		writer.flush();  writer.close();
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
