package sandraJune2012Rivers;


public class PCA_Pivot
{
	/*
	public static void main(String[] args) throws Exception
	{	
		pivot();
	}
	
	private static void pivot() throws Exception
	{	
		List<String> sampleId= new ArrayList<String>();
		List<String> numSequences = new ArrayList<String>();
		List<String> richness = new ArrayList<String>();
		List<String> evenness = new ArrayList<String>();
		List<String> shannonDiversity = new ArrayList<String>();
		List<String> richness5000 = new ArrayList<String>();
		List<String> vsl3Status = new ArrayList<String>();
		List<String> tissue =new ArrayList<String>();
		List<String> dysplasia =new ArrayList<String>();
		List<String>  invasion= new ArrayList<String>();
		
		 
		System.out.println("Getting wrapper");
				
		OtuWrapper wrapper = new OtuWrapper(new File(
				ConfigReader.getJobinlabJune2012Dir() + File.separator + "VSL3" + 
						File.separator + "VSL3_PivotedTaxaAsColumns.txt"));
		

		System.out.println("Got wrapper " + wrapper.getSampleNames().size() + " " + 
				wrapper.getOtuNames().size());
		
		
		double[][] d=  wrapper.getPresenceAbsenceArray(); //wrapper.getNormalizedThenLoggedAsArray();
		
		for( String key : wrapper.getSampleNames() )
		{
			VSL3_MetadataParser metaLine= metaMap.get(key);
			
			primerKey.add(key);
			sampleNumber.add("" + metaLine.getSampleNUM());
			sampleId.add("" + metaLine.getAnimalID());
			
			numSequences.add("" + wrapper.getCountsForSample(key));
			cage.add("" + metaLine.getAnimalID().substring(0,2));
			
			richness.add(""+wrapper.getRichness(key));
			evenness.add("" + wrapper.getEvenness(key));
			shannonDiversity.add("" + wrapper.getShannonEntropy(key));
			richness5000.add("" + -1);
			//richness5000.add("" + wrapper.getRarefactionCurve(wrapper.getIndexForSampleName(key), 1000)[5000]);
			
			vsl3Status.add(metaLine.getVsl3Status());
			tissue.add(metaLine.getTissue());
			dysplasia.add("" + metaLine.getDysplasia());
			invasion.add(metaLine.getInvasion());
			
			System.out.println(key );
		}
		
		
		List<String> catHeaders = new ArrayList<String>();
		catHeaders.add("primerKey");
		catHeaders.add("sampleID");catHeaders.add("sampleNumber");
		catHeaders.add("cage"); catHeaders.add("richness");
		catHeaders.add("evenness");  catHeaders.add("shannonDiversity");
		catHeaders.add("richness5000");  catHeaders.add("vsl3Status"); catHeaders.add("tissue");
		catHeaders.add("dysplasi"); catHeaders.add("invasion"); 
		
		List<List<String>> categories = new ArrayList<List<String>>();
		categories.add(primerKey); categories.add(sampleId); 
		categories.add(sampleNumber); categories.add(cage); 	categories.add(richness);
		categories.add(evenness); categories.add(shannonDiversity); categories.add(richness5000);
		categories.add(vsl3Status); categories.add(tissue); categories.add(dysplasia); categories.add(invasion);
		
		File outFile = 
			new File(ConfigReader.getJobinlabJune2012Dir() + File.separator + "VSL3" + 
					File.separator
				+"PCA_VSL_presenceAbsence.txt");
		
		System.out.println("Writing " + outFile.getAbsolutePath());
		PCA.writePCAFile(sampleNumber, catHeaders, categories,
				d, outFile, new CovarianceMatrixDistanceMeasure()
				);
*/
	//}	
}
	