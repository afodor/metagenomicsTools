package scripts.DonaldsonReparse;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

public class MetaMapFileLine
{
	private final int visitType;
	private final int subjectID;
	
	public int getVisitType()
	{
		return visitType;
	}
	
	public int getSubjectID()
	{
		return subjectID;
	}
	
	private MetaMapFileLine(String s)
	{
		String[] splits = s.split("\t");
		this.visitType = Integer.parseInt(splits[1]);
		this.subjectID = Integer.parseInt(splits[0]);
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, MetaMapFileLine>  metaMap =getMetaMap();
		
		HashMap<String, String> barcodeMap = BarcodeToSampleMap.getBarcodeToSampleMap();
		
		for(String barcode : barcodeMap.keySet())
		{
			String sampleID = barcodeMap.get(barcode);
			MetaMapFileLine mfl = metaMap.get( sampleID );
			
			if( mfl != null)
			{
				System.out.println(sampleID + " " + barcode + " " + mfl.getSubjectID() + " " + mfl.getVisitType() );	
			}
		}
			
	}
	
	public static HashMap<String, MetaMapFileLine> getMetaMap() throws Exception
	{
		HashMap<String, MetaMapFileLine> map= new HashMap<String, MetaMapFileLine>();
		
		BufferedReader reader = new BufferedReader(new FileReader("C:\\wolfgangDonaldsonCombinedDir\\tRFLP exploratory covariates 031210_B.txt"));
	
		reader.readLine();
		
		for(String s = reader.readLine(); s != null && s.trim().length()>0 ; s =reader.readLine())
		{
			String[] splits = s.split("\t");
			String key = splits[3];
			
			if( map.containsKey(key))
				throw new Exception("Duplicate");
			
			map.put(key, new MetaMapFileLine(s));
		}
		
		
		reader.close();
		return map;
	}
}
