package creOrthologs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.zip.GZIPInputStream;

import creOrthologs.MergeThree.Holder;
import utils.ConfigReader;

public class GeneAnnotationsToBestHits
{
	
	public static void main(String[] args) throws Exception
	{

		HashMap<String, MeanFileLine> meanMap = MeanFileLine.getMeans();
		
		HashMap<Integer, String> lineMap = AddGeneAnnotations.getLineDescriptions();
		
		HashMap<String, Holder> holderMap = MergeThree.getHolderMap();
		
		
		File hitsDir = new File(ConfigReader.getCREOrthologsDir() + File.separator + 
									"topHitsDir");
		
		File annotatedHitsDir = new File(ConfigReader.getCREOrthologsDir() + File.separator + 
				"annotatedHitsDir");
		
		for(String s : hitsDir.list())
			if( s.endsWith("gz"))
			{
				System.out.println(s);
				BufferedReader reader = 
					new BufferedReader(new InputStreamReader( 
						new GZIPInputStream( new FileInputStream( 
								hitsDir.getAbsolutePath()+ File.separator + s))));		
				
				BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
					annotatedHitsDir.getAbsoluteFile() + File.separator +  s.replace(".gz", ""))));
				
				writer.write("lineID\tmeanRes\tmeanCarolina\tmeanSuc\tqueryContig\tqueryStart\tqueryEnd\tbitScore\tpValueSucVsRes\tpValueCHSVsSuc\tpValueCHSVsRes\tdesciprtion\n");
				
				reader.readLine();
				for(String s2 = reader.readLine(); s2 != null; s2 = reader.readLine())
				{
					String[] splits =s2.split("\t");
					
					
					int keyInt = Integer.parseInt(new StringTokenizer(splits[0], "@").nextToken());
					writer.write(keyInt + "\t");
					
					MeanFileLine mfl = meanMap.get("Line_" + keyInt);
					
					if( mfl == null)
						throw new Exception("Could not find "  + "Line_" + keyInt);
					
					writer.write(mfl.getMeanRes() + "\t" + mfl.getMeanCar() + "\t" + mfl.getMeanSuc() + "\t");
					
					writer.write("contig_" + splits[1] + "\t");
					writer.write(splits[2] + "\t");
					writer.write(splits[3] + "\t");
					writer.write(splits[4] + "\t");
					
					Holder h = holderMap.get("\"Line_" + keyInt + "\"");
					
					if( h != null)
					{
						writer.write(
								"" + log10AndFlip(h.resVsSuc , mfl.getMeanSuc(), mfl.getMeanRes()));
						
						writer.write("\t" + log10AndFlip(h.carVsSuc , mfl.getMeanSuc(), mfl.getMeanCar()));
						
						writer.write("\t"+ log10AndFlip(h.carVsRes , mfl.getMeanRes(), mfl.getMeanCar()) 
													+ "\t" );
					}
					else
					{
						writer.write( "\t\t\t");
						
					}
						
					writer.write( lineMap.get(keyInt) + "\n");
				}
				
				writer.flush();  writer.close();
				
				reader.close();
			}
	}
	
	private static Double log10AndFlip( Double p, double meanSensitive, double meanResistant )
	{
		if( p == null)
			return null;
		
		double log10 = Math.log10(p);
		
		if( meanResistant > meanSensitive)
			log10 = -log10;
		
		return log10;
	}
}
