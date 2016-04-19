package kw_cre;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.NumberFormat;
import java.util.ArrayList;
//import java.util.Collections;
import java.util.List;

//import parsers.FastaSequence;
import utils.ConfigReader;

public class FindChunksAcrossMultipleContigsPvals
{
	//public static final double INITIATION_THRESHOLD = 0.85f;
	//public static final double EXTENSION_THRESHOLD = 0.9f;
	
	public static void main(String[] args) throws Exception
	{
		System.out.println("getting lists");
		List<Holder> carVres = getList(4);
		List<Holder> carVsus = getList(5);
		List<Holder> resVsus = getList(6);
		/*list = refineList(list);
		writeAllHolders(list);*/
		
		//List<ChunkHolder> chunks = getChunks(carVres, 30, 15, "car_v_res");
		System.out.println("getting chunks");
		getChunks(carVres, 15, 10, "car_v_res");
		getChunks(carVres, 10, 5, "car_v_res");
		
		getChunks(carVsus, 35, 15, "car_v_sus");
		getChunks(carVsus, 30, 15, "car_v_sus");
		getChunks(carVsus, 30, 10, "car_v_sus");
		getChunks(carVsus, 20, 10, "car_v_sus");
		getChunks(carVsus, 15, 10, "car_v_sus");
		
		getChunks(resVsus, 20, 10, "res_v_sus");
		getChunks(resVsus, 15, 10, "res_v_sus");
		getChunks(resVsus, 10, 5, "res_v_sus");
 	}
	
	/*private static void writeAllHolders(List<Holder> list) throws Exception
	{
		BufferedWriter writer =new BufferedWriter(new FileWriter(new File(
				ConfigReader.getCREOrthologsDir()  +File.separator + 
				"allHolders.txt")));
		
		writer.write("contig\tstart\tstop\tpearsonPneuOnly\n");
		
		for(Holder h : list)
		{
			writer.write("\"Contig_" + h.contig + "\"" + "\t" + h.startPos+ "\t" + h.endPos + "\t" + h.spearmanPneuOnly + "\n");
		}
		
		writer.flush();  writer.close();
	}*/
	
	/*private static List<Holder> refineList(List<Holder> list ) throws Exception
	{
		List<Holder> newList =new ArrayList<Holder>();
		
		List<FastaSequence> fastaList= 
				FastaSequence.readFastaFile(ConfigReader.getCREOrthologsDir() + File.separator + 
						"carolina" + File.separator + 
							"klebsiella_pneumoniae_chs_11.0.scaffolds.fasta");
		
		int found =0;
		int notFound = 0;
		for(FastaSequence fs : fastaList)
		{
			int length = fs.getSequence().length();
			
			boolean keepGoing = true;
			int start =0;
			int stop = Math.min(start + 5000, length-1);
			
			while(keepGoing)		
			{
		
				Holder h = getOne(fs.getFirstTokenOfHeader(), start, stop, list);
				
				if( h == null )
				{
					System.out.println("Couldn't find " + fs.getFirstTokenOfHeader() + " " 
							+ start + " " + stop);
					
					notFound++;
		
				}
				else
				{
					newList.add(h);
					found++;
				}
				
				start += 1000;
				stop = Math.min(start+5000, length-1);
				
				if( start > length)
					keepGoing = false;
			}
		}
		System.out.println( found + "  " + notFound);
	
		return newList;
	}*/
	
	/*private static Holder getOne(String contig, int start , int end,
				List<Holder> list) throws Exception
	{
		for( Holder h : list)
			if( h.contig.equals(contig) && h.startPos == start && h.endPos == end) 
				return h;
		
		return null;
	}*/
	
