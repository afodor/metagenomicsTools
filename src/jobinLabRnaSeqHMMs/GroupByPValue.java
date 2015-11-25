package jobinLabRnaSeqHMMs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import utils.ConfigReader;

public class GroupByPValue
{
	public static double INITATION_THRESHOLD = 0.95;
	public static double EXTENSION_THRESHOLD = 0.75;
	
	public enum Assigned{
		UP, DOWN, NOT_ASSIGNED
	}
	
	private static String headerLine = null;
	
	private static class Gatherer implements Comparable<Gatherer>
	{
		List<Holder> list = new ArrayList<Holder>();
		
		@Override
		public int compareTo(Gatherer arg0)
		{
			return arg0.list.size() - this.list.size();
		}
	}
	
	/*
	 * This is dependent on jobLabRnaSeqHMMs.PosteriorDecode
	 */
	public static void main(String[] args) throws Exception
	{
		List<Holder> list = getHolderList(ConfigReader.getJobinLabRNASeqDir() + File.separator + "12WeeksVs18WeeksPlusAnnotationDecoded.txt");
		markUpProb(list);
		markDownProb(list);
		writeResults(list,ConfigReader.getJobinLabRNASeqDir() + File.separator + "12WeeksVs18WeeksPlusAnnotationDecodedAssignment.txt" );
		writeGatheredResults(list, ConfigReader.getJobinLabRNASeqDir() + File.separator + "12WeeksVs18WeeksPlusAnnotationDecodedAssignmentGathered.txt");
	}
	
	private static void writeGatheredResults( List<Holder> list, String filePath ) throws Exception
	{
		int clusterNum=1;
		List<Gatherer> gList = new ArrayList<Gatherer>();
		
		int index=0;
		
		while( index < list.size())
		{
			Holder h = list.get(index);
			String oldGff = h.gffFile;
			
			if( h.assignment == Assigned.UP)
			{
				Gatherer g = new Gatherer();
				gList.add(g);
				g.list.add(h);
				index++;
				
				while(index < list.size() && list.get(index).assignment==Assigned.UP 
						&& oldGff.equals(list.get(index).gffFile))
				{
					g.list.add(list.get(index)); index++;
				}
				
			} else if ( h.assignment == Assigned.DOWN) 
			{
				Gatherer g = new Gatherer();
				gList.add(g);
				g.list.add(h);
				index++;
				
				while(index < list.size() && list.get(index).assignment==Assigned.DOWN && 
						oldGff.equals(list.get(index).gffFile))
				{
					g.list.add(list.get(index)); index++;
				}
			}
			else
			{
				index++;
			}	
		}
		
		Collections.sort(gList);
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(filePath)));
		
		for( Gatherer g : gList)
		{
			writer.write( "Cluster:" + clusterNum + "\tsize=" +  g.list.size() + "\n" );
			clusterNum++;
			
			writer.write("\t" + headerLine + "\tassignment\n");
			for( Holder h : g.list)
			{
				writer.write("\t" + h.fileLine + "\t" + h.assignment + "\n");
			}
		}
		
		writer.flush();  writer.close();
	}
	
	private static void writeResults(List<Holder> list, String filePath) throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(filePath)));
		
		writer.write(headerLine + "\tAssignment\n");
		
		for( Holder h : list)
			writer.write(h.fileLine + "\t" + h.assignment + "\n");
	
		
		writer.flush(); writer.close();
	}
	
	private static void markDownProb(List<Holder> list)
	{
		List<Holder> listCopy = new ArrayList<Holder>( list );
		Collections.sort(listCopy, new SortByDownProb());
		
		for( Holder h : listCopy)
		{
			if( h.downProb>= INITATION_THRESHOLD && h.assignment == Assigned.NOT_ASSIGNED)
			{
				h.assignment = Assigned.DOWN;
				
				// walk up
				int index = h.index + 1;
				
				boolean keepGoing = true;
				
				while( keepGoing && index < list.size() )
				{
					Holder otherHolder = list.get(index);
					
					if( otherHolder.assignment != Assigned.NOT_ASSIGNED || otherHolder.downProb< EXTENSION_THRESHOLD 
							|| ! otherHolder.gffFile.equals(h.gffFile) )
					{
						keepGoing = false;
					}
					else
					{
						otherHolder.assignment= Assigned.DOWN;
						index++;
					}
				}
				
				// walk down
				index = h.index -1;
				
				keepGoing = true;
				
				while( keepGoing && index >= 0 )
				{
					Holder otherHolder = list.get(index);
					
					if( otherHolder.assignment != Assigned.NOT_ASSIGNED  || otherHolder.downProb< EXTENSION_THRESHOLD 
							|| ! otherHolder.gffFile.equals(h.gffFile) )
					{
						keepGoing = false;
					}
					else
					{
						otherHolder.assignment = Assigned.DOWN;
						index--;
					}
				}
			}
		}
	}
	
	private static void markUpProb(List<Holder> list)
	{
		List<Holder> listCopy = new ArrayList<Holder>( list );
		Collections.sort(listCopy, new SortByUpProb());
		
		for( Holder h : listCopy)
		{
			if( h.upProb >= INITATION_THRESHOLD && h.assignment == Assigned.NOT_ASSIGNED)
			{
				h.assignment = Assigned.UP;
				
				// walk up
				int index = h.index + 1;
				
				boolean keepGoing = true;
				
				while( keepGoing && index < list.size() )
				{
					Holder otherHolder = list.get(index);
					
					if( otherHolder.assignment != Assigned.NOT_ASSIGNED || otherHolder.upProb < EXTENSION_THRESHOLD 
							|| ! otherHolder.gffFile.equals(h.gffFile) )
					{
						keepGoing = false;
					}
					else
					{
						otherHolder.assignment= Assigned.UP;
						index++;
					}
				}
				
				// walk down
				index = h.index -1;
				
				keepGoing = true;
				
				while( keepGoing && index >= 0 )
				{
					Holder otherHolder = list.get(index);
					
					if( otherHolder.assignment != Assigned.NOT_ASSIGNED  || otherHolder.upProb < EXTENSION_THRESHOLD 
							|| ! otherHolder.gffFile.equals(h.gffFile) )
					{
						keepGoing = false;
					}
					else
					{
						otherHolder.assignment = Assigned.UP;
						index--;
					}
				}
			}
		}
	}
	
	private static class SortByUpProb implements Comparator<Holder>
	{
		@Override
		public int compare(Holder arg0, Holder arg1)
		{
			return Double.compare(arg1.upProb, arg0.upProb);
		}
	}
	
	private static class SortByDownProb implements Comparator<Holder>
	{
		@Override
		public int compare(Holder arg0, Holder arg1)
		{
			return Double.compare(arg1.downProb, arg0.downProb);
		}
	}
	
	private static List<Holder> getHolderList( String filePath ) throws Exception
	{
		List<Holder> list = new ArrayList<Holder>();
		
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		
		headerLine = reader.readLine();
		
		int index =0;
		for(String s = reader.readLine();	
					s != null;
						s = reader.readLine())
		{
			Holder h = new Holder();
			h.fileLine = s;
			list.add(h);
			String[] splits = s.split("\t");
			h.downProb = Double.parseDouble(splits[11]);
			h.upProb = Double.parseDouble(splits[13]);
			h.gffFile = splits[5];
			h.index = index;
			index++;
		}
		
		reader.close();
		
		return list;
	}
	
	private static class Holder
	{
		String fileLine;
		double downProb;
		double upProb;
		String gffFile;
		Assigned assignment = Assigned.NOT_ASSIGNED;
		int index;
	}
}
