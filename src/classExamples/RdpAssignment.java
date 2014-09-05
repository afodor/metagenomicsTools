package classExamples;

import java.util.List;

public class RdpAssignment
{
	public static final String PHYLUM = "phylum";
	public static final String CLASS= "class";
	public static final String ORDER= "order";
	public static final String FAMILY= "family";
	public static final String GENUS= "genus";
	
	private RdpAssignment(String rdpLine) throws Exception
	{
		
	}
	
	/*
	 * Returns the taxomony assignment for taxonomicLevel or null
	 * if the taxonomy is not represented by in the assignment line.
	 * 
	 * Possible taxonomic levels include RdpAssignment.PHYLUM , 
	 * 	RdpAssignment.CLASS,  RdpAssignment.ORDER, RdpAssignment.FAMILY and
	 *  RdpAssignment.GENUS
	 */
	public String getTaxomonyName(String taxonomicLevel) throws Exception
	{
		return null;
	}
	
	/*
	 *Returns the score (between 0 and 100 ) for taxonomicLevel or null
	 * if the taxonomy is not represented by in the assignment line.
	 * 
	 * Possible taxonomic levels include RdpAssignment.PHYLUM , 
	 * 	RdpAssignment.CLASS,  RdpAssignment.ORDER, RdpAssignment.FAMILY and
	 *  RdpAssignment.GENUS
	 */
	public Integer getTaxonomyScore(String taxonomicLevel) throws Exception
	{
		return null;
	}
	
	public static List<RdpAssignment> parseFile(String filepath) throws Exception
	{
		return null;
	}
	
	public static void main(String[] args) throws Exception
	{
		List<RdpAssignment> rdpList = RdpAssignment.parseFile("C:\\classes\\ProgrammingIII_2014\\HW_2\\rdpOutFromLength200");
		
		long startTime = System.currentTimeMillis();
		
		int x=0;
		// time for iteration
		for(RdpAssignment rdp : rdpList)
			if( rdp.getTaxomonyName(PHYLUM) != null);
		
		float elapsedTime = (System.currentTimeMillis()- startTime )/ 1000f;
		
		System.out.println("Read " + x + " assgined records in " + elapsedTime + " seconds ");
		
		startTime = System.currentTimeMillis();
		
		while( rdpList.size() > 0  )
			rdpList.remove(0);
		
		System.out.println("Deleted " + x + " assgined records in " + elapsedTime + " seconds ");
		
	}
}
