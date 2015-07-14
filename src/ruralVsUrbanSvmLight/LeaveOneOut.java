package ruralVsUrbanSvmLight;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import utils.ConfigReader;

public class LeaveOneOut
{
	private static void writeALine(String s , BufferedWriter writer, boolean mask ,
				int patientToLeaveOut) throws Exception
	{

		String[] splits = s.split("\t");
		
		
		if( Integer.parseInt(splits[1])== 1 && splits[4].equals("first_A")
						&& Integer.parseInt(splits[2]) != patientToLeaveOut)
		{
			if( mask)
			{
				writer.write( " 0 : ");
			}
			else 
			{
				if(splits[3].equals("rural"))
					writer.write("-1 : ");
				else if ( splits[3].equals("urban"))
					writer.write("1 : ");
				else throw new Exception("No");
			}
			
			for( int x=5; x < splits.length; x++)
			{
				writer.write( x + ":" + splits[x] + " " );
			}
			
			writer.write("\n");
		}
		
		writer.flush();
	}
	
	public static void main(String[] args) throws Exception
	{
		runATrial(-1);
	}
	
	public static void runATrial(int patientIDToLeaveOut) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(new File(ConfigReader.getChinaDir() 
					+ File.separator + "phylum_taxaAsColumnsLogNorm_WithMetadata.txt")));
		
		reader.readLine();
		
		File outFile = new File(ConfigReader.getSvmDir() + File.separator + 
				"trainWithNo" + patientIDToLeaveOut+ ".txt");
		
		outFile.delete();
		
		if(outFile.exists())
			throw new Exception("Could not delete " + outFile.getAbsolutePath());
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
		
		for(String s= reader.readLine(); s != null; s = reader.readLine())
		{
			writeALine(s, writer, false, patientIDToLeaveOut);
		}
		
		writer.flush();  writer.close();
	}
}
