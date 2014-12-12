package scripts.vanderbilt.kraken;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashSet;

import parsers.NewRDPParserFileLine;
import parsers.OtuWrapper;
import utils.ConfigReader;

public class MergeRDPKraken
{
	private static  HashSet<String> getTaxaToInclude(OtuWrapper rdpWrapper, OtuWrapper krakenWrapper, HashSet<String> samples)
		throws Exception
	{

		HashSet<String> taxa = new HashSet<String>();
		taxa.addAll(rdpWrapper.getOtuNames());
		taxa.addAll(krakenWrapper.getOtuNames());
		
		HashSet<String> taxaToRemove = new HashSet<String>();
		
		for(String otu: taxa)
		{
			boolean removeTaxa = true;
			int rdpOTUKey = rdpWrapper.getIndexForOtuName(otu);
			int krakenOTUKey = krakenWrapper.getIndexForOtuName(otu);
			
			for(String s : samples)
			{
				if( rdpOTUKey != -1 )
				{
					int rdpSampleKey = rdpWrapper.getIndexForSampleName(s);
					
					if( rdpWrapper.getDataPointsUnnormalized().get(rdpSampleKey).get(rdpOTUKey) >0)
						removeTaxa =false;
				}
				
				if( krakenOTUKey != -1)
				{
					int krakenSampleKey = krakenWrapper.getIndexForSampleName(s);
					
					if( krakenWrapper.getDataPointsUnnormalized().get(krakenSampleKey).get(krakenOTUKey) > 0)
						removeTaxa = false;
				}
			}
			
			if( removeTaxa)
				taxaToRemove.add(otu);
		}
		
		taxa.removeAll(taxaToRemove);
		return taxa;
	}
	
	private static double getVal(OtuWrapper wrapper, int sampleIndex, String otuName) throws Exception
	{
		int otuIndex = wrapper.getIndexForOtuName(otuName);
		
		if( otuIndex == -1)
			return 0;
		
		return wrapper.getDataPointsNormalizedThenLogged().get(sampleIndex).get(otuIndex);
	}
	
	public static void main(String[] args) throws Exception
	{
		for( int x=1; x < NewRDPParserFileLine.TAXA_ARRAY.length; x++)
			writeForLevel(NewRDPParserFileLine.TAXA_ARRAY[x]);
	}
	
	//the outer key is the sampleID; inner key is taxa to holder
	private static void writeForLevel(String level) throws Exception
	{
		System.out.println(level);
		BufferedWriter writer =new BufferedWriter(new FileWriter(new File(ConfigReader.getVanderbiltDir() + File.separator + 
				"spreadsheets" + File.separator + 
				"mergedKrakenRDP_" + level + ".txt")));
		writer.write("sample\tisStoolOrSwab\ttaxa\tkrakenLevel\trdpLevel\n");
		
		OtuWrapper rdpWrapper = new OtuWrapper(ConfigReader.getVanderbiltDir() + File.separator + "spreadsheets" + 	
							File.separator + "pivoted_"+ level  + "asColumnsLogNormal.txt");
		
		OtuWrapper krakenWrapper = new OtuWrapper(ConfigReader.getVanderbiltDir() + File.separator + "spreadsheets" + 	
				File.separator + "kraken_" + level + "_taxaAsColumnsLogNorm.txt");
		
		HashSet<String> samples = new HashSet<String>();
		samples.addAll(rdpWrapper.getSampleNames());
		samples.retainAll(krakenWrapper.getSampleNames());
		HashSet<String> taxa = getTaxaToInclude(rdpWrapper, krakenWrapper, samples);
		
		for(String s : samples)
		{
			int rdpSampleKey = rdpWrapper.getIndexForSampleName(s);
			int krakenSampleKey = krakenWrapper.getIndexForSampleName(s);
			
			for(String otu : taxa)
			{
				writer.write(s + "\t");
				
				if( s.startsWith("ST"))
					writer.write("stool\t");
				else if ( s.startsWith("SW"))
					writer.write("swab\t");
				else throw new Exception(s);
				
				writer.write(otu + "\t");
				writer.write( getVal(krakenWrapper, krakenSampleKey, otu) + "\t" );
				writer.write( getVal(rdpWrapper, rdpSampleKey, otu) + "\n" );
			}
		}
		
		writer.flush();  writer.close();
	}
}
