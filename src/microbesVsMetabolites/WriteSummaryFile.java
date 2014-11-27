package microbesVsMetabolites;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import utils.Avevar;
import utils.ConfigReader;

public class WriteSummaryFile
{
	public static void main(String[] args) throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(
				new File(ConfigReader.getMicrboesVsMetabolitesDir() + File.separator + 
					"summary.txt"	)));
		
		writer.write("component\tplasmaAvgR\tplasmaSDR\tplasmaScrambedAvgR\tplasmaScrambledSDR");
		
		for( int x=1; x <= 4; x++)
		{
			System.out.println(x);
			File unscrambled = new File(ConfigReader.getMicrboesVsMetabolitesDir() + File.separator + 
												"trials_Comp"+  x + ".txt");
			
			File scrambed = new File(ConfigReader.getMicrboesVsMetabolitesDir() + File.separator + 
										"trials_comp" + x+ "scramble.txt");
			
			writer.write(x + "\t");
			
			Avevar plasma = getAveVar(unscrambled, 1);
			writer.write(plasma.getAve() + "\t");
			writer.write(plasma.getSD() + "\t");
			
			Avevar scrambledPlasma = getAveVar(scrambed, 1);
			writer.write(scrambledPlasma.getAve() + "\t");
			writer.write(scrambledPlasma.getSD() + "\n");
		}
		writer.flush(); writer.close();
		
	}
	
	private static Avevar getAveVar(File file , int column) throws Exception
	{
		List<Double> list = new ArrayList<Double>();
		
		BufferedReader reader =new BufferedReader(new FileReader(file));
		
		reader.readLine();
		
		for(String s=reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			list.add(Double.parseDouble(splits[column]));
		}
		
		reader.close();
		return new Avevar(list);
	}
	
}
