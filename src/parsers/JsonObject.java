package parsers;

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
	
	public static JsonObject parseJsonFileWithChildren(String filepath)
		throws Exception
	{
		return null;
	}
	
	public static void main(String[] args) throws Exception
	{
		JsonObject root= 
				parseJsonFileWithChildren(
						"C:\\Users\\Anthony\\workspace\\fodorwebsite\\war\\testOperon.json");
	}
}
