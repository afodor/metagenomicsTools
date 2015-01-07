package scripts.ratSach2.rdpAnalysis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import parsers.OtuWrapper;
import scripts.ratSach2.GreengenesOtuLookup;
import scripts.ratSach2.MappingFileLine;
import utils.Avevar;
import utils.ConfigReader;

public class ListSignificantTaxaAcrossLevels
{
	private static class Holder
	{
		List<Double> hiSach = new ArrayList<Double>();
		List<Double> lowSach = new ArrayList<Double>();
	}
	
	private static Holder getUnnormalizedData( HashMap<String, MappingFileLine> metaMap ,
							String tissue, String taxa, OtuWrapper wrapper ) throws Exception
	{
		Holder h= new Holder();
		
		int taxaIndex = wrapper.getIndexForOtuName(taxa);
		
		if( taxaIndex == -1 )
			throw new Exception("Could not find " + taxa);
		
		
		for( int x=0; x < wrapper.getSampleNames().size(); x++)
		{
			String sampleName = wrapper.getSampleNames().get(x);
			MappingFileLine mfl = metaMap.get(sampleName);
			
			if( mfl.getTissue().equals(tissue))
			{
				if(mfl.getLine().equals("Low"))
					h.lowSach.add(wrapper.getDataPointsNormalized().get(x).get(taxaIndex));
				else if( mfl.getLine().equals("High"))
					h.hiSach.add(wrapper.getDataPointsNormalized().get(x).get(taxaIndex));
				else throw new Exception("Unexpected line " + mfl.getLine());
			}
		}
		
		
		return h;
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, MappingFileLine> metaMap = MappingFileLine.getMap();
		
		NumberFormat nf = NumberFormat.getInstance();
		
		nf.setMinimumFractionDigits(3);
		HashMap<String, String> otuTaxMap = GreengenesOtuLookup.getLookupMap();
		HashMap<String, String> genusMap = RDPLookup.getRDPLookupByGenus();
		
		String[] tissues = { "Cecal Content", "Colon content" };
		
		for(String tissue: tissues)
		{
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
					ConfigReader.getRachSachReanalysisDir()
					+ File.separator + "rdpAnalysis" 
					+ File.separator + "pValueTaxaSummary" + tissue +  ".txt")));
			
			writer.write("taxa\tfdrPValue\tupIn\tfullTax\tavgHigh\tavgLow\tratio\n");
			
			String[] levels = { "phylum","class","order","family","genus", "otu" };
				for( String level: levels)
			{
					OtuWrapper wrapper = level.equals("otu") ? 
							new OtuWrapper(
									ConfigReader.getRachSachReanalysisDir() + File.separator + "rdpAnalysis" + File.separator + 
									"sparseThreeColumn_" +  "otu" +  "_AsColumns_" + "all"+  ".txt") : 
							
							new OtuWrapper(ConfigReader.getRachSachReanalysisDir()
							+ File.separator + "rdpAnalysis" 
							+ File.separator + "sparseThreeColumn_" + level + 
								"_AsColumns.txt");	
					
				System.out.println(level);
				writer.write("\n" + level + "\n");
				
				BufferedReader reader = new BufferedReader(new FileReader(new File(
						ConfigReader.getRachSachReanalysisDir()
						+ File.separator + "rdpAnalysis" 
						+ File.separator + "pValuesForTime_taxa_"+ tissue +  "_" + level + ".txt")));
				
				reader.readLine();
				
				for(String s = reader.readLine() ; s != null; s= reader.readLine())
				{
					String[] splits = s.split("\t");
					if( splits.length != 5)
						throw new Exception("Parsing error");
					
					String key = splits[0].replaceAll("\"","");
					
					if( key.equals("Clostridium.XVIII"))
						key = "Clostridium XVIII";
					else 
						key = key.replaceAll("\\.", " ");
					
					if( key.equals("Escherichia Shigella"))
						key = "Escherichia/Shigella";
					
					if( level.equals("otu"))
						key = key.replaceAll("X", "");
					
					Holder h = getUnnormalizedData(metaMap, tissue, key, wrapper);
					
					if( Double.parseDouble(splits[4]) < 0.10 )
					{
						String higher = "high sac";
						
						if( Double.parseDouble(splits[3] ) > Double.parseDouble(splits[2]))
							higher = "low sac";
						
						writer.write( key + "\t" 
								+ nf.format(Double.parseDouble(splits[4]))  + "\t" +  higher + "\t");
						
						if( level.equals("otu") )
							writer.write(otuTaxMap.get(key) + "\t");
						else if( level.equals("genus"))
							writer.write(genusMap.get(key) + "\t");
						else
							writer.write("NA\t");
						
						double meanHigh = new Avevar(h.hiSach).getAve();
						double meanLow= new Avevar(h.lowSach).getAve();
						
						double ratio= meanHigh / meanLow;
						
						if( ratio< 1)
							ratio = -1/ ratio;
						
						writer.write(meanHigh + "\t");
						writer.write(meanLow + "\t");
						writer.write(ratio + "\n");
						
						writer.flush();
					}
				}
			}
				
			writer.flush();  writer.close();
		}
	}
}
