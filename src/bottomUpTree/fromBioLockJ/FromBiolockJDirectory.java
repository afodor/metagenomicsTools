package bottomUpTree.fromBioLockJ;

import java.io.File;
import java.util.HashMap;

import parsers.NewRDPNode;
import parsers.NewRDPParserFileLine;

public class FromBiolockJDirectory
{
	// todo this will ultimately come from a properties file
	private static final String BIOLOCK_J_PROJECT_DIR = 
			"C:\\biolockJProjects";
	
	// todo this will ultimately come from a properties file
	private static final int RDP_THRESHOLD = 80;
	
	private static final String ROOT_NODE= "ROOT";
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, HashMap<String,Holder>> map = getTaxonomyMapWithCounts();
	
	}
	

	// outer key is the taxonomic level
	// inner key is the name of the taxa
	private static HashMap<String, HashMap<String,Holder>> getTaxonomyMapWithCounts()
		throws Exception
	{
		HashMap<String, HashMap<String,Holder>> map = new HashMap<>();
		
		Holder h = new Holder();
		h.name = ROOT_NODE;
		h.parent = null;
		
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
				addOneFileToMap(map, inFile, h);
			}
		}
		
		return map;
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
					}
					
					treeNode.numSequences++;
					
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
		int numSequences;
	}
}
