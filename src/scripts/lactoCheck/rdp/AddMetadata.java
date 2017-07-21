package scripts.lactoCheck.rdp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

import parsers.NewRDPParserFileLine;
import parsers.OtuWrapper;
import scripts.lactoCheck.PCR_DataParser;
import utils.ConfigReader;

public class AddMetadata
{
	private static HashMap<Integer, String> getBirthType() throws Exception
	{
		HashMap<Integer, String>  map = new HashMap<Integer,String>();
		
		map.put(1, "V");
		map.put(2, "V");
		map.put(5, "C");
		map.put(6, "C");
		map.put(7, "C");
		map.put(8, "V");
		map.put(9, "V");
		map.put(10, "C");
		map.put(11, "C");
		map.put(12, "C");
		map.put(13, "C");
		map.put(14, "C");
		map.put(15, "C");
		map.put(16, "C");
		map.put(17, "V");
		map.put(18, "V");
		map.put(19, "C");
		map.put(20, "C");
		map.put(21, "C");
		map.put(22, "V");
		map.put(23, "C");
		map.put(24, "C");
		map.put(25, "V");
		map.put(26, "V");
		map.put(27, "C");
		map.put(28, "C");
		map.put(29, "V");
		
		
		return map;
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
	
	public static void main(String[] args) throws Exception
	{
		for(int x=1; x < NewRDPParserFileLine.TAXA_ARRAY.length; x++)
		{
			String level = NewRDPParserFileLine.TAXA_ARRAY[x];
			
			OtuWrapper wrapper = 
					new OtuWrapper(
							ConfigReader.getLactoCheckDir()+ File.separator + "rdp" 
									+ File.separator +
									 level + "asColumns.txt");
			
			File normFile = new File(  ConfigReader.getLactoCheckDir()+ File.separator + "rdp" 
									+ File.separator +
									 level + "asColumnsNorm.txt");
			
			File normFileWithMetadata = new File(  ConfigReader.getLactoCheckDir()+ File.separator + "rdp" 
					+ File.separator +
					 level + "asColumnsNormWithMetadata.txt");
			
			// this is hacked and avgNumber needs to be changed in the OtuWrapper class
			//wrapper.writeNormalizedDataToFile(normFile);
			
			File logNormFile = new File(ConfigReader.getLactoCheckDir()+ File.separator + "rdp" 
					+ File.separator + level + "asColumnsLogNorm.txt");
			
			File metaFile =new File(ConfigReader.getLactoCheckDir()+ File.separator + "rdp" 
					+ File.separator + level + "asColumnsLogNormPlusMeta.txt");
			
			addMetadata(logNormFile, metaFile, wrapper);
			addMetadata(normFile, normFileWithMetadata, wrapper);
			
		}
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
		HashMap<Integer, String> birthTypeMap = getBirthType();
		
		BufferedReader reader = new BufferedReader(new FileReader(inFile));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
		
		String[] topSplits = reader.readLine().split("\t");

		writer.write(topSplits[0].replace("rdp_", "").replace(".txt.gz", "") 
				+ "\trun\tread\tgroupID\tgroupNumber\tstoolOrGa\tsubjectNumber\tbirthMode\tmostFrequentTaxa\tfractionMostFrequent\tqPCR16S\tL_crispatus\tL_iners\tbglobulin\tsequencingDepth\tshannonDiveristy");
		
		for( int x=1; x < topSplits.length; x++)
			writer.write("\t" + topSplits[x]);
		
		writer.write("\n");
		
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			
			String[] splits = s.split("\t");
			
			Holder h = getMostFrequent(splits[0], originalWrapper);
					
			
			String key = splits[0].replace("rdp_", "").replace(".txt.gz", "");
					
			String[] codes = key.split("_");
			
			if(codes.length != 3)
				throw new Exception("No " + key);

			if( codes[2].startsWith("G")  || key.endsWith("neg") || codes[2].startsWith("S"))
				if(! codes[2].equals("S30"))
			{
				System.out.println(key);
				if( codes[2].startsWith("G")   || codes[2].startsWith("S"))
				{
					Integer birthGroup = birthMap.get(codes[2].replace("S","G"));
					
					PCR_DataParser pcr = pcrMap.get(codes[2].replace("S", "G"));
					
					if( pcr == null || ! pcr.getGroup().equals(codes[2].replace("S", "G")))
						throw new Exception("No");
					
					String qPCRString = "NA";
					
					if( qPCR16SMap.get(codes[2]) != null )
						qPCRString = qPCR16SMap.get(codes[2]).toString();
					
					Integer subjectNumber = Integer.parseInt(codes[2].replace("S", "").replace("G", "") );
					String birthMode = "U";
					
					if( birthTypeMap.containsKey(subjectNumber) )
						birthMode = birthTypeMap.get(subjectNumber);
					
					writer.write(key + "\t" + codes[0] + "\t" + codes[1] + "\t" +  codes[2] + "\t" + birthGroup +  "\t" + 
							codes[2].substring(0,1) + "\t"+ subjectNumber+ "\t" + birthMode + "\t" + 
							h.otuName + "\t" + h.frequency + "\t" + 
					qPCRString + "\t" + 
					+ pcr.getL_crispatus() + "\t" + pcr.getL_iners() + 
							"\t" + pcr.getBglobulin() + "\t" + originalWrapper.getNumberSequences(splits[0]) + "\t" +
									originalWrapper.getShannonEntropy(splits[0]));
				
				}
				else if ( key.endsWith("neg"))
				{
					System.out.println("In neg");
					writer.write(splits[0] + "\t" + codes[0] +"\t" +codes[1] + "\t" +  codes[2] + "\t" + "0"+  "\tneg\t0\t"  + 
							"neg\t"  + h.otuName + "\t"+ h.frequency + "\t" 
				+"0\t"+ "0"+ "\t" + "0" + 
							"\t" + "0"+ "\t" + originalWrapper.getNumberSequences(splits[0]) + "\t" +
									originalWrapper.getShannonEntropy(splits[0]));
				}
				else throw new Exception("No");
					
				for( int x=1; x < splits.length; x++)
					writer.write("\t" + splits[x]);
				
				writer.write("\n");
			}
			else
			{
				//System.out.println("Could not find" + splits[0]);
			}
			
		}
		
		writer.flush();  writer.close();
		reader.close();
	}
}
