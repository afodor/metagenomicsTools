package jobinLabRNASeq;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

import jobinLabRNASeq.AddNamesToROutput.AnnotationHolder;

import parsers.GffParser;
import utils.ConfigReader;

public class AddAnnotationsToCrawl
{
	public static void main(String[] args) throws Exception
	{
		HashMap<String, AnnotationHolder> annotationMap = 
			AddNamesToROutput.getAnnotationMap();
		
		HashMap<String, GffParser> gffMap = 
			FindGeneForMappedSequence.getGeneFileMap();
		
		File topDir = new File(ConfigReader.getJobinLabRNASeqDir());
		
		for(String s : topDir.list())
			if( s.endsWith("genomeCounts.txt"))
			{
				String gffName = s.replace("genomeCounts.txt", "");
				GffParser parser = gffMap.get(gffName);
				
				if(parser == null)
					throw new Exception("No");
				
				BufferedReader reader = new BufferedReader(new FileReader(new File( 
						topDir.getAbsoluteFile() + File.separator + s)));
				
				BufferedWriter writer = new BufferedWriter(new FileWriter( 
						ConfigReader.getJobinLabRNASeqDir() + File.separator + 
						gffName + "genomeWithAnnotations.txt"));
				
				writer.write(reader.readLine() + "\tannotation\n");
				
				for( GffParser.Range range : parser.getRangeList() )
				{
					for( int x=range.lower; x <= range.upper; x++)
					{
						String annotation = "";
						
						AnnotationHolder holder = 
							annotationMap.get(range.locusTag);
						
						if( holder != null)
							annotation = holder.product;
						
						writer.write("AN\tAN\t" + x + "\t-100.0\t" + 
								annotation +  "_" + range.locusTag + "\n");
					}
				}
				
				for(String s2 = reader.readLine();
							s2 != null;
								s2 = reader.readLine())
				{
					String[] splits = s2.split("\t");
					if( ! splits[0].startsWith("11") && Double.parseDouble(splits[3]) > 0 )
						writer.write(s2 + "\tNA\n");
				}
				
				writer.flush();  writer.close();
				reader.close();
				System.out.println(s);
			}
			
	}
}
