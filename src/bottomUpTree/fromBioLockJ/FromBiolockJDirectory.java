package bottomUpTree.fromBioLockJ;

import java.util.HashMap;

import parsers.NewRDPParserFileLine;

public class FromBiolockJDirectory
{
	// todo this will ultimately come from a properties file
	private static final String BIOLOCK_J_PROJECT_DIR = 
			"C:\\biolockJProjects";
	
	public static void main(String[] args)
	{
		HashMap<String, HashMap<String,Holder>> map = getTaxonomyMapWithCounts();
		
		for(int x=1; x < NewRDPParserFileLine.TAXA_ARRAY.length; x++)
		{
			
		}
	}
	

	// outer key is the taxonomic level
	// inner key is the name of the taxa
	private static HashMap<String, HashMap<String,Holder>> getTaxonomyMapWithCounts()
		throws Exception
	{
		return null;
	}
	
	private static class Holder
	{
		String name;
		Holder parent;
		int numSequences;
	}
}
