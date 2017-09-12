package bottomUpTree.fromBioLockJ;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import parsers.NewRDPNode;
import parsers.NewRDPParserFileLine;

public class FromBiolockJDirectory
{
	// todo this will ultimately come from a properties file
	private static final String BIOLOCK_J_PROJECT_DIR = 
			"C:\\biolockJProjects";
	
	// todo this will ultimately come from a properties file
	private static final int RDP_THRESHOLD = 80;
	
	private static final String ROOT_NODE= "root";
	
	public static void main(String[] args) throws Exception
	{
		Holder root = new Holder();
		root.name = ROOT_NODE;
		root.parent = null;
		
		HashMap<String, HashMap<String,Holder>> map = getTaxonomyMapWithCounts(root);
		addPValues(map);
		
		/* dump genus and parents to the console
		for( int x=1; x < NewRDPParserFileLine.TAXA_ARRAY.length; x++)
			System.out.println(map.get(NewRDPParserFileLine.TAXA_ARRAY[x]).size());
		
		HashMap<String, Holder> genusMap = map.get(NewRDPParserFileLine.GENUS);
		
		for(String s : genusMap.keySet())
			System.out.println(s + " " + genusMap.get(s).parent.name);
			*/
		
		writeJSON(map, root);
		
	}
	
	// todo: The destination for the JSON file should be determined by BioLockJ
	private static void writeJSON(HashMap<String, HashMap<String,Holder>> map, Holder rootNode) throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				BIOLOCK_J_PROJECT_DIR +File.separator + "lactoExample.json")));
		
		writeNodeAndChildren(writer, rootNode, map, 0);
		
		writer.flush(); writer.close();
	}
	
	private static void writeNodeAndChildren( BufferedWriter writer,
			Holder node,HashMap<String, HashMap<String,Holder>> map ,
			int nodeLevel) throws Exception
	{
		String taxaLevel = nodeLevel == 0 ? ROOT_NODE : NewRDPParserFileLine.TAXA_ARRAY[nodeLevel];
		writer.write("{\n");

		writer.write("\"numSeqs\": " +  node.numSequences + ",\n");		
		writer.write("\"rpdLevel\": \"" +  taxaLevel +  "\",\n");
		
		if( node.pValues.size() > 0 )
		{
			for(String s : node.pValues.keySet())
			{
				writer.write("\"" + s + "\": " +  node.pValues.get(s)+ ",\n");
			}
		}
		
		writer.write("\"rdptaxa\": \"" + node.name + "\"\n");
		
		
		if( nodeLevel < NewRDPParserFileLine.TAXA_ARRAY.length -1  )
		{
			List<Holder> toAdd = new ArrayList<Holder>();
			
			HashMap<String, Holder> innerMap = map.get(NewRDPParserFileLine.TAXA_ARRAY[nodeLevel+1]);
			
			for(String s : innerMap.keySet())
			{
				Holder innerNode = innerMap.get(s);
				
				if( innerNode.parent == node )
					toAdd.add(innerNode);
			}
			
			if ( toAdd.size() > 0) 
			{
				writer.write(",\"children\": [\n");
					
				for( Iterator<Holder> i = toAdd.iterator(); i.hasNext();)
				{
					writeNodeAndChildren(writer,i.next(), map, nodeLevel + 1);
					if( i.hasNext())
						writer.write(",");
				}
						writer.write("]\n");
			}
		}

		writer.write("}\n");
}
	

	// outer key is the taxonomic level
	// inner key is the name of the taxa
	private static HashMap<String, HashMap<String,Holder>> getTaxonomyMapWithCounts(Holder rootNode)
		throws Exception
	{
		HashMap<String, HashMap<String,Holder>> map = new HashMap<>();
		
		for( int x=1; x < NewRDPParserFileLine.TAXA_ARRAY.length; x++)
		{
			String taxaLevel =  NewRDPParserFileLine.TAXA_ARRAY[x];
			map.put(taxaLevel, new HashMap<String,Holder>());
		}
		
		File directoryToAdd = new File(BIOLOCK_J_PROJECT_DIR + File.separator + 
				"0_RdpClassifier" + File.separator + "output" );
		
		String[] dirs = directoryToAdd.list();
		
		for(String s : dirs)
		{
			if(s.endsWith("_reported.tsv"))
			{
				File inFile = new File(directoryToAdd.getAbsolutePath() + File.separator + 
						s);
				addOneFileToMap(map, inFile, rootNode);
			}
		}
		
		return map;
	}
	
	private static void addPValues( HashMap<String, HashMap<String,Holder>> map )
		throws Exception
	{
		// todo: This directory should be set by BIOLOCK_J
		File pValueDirectory = new File(BIOLOCK_J_PROJECT_DIR + File.separator + 
				"localR" + File.separator  );
		
		for( int x=1; x < NewRDPParserFileLine.TAXA_ARRAY.length; x++)
		{
			String taxaLevel = NewRDPParserFileLine.TAXA_ARRAY[x];
			
			File inFile = new File(pValueDirectory.getAbsolutePath() + File.separator + 
					"meta_pValuesFor_" + taxaLevel + ".txt");
			
			BufferedReader reader = new BufferedReader(new FileReader(inFile));
			
			List<String> includeList = new ArrayList<>();
			
			String[] topSplits = reader.readLine().replaceAll("\"", "").split("\t");
			
			for( String s : topSplits)
				if( s.startsWith("pValues"))
					includeList.add(s);
				else
					includeList.add(null);
			
			HashMap<String, Holder> innerMap = map.get(taxaLevel);
			
			for(String s= reader.readLine(); s!= null; s= reader.readLine())
			{
				String[] splits = s.split("\t");
				
				if( splits.length != includeList.size())
					throw new Exception("Parsing error unequal number of tokens " + inFile.getAbsolutePath() );
				
				Holder h = innerMap.get(splits[0].replaceAll("\"", ""));
				
				if( h != null)
				{
					for( int y=1; y< includeList.size(); y++)
					{
						String pValueString = includeList.get(y);
						
						if(pValueString != null)
						{
							h.pValues.put(pValueString, Double.parseDouble(splits[y]));
						}
					}
				}
				else
				{
					System.out.println("Could not find " + splits[0].replaceAll("\"", ""));
				}
				
				
			}
			
			reader.close();
		}
	}
	
	
	private static void addOneFileToMap(HashMap<String, HashMap<String,Holder>> map,
			File file, Holder rootNode) throws Exception
	{
		HashMap<String, NewRDPParserFileLine> rdpMap= 
				NewRDPParserFileLine.getAsMapFromSingleThread(file);
		
		for( NewRDPParserFileLine rdpLine : rdpMap.values())
		{
			Holder parent = rootNode;
			
			for( int x=1; x < NewRDPParserFileLine.TAXA_ARRAY.length; x++)
			{
				String taxaLevel = NewRDPParserFileLine.TAXA_ARRAY[x];
				NewRDPNode node = rdpLine.getTaxaMap().get(taxaLevel);
				
				// this assumes that scores always go down
				if( node != null && node.getScore() >= RDP_THRESHOLD)
				{
					HashMap<String,Holder> innerMap = map.get(taxaLevel);
					
					Holder treeNode= innerMap.get(node.getTaxaName());
					
					if( treeNode == null)
					{
						treeNode = new Holder();
						treeNode.name = node.getTaxaName();
						treeNode.parent = parent;
						innerMap.put(node.getTaxaName(), treeNode);
					}
					
					treeNode.numSequences++;
					
					//makes prettier trees if this is zero..
					//rootNode.numSequences++;
					
					if( treeNode.parent != parent)
						throw new Exception("Parents don't match " + treeNode.parent + " " + parent);
					
					parent = treeNode;
				}
				
			}
		}
	}
	
	private static class Holder
	{
		String name;
		Holder parent;
		long numSequences;
		
		HashMap<String, Double> pValues = new LinkedHashMap<>();
	}
}
