package mbqc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.StringTokenizer;

import utils.ConfigReader;

public class AddMetadataToDiversity
{
	public static void main(String[] args) throws Exception
	{
		addMetadata("merged-final-unrarefied.txt");
	}
	
	public static void addMetadata(String fileName) throws Exception
	{
		HashMap<String, RawDesignMatrixParser> metaMap = 
				RawDesignMatrixParser.getByFullId();
		
		HashMap<String, String> kitMap = PValuesByExtraction.getManualKitManufacturer();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getMbqcDir() + File.separator + 
				 File.separator +  "fromGaleb" + File.separator + fileName)));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				ConfigReader.getMbqcDir() + File.separator + 
				 File.separator +  "fromGaleb" + File.separator + 
					fileName.replace(".txt", "") + "plusMetadata.txt"
					)));
		
		writer.write("fullID\tinformaticsToken\tobscuredToken\tnumberToken\textractionWetlab\tsequencingWetlab\tmbqcID\textractionIsNA\tkitManufactuer\tmoBioOrOther");
		writer.write( reader.readLine() +  "\n");
		
		int numFound =0;
		int numMissed=0;
		
		
		for(String s= reader.readLine();  s !=null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			
			String key = splits[0].replaceAll("\"", "");
			if( metaMap.containsKey(key))
			{
				numFound++;
				writer.write(key+ "\t");
				
				StringTokenizer innerSplits = new StringTokenizer(key, ".");
				writer.write(innerSplits.nextToken() + "\t" + innerSplits.nextToken() + "\t" + 
						innerSplits.nextToken()  + "\t");
				
				if( innerSplits.hasMoreTokens())
					throw new Exception("No");
				
				RawDesignMatrixParser rdmp = metaMap.get(key);
				writer.write(rdmp.getExtractionWetlab() + "\t" + rdmp.getSequecingWetlab() + "\t" + rdmp.getMbqcID() + "\t"
						+  rdmp.getExtractionWetlab().equals("NA") + "\t" );
				
				String kit = kitMap.get(rdmp.getExtractionWetlab());
				
				writer.write(kit + "\t");
				
				if( kit.equals("MO-BIO"))
					writer.write("MO-BIO");
				else
					writer.write("other");
					
				
				for(int x=1; x<=4; x++)
					writer.write("\t" + splits[x]);
				
				writer.write("\n");
				
				if( splits.length != 5)
					throw new Exception("No " + key);
			}
			else
			{
				numMissed++;
			}
		}
		
		writer.flush();  writer.close();
		
		System.out.println(numFound + " " + numMissed);
	}
}
