package creOrthologs.kmers.chunk;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import parsers.FastaSequence;
import utils.ConfigReader;

public class FindChunksAcrossMultipleContigs
{
	public static final double INITIATION_THRESHOLD = 0.85f;
	public static final double EXTENSION_THRESHOLD = 0.9f;
	
	public static void main(String[] args) throws Exception
	{
		List<Holder> list = getList();
		list = refineList(list);
		writeAllHolders(list);
		
		List<ChunkHolder> chunks = getChunks(list);
		writeChunks(chunks);
 	}
	
	private static void writeAllHolders(List<Holder> list ) throws Exception
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
	}
	
	private static List<Holder> refineList(List<Holder> list ) throws Exception
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
	}
	
	private static Holder getOne(String contig, int start , int end,
				List<Holder> list) throws Exception
	{
		for( Holder h : list)
			if( h.contig.equals(contig) && h.startPos == start && h.endPos == end) 
				return h;
		
		return null;
	}
	
	private static void writeChunks( List<ChunkHolder> list ) throws Exception
	{
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
			ConfigReader.getCREOrthologsDir() + File.separator + 
			"pneuOnlyChunksWithContigs_" + nf.format(INITIATION_THRESHOLD) + "_" 
					+  nf.format(EXTENSION_THRESHOLD) + ".txt")));
		
		writer.write("Contig\tInitialStart\tEndStart\tlength\taverage\n");
		
		for(ChunkHolder ch : list)
		{
			writer.write("\"Contig_" +ch.contig +"\"" + "\t");
			writer.write(ch.firstStart + "\t");
			writer.write(ch.lastStart + "\t");
			writer.write(ch.n + "\t");
			writer.write( ch.spearmanSum / ch.n + "\n");
		}
		
		writer.flush();  writer.close();
	}
	
	
	private static List<Holder> getList() throws Exception
	{
		List<Holder> list = new ArrayList<Holder>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
			ConfigReader.getCREOrthologsDir() + File.separator + 
				"allContigsChs11PneuPlusNoPneu.txt")));
		
		reader.readLine();
		
		for(String s =reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			if( splits.length != 3)
				throw new Exception("No");
			
			String[] nameSplits = splits[0].split("_");
			
			if( nameSplits.length != 8)
				throw new Exception("No");
			
			if( ! nameSplits[3].equals("11.0"))
				throw new Exception("No " + splits[0]);
			
			Holder h = new Holder();
			list.add(h);
			h.contig = nameSplits[4];
			h.startPos = Integer.parseInt(nameSplits[5]);
			h.endPos = Integer.parseInt(nameSplits[6]);
			h.spearmanPneuOnly = Double.parseDouble(splits[2]);
		}
		
		Collections.sort(list);
		return list;
	}
	
	private static class ChunkHolder
	{
		private final String contig;
		private int firstStart;
		private int lastStart;
		double spearmanSum =0;
		int n=0;
		
		public ChunkHolder(String contig, int firstStart, int lastStart)
		{
			this.contig = contig;
			this.firstStart = firstStart;
			this.lastStart = lastStart;
		}
		
		
	}
	

	private static boolean positionIsInChunk(List<ChunkHolder> list, int position)
	{
		for(ChunkHolder ch : list)
			if( ch.firstStart >= position && ch.lastStart <= position)
				return true;
		
		return false;
	}
	
	private static List<ChunkHolder> getChunks( List<Holder> list )
	{
		List<ChunkHolder> chunks = new ArrayList<ChunkHolder>();
		
		ChunkHolder currentChunk = null;
		
		int lastIndex = 0;
		
		while(lastIndex < list.size())
		{
			Holder thisHolder = list.get(lastIndex);
			if( currentChunk == null)
			{
				if(thisHolder.spearmanPneuOnly<= INITIATION_THRESHOLD)
				{
					System.out.println("Initating " + thisHolder.contig + " "+  thisHolder.startPos + " " + 
							thisHolder.spearmanPneuOnly);
					currentChunk = new ChunkHolder(thisHolder.contig,thisHolder.startPos,thisHolder.endPos);
					chunks.add(currentChunk);
					currentChunk.n = 1;
					currentChunk.spearmanSum += thisHolder.spearmanPneuOnly;
					
					int lookBack = lastIndex -1;
					
					if( chunks.size() ==20)
						System.exit(1);
					
					boolean stop = false;
					while(lookBack > 0 && ! stop)
					{
						Holder previous = list.get(lookBack);
						
						if( previous.contig.equals(currentChunk.contig) && 
										previous.spearmanPneuOnly <= EXTENSION_THRESHOLD && 
								! positionIsInChunk(chunks, previous.startPos))
						{
							currentChunk.firstStart = previous.startPos;
							currentChunk.n = currentChunk.n + 1;
							currentChunk.spearmanSum += thisHolder.spearmanPneuOnly;
							lookBack--;
						}
						else
						{
							stop = true;
						}
					}
				}
			}
			else
			{
				if( thisHolder.spearmanPneuOnly <= EXTENSION_THRESHOLD 
						&& thisHolder.contig.equals(currentChunk.contig))
				{
					currentChunk.lastStart = thisHolder.startPos;
					currentChunk.n = currentChunk.n + 1;
					currentChunk.spearmanSum+= thisHolder.spearmanPneuOnly;
				}
				else
				{
					currentChunk = null;
				}
			}
			
			lastIndex++;
		}
		
		return chunks;
	}
	
	private static class Holder implements Comparable<Holder>
	{
		String contig;
		int startPos;
		int endPos;
		double spearmanPneuOnly;
		
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
