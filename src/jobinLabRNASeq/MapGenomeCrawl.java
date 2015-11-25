package jobinLabRNASeq;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.zip.GZIPInputStream;


import parsers.GffParser;
import utils.ConfigReader;

public class MapGenomeCrawl
{
	/*
	 * From DESeq for just one paired end - pretty rough...
	 * Would need to refine for publication
	 */
	public static double[] sizeFactors
	= { 0.9182634 , 0.2560295, 0.1198347 ,0.4634456,2.3044328, 0.8166222,2.3854908 ,
		1.3551541, 3.9599639 , 3.3995081};

	//private static int NUM_SAMPLES = 10;
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, HashMap<Integer, double[]>>  countMap = 
			getCountMap();
		
		writeResults(countMap);
	}
	
	private static void writeResults( HashMap<String, HashMap<Integer, double[]>>  countMap )
		throws Exception
	{
		/*
		System.out.println("Writing results");
		double[] norms = new double[NUM_SAMPLES+1];
		
		for( String s : countMap.keySet() )
		{
			HashMap<Integer, double[]> map = countMap.get(s);
			
			for(Integer i : map.keySet())
			{
				double[] a = map.get(i);
				
				for( int x=1; x < a.length; x++)
					norms[i] += a[x];
			}
		}*/
		
		HashMap<String, BufferedWriter> writerMap = new HashMap<String, BufferedWriter>();
			
		
		for(String s : countMap.keySet())
		{
			BufferedWriter writer = writerMap.get(s);
			
			if( writer == null)
			{
				writer= new BufferedWriter(new FileWriter(new File(ConfigReader.getJobinLabRNASeqDir() + File.separator + 
						s+ "genomeCounts.txt")));

				writer.write("sampleKey\tsampleAnnotation\tposition\tcount\n");
				writerMap.put(s, writer);
				
			}
			
			HashMap<Integer, double[]> innerMap = countMap.get(s);
			
			for( Integer i : innerMap.keySet())
			{
				String timeAnnotation = TimeAnnotations.getAnnotation(i + "MM");
				
				double[] a = innerMap.get(i);
				
				for( int x=1; x < a.length; x++ )
				{
					writer.write(i + "\t");
					writer.write(timeAnnotation + "\t");
					writer.write( x+ "\t");
					writer.write( a[x] / sizeFactors[i-1] + "\n" );
				}
			}
		}
		
		for( BufferedWriter writer : writerMap.values() )
		{
			writer.flush(); writer.close();
		}
	}

	private static HashMap<String, HashMap<Integer, double[]>> 
		getCountMap() throws Exception
	{
		HashMap<String, GffParser> gffMap = 
			FindGeneForMappedSequence.getGeneFileMap();
		
		// the outer key is the gff file name
		// the inner key is the sampleID
		// the float[] is the number of covered nucleotides
		HashMap<String, HashMap<Integer, double[]>> countMap = 
				new HashMap<String, HashMap<Integer,double[]>>();
		
		List<File> outputFiles = FindGeneForMappedSequence.getOutputFiles();
		
		for(File f : outputFiles) if ( ! f.getName().startsWith("11MM"))
		{
			System.out.println(f.getAbsolutePath());
			Integer key = Integer.parseInt(new StringTokenizer(f.getName(), "MM").nextToken());
			
			BufferedReader reader = new BufferedReader( new InputStreamReader( 
					new GZIPInputStream( new FileInputStream(f))));
			
			reader.readLine();
			
			for(String s= reader.readLine(); 
			s != null;
				s= reader.readLine())
				{
					String[] splits= s.split("\t");
					
					StringTokenizer sToken = new StringTokenizer(splits[1], "|");
					
					sToken.nextToken();sToken.nextToken();sToken.nextToken();
					String targetName = sToken.nextToken().replace(".1", "");
					
					GffParser gffParser = gffMap.get(targetName);
					
					if( gffParser == null)
						throw new Exception("No");
					
					HashMap<Integer, double[]> innerMap = 
						countMap.get(targetName);
					
					if(innerMap == null)
					{
						innerMap = new HashMap<Integer, double[]>();
						countMap.put(targetName, innerMap);
					}
					
					double[] array = innerMap.get(key);
					
					if( array == null)
					{
						array = new double[gffParser.getRegionEnd() + 1];
						innerMap.put(key, array);
					}
					
					int startPosition = Integer.parseInt(splits[2]);
					int endPos = Math.min(array.length -1, startPosition + 100);
					
					for( int x=startPosition; x <= endPos; x++)
						array[x]++;
					
				}
			
			reader.close();
		}
		
		return countMap;
	}
}
