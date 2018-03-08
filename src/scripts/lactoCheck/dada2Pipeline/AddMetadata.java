package scripts.lactoCheck.dada2Pipeline;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

import parsers.OtuWrapper;
import scripts.lactoCheck.PCR_DataParser;
import utils.ConfigReader;

public class AddMetadata
{
	public static void main(String[] args) throws Exception
	{
		File inFile = new File(ConfigReader.getLactoCheckDir() + 
				File.separator + "dada2AllTogetherPivoted.txt");
		OtuWrapper wrapper = new OtuWrapper(inFile);
		
		File normFile = new File(ConfigReader.getLactoCheckDir() + 
				File.separator + "dada2AllTogetherPivotedNorm.txt");
		
		File logNormFile = new File(
				ConfigReader.getLactoCheckDir() + 
				File.separator + "dada2AllTogetherPivotedLogNorm.txt");
		
		wrapper.writeNormalizedLoggedDataToFile(logNormFile);
		wrapper.writeNormalizedDataToFile(normFile);
		
		File metaFile =new File(ConfigReader.getLactoCheckDir() + 
				File.separator + "dada2AllTogetherPivotedLogNormPlusMeta.txt");
		
		addMetadata(logNormFile, metaFile, wrapper);
		
		metaFile =new File(ConfigReader.getLactoCheckDir() + 
				File.separator + "dada2AllTogetherPivotedNormPlusMeta.txt");
		
		addMetadata(normFile, metaFile, wrapper);
		
	}
	
	private static HashMap<String, Double> getPCRMap() throws Exception
	{
		HashMap<String, Double> map = new HashMap<String,Double>();
		
		BufferedReader reader= new BufferedReader(new FileReader(new File(
				ConfigReader.getLactoCheckDir() + File.separator + 
					"qPCRData.txt")));
		
		reader.readLine();
		
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits =s.split("\t");
			
			if( map.containsKey(splits[0]))
				throw new Exception("No");
			
			map.put(splits[0], Double.parseDouble(splits[2]));
		}
		
		reader.close();
		
		return map;
	}
	
	private static class Holder
	{
		String otuName= null;
		double frequency = -1;
	}
	
	private static Holder getMostFrequent(String sampleName, OtuWrapper wrapper) throws Exception
	{
		Holder h = new Holder();
		
		int sampleIndex = wrapper.getIndexForSampleName(sampleName);
		double sum = wrapper.getCountsForSample(sampleIndex);
		for(int x=0; x < wrapper.getOtuNames().size();x++)
		{
			double val = wrapper.getDataPointsUnnormalized().get(sampleIndex).get(x)/ sum;
			
			if( h.frequency < val)
			{
				h.frequency = val;
				h.otuName = wrapper.getOtuNames().get(x);
			}
		}
		
		return h;
	}
	
	private static HashMap<String, Integer> getBirthGroupMap() throws Exception
	{
		HashMap<String, Integer> map = new HashMap<String,Integer>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getLactoCheckDir() + File.separator + "BirthGroup.txt")));
		
		reader.readLine();
		
		for(String s = reader.readLine() ; s!= null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			if( splits.length != 2)
				throw new Exception("No");
			
			if( map.containsKey(splits[0]))
				throw new Exception("No");
			
			map.put(splits[0], Integer.parseInt(splits[1]));
			
		}
		
		reader.close();
		return map;
	}
	
	private static void addMetadata(File inFile, File outFile, OtuWrapper originalWrapper) throws Exception
	{
		HashMap<String, PCR_DataParser> pcrMap = PCR_DataParser.getPCRData();
		HashMap<String, Integer> birthMap = getBirthGroupMap();
		HashMap<String, Double> qPCR16SMap = getPCRMap();
		HashMap<Integer, String> birthTypeMap =  scripts.lactoCheck.rdp.AddMetadata.getBirthType();
		
		BufferedReader reader = new BufferedReader(new FileReader(inFile));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
		
		String[] topSplits = reader.readLine().split("\t");

		writer.write(topSplits[0].replace("rdp_", "").replace(".txt.gz", "") 
				+ "\tbirthGroup\tsubjectNumber\tbirthMode\tmostFrequentTaxa\tfractionMostFrequent\tqPCR16S\tL_crispatus\tL_iners\tbglobulin\tsequencingDepth\tshannonDiveristy");
		
		for( int x=1; x < topSplits.length && x < 50; x++)
			writer.write("\t" + topSplits[x]);
		
		writer.write("\n");
		
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			
			Holder h = getMostFrequent(splits[0], originalWrapper);
			
			String key = splits[0];
					
			System.out.println(key);
			
			if( key.startsWith("G")   || key.startsWith("S"))
			{
				Integer birthGroup = birthMap.get(key.replace("S","G"));
					
				PCR_DataParser pcr = pcrMap.get(key.replace("S", "G"));
					
				if( pcr == null || ! pcr.getGroup().equals(key.replace("S", "G")))
					throw new Exception("No");
					
				String qPCRString = "NA";
					
				if( qPCR16SMap.get(key) != null )
					qPCRString = qPCR16SMap.get(key).toString();
					
				Integer subjectNumber = Integer.parseInt(key.replace("S", "").replace("G", "") );
				String birthMode = "U";
					
				if( birthTypeMap.containsKey(subjectNumber) )
						birthMode = birthTypeMap.get(subjectNumber);
					
				writer.write(key + "\t" + birthGroup +  "\t" + subjectNumber+ "\t" + birthMode + "\t" + 
							h.otuName + "\t" + h.frequency + "\t" + 
					qPCRString + "\t" + 
					+ pcr.getL_crispatus() + "\t" + pcr.getL_iners() + 
							"\t" + pcr.getBglobulin() + "\t" + originalWrapper.getNumberSequences(splits[0]) + "\t" +
									originalWrapper.getShannonEntropy(splits[0]));
				
			}
			else if ( key.endsWith("neg") || key.startsWith("H2") || key.startsWith("NC101") )
			{
				System.out.println("In neg");
					writer.write(key + "\t" + "neg\t0\t"  + 
							"neg\t"  + h.otuName + "\t"+ h.frequency + "\t" 
				+"0\t"+ "0"+ "\t" + "0" + 
							"\t" + "0"+ "\t" + originalWrapper.getNumberSequences(splits[0]) + "\t" +
									originalWrapper.getShannonEntropy(splits[0]));
			}
			else throw new Exception("No");
					
			for( int x=1; x < splits.length&& x < 50; x++)
				writer.write("\t" + splits[x]);
				
			writer.write("\n");
			
		}
		
		writer.flush();  writer.close();
		reader.close();
	}
}
