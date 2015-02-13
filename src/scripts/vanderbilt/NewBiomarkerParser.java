package scripts.vanderbilt;

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
	
		if( tr.hasMore())
		{
			this.bax_overall_quant = Double.parseDouble(tr.nextToken());
			this.bax_surface_quant = Double.parseDouble(tr.nextToken());
			this.bax_bottom_quant = Double.parseDouble(tr.nextToken());
		}
		else
		{

			this.bax_overall_quant = null;
			this.bax_surface_quant = null;
			this.bax_bottom_quant = null;
		}
		
		if( tr.hasMore())
			throw new Exception("No");
		
	}
	
	public static void main(String[] args) throws Exception
	{
		
	}
}
