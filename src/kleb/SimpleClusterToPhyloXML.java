package kleb;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import utils.ConfigReader;


public class SimpleClusterToPhyloXML
{
	/*
	 * Run QuickSnpDistance first
	 */
	public static void main(String[] args) throws Exception
	{
		HashMap<Integer, StrainMetadataFileLine> metaMap = StrainMetadataFileLine.parseMetadata();
		List<DistanceHolder> initialDistances = getInitialDistances();
		List<DistanceHolder> mergedList= new ArrayList<DistanceHolder>();
		
		while( initialDistances.size() > 0)
			mergeOne(initialDistances, mergedList);
		
		for( DistanceHolder dh : initialDistances)
			System.out.println(dh);
		
		System.out.println("MERGES   MERGES");
		
		for( DistanceHolder dh : mergedList)
			System.out.println(dh);
		
		writePhyloXml(mergedList, metaMap);
		
	}
	
	private static void mergeOne( List<DistanceHolder> distanceList, List<DistanceHolder> mergedNodes)
		throws Exception
	{
		DistanceHolder topHolder = distanceList.remove(0);
		mergedNodes.add(topHolder);
		
		List<DistanceHolder> toMerge = new ArrayList<DistanceHolder>();
		
		for( Iterator<DistanceHolder> i = distanceList.iterator(); i.hasNext();)
		{
			DistanceHolder dh = i.next();
			
			if( dh.leftStrains.equals(topHolder.leftStrains) || 
					dh.leftStrains.equals(topHolder.rightStrains) || 
					dh.rightStrains.equals(topHolder.rightStrains) ||
					dh.rightStrains.equals(topHolder.leftStrains))
			{
				toMerge.add(dh);
				i.remove();
			}
		}
		
		//System.out.println("MERGE LIST");
		for( DistanceHolder dh : toMerge)
		{

			if( dh.rightStrains.equals(topHolder.rightStrains) && dh.leftStrains.equals(topHolder.leftStrains) )
			{
				throw new Exception("Logic error");
			}
		
			
			if( ! dh.rightStrains.equals(topHolder.leftStrains) && ! dh.rightStrains.equals(topHolder.rightStrains) )
			{
				List<Integer> temp = dh.leftStrains;
				dh.leftStrains = dh.rightStrains;
				dh.rightStrains= temp;
			}
		}
		
		HashMap<List<Integer>, LeftAndRightMatching> 
				leftRightMap = pairHolders(toMerge, topHolder.leftStrains , topHolder.rightStrains);
		
		for(List<Integer> list : leftRightMap.keySet())
		{
			if( ! leftRightMap.get(list).leftMatching.leftStrains.equals(list))
				throw new Exception("Logic error");
			
			if( ! leftRightMap.get(list).rightMatching.leftStrains.equals(list))
				throw new Exception("Logic error");
			
			//System.out.println(list + " "
				//		+ leftRightMap.get(list).leftMatching.rightStrains + " " + leftRightMap.get(list).leftMatching.distance 
					//		+ " " + leftRightMap.get(list).rightMatching.rightStrains + 
						//	" " + leftRightMap.get(list).rightMatching.distance );
			
			DistanceHolder newHolder = new DistanceHolder();
			newHolder.leftStrains = leftRightMap.get(list).leftMatching.leftStrains;
			newHolder.rightStrains = new ArrayList<Integer>();
			newHolder.rightStrains.addAll(leftRightMap.get(list).leftMatching.rightStrains );
			newHolder.rightStrains.addAll(leftRightMap.get(list).rightMatching.rightStrains  );
			Collections.sort(newHolder.rightStrains);
			
			newHolder.distance = leftRightMap.get(list).leftMatching.distance 
					* leftRightMap.get(list).leftMatching.rightStrains.size();
			
			newHolder.distance += leftRightMap.get(list).rightMatching.distance *
					leftRightMap.get(list).rightMatching.rightStrains.size();
			
			newHolder.distance /= (leftRightMap.get(list).leftMatching.rightStrains.size() 
					+ leftRightMap.get(list).rightMatching.rightStrains.size());
			
			distanceList.add(newHolder);
			
		}
		
		Collections.sort(distanceList);
	}
	
	private static class LeftAndRightMatching
	{
		DistanceHolder leftMatching;
		DistanceHolder rightMatching;
	}
	