	/*
	 * writes the chunks given in list to a file, with the initiation and extension
	 * thresholds and given name in the output file name
	 */
	private static void writeChunks( List<ChunkHolder> list, 
			double inititationThreshold, double extensionThreshold,
			String name) throws Exception
	{
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
			ConfigReader.getCREOrthologsDir() + File.separator + 
			"pneuOnlyPvalChunksWithContigs_" + name + "_I"
					+ nf.format(inititationThreshold) + "_E" 
					+  nf.format(extensionThreshold) + ".txt")));
		
		writer.write("Contig\tstart\tend\tnumberGenes\taverageP\n");
		
		for(ChunkHolder ch : list)
		{
			writer.write(ch.contig + "\t");
			writer.write(ch.start+ "\t");
			writer.write(ch.end+ "\t");
			writer.write(ch.n + "\t");
			writer.write(ch.pSum / ch.n + "\n");
		}
		
		writer.flush();  writer.close();
	}
	
	/*
	 * returns a list of holders (which hold scaffold, start, stop and p-value)
	 * pcol = column containing p-values to analyze
	 */
	private static List<Holder> getList(int pcol) throws Exception
	{
		List<Holder> list = new ArrayList<Holder>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
			ConfigReader.getCREOrthologsDir() + File.separator + 
				"chs11_klebpneu_rbh_pvalues_log_withGenePos_sorted.txt")));
		
		reader.readLine();
		
		for(String s =reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			if( splits.length != 10) {
				reader.close();
				throw new Exception("Incorrect line length " + splits.length + " "
						+ s);
			}
			
			if(!splits[pcol].equals("NA")) {//skip NAs
				Holder h = new Holder();
				list.add(h);
				h.contig = splits[1];
				h.startPos = Integer.parseInt(splits[2]);
				h.endPos = Integer.parseInt(splits[3]);
				h.pValue = Double.parseDouble(splits[pcol]);
			}
		}
		
		reader.close();
		//Collections.sort(list);//table is sorted
		return list;
	}
	
	private static class ChunkHolder
	{
		private final String contig;
		private int start;
		private int end;
		double pSum =0;
		int n;
		
		public ChunkHolder(String contig, int start, int end, double initialSpearman)
		{
			this.contig = contig;
			this.start= start;
			this.end = end;
			
			this.n = 1;
			this.pSum += initialSpearman;
			
		}
		
		
	}
	

	private static boolean positionIsInChunk(List<ChunkHolder> list, int position)
	{
		for(ChunkHolder ch : list)
			if((ch.start>= position && ch.end<= position) ||
					(ch.start <= position && ch.end >= position))
				return true;
		
		return false;
	}
	
	/*
	 * returns the chunks for the given list and initiation/extension threshold
	 * when the initiationThreshold is seen, the peak is extended until the 
	 * extensionThreshold is seen
	 * then writes the results to a file containing the given name
	 */
	private static List<ChunkHolder> getChunks(List<Holder> list, 
			double inititationThreshold, double extensionThreshold,
			String name) throws Exception
	{
		List<ChunkHolder> chunks = new ArrayList<ChunkHolder>();
		
		ChunkHolder currentChunk = null;
		
		int lastIndex = 0;
		
		while(lastIndex < list.size())
		{
			Holder thisHolder = list.get(lastIndex);
			if(currentChunk == null)
			{
				if(Math.abs(thisHolder.pValue) >= inititationThreshold) //new peak
				{
					currentChunk = new ChunkHolder(thisHolder.contig,thisHolder.startPos,thisHolder.endPos,
							thisHolder.pValue);
					chunks.add(currentChunk);
					
					int lookBack = lastIndex -1;
					
					boolean stop = false;
					while(lookBack > 0 && ! stop) 
					{
						Holder previous = list.get(lookBack);
						
						if(previous.contig.equals(currentChunk.contig) && 
								Math.abs(previous.pValue) >= extensionThreshold && 
								! positionIsInChunk(chunks, previous.startPos))
						{//extend chunk
							currentChunk.start= previous.startPos;
							currentChunk.n = currentChunk.n + 1;
							currentChunk.pSum += thisHolder.pValue;
							lookBack--;
						}
						else
						{//chunk is done
							stop = true;
						}
					}
				}
			}
			else
			{
				if(Math.abs(thisHolder.pValue) >= extensionThreshold 
						&& thisHolder.contig.equals(currentChunk.contig))
				{
					currentChunk.end= thisHolder.endPos;
					currentChunk.n = currentChunk.n + 1;
					currentChunk.pSum+= thisHolder.pValue;
				}
				else if(Math.abs(thisHolder.pValue) >= inititationThreshold)
				{
					// if we are here, new chunk on new contig
					currentChunk = new ChunkHolder(thisHolder.contig,thisHolder.startPos,thisHolder.endPos,
							thisHolder.pValue);
					chunks.add(currentChunk);
				}
				else
				{
					currentChunk = null;
				}
			}
			
			lastIndex++;
		}
		
		writeChunks(chunks, inititationThreshold, extensionThreshold, name);
		
		return chunks;
	}
	
	private static class Holder implements Comparable<Holder>
	{
		String contig;
		int startPos;
		int endPos;
		double pValue;
		
		@Override
		public int compareTo(Holder o)
		{
			if( ! this.contig.equals(o.contig) )
			{
				return this.contig.compareTo(o.contig);
			}
			
			return this.startPos - o.startPos;
		}
	}
}
