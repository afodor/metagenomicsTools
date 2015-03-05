package mbqc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import utils.ConfigReader;

public class ReadDistanceMatrix
{
	public static void main(String[] args) throws Exception
	{
		List<Double> list = new ArrayList<Double>();
		
		BufferedReader reader =new BufferedReader(new FileReader(new File(
				ConfigReader.getMbqcDir() + File.separator + 
				"dropbox" + File.separator +  "alpha-beta-div" + 
				File.separator + "beta-div" + File.separator + 
				"distances.txt")));
		
		reader.readLine();
		
		for( String s = reader.readLine(); s != null; s= reader.readLine())
		{
			list.add(Double.parseDouble(s));
			
			if(list.size() % 1000000 == 0 )
				System.out.println("at " + list.size());
		}
		
		System.out.println("finished " + list.size());
	}
}