	private static void writePhyloXml(List<DistanceHolder> mergedList, HashMap<Integer, StrainMetadataFileLine> metaMap )
		throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(ConfigReader.getKlebDir() + 
				File.separator + "kleb76.xml")));
		
		writer.write("<phyloxml xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " + ""
				+ "xsi:schemaLocation=\"http://www.phyloxml.org http://www.phyloxml.org/1.10/phyloxml.xsd\" " + 
		"xmlns=\"http://www.phyloxml.org\">\n");
		
		writer.write("<phylogeny rooted=\"false\">\n");
		
		writeNodeAndChildren( writer, mergedList, mergedList.remove(mergedList.size()-1), metaMap);
		
		writer.write("</phylogeny>\n");
		writer.write("</phyloxml>\n");
		
		writer.flush();  writer.close();
		
		if(mergedList.size() != 0)
			throw new Exception("Logic error");
		
	}
	
	private static void writeNodeAndChildren( BufferedWriter writer, 
			List<DistanceHolder> mergedList, 
			DistanceHolder node, 
			HashMap<Integer, StrainMetadataFileLine> metaMap)
		throws Exception
	{
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(1);
		
		writer.write("<clade>\n");
		
		double distance = Math.log10(node.distance+1);
		
		writer.write("<branch_length>" + distance+ "</branch_length>\n");
		writer.write(" <taxonomy><scientific_name>" + nf.format( node.distance) + "</scientific_name></taxonomy>\n" );
		
		DistanceHolder leftNode = findAndRemoveChildNode(mergedList, node.leftStrains);
		DistanceHolder rightNode = findAndRemoveChildNode(mergedList, node.rightStrains);
		
		if( leftNode != null)
			writeNodeAndChildren(writer, mergedList, leftNode,metaMap );
		else
			writeTip(writer, node.leftStrains, metaMap);
			
		if( rightNode != null)
			writeNodeAndChildren(writer, mergedList, rightNode,metaMap);
		else
			writeTip(writer, node.rightStrains,metaMap);
		
		writer.write("</clade>\n");
	}
	
	private static void writeTip(BufferedWriter writer, List<Integer> nameList, 
			HashMap<Integer, StrainMetadataFileLine> metaMap)
		throws Exception
	{
		if( nameList.size() != 1)
			throw new Exception("Logic error");
		
		StrainMetadataFileLine meta = metaMap.get(nameList.get(0));
		
		writer.write("<clade><name>" + nameList+ "</name><branch_length>0</branch_length>\n");
		writer.write(meta.getColorStringByLocation() + "\n");
		writer.write("</clade>\n");

	}
	
	//todo: This is embarassingly naive and inefficeint..
	// should be keeping track of children as build the list
	// we get away with this here because there are so few genomes
	// this will not scale..
	private static DistanceHolder findAndRemoveChildNode( List<DistanceHolder> mergedList, List<Integer> parentList )
		throws Exception
	{
		if( parentList.size() < 1)
			throw new Exception("Logic error");
		
		if( parentList.size() == 1)
			return null;
		
		for(Iterator<DistanceHolder> i = mergedList.iterator(); i.hasNext();)
		{
			DistanceHolder dh = i.next();
			List<Integer> aList = new ArrayList<Integer>();
			aList.addAll(dh.leftStrains);
			aList.addAll(dh.rightStrains);
			Collections.sort(aList);
			
			if( aList.equals(parentList))
			{
				i.remove();
				return dh;
			}
		}
		
		return null;
	}
	
	private static HashMap<List<Integer>, LeftAndRightMatching> pairHolders(List<DistanceHolder> toMerge, List<Integer> leftStrains,
					List<Integer> rightStrains) throws Exception
	{
		HashMap<List<Integer>, LeftAndRightMatching> map = new HashMap<List<Integer>, LeftAndRightMatching>();
		
		for( DistanceHolder dh : toMerge)
		{
			LeftAndRightMatching lf = map.get( dh.leftStrains);
			
			if( lf == null)
			{
				lf = new LeftAndRightMatching();
				map.put(dh.leftStrains, lf);
			}
			
			if( dh.rightStrains.equals(leftStrains) )
			{
				if( lf.leftMatching != null)
					throw new Exception("No");
				
				lf.leftMatching = dh;
			}
			else if ( dh.rightStrains.equals(rightStrains))
			{
				if( lf.rightMatching != null)
					throw new Exception("No");
				
				lf.rightMatching = dh;
			}
			else throw new Exception("Logic error");
		}
		
		return map;
	}
	
	private static class DistanceHolder implements Comparable<DistanceHolder>
	{
		List<Integer> leftStrains = new ArrayList<Integer>();
		List<Integer> rightStrains = new ArrayList<Integer>();
		double distance;
		@Override
		public int compareTo(DistanceHolder arg0)
		{
			return Double.compare(this.distance, arg0.distance);
		}
		
		@Override
		public String toString()
		{
			return leftStrains + " " + rightStrains + " " + distance;
		}
	}
	
	private static List<DistanceHolder> getInitialDistances() throws Exception
	{
		List<DistanceHolder> list = new ArrayList<DistanceHolder>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
			ConfigReader.getKlebDir() + File.separator +  "distancesUpperTriangleNoDiag.txt")));
		
		reader.readLine();
		
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			DistanceHolder h = new DistanceHolder();
			list.add(h);
			h.leftStrains.add(Integer.parseInt(splits[0]));
			h.rightStrains.add(Integer.parseInt(splits[1]));
			h.distance = Double.parseDouble(splits[2]);
		}
		
		Collections.sort(list);
		return list;
	}
}
