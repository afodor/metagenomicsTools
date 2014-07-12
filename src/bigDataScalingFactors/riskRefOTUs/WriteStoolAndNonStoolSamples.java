package bigDataScalingFactors.riskRefOTUs;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.HashSet;

import parsers.OtuWrapper;
import utils.ConfigReader;

public class WriteStoolAndNonStoolSamples
{
	public static void writePivots(boolean stoolOnly) throws Exception
	{
		//the first row and last column were manually removed...
		File asColumnsFile =new File(ConfigReader.getBigDataScalingFactorsDir() + File.separator + "risk" + 
					File.separator + "dirk" + File.separator + "may2013_refOTU_Table-subsetTaxaAsColumns.filtered.txt");
		
		OtuWrapper.transpose(
				ConfigReader.getBigDataScalingFactorsDir() + File.separator + "risk" + 
				File.separator + "dirk" + File.separator + "may2013_refOTU_Table-subset.filtered.txt"
				, asColumnsFile.getAbsolutePath(), false);
		
		OtuWrapper wrapper = new OtuWrapper(asColumnsFile);
		
		HashSet<String> excludedSamples = new HashSet<String>();
		
		HashMap<String, MetadataParserFileLine> metaMap = MetadataParserFileLine.getMetaMap();
		
		for(String s : wrapper.getSampleNames())
		{
			MetadataParserFileLine mpfl = metaMap.get(s);
			
			if( s== null)
				throw new Exception("Could not find " + s);
			
			if(stoolOnly)
			{
				if( ! mpfl.getSampleLocation().equals("stool"))
					excludedSamples.add(s);
			}
			else
			{
				if( mpfl.getSampleLocation().equals("stool"))
					excludedSamples.add(s);
			}
		}
		
		HashSet<String> excludedOtus = new HashSet<String>();

		for(int x=0; x < wrapper.getOtuNames().size(); x++)
		{
			String otuName = wrapper.getOtuNames().get(x);
			if( wrapper.getCountForTaxaExcludingTheseSamples(x, excludedSamples) < 0.1 )
				excludedOtus.add(otuName);
		}
		
		wrapper = new OtuWrapper(asColumnsFile, excludedSamples, excludedOtus);
		
		wrapper.writeUnnormalizedDataToFile(new File(
				ConfigReader.getBigDataScalingFactorsDir() 
						+ File.separator + "risk" + 
						File.separator + "dirk" 
						+ File.separator + "may2013_refOTU_Table-subsetTaxaAsColumns" + 
							(stoolOnly ? "stoolOnly" : "stoolExclued") + 
						".filtered.txt"
				));
		
		wrapper.writeNormalizedDataToFile(new File(
				ConfigReader.getBigDataScalingFactorsDir() 
						+ File.separator + "risk" + 
						File.separator + "dirk" 
						+ File.separator + "may2013_refOTU_Table-subsetTaxaAsColumns" + 
							(stoolOnly ? "stoolOnly" : "stoolExclued") + 
						"Normalized.filtered.txt"
				));
		
		wrapper.writeLoggedDataWithTaxaAsColumns(new File(
				ConfigReader.getBigDataScalingFactorsDir() 
						+ File.separator + "risk" + 
						File.separator + "dirk" 
						+ File.separator + "may2013_refOTU_Table-subsetTaxaAsColumns" + 
							(stoolOnly ? "stoolOnly" : "stoolExclued") + 
						"LogNormalized.filtered.txt"
				));

		wrapper.writeLoggedDataWithTaxaAsColumns(new File(
				ConfigReader.getBigDataScalingFactorsDir() 
						+ File.separator + "risk" + 
						File.separator + "dirk" 
						+ File.separator + "may2013_refOTU_Table-subsetTaxaAsColumns" + 
							(stoolOnly ? "stoolOnly" : "stoolExclued") + 
						"Ranked.filtered.txt"
				));		
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				ConfigReader.getBigDataScalingFactorsDir() 
				+ File.separator + "risk" + 
						File.separator + "dirk" 
						+ File.separator + "may2013_refOTU_Table-subsetTaxaAsColumnsWithDiagnosis.filtered.txt"
				)));
		
		writer.write("sample\tdiagnosis");
		
		for(String s: wrapper.getOtuNames())
			writer.write("\t" + s);
		
		writer.write("\n");
		
		for(int x=0; x < wrapper.getSampleNames().size(); x++)
		{
			String sampleName = wrapper.getSampleNames().get(x);
			writer.write(sampleName + "\t");
			
			MetadataParserFileLine mpfl = metaMap.get(sampleName);
			
			if( mpfl == null)
				throw new Exception("No");
			
			writer.write(mpfl.getDiagnosis());
			
			for( int y=0; y < wrapper.getOtuNames().size(); y++)
				writer.write("\t" + wrapper.getDataPointsUnnormalized().get(x).get(y));
			
			writer.write("\n");
		}
		
		writer.flush();  writer.close();
	}
	
	public static void main(String[] args) throws Exception
	{
		writePivots(true);
		writePivots(false);
	}
}
