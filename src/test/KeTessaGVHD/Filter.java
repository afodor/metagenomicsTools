package test.KeTessaGVHD;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class Filter
{
	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception
	{
		List<Double> sums = new ArrayList<>();
		
		BufferedReader reader = new BufferedReader(new FileReader(				
				(new File("C:\\ke_tessa_test\\GVHDProject-main\\CountsTables\\bracken_species_TransposedLogNormPlusMeta.txt"))));
		
		String[] topSplits = reader.readLine().split("\t");
		
		for( int x=0; x < topSplits.length; x++)
			sums.add(0.0);
		
		int numSamples =0;
		for(String s = reader.readLine(); s != null; s= reader.readLine())
		{
			numSamples++;
			
			String[] splits = s.split("\t");
		
			if( splits.length != topSplits.length)
				throw new Exception("No");
			
			for( int x=4; x < splits.length; x++)
			{
				sums.set(x, sums.get(x) + Double.parseDouble(splits[x]));
			}
		}
		
		List<Boolean> keeps = new ArrayList<>();
		keeps.add(true); keeps.add(true); keeps.add(true); keeps.add(true);
		
		for( int x=4; x < sums.size(); x++)
		{
			if( sums.get(x) / numSamples  >= 2.0 ) 
				keeps.add(true);
			else
				keeps.add(false);
		}
		if( keeps.size() != sums.size())
			throw new Exception("Logic error");
		
		reader.close();
		
		reader = new BufferedReader(new FileReader(				
				(new File("C:\\ke_tessa_test\\GVHDProject-main\\CountsTables\\bracken_species_TransposedLogNormPlusMeta.txt"))));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(
				new File("C:\\ke_tessa_test\\GVHDProject-main\\CountsTables\\bracken_species_TransposedLogNormPlusMetaFiltered.txt")));
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			if( splits.length != keeps.size())
				throw new Exception("No");
			
			writer.write(splits[0] + "\t" + splits[1] + "\t" + splits[2] + "\t" + splits[3]  );
			
			for( int x=4; x < splits.length; x++)
				if( keeps.get(x))
					writer.write("\t" + splits[x]);
			
			writer.write("\n");
		}
		
		writer.flush(); writer.close();
	}
}
