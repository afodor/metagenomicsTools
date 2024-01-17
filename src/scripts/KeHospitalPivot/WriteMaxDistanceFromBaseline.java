package scripts.KeHospitalPivot;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

public class WriteMaxDistanceFromBaseline
{
	private static class Holder
	{
		double distance;
		MetaMapFileLine mmfl;
	}
	
	@SuppressWarnings("resource")
	private static void writeResults(HashMap<String, Holder> maxDistanceMap) throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				"C:\\Ke_Hospital\\maxDistancesFromPreForEachPatient.txt")));
		
		writer.write("patientID\tinOut\tDonor\tday\tbin\tdistance\n");
		
		for(String s : maxDistanceMap.keySet())
		{
			Holder h= maxDistanceMap.get(s);
			
			if( ! h.mmfl.getPatientID().equals(s))
				throw new Exception("Logic error");
			
			writer.write(s  + "\t" + h.mmfl.getPatientInOut() + "\t" + h.mmfl.getDonor() + "\t" + 
							h.mmfl.getTimepoint() + "\t" + h.mmfl.getBin() + "\t" + h.distance + "\n");
		}
		
		writer.flush();  writer.close();
		
	}
	

	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception
	{
		HashMap<String, MetaMapFileLine> metaMap = MetaMapFileLine.getMetaMap();
		
		HashMap<String, Double> brayMap = WriteDistanceFromBaseline.getBrayDistanceMap();
		
		List<MetaMapFileLine> preSamples = WriteDistanceFromBaseline.getBaselineSamples();
		
		// key is the patientID; value is the maximal distance for each patient
		HashMap<String, Holder> maxDistanceMap = new HashMap<>();
		
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
							&& mfl1.getBin().equals("PRE") && mfl2.getTimepoint() > 0)
				{
					Holder h = maxDistanceMap.get(prePatient);
					double distance = brayMap.get(s);
					
					if( h== null || h.distance< distance)
					{
						h= new Holder();
						h.distance = distance;
						h.mmfl = mfl2;
						maxDistanceMap.put(prePatient, h);
					}				
				}
				
			}
		
		}
		
		writeResults(maxDistanceMap);
	}
}
