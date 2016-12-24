package ruralVsUrban;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.StringTokenizer;

import parsers.OtuWrapper;
import utils.ConfigReader;

public class AddMetadataForKraken
{
	private static int getreadNumber(String s)
	{
		int skipNum = 0;
		
		if( s.startsWith("FCA5PCT"))
			skipNum =3;
		
		if( s.startsWith("FCA5PCT"))
			skipNum =3;
		
		if ( s.startsWith("FCAB0GM"))
			skipNum = 2;
		
		for (int x=0; x < skipNum; x++)
		{
			s= s.substring(s.indexOf("_")+1, s.length());
		}
		
		s = s.substring(s.indexOf("_") + 1, s.length());
		
		return Integer.parseInt("" + s.charAt(0));
	}
	
	private static String getTimepoint(String s)
	{
		s = s.substring(s.lastIndexOf("_") + 1, s.length());
		
		if( s.startsWith("B"))
			return 	"second_B";
		else return "first_A";
	}
	
	private static int getPatientID(String s)
	{
		s = s.substring(s.lastIndexOf("_") + 1, s.length());
		return Integer.parseInt(s.replaceAll("A", "").replaceAll("B", "").replace(".fasta", ""));	
	}
	
	private static void addSomeMetadata(File outFile, File logNormalFile, String timepoint) throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
		
		BufferedReader reader = new BufferedReader(new FileReader(logNormalFile));
		
		writer.write("sampleID\turbanRural");
		
		String[] splits = reader.readLine().split("\t");
			for( int x=1; x < splits.length; x++)
				writer.write("\t"  + splits[x]);
			
			writer.write("\n");
		
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			splits = s.split("\t");
			
			
			int readNumber = getreadNumber(splits[0]);
			String aTimepoint = getTimepoint(splits[0]);
			int patientID = getPatientID(splits[0]);
			
			if( readNumber == 1 || readNumber == 2)
			{
				System.out.println(splits[0] + " " + readNumber + " " + aTimepoint + " " + 
								patientID);
			}
			else
				throw new Exception("Parsing error");
		}
		
		writer.flush();  writer.close();
		
		reader.close();
	}
	
	/*
	 * Something like:
	 * 
	 *    run the RDP in package cluster stuff on viper
	 *    download the zipped RPP results
	 *    MakeRDPThreeColumnFile
	 *    PivotRDPs
	 *    NormalizeRDPs
	 *    pcoa.txt (in R)
	 *    AddMetadata
	 */
	public static void main(String[] args) throws Exception
	{
		for( int x=0; x < scripts.TopeSeptember2015Run.AddMetadataForKraken.TAXA_ARRAY.length; x++)
		{
			String taxa = scripts.TopeSeptember2015Run.AddMetadataForKraken.TAXA_ARRAY[x];
			
			File unloggedFile = new File(ConfigReader.getChinaDir() + 
					File.separator + "krakenSummary" + File.separator + 
						"China_2015_kraken_" + taxa +".txt");
			
			OtuWrapper wrapper = new OtuWrapper(unloggedFile);
			
			File loggedFile = new File(ConfigReader.getChinaDir() + 
					File.separator + "krakenSummary" + File.separator + 
						"China_2015_kraken_" + taxa +"logged.txt");
			
			wrapper.writeNormalizedLoggedDataToFile(loggedFile);
			
			String[] timepoints = {	"second_B","first_A"};
			
			for(String time : timepoints)
			{

				File loggedFileWithMetadata = new File(ConfigReader.getChinaDir() + 
						File.separator + "krakenSummary" + File.separator + 
						"China_2015_kraken_" + taxa +"loggedWithMetadata_" + time +".txt");
			
				
				addSomeMetadata(loggedFileWithMetadata, loggedFile, time);
			
			}
		}	
	}
}
