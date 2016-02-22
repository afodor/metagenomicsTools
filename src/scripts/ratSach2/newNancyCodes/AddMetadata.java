package scripts.ratSach2.newNancyCodes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

import parsers.NewRDPParserFileLine;
import parsers.OtuWrapper;
import scripts.ratSach2.MappingFileLine;
import utils.ConfigReader;

public class AddMetadata
{
	private static HashMap<String, String> getCageMap() throws Exception
	{
		HashMap<String, String> cageMap = new HashMap<String, String>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File( 
			ConfigReader.getRachSachReanalysisDir() + File.separator + "TTULyteCages.txt"	)));
		
		reader.readLine(); 
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits =s.split("\t");
			
			if( cageMap.containsKey(splits[1]))
				throw new Exception("No");
			
			cageMap.put(splits[1],splits[0]);
		}
		
		return cageMap;
	}
	
	public static void main(String[] args) throws Exception
	{
		for( int x=1; x < NewRDPParserFileLine.TAXA_ARRAY.length; x++)
		{
			String level = NewRDPParserFileLine.TAXA_ARRAY[x];
			System.out.println(level);
			
			File inFile = new File( ConfigReader.getRachSachReanalysisDir() + File.separator +
					"NancyCoHouse" + File.separator + 
					 "cohousingRun_" + level +  "_AsColumnsLogNormalized.txt");
			
			OtuWrapper wrapper = new OtuWrapper(			
					ConfigReader.getRachSachReanalysisDir() + File.separator +
			"rdpAnalysis" + File.separator + 
			"sparseThreeColumn_" + level + "_AsColumns.txt");
			
			File outFile = new File(ConfigReader.getRachSachReanalysisDir() + File.separator +
					"NancyCoHouse" + File.separator + 
					 "cohousingRun_" + level +  "_AsColumnsLogNormalizedPlusMetadata.txt");
			
					 
			writeFile(inFile, outFile, wrapper,false);
			
			inFile = new File( ConfigReader.getRachSachReanalysisDir() + File.separator +
					"NancyCoHouse" + File.separator + 
					"mds_cohousingRun_" + level + "_AsColumnsLogNormalized.txt");
			

			outFile = new File( ConfigReader.getRachSachReanalysisDir() + File.separator +
					"NancyCoHouse" + File.separator + 
					"mds_cohousingRun_" + level + "_AsColumnsLogNormalizedPlusMetadata.txt");
			
			writeFile(inFile, outFile, wrapper, true);
			
			
		}
	}
 	
	private static void writeFile(File inFile, File outFile,  OtuWrapper wrapper,  boolean fromR) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(inFile));
		
		BufferedWriter writer =new BufferedWriter(new FileWriter(outFile));
		
		writer.write("sample\tnumSequencesPerSample\tshannonDiversity\tline\ttissue\tcage\ttime\tbatch\tline");
		
		String[] topSplits = reader.readLine().split("\t");
		
		int startPos =1;
		
		if( fromR)
			startPos =0;
		
		for( int x=startPos; x < topSplits.length; x++)
			writer.write("\t" + topSplits[x]);
		
		writer.write("\n");
		
		HashMap<String, String> cageMap = getCageMap();
		HashMap<String, MappingFileLine> map = MappingFileLine.getMap();
		HashMap<String, NewNancyCodesMetaline> newMap = NewNancyCodesMetaline.getMetaMap();
	
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			
			NewNancyCodesMetaline nncm = newMap.get(splits[0].replaceAll("\"", ""));
			
			if( nncm != null)
			{
				MappingFileLine mfl = map.get(splits[0].replaceAll("\"", ""));
				String cage = cageMap.get(mfl.getRatID());
			
				
				writer.write(splits[0].replaceAll("\"", "") + "\t");
				
				writer.write(wrapper.getCountsForSample(splits[0].replaceAll("\"", "")) + "\t");
				writer.write(wrapper.getShannonEntropy(splits[0].replaceAll("\"", "")) + "\t");
				
				writer.write(mfl.getLine() + "\t");
				writer.write(mfl.getTissue() + "\t");
				writer.write(cage + "\t");
				writer.write(nncm.getTime() + "\t");
				writer.write(nncm.getBatch() + "\t");
				writer.write(nncm.getLine() + "");
					
				for(int x=1; x < splits.length; x++)
					writer.write("\t" + splits[x]);
					
				writer.write("\n");
			}
		}
		
		writer.flush();  writer.close();
		
	}
}
