package scripts.TopeControlOnly;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import parsers.NewRDPParserFileLine;
import parsers.OtuWrapper;
import utils.ConfigReader;

public class AddMetadata
{
	public static void main(String[] args) throws Exception
	{
		for( int x=1; x < NewRDPParserFileLine.TAXA_ARRAY.length; x++)
		{
			System.out.println(NewRDPParserFileLine.TAXA_ARRAY[x]);
			
			OtuWrapper wrapper = new OtuWrapper( ConfigReader.getTopeControlDir() 
					+ File.separator + "spreadsheets" +
					File.separator + "pivoted_" + 
			NewRDPParserFileLine.TAXA_ARRAY[x] + "asColumns.txt");
			
			File logNormalizedFile = new File(	ConfigReader.getTopeControlDir() 
					+ File.separator + "spreadsheets" +
					File.separator + "pivoted_" + 
			NewRDPParserFileLine.TAXA_ARRAY[x] + "asColumnsLogNormal.txt");
			
			File outFile = new File( ConfigReader.getTopeControlDir() 
					+ File.separator + "spreadsheets" +
					File.separator + "pivoted_" + 
					NewRDPParserFileLine.TAXA_ARRAY[x] + "asColumnsLogNormalPlusMetadata.txt");
			
			addMetadata(wrapper, logNormalizedFile, outFile,false);
			
			logNormalizedFile = new File(ConfigReader.getTopeControlDir() 
					+ File.separator + "spreadsheets" +
					File.separator + "mds_"+ NewRDPParserFileLine.TAXA_ARRAY[x] +  ".txt" );
			
			outFile = new File( ConfigReader.getTopeControlDir() 
					+ File.separator + "spreadsheets" +
					File.separator + "pivoted_" + 
					NewRDPParserFileLine.TAXA_ARRAY[x] + "mdsPlusMetadata.txt");
			
			addMetadata(wrapper, logNormalizedFile, outFile,true);
			
		}
		
	}
	
	private static String getFile(String key ) throws Exception
	{
		if(key.indexOf("file3") != -1)
			return "file3";
		
		if( key.indexOf("file4") != -1)
			return "file4";
		
		throw new Exception("No");
	}
	
	private static int getReadNum(String key) throws Exception
	{
		//System.out.println(key);
		String[] splits = key.split("_");
		
		int splitID = 2;
		int val = -1;
		
		try
		{
			 val = Integer.parseInt(splits[splitID]);
		}
		catch(Exception ex)
		{
			splitID = 1;
			val = Integer.parseInt(splits[splitID]);	
		}
		
		if( val != 1 && val != 4)
			throw new Exception("No ");
		
		return val;
	}
	
	private static void addMetadata( OtuWrapper wrapper, File inFile, File outFile,
				boolean fromR) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(inFile));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
		
		writer.write("id\treadNumber\tnumberSequencesPerSample\tshannonEntropy\tset\tisMouse\tisbacteria\tisStrep\t"
				+"isNegativeContol");
		
		String[] firstSplits = reader.readLine().split("\t");
		
		int startPos = fromR ? 0 : 1;
		
		for( int x=startPos; x < firstSplits.length; x++)
			writer.write("\t" + firstSplits[x]);
		
		writer.write("\n");
		
		for(String s = reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			String key = splits[0].replaceAll("\"", "");
			
			writer.write(key.replaceAll("_file3.fastatoRDP.txt", "").replaceAll("_file4.fastatoRDP.txt", "")
					+ "\t" + getReadNum(key) + "\t" +  wrapper.getNumberSequences(key) 
			
						+ "\t" + wrapper.getShannonEntropy(key) + "\t" + 
					getFile(key) +  "\t" + (key.toLowerCase().indexOf("mouse") != -1) + "\t" + 
									(key.toLowerCase().indexOf("bacteria") != -1) + "\t"+ 
									 (key.toLowerCase().indexOf("5bm-007") != -1) + "\t" + 
										(key.toLowerCase().indexOf("dv-000-") != -1) );
			
			for( int x=1; x < splits.length; x++)
				writer.write("\t" + splits[x]);
			
			writer.write("\n");
		}
		
		writer.flush();  writer.close();
		reader.close();
	}
	
}
