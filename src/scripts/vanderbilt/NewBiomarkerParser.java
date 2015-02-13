package scripts.vanderbilt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.LinkedHashMap;

import utils.ConfigReader;
import utils.TabReader;

public class NewBiomarkerParser
{
	//study_id	visit	stool_id	swab_id	bax_overall	bax_surface	bax_bottom	bax_overall_quant	bax_surface_quant	bax_bottom_quant
	private final String studyID;
	private final String visit;
	private final String stoolID;
	private final String swabID;
	private final Integer bax_overall;
	private final Integer bax_surface;
	private final Integer bax_bottom;
	private final Double bax_overall_quant;
	private final Double bax_surface_quant;
	private final Double bax_bottom_quant;
	
	public String getStudyID()
	{
		return studyID;
	}

	public String getVisit()
	{
		return visit;
	}

	public String getStoolID()
	{
		return stoolID;
	}

	public String getSwabID()
	{
		return swabID;
	}

	public Integer getBax_overall()
	{
		return bax_overall;
	}

	public Integer getBax_surface()
	{
		return bax_surface;
	}

	public Integer getBax_bottom()
	{
		return bax_bottom;
	}

	public Double getBax_overall_quant()
	{
		return bax_overall_quant;
	}

	public Double getBax_surface_quant()
	{
		return bax_surface_quant;
	}

	public Double getBax_bottom_quant()
	{
		return bax_bottom_quant;
	}
	
	/*
	 * Key is studyID followed by "_" visot
	 */
	public static HashMap<String, NewBiomarkerParser> getMetaMap() throws Exception
	{
		HashMap<String, NewBiomarkerParser> map = new LinkedHashMap<String, NewBiomarkerParser>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File( 
				ConfigReader.getVanderbiltDir() + File.separator + "biomarker_new1.txt"	)));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null && s.length() > 0; s = reader.readLine())
		{
			NewBiomarkerParser nbp = new NewBiomarkerParser(s);
			
			//System.out.println( nbp.studyID + " "  +  nbp.stoolID + "_" + nbp.visit + " " + nbp.swabID+ "_" + nbp.visit);
			
			String key = nbp.studyID + "_" + nbp.visit;
			
			if( map.containsKey(key))
			{
				throw new Exception("Could not find key  " + nbp.studyID + " "  +  nbp.stoolID + "_" + nbp.visit + " " + nbp.swabID+ "_" + nbp.visit);
			}
			else
			{
				map.put(key,nbp);
			}
			
		}
		
		return map;
	}
	
	private Double getNullOrVal(String s)
	{
		if( s.trim().length() == 0 )
			return null;
		
		return Double.parseDouble(s);
	}

	private NewBiomarkerParser(String s) throws Exception
	{
		TabReader tr = new TabReader(s);
		this.studyID = tr.nextToken();
		this.visit = tr.nextToken();
		this.stoolID = tr.nextToken();
		this.swabID = tr.nextToken();
		this.bax_overall = Integer.parseInt(tr.nextToken());
		this.bax_surface = Integer.parseInt(tr.nextToken());
		this.bax_bottom = Integer.parseInt(tr.nextToken());
	
		this.bax_overall_quant = getNullOrVal(tr.nextToken());
		this.bax_surface_quant = getNullOrVal(tr.nextToken());
		this.bax_bottom_quant = getNullOrVal(tr.nextToken());
		
		if( tr.hasMore())
			throw new Exception("No");
		
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, NewBiomarkerParser> map = getMetaMap();
		
		for(String s : map.keySet() )
		{
			System.out.println( s);
		}
	}
}
