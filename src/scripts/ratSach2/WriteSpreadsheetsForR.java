package scripts.ratSach2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;


import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import parsers.OtuWrapper;
import utils.ConfigReader;

public class WriteSpreadsheetsForR
{
	public static void main(String[] args) throws Exception
	{
		writeAndNormalize("all");
		writeAndNormalize("Cecal Content");
		writeAndNormalize("Colon content");
		//writeForATissue("Fecal content");
	}
	
	private static void writeAndNormalize(String tissue) throws Exception
	{
		String tissueString = "_" + tissue;
		
		if( tissue.equals("all"))
			tissueString = "";
		
		File aFile = writeForATissue(tissue);
		File countFile = new File(
				ConfigReader.getRachSachReanalysisDir() + File.separator + "otu" + tissueString + "taxaAsCols.txt" );
		OtuWrapper.transpose(aFile.getAbsolutePath(),countFile.getAbsolutePath(), false);
		
		OtuWrapper wrapper = new OtuWrapper(countFile);
		
		File logNormFile = new File(
				ConfigReader.getRachSachReanalysisDir() + File.separator + "otu" + tissueString + "taxaAsColsLogNorm.txt" );
		
		wrapper.writeNormalizedLoggedDataToFile(logNormFile);
		
		File anotherFilePath = new File(
				ConfigReader.getRachSachReanalysisDir() + File.separator + "rdpAnalysis" + File.separator + 
				"sparseThreeColumn_" +  "otu" +  "_AsColumnsLogNormalized" + tissueString +  ".txt");
		
		wrapper.writeNormalizedLoggedDataToFile(anotherFilePath);
		
		anotherFilePath = new File(
				ConfigReader.getRachSachReanalysisDir() + File.separator + "rdpAnalysis" + File.separator + 
				"sparseThreeColumn_" +  "otu" +  "_AsColumns" + tissueString+  ".txt");
		
		OtuWrapper.transpose(aFile.getAbsolutePath(),anotherFilePath.getAbsolutePath(), false);
	}
	
	private static File writeForATissue(String tissue) throws Exception
	{
		File pivotFile = new File(ConfigReader.getRachSachReanalysisDir() + File.separator + "otu_" + tissue+ ".txt");
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(pivotFile));
		
		File otuFile = new File(
				ConfigReader.getRachSachReanalysisDir() + File.separator + 
				"otu_table.txt");
		BufferedReader reader = new BufferedReader(new FileReader(otuFile));
		
		List<Boolean> includeList = new ArrayList<Boolean>();
		
		HashMap<String, MappingFileLine> metaMap = MappingFileLine.getMap();
		
		String[] splits = reader.readLine().split("\t");
		
		for( int x=1; x < splits.length-1; x++)
		{
			String sample = splits[x];
			
			System.out.println(sample);
			MappingFileLine mfl = metaMap.get(sample);
			
			boolean include =false;
			
			if( mfl != null && ( mfl.getLine().equals("Low") || mfl.getLine().equals("High")) )
			{
				if( mfl.getTissue().equals(tissue))
					include= true;
			}
			
			if( tissue.equals("all"))
				include = true;
			
			includeList.add(include);
		}
		
		writer.write("sampleID");
		
		for( int x=1; x < splits.length-1; x++)
			if( includeList.get(x-1))
				writer.write("\t" + splits[x]);
		
		writer.write("\n");
		
		for(String s= reader.readLine() ; s != null; s = reader.readLine())
		{
			splits = s.split("\t");
			
			writer.write(splits[0]);
			
			for( int x=1; x < splits.length-1; x++)
				if( includeList.get(x-1))
					writer.write("\t" + splits[x]);
			
			writer.write("\n");
		}
		
		writer.flush();  writer.close();
		
		return pivotFile;
	}
}
