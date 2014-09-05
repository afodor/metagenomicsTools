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

		long startTime = System.currentTimeMillis();
		
		List<RdpAssignment> rdpList = RdpAssignment.parseFile("C:\\classes\\ProgrammingIII_2014\\HW_2\\rdpOutFromLength200");
		
		float elapsedTime = (System.currentTimeMillis()- startTime )/ 1000f;
		System.out.println("Parsed file in " + elapsedTime + " seconds ");
		
		int forwardCounts =0, backwardCounts =0;
		
		startTime = System.currentTimeMillis();
		// iterate forward
		for(RdpAssignment rdp : rdpList)
			if( rdp.getTaxomonyName(PHYLUM) != null)
				forwardCounts++;
		
		elapsedTime = (System.currentTimeMillis()- startTime )/ 1000f;
		System.out.println("Read " + forwardCounts+ " assigned records forward in " + elapsedTime + " seconds ");
		

		startTime = System.currentTimeMillis();
		// iterate a sub-list backwards
		for( int i= Math.min(rdpList.size() -1,30000); i >= 0; i--)
			if( rdpList.get(i).getTaxonomyScore(PHYLUM) != null)
				backwardCounts++;
		
		elapsedTime = (System.currentTimeMillis()- startTime )/ 1000f;
		System.out.println("Read " + backwardCounts + " assigned records backwards in " + elapsedTime + " seconds ");
		
		startTime = System.currentTimeMillis();
		
		while( rdpList.size() > 0  )
			rdpList.remove(0);
		
		elapsedTime = (System.currentTimeMillis()- startTime )/ 1000f;
		
		System.out.println("Deleted " + forwardCounts+ " assigned records in " + elapsedTime + " seconds ");
		
	}
}
