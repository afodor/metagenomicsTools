package creOrthologs.coinFlip;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.StringTokenizer;

import bitManipulations.Encode;
import creOrthologs.coinFlip.CoinFlipTest.RangeHolder;
import parsers.FastaSequence;
import utils.ConfigReader;
import utils.Translate;

public class WriteKmersWithDifferentAdjacentPValues
{
	private static class GeneHolder
	{
		List<RangeHolder> rangeList=new ArrayList<RangeHolder>();
		String chr;
		List<Long> includedKmers = new ArrayList<Long>();
		List<Float> pValues = new ArrayList<Float>();
		List<Float> ratioConserved = new ArrayList<Float>(); 
		
		int getIndex(long aLong) throws Exception
		{
			for( int x=0; x < includedKmers.size(); x++)
				if( includedKmers.get(x).equals(aLong))
					return x;
			
			throw new Exception("Could not find " + aLong);
		}
	}
	
	
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, GeneHolder> map = parseGTFFile();
		List<BinHolder> list = parseBinFile();
		addKmersToGenes( new ArrayList<GeneHolder>(map.values()));
		
		addPValues(map, list);
		writeResults(map);
			
	}
	
	private static void writeResults(HashMap<String, GeneHolder> map) throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				ConfigReader.getBioLockJDir() + File.separator + "resistantAnnotation" + 
						File.separator + "nonRedundantPValsVsCons_ResVsSuc.txt"	)));
		
		writer.write( "geneID\tchromosome\tnumberOfKmers\tencodedKmer\tpValue\tconservation\n" );
		
		for(String s : map.keySet())
		{
			GeneHolder h = map.get(s);
			
			Float last = null;
			
			for( int x=0; x < h.pValues.size(); x++ )
			{
				Float thisP = h.pValues.get(x);
				
				if( thisP != null)
					if( last == null || ! thisP.equals(last))
					{
						writer.write( s + "\t" + h.chr + "\t" + h.includedKmers.size() + "\t" + 
										h.includedKmers.get(x) + "\t" + h.pValues.get(x) + "\t" + 
												h.ratioConserved + "\n");
						last = thisP;
					}
			}
			
			
		}
		
		writer.flush();  writer.close();
	}
	
	private static void addKmersToGenes(List<GeneHolder> geneHolderList) throws Exception
	{
		HashMap<String, FastaSequence> fastaMap = FastaSequence.getFirstTokenSequenceMap(
				ConfigReader.getBioLockJDir() + File.separator + "resistantAnnotation" + 
						File.separator +"refGenome" + File.separator 
						+ "klebsiella_pneumoniae_chs_11.0.scaffolds.fasta");
		
		int index=0;
		for(GeneHolder gh: geneHolderList)
		{
			System.out.println(index++ + " " + geneHolderList.size());
			FastaSequence fs = fastaMap.get(gh.chr);
			String seq = fs.getSequence();
			List<Long> backEncodes = new ArrayList<Long>();
			
			for( RangeHolder rh : gh.rangeList )
			{
				for( int x=rh.start - 1; x +  CoinFlipTest.KMER_SIZE <= rh.stop ; x++)
				{
					String nucl = seq.substring(x, x + CoinFlipTest.KMER_SIZE);
					
					Long encode = Encode.makeLong(nucl);
					
					if( encode != null)
						gh.includedKmers.add(encode);
					
					nucl = Translate.safeReverseTranscribe(nucl);
					encode = Encode.makeLong(nucl);
					
					if( encode != null)
						backEncodes.add(encode);					
				}
			}
			
			gh.includedKmers.addAll(backEncodes);
			
			for( int x=0; x < gh.includedKmers.size(); x++)
			{
				gh.pValues.add(null);
				gh.ratioConserved.add(null);
			}
		}
	}
	
	private static HashMap<Long, HashSet<GeneHolder>> getAsLongMap(  HashMap<String, GeneHolder> map)
		throws Exception
	{
		HashMap<Long, HashSet<GeneHolder>> longMap = new HashMap<Long, HashSet<GeneHolder>>();
				
		for(GeneHolder gh : map.values())
			for( Long l : gh.includedKmers )
			{
				HashSet<GeneHolder> set =longMap.get(l);
				
				if( set==null)
				{
					set = new HashSet<GeneHolder>();
					longMap.put(l, set);
				}
				
				set.add(gh);
			}
		
		return longMap;
				
	}
 	
	//todo: this is inefficient
	private static BinHolder getABin(double val, List<BinHolder> binList) throws Exception
	{
		for( BinHolder bh : binList)
			if( val >= bh.low && val <= bh.high )
				return bh;
		
		throw new Exception("Could not find " + val);
	}
	
	private static void addPValues(HashMap<String, GeneHolder> geneMap,
					List<BinHolder> binList) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(
			ConfigReader.getBioLockJDir() + File.separator + "resistantAnnotation" 
						+ File.separator + 
					"resistantVsSuc_kneu.txt"));
	
		
		reader.readLine();
	
		System.out.println("Building Map");
		HashMap<Long, HashSet<GeneHolder>> longMap = getAsLongMap(geneMap);
		System.out.println("Done");
		
		int index=0;
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			HashSet<GeneHolder> set = longMap.get( Long.parseLong(splits[0]));
			
			if( set != null)
			{
				long encodedLong = Long.parseLong(splits[0]);
				float conservation = Float.parseFloat(splits[6]);
				BinHolder bh = getABin(conservation, binList);
				
				Float val = (float) (- Math.log10(Double.parseDouble(splits[5])));
								
				for(GeneHolder gh : set)
				{
					int listIndex = gh.getIndex(encodedLong);
					gh.ratioConserved.set(listIndex, conservation);
					gh.pValues.set(listIndex, val);
				}
				
			}
			
			index++;
			
			if( index % 1000 ==0)
				System.out.println(index);
			
		}
		
		reader.close();
	}
	
	
	private static class BinHolder
	{
		private double low;
		private double high;
		
		private double average;
	}
	
	private static List<BinHolder> parseBinFile() throws Exception
	{
		List<BinHolder> list = new ArrayList<BinHolder>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getBioLockJDir() + 
		File.separator + "resistantAnnotation" + File.separator + "resistantVsSucSummary.txt")));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			BinHolder h = new BinHolder();
			h.low = Double.parseDouble(splits[0]);
			h.high = Double.parseDouble(splits[1]);
			h.average = Double.parseDouble(splits[3]);
			list.add(h);
		}
			
		
		return list;
	}
	
	private static HashMap<String, GeneHolder> parseGTFFile() throws Exception
	{
		HashMap<String, GeneHolder> map = new LinkedHashMap<String, GeneHolder>();
		
		BufferedReader reader =new BufferedReader(new FileReader(
			ConfigReader.getBioLockJDir() + File.separator + 
				"resistantAnnotation" + File.separator + 
						"klebsiella_pneumoniae_chs_11.0.genes.gtf"	));
		
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			String key = new StringTokenizer(splits[8], ";").nextToken();
			key = key.replace("gene_id", "").replaceAll("\"", "");
			
			GeneHolder gh = map.get(key);
			
			if( gh == null)
			{
				gh = new GeneHolder();
				gh.chr = splits[0];
				map.put(key,gh);
			}
			
			RangeHolder h = new RangeHolder(Integer.parseInt(splits[3]), Integer.parseInt(splits[4]));
			gh.rangeList.add(h);
			if( ! gh.chr.equals(splits[0]))
				throw new Exception("Parsing error");
		}
		
		reader.close();
		
		return map;
	}
}
