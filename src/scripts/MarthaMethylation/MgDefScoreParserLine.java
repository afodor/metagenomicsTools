package scripts.MarthaMethylation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import utils.TabReader;

public class MgDefScoreParserLine
{
	private static final String MG_FILE_PATH = 
			"C:\\MarthaMethylation\\Mg Deficiency Score data\\methylation Mg deficiency score.txt";
	
	private Double BS_pred_methy;
	private Double BS_pred_methy_post;
	private Double mc_pred_methy;
	private Double mc_pred_methy_post;

	public static String getMgFilePath()
	{
		return MG_FILE_PATH;
	}

	public Double getBS_pred_methy()
	{
		return BS_pred_methy;
	}

	public Double getBS_pred_methy_post()
	{
		return BS_pred_methy_post;
	}

	public Double getMc_pred_methy()
	{
		return mc_pred_methy;
	}

	public Double getMc_pred_methy_post()
	{
		return mc_pred_methy_post;
	}
	
	private Double getDoubleOrNull(String s)
	{
		if( s.trim().length() > 0 )
			return Double.parseDouble(s);
		
		return null;
	}
	
	private MgDefScoreParserLine(String s ) throws Exception
	{
		TabReader tReader = new TabReader(s);
		
		tReader.nextToken();
		this.BS_pred_methy = getDoubleOrNull(tReader.nextToken());
		this.BS_pred_methy_post = getDoubleOrNull(tReader.nextToken());
		tReader.nextToken();
		tReader.nextToken();		
		this.mc_pred_methy = getDoubleOrNull(tReader.nextToken());
		this.mc_pred_methy_post = getDoubleOrNull(tReader.nextToken());
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, MgDefScoreParserLine>  map = getMgScoreMap();
		
		for(String s : map.keySet())
		{
			System.out.println(s + " "+ map.get(s).BS_pred_methy);
		}
	}

	public static HashMap<String, MgDefScoreParserLine> getMgScoreMap() throws Exception
	{
		HashMap<String, MgDefScoreParserLine> map = new HashMap<>();
		
		BufferedReader reader =new BufferedReader(new FileReader(new File(MG_FILE_PATH)));
		
		reader.readLine();
		
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			
			if( splits.length > 1)
			{
				String id = splits[0];
				
				if( map.containsKey(id))
					throw new Exception("Duplicate " + id);
				
				map.put(id, new MgDefScoreParserLine(s));
			}
		}
		
		reader.close();
		
		return map;
	}
}
