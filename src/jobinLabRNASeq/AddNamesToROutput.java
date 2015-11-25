package jobinLabRNASeq;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import cogFunctionalCategoryParser.CogFunctionalCategory;
import cogFunctionalCategoryParser.CogFunctionalMap;

import utils.ConfigReader;

public class AddNamesToROutput
{
	private static class Holder implements Comparable<Holder>
	{
		String id;
		double pval;
		double pvaladjusted;
		double foldChange;
		
		@Override
		public int compareTo(Holder o)
		{
			return Double.compare(this.pvaladjusted, o.pvaladjusted);
		}
	}
	
	/*
	 * Dependent on the output of DESEQ_Script.txt from R
	 */
	public static void main(String[] args) throws Exception
	{
		writeAPair(ConfigReader.getJobinLabRNASeqDir() + File.separator + "2DayVs12Weeks.txt",
				ConfigReader.getJobinLabRNASeqDir() +
				File.separator + "2DayVs12WeeksPlusAnnotation.txt");
		
		writeAPair(ConfigReader.getJobinLabRNASeqDir() + File.separator + "12WeeksVs18Weeks.txt",
				ConfigReader.getJobinLabRNASeqDir() +
				File.separator + "12WeeksVs18WeeksPlusAnnotation.txt");
		

		writeAPair(ConfigReader.getJobinLabRNASeqDir() + File.separator + "2DayVs18Weeks.txt",
				ConfigReader.getJobinLabRNASeqDir() +
				File.separator + "2DayVs18WeeksPlusAnnotation.txt");
	}
	
	public static void writeAPair(String inFile, String outFile) throws Exception
	{
		HashMap<String, AnnotationHolder> map = getAnnotationMap();
				
		List<Holder> list = getList(inFile);
		writeResults(outFile, list, map);
		
		
	
	}
	private static void writeResults( String outFile,  List<Holder> list, HashMap<String, AnnotationHolder> map ) throws Exception
	{
		
		HashMap<String, CogFunctionalCategory> cogMap = CogFunctionalCategory.getAsMap();
		HashMap<String, String> cogAnnotations = CogFunctionalMap.getCogFunctionalMap();
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(outFile)));
		
		writer.write("id\tfoldChange\tpval\tpValAdjusted\tannotation\tgffFile\tstartPos\tcogID\tcogAnnotation\tcogFunctionalChar\tcogFunctionalAnnotation\n");
		
		for( Holder h : list)
		{
			writer.write(h.id + "\t");
			writer.write(h.foldChange + "\t");
			writer.write( (h.foldChange < 1 ?  Math.log( h.pval) : -Math.log(h.pval))
					+ "\t");
			writer.write( h.pvaladjusted + "\t");
			
			AnnotationHolder ah = map.get(h.id);
			
			if( ah == null)
				throw new Exception("Could not find " + h.id);
			
			writer.write(ah.product + "\t");
			writer.write(ah.filename + "\t");
			writer.write(ah.startPos + "\t");
			
			CogFunctionalCategory cfc = cogMap.get(ah.cogID);
			System.out.println(ah.cogID);
			
			if( cfc != null)
				writer.write(cfc.getCogID() + "\t" + cfc.getProduct() + "\t" + cfc.getFunctionalChar() + "\t" + 
								cogAnnotations.get("" + cfc.getFunctionalChar()) + "\n"	);
			else
				writer.write("NA\tNA\tNA\tNA\n");
		}
		
		writer.flush();  writer.close();
	}
	
	
	private static List<Holder> getList(String filepath) throws Exception
	{
		List<Holder> list = new ArrayList<Holder>();
		
		BufferedReader reader = new BufferedReader(new FileReader(filepath));
		
		reader.readLine();
		
		for(String s = reader.readLine();
					s != null;
						s = reader.readLine())
		{
			String[] splits = s.split("\t");
			Holder h= new Holder();
			list.add(h);
			
			h.id = splits[1].trim().replaceAll("\"", "");
			h.foldChange = 0;
			
			try{
				h.foldChange = Double.parseDouble(splits[5]);
			}catch(Exception e){}
			
			h.pval = 1; 
			
			try{
				h.pval = Double.parseDouble(splits[7]);
			}
			catch(Exception e) {}
			
			h.pvaladjusted = 1;
			
			try{
				h.pvaladjusted = Double.parseDouble(splits[8]);
			}
			catch(Exception ex) {}
			
		}
		
		reader.close();
		
		Collections.sort(list);
		return list;
	}
	
	public static class AnnotationHolder
	{
		public String product;
		public String filename;
		public int startPos;
		public String cogID;
	}
	
	public static HashMap<String, AnnotationHolder> getAnnotationMap() throws Exception
	{
		HashMap<String, AnnotationHolder> map = new HashMap<String, AnnotationHolder>();
		
		File baseDir = new File(ConfigReader.getJobinLabRNASeqDir());
		
		if( ! baseDir.exists())
			throw new Exception("No " + baseDir.getAbsolutePath());
		
		String[] list = baseDir.list();
		
		for( String s : list)
		{
			if( s.endsWith(".gff"))
				addAnnotationFromFile(map, baseDir.getAbsolutePath() + File.separator + s);
		
		}
			
		return map;
	}
	
	private static void addAnnotationFromFile( HashMap<String, AnnotationHolder> map, String filePath ) throws Exception
	{
		System.out.println(filePath);
		BufferedReader reader = new BufferedReader(new FileReader(new File(filePath)));
		
		for(String s = reader.readLine(); 
				s != null; 
					s = reader.readLine())
		{
			//System.out.println(s);
			StringTokenizer sToken = new StringTokenizer(s);
			
			if( sToken.countTokens() > 3)
			{
				sToken.nextToken(); sToken.nextToken(); 
				
				if( sToken.nextToken().equals("gene"))
				{
					int startPos = Integer.parseInt(sToken.nextToken());
					for( int x=0; x < 4; x++)
						sToken.nextToken();
					
					String lastToken = sToken.nextToken();
					
					while(sToken.hasMoreTokens())
						lastToken = lastToken + " " + sToken.nextToken();
					
					StringTokenizer innerToken = new StringTokenizer(lastToken, ";");
					
					String locusTag = innerToken.nextToken();
					
					while(! locusTag.startsWith("locus_tag="))
						locusTag = innerToken.nextToken();
					
					locusTag = locusTag.replace("locus_tag=", "");
					
					String nextLine = reader.readLine();
					//System.out.println(nextLine);
					
					String cogID = null;
					sToken = new StringTokenizer(nextLine);
					
					for( int x=0; x < 8; x++)
						sToken.nextToken();
					
					
					lastToken = sToken.nextToken();
					
					while(sToken.hasMoreTokens())
						lastToken = lastToken + " "+ sToken.nextToken();
					
					innerToken = new StringTokenizer(lastToken, ";");
					
					String product = innerToken.nextToken();
					
					while ( ! product.startsWith("product="))
					{
						product = innerToken.nextToken();
						
						if( product.startsWith("Note=COG"))
							cogID = product.replace("Note=", "");
						
					}
						
					AnnotationHolder ah = new AnnotationHolder();
					ah.product = product.replace("product=", "");
					ah.filename = new File(filePath).getName();
					ah.startPos = startPos;
					
					if( cogID != null)
						cogID = new StringTokenizer(cogID).nextToken();
					
					ah.cogID = cogID;
					
					map.put(locusTag, ah);
				}
			}
		}
		
		reader.close();
	}
}
