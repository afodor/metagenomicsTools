package scripts.emilyJan2018;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.zip.GZIPInputStream;

import parsers.FastQ;
import utils.ConfigReader;
import utils.Translate;

public class Demultiplex
{
	/*
	 * 
AN703 R1: http://www.med.unc.edu/uploads/hupdj.undetermin.gz
AN703 Index: http://www.med.unc.edu/uploads/hfyeg.undetermin.gz
AN703 R2: http://www.med.unc.edu/uploads/gwhib.undetermin.gz

AN40 R1: http://www.med.unc.edu/uploads/gwqqu.an40undete.gz
AN40 Index: http://www.med.unc.edu/uploads/fexko.an40undete.gz
AN40 R2: http://www.med.unc.edu/uploads/ibmhs.an40undete.gz

AN34 R1: http://www.med.unc.edu/uploads/sspwa.an34undete.gz
AN34 Index: http://www.med.unc.edu/uploads/aszxo.an34undete.gz
AN34 R2: http://www.med.unc.edu/uploads/uvkap.an34undete.gz

AN81 R1: rszzz.an81s0l001.gz
AN81 index: zqsfx.an81s0l001.gz
AN81 R2: goicf.an81s0l001.gz

	 */
	
	public static void main(String[] args) throws Exception
	{
		/*
		demultiplexASample("AN703"
				, new File( ConfigReader.getEmilyJan2018Dir() + File.separator+  "hupdj.undetermin.gz"), 
				new File(ConfigReader.getEmilyJan2018Dir() + File.separator+ "hfyeg.undetermin.gz"), 
				new File(ConfigReader.getEmilyJan2018Dir() + File.separator+ "gwhib.undetermin.gz"),
				new File(ConfigReader.getEmilyJan2018Dir() + File.separator + "2018-01-10_AN703_16S metadata.txt"));
		
		demultiplexASample("AN40"
				, new File( ConfigReader.getEmilyJan2018Dir() + File.separator+  "gwqqu.an40undete.gz"), 
				new File(ConfigReader.getEmilyJan2018Dir() + File.separator+ "fexko.an40undete.gz"), 
				new File(ConfigReader.getEmilyJan2018Dir() + File.separator+ "ibmhs.an40undete.gz"),
				new File(ConfigReader.getEmilyJan2018Dir() + File.separator + "2018-01-10_AN40_16S metadata.txt"));
		
		demultiplexASample("AN34"
				, new File( ConfigReader.getEmilyJan2018Dir() + File.separator+  "sspwa.an34undete.gz"), 
				new File(ConfigReader.getEmilyJan2018Dir() + File.separator+ "aszxo.an34undete.gz"), 
				new File(ConfigReader.getEmilyJan2018Dir() + File.separator+ "uvkap.an34undete.gz"),
				new File(ConfigReader.getEmilyJan2018Dir() + File.separator + "2018-01-10_AN34_16S metadata.txt"));
				*/
		
		demultiplexASample("AN81"
				, new File( ConfigReader.getEmilyMay2018Dir() + File.separator+  "rszzz.an81s0l001.gz"), 
				new File(ConfigReader.getEmilyMay2018Dir() + File.separator+ "zqsfx.an81s0l001.gz"), 
				new File(ConfigReader.getEmilyMay2018Dir() + File.separator+ "goicf.an81s0l001.gz"),
				new File(ConfigReader.getEmilyMay2018Dir() + File.separator + "2018-04-30_AN81_16S_metadata.txt"));
		
	}
	
	private static BufferedWriter getOrCreateWriter(String filepath, HashMap<String, BufferedWriter> map)
		throws Exception
	{
		BufferedWriter writer = map.get(filepath);
		
		if(writer== null)
		{
			writer=new BufferedWriter(new FileWriter(filepath));
			
			map.put(filepath, writer);
		}
		
		return writer;
	}
	
