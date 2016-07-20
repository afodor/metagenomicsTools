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
import parsers.FastaSequence;
import utils.ConfigReader;
import utils.Translate;

public class CoinFlipTest
{
	public static final int KMER_SIZE = 31;
	
	private static class GeneHolder
	{
		List<RangeHolder> rangeList=new ArrayList<RangeHolder>();
		String chr;
		int numOver;
		int numUnder;
		double conservationSum =0;
		double pValueSum =0;
		double ratio1Sum =0;
		double ratio2Sum =0;
		
		HashSet<Long> includedKmers = new HashSet<Long>();
	}
	
	static class RangeHolder 
	{
		int stop;
		int start;
		
		RangeHolder(int start, int stop)
		{
			this.stop = stop;
			this.start = start;
			
			if( this.stop < this.start)
			{
				int temp = this.stop;
				this.stop = this.start;
				this.start = temp;
			}
		}
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, GeneHolder> map = parseGTFFile();
		List<BinHolder> list = parseBinFile();
		addKmersToGenes( new ArrayList<GeneHolder>(map.values()));
		
		addCoinFlips(map, list);
		writeResults(map);
			
	}
	
	private static void writeResults(HashMap<String, GeneHolder> map) throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				ConfigReader.getBioLockJDir() + File.separator + "resistantAnnotation" + 
						File.separator + "coinFlipsSucVsRes.txt"	)));
		
		writer.write( "geneID\tchromosonme\tnumberOfKmers\tnumUp\tnumDown\tfractionUp\tconservationAvg\tpValueAvg\t" );
		writer.write("ratio1Sum\tratio2Sum\n");
		
		for(String s : map.keySet())
		{
			GeneHolder h = map.get(s);
			
			writer.write(s + "\t" + h.chr + "\t" + h.includedKmers.size() + "\t" + 
							h.numOver + "\t" + h.numUnder + "\t");
			
			int totalNum = h.numOver + h.numUnder;
			float fraction = ((float)h.numOver) /totalNum;
			
			writer.write(fraction + "\t");
			writer.write( (h.conservationSum / totalNum) + "\t");
			writer.write( (h.pValueSum/totalNum) + "\t");
			writer.write( (h.ratio1Sum / totalNum) + "\t");
			writer.write( (h.ratio2Sum / totalNum) + "\n");
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
			
			for( RangeHolder rh : gh.rangeList )
			{
				for( int x=rh.start - 1; x + KMER_SIZE <= rh.stop ; x++)
				{
					String nucl = seq.substring(x, x + KMER_SIZE);
					
					Long encode = Encode.makeLong(nucl);
					
					if( encode != null)
						gh.includedKmers.add(encode);
					
					nucl = Translate.safeReverseTranscribe(nucl);
					encode = Encode.makeLong(nucl);
					
					if( encode != null)
						gh.includedKmers.add(encode);					
			}
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
	
	private static void addCoinFlips(HashMap<String, GeneHolder> geneMap,
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
				double conservation = Double.parseDouble(splits[6]);
				BinHolder bh = getABin(conservation, binList);
				
				double val = - Math.log10(Double.parseDouble(splits[5]));
				boolean isOver =  val >= bh.average;
				
				int cond1Withkmer = Integer.parseInt(splits[1]);
				float totalcond1 = Float.parseFloat(splits[1]) + Float.parseFloat(splits[2]);
				float ratio1Sum = cond1Withkmer /totalcond1;
				

				int cond2Withkmer = Integer.parseInt(splits[3]);
				float totalcond2 = Float.parseFloat(splits[3]) + Float.parseFloat(splits[4]);
				float ratio2Sum = cond2Withkmer /totalcond2;
				
				
				for(GeneHolder gh : set)
				{
					if(isOver)
						gh.numOver++;
					else
						gh.numUnder++;
					
					gh.conservationSum += conservation;
					gh.pValueSum += val;
					gh.ratio1Sum += ratio1Sum;
					gh.ratio2Sum += ratio2Sum;
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
	
	static HashMap<String, GeneHolder> parseGTFFile() throws Exception
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
