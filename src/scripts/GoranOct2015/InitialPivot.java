package scripts.GoranOct2015;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import utils.ConfigReader;

public class InitialPivot
{
	public static void main(String[] args) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getGoranOct2015Dir() + File.separator + 
				"PC_0016 Metagenomics Study Report" + File.separator + "PC_0016 Data" 
						+ File.separator + "PC_0016 Seq_data.txt")));
		
		reader.readLine();  reader.readLine();
		
		for( String s= reader.readLine();  s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			
			String rdpLine= splits[2].trim().replaceAll("\"", "");
					
			if( rdpLine.endsWith(";"))
				rdpLine = rdpLine.substring(0, rdpLine.length()-1);
			
			String[] rdpCalls = rdpLine.split(";");
			
			//for( int x=0; x < rdpCalls.length; x++)
				//System.out.println(x + " " + rdpCalls[x]);
			
			if( rdpCalls.length != 6)
				throw new Exception("No " + rdpCalls.length + " " + rdpLine );
		}
	}
}