	private static void demultiplexASample(String suffix, File r1File, File indexFile, File r2File,
				File metaFile) throws Exception
	{
		System.out.println("\n\nStart " + metaFile.getAbsolutePath());
		HashMap<String, BufferedWriter> writerMap = new HashMap<>();
		
		HashMap<String, String> barcodeMap = getSequenceToBarcodeMap(metaFile);
		
		//for(String s : barcodeMap.keySet())
		//	System.out.println(s + " "+  barcodeMap.get(s));
		
		BufferedReader reader1 = new BufferedReader((new InputStreamReader( new GZIPInputStream(
				new FileInputStream(r1File)))));
		BufferedReader reader2 = new BufferedReader((new InputStreamReader( new GZIPInputStream(
				new FileInputStream(r2File)))));
		BufferedReader indexReader= new BufferedReader((new InputStreamReader( new GZIPInputStream(
				new FileInputStream(indexFile)))));
		
		long index =0;
		double success =0;
		for( FastQ fastq1 = FastQ.readOneOrNull(reader1) ; fastq1 != null; fastq1 =FastQ.readOneOrNull(reader1) )
		{
			index++;
			FastQ fastq2 = FastQ.readOneOrNull(reader2);
			FastQ fastqIndex = FastQ.readOneOrNull(indexReader);
			
			//if( ! fastq1.getFirstTokenOfHeader().equals(fastq2.getFirstTokenOfHeader()))
			//	throw new Exception("non identical header "+ fastq1.getFirstTokenOfHeader() + " " + 
				//		fastq2.getFirstTokenOfHeader() );

			//if( ! fastq1.getFirstTokenOfHeader().equals(fastqIndex.getFirstTokenOfHeader()))
				//throw new Exception("non identical header");
			
			String keySeq = fastqIndex.getSequence();
			keySeq = Translate.reverseTranscribe(keySeq);
			keySeq = keySeq.substring(1);
			
			String barcode = barcodeMap.get(keySeq);
			
			if( barcode != null)
			{
				success++;
				
				String outfasta1 = ConfigReader.getEmilyJan2018Dir() + File.separator + 
						"fastaOut" + File.separator + barcode +"_" + suffix + "_1.fasta";
				
				String outfasta2 = ConfigReader.getEmilyJan2018Dir() + File.separator + 
						"fastaOut" + File.separator + barcode +"_" + suffix + "_2.fasta";
				
				BufferedWriter writer1 = getOrCreateWriter(outfasta1, writerMap);
				BufferedWriter writer2 = getOrCreateWriter(outfasta2, writerMap);
				
				writer1.write(">"+ fastq1.getFirstTokenOfHeader() + "\n");
				writer1.write(fastq1.getSequence() + "\n");
				writer1.flush();
				
				writer2.write(">"+ fastq2.getFirstTokenOfHeader() + "\n");
				writer2.write(fastq2.getSequence() + "\n");
				writer2.flush();
				
			}
			else
			{
			//	System.out.println("Could not find " + fastqIndex.getSequence());
			//	System.exit(1);
			}
			
			if( index % 1000000 ==0)
				System.out.println(success +  " " + index + " " + (success/index));
		}
		
		if ( FastQ.readOneOrNull(reader2) != null) 
			throw new Exception("extra ");
		
		if ( FastQ.readOneOrNull(indexReader) != null) 
			throw new Exception("extra ");
		
		for( BufferedWriter writer : writerMap.values())
		{
			writer.flush();  writer.close();
		}
		
		reader1.close();
		reader2.close();
		indexReader.close();
		
		System.out.println("Finished " + success +  " " + index + " " + (success/index));
	}
	
	private static HashMap<String, String> getSequenceToBarcodeMap( File metaFile) throws Exception
	{
		HashMap<String, String> map =new HashMap<>();
		
		BufferedReader reader = new BufferedReader(new FileReader(metaFile));
		
		reader.readLine();
		
		for(String s = reader.readLine(); s != null && s.trim().length() > 0; s= reader.readLine())
		{
			StringTokenizer sToken =new StringTokenizer(s.replaceAll("\"", ""), "\t");
			
			String value = new String(sToken.nextToken());
			
			//System.out.println(key+  " " + value);
			
			//skipping a column not sure what is going on with the tabs here
			if( !value.startsWith("Mouse"))
			{
				String key = sToken.nextToken().trim();
				
				if(map.containsKey(key))
					throw new Exception("Duplicate key " + key);
				
				if(map.containsValue(value))
					throw new Exception("Duplicate value " + value);
				
				map.put(  key, value);
			}
			else
			{
				System.out.println("Warning invalid line " + value);
			}
			
		}
		
		reader.close();
		return map;
	}
}
