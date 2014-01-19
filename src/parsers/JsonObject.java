package parsers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JsonObject 
{
	private HashMap<String, String> nameValuePairMap = 
			new HashMap<String,String>();
	
	private List<JsonObject> children = null;
	
	public void makeNewEmptyChildrenList()
	{
		this.children = new ArrayList<JsonObject>();
	}
	
	public HashMap<String,String> getNameValuePairMap()
	{
		return nameValuePairMap;
	}
	
	public List<JsonObject> getChildren()
	{
		return children;
	}
	
	public static JsonObject parseJsonFileWithChildren(String filePath)
		throws Exception
	{
		TokensInFileParser tifp = new TokensInFileParser(filePath,":,");
		
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
			
			if( key.equals("}"))
			{
				keepParsing = false;
			}
			else if( key.equals("children"))
			{
				boolean addingChildren = true;
				
				json.children = new ArrayList<JsonObject>();
				
				while( addingChildren)
				{
					String nextToken = tifp.nextToken();
					
					if( nextToken.equals("["))
						nextToken = tifp.nextToken();
					
					if( nextToken.equals("]"))
					{
						addingChildren = false;
					}
					else
					{
						if( ! nextToken.equals("{"))
							throw new Exception("First token should be { " + nextToken );
				
						json.children.add(getObjectAfterBrace(tifp));
					}
				
				}
			}
			else
			{
				String value = tifp.nextToken().replaceAll("\"", "");
				json.nameValuePairMap.put(key, value);
			}
		}
		
		return json;
	}
	
	public static void dumpChildrenToConsole(JsonObject aJson, int indentatioNum)
	{
		System.out.println();
		String indentationString = "";
			
		for(int x=0; x < indentatioNum; x++)
			indentationString += "\t" +"";
		
		indentatioNum++;
		
		for(String s : aJson.nameValuePairMap.keySet())
			System.out.println(indentationString + " " + 
							s + " " + aJson.nameValuePairMap.get(s));
		
		if( aJson.children != null ) 
			for( JsonObject child : aJson.children)
				dumpChildrenToConsole(child, indentatioNum);
	}
	
	public static void main(String[] args) throws Exception
	{
		JsonObject root= 
				parseJsonFileWithChildren(
						"C:\\Users\\Anthony\\workspace\\fodorwebsite\\war\\testOperon.json");
		
		dumpChildrenToConsole(root, 0);
		
		
	}
}
