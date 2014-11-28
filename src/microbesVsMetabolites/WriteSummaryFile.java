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
					"summary.txt")));
		
		writer.write("component\tplasmaAvgR\tplasmaSDR\tplasmaScrambedAvgR\tplasmaScrambledSDR\t");
		writer.write("urineAvgR\turineSDR\turineScrambedAvgR\turineScrambledSDR\t");
		writer.write("metadataAvgR\tmetadataSDR\tmetadataScrambedAvgR\tmetadataScrambledSDR\n");
		
		
		for( int x=1; x <= 14; x++)
		{
			writer.write(x+ "\t");
			writeASet(writer, x, 1, false);
			writeASet(writer, x, 3, false);
			writeASet(writer, x, 7, true);
			
		}
		writer.flush(); writer.close();
		
	}
	
	private static void writeASet( BufferedWriter writer, int compNum, int colNum,  boolean endLine)
		throws Exception
	{
		System.out.println(compNum);
		File unscrambled = new File(ConfigReader.getMicrboesVsMetabolitesDir() + File.separator + 
											"trials_Comp"+  compNum + ".txt");
		
		File scrambed = new File(ConfigReader.getMicrboesVsMetabolitesDir() + File.separator + 
									"trials_comp" + compNum+ "scramble.txt");
		
		Avevar plasma = getAveVar(unscrambled, colNum);
		writer.write(plasma.getAve() + "\t");
		writer.write(plasma.getSD() + "\t");
		
		Avevar scrambledPlasma = getAveVar(scrambed, colNum);
		writer.write(scrambledPlasma.getAve() + "\t");
		writer.write(scrambledPlasma.getSD() + (endLine ?  "\n" : "\t") );
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
