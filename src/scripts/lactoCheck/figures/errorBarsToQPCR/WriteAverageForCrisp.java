package scripts.lactoCheck.figures.errorBarsToQPCR;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import utils.ConfigReader;

public class WriteAverageForCrisp
{
	public static void main(String[] args) throws Exception
	{
		HashMap<String, Holder> map = getVals();
	}
	
	private static HashMap<String, Holder> getVals() throws Exception
	{
		HashMap<String, Holder> map = new HashMap<>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getLactoCheckDir() + File.separator + 
				"qPCRWithErrorBars" + File.separator + 
					"dataForCripsers.txt")));
		
		for(String s= reader.readLine(); s!= null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			
			if( splits.length == 5 && splits[2].equals("L. crispatus") && splits[4].trim().length() >0)
			{
				Holder h = new Holder();
				h.avg = Double.parseDouble(splits[4]);
				h.sd = Double.parseDouble(splits[5]);
				
				String key = splits[1].trim().replaceAll("\"", "");
				
				if( map.containsKey(key))
					throw new Exception("No");
				
				map.put(key, h);
			}
		}
		
		return map;
	}
	
	private static class Holder
	{
		double avg;
		double sd;
	}
}
