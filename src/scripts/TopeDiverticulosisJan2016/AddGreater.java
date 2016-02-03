package scripts.TopeDiverticulosisJan2016;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;

import parsers.OtuWrapper;

public class AddGreater
{
	private static HashSet<String> getSetToInclude() throws Exception
	{
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		return null;
		
	}
	
	private static void addToMap( File file, HashMap<String, Integer> map ) throws Exception
	{
		OtuWrapper wrapper = new OtuWrapper(file);
	}
}
