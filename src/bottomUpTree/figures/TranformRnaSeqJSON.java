package bottomUpTree.figures;

import java.util.StringTokenizer;

import parsers.JsonObject;

public class TranformRnaSeqJSON
{
	public static void main(String[] args) throws Exception
	{

		JsonObject root= 
			 JsonObject.parseJsonFileWithChildren(
						"C:\\Users\\Anthony\\workspace\\fodorwebsite\\war\\testOperon.json");
	
		transformNodeAndChildren(root);
	}
	
	private static void transformNodeAndChildren( JsonObject json )
		throws Exception
	{
		System.out.println(json.getNameValuePairMap());
		double aPValue = Double.parseDouble(json.getNameValuePairMap().get("pValue.il02.ilaom02"));
		json.getNameValuePairMap().remove("pValue.il02.ilaom02");
		json.getNameValuePairMap().put("log_pValue.il02.ilaom02","" + -Math.log10(aPValue));
		
		aPValue = Double.parseDouble(json.getNameValuePairMap().get("pValue.il12.ilaom12"));
		json.getNameValuePairMap().remove("pValue.il12.ilaom12");
		json.getNameValuePairMap().put("log_pValue.il12.ilaom12","" + -Math.log10(aPValue));
		
		String location = json.getNameValuePairMap().get("genomic.location");
		StringTokenizer sToken = new StringTokenizer(location, ".");
		
		if( sToken.countTokens() != 2)
			throw new Exception("Parsing error");
		
		json.getNameValuePairMap().put("contig","" + sToken.nextToken() );
		json.getNameValuePairMap().put("position","" + sToken.nextToken() );
		
		if( json.getChildren() != null)
			for( JsonObject child : json.getChildren())
				transformNodeAndChildren(child);
	}
	
	
}
