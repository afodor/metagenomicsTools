package parsers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JsonObject 
{
	private HashMap<String, String> nameValuePairMap = 
			new HashMap<String,String>();
	
	private List<JsonObject> children = null;
	
	public HashMap<String,String> getNameValuePairMap()
	{
		return null;
	}
	
	public List<JsonObject> getChildren()
	{
		return null;
	}
	
	public static JsonObject parseJsonFileWithChildren(String filePath)
		throws Exception
	{
		TokensInFileParser tifp = new TokensInFileParser(filePath);
		
		if( ! tifp.nextToken().equals("{"))
			throw new Exception("First token should be {");
		
		return getObjectAfterBrace(tifp);
	}
	
	private static JsonObject getObjectAfterBrace( TokensInFileParser tifp) 
		throws Exception
	{
		JsonObject json = new JsonObject();
		
		boolean keepParsing = true;
		
		while(keepParsing)
		{
			String key = tifp.nextToken().replaceAll("\"", "");
			
			if( ! tifp.nextToken().equals(":"))
				throw new Exception("Expecting : to separate keys and values");
			
			if( key.equals("children"))
			{
				if( ! tifp.nextToken().equals("["))
					throw new Exception("Expecting stat of list");
				
				boolean addingChildren = true;
				
				json.children = new ArrayList<JsonObject>();
				
				while( addingChildren)
				{
					if( ! tifp.nextToken().equals("{"))
						throw new Exception("First token should be {");
			
					json.children.add(getObjectAfterBrace(tifp));
					
					String sepToken= tifp.nextToken();
					
					if( sepToken.equals("]"))
						addingChildren = false;
					else if ( ! sepToken.equals(","))
						throw new Exception("Parsing error expecting comma " + sepToken);
				}
			}
			
			String value = tifp.nextToken().replaceAll("\"", "");
			json.nameValuePairMap.put(key, value);
			
			String sepToken = tifp.nextToken();
			
			if( sepToken.equals("}"))
				keepParsing = false;
			else if ( ! sepToken.equals(","))
				throw new Exception("Parsing error expecting comma " + sepToken);
	
		}
		
		return json;
	}
	
	public static void main(String[] args) throws Exception
	{
		JsonObject root= 
				parseJsonFileWithChildren(
						"C:\\Users\\Anthony\\workspace\\fodorwebsite\\war\\testOperon.json");
	}
}
