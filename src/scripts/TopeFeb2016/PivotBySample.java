package scripts.TopeFeb2016;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import parsers.NewRDPParserFileLine;
import utils.ConfigReader;

public class PivotBySample
{
	private static class Holder
	{
		Double set1_1;
		Double set1_4;
		Double set3_1;
		Double set3_4;
		double numSeqs_set1_1=0;
		double numSeqs_set1_4=0;
		double numSeqs_set3_1=0;
		double numSeq_set3_4=0;
		
		double getNumSeqs() 
		{
			List<Double> list = new ArrayList<Double>();
			list.add(numSeqs_set1_1);
			list.add(numSeqs_set1_4);
			
			list.add(numSeqs_set3_1);
			list.add(numSeq_set3_4);
			
			
			Collections.sort(list);
			
			int index =0;
			
			while(list.get(index)==0)
				index++;
			
			return list.get(index);
		}
	}
	
	private static HashMap<String, Holder> getHolderMap(int level) throws Exception
	{
		HashMap<String, Holder> map = new HashMap<String, Holder>();
		
		BufferedReader reader = new BufferedReader(new FileReader(
				new File( ConfigReader.getTopeFeb2016Dir() 
				+ File.separator + "spreadsheets" +
				File.separator + "pivoted_" + 
				NewRDPParserFileLine.TAXA_ARRAY[level] + "mdsPlusMetadata.txt")));
		
		reader.readLine();
		
		for(String s= reader.readLine() ; s != null ; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			
			Holder h = map.get(splits[1]);
			
			if( h == null)
			{
				h = new Holder();
				map.put(splits[1], h);
			}
			
			String set = splits[5];
			int read = Integer.parseInt(splits[6]);
			Double val = Double.parseDouble(splits[7]);
			int numSeqs = (int) Double.parseDouble(splits[2]);
			
			if( set.equals("set1") && read == 1)
			{
				h.set1_1 = val;
				h.numSeqs_set1_1 = numSeqs;
			}
			else if( set.equals("set1") && read == 4)
			{
				h.set1_4 = val;
				h.numSeqs_set1_4 = numSeqs;
			}
			else if(set.equals("set3") && read == 1 )
			{
				h.set3_1 = val;
				h.numSeqs_set3_1 = numSeqs;
			}
			else if(set.equals("set3") && read == 4 )
			{
				h.set3_4 = val;
				h.numSeq_set3_4 = numSeqs;
			}
			else
				throw new Exception("parsing error ");
		}
		
		reader.close();
		
		return map;
		
	}
	
	private static String getValOrNone( Double d )
	{
		return d == null ? "" : d.toString();
	}
	
	public static void main(String[] args) throws Exception
	{
		for( int x=1; x < NewRDPParserFileLine.TAXA_ARRAY.length; x++)
		{
			System.out.println(NewRDPParserFileLine.TAXA_ARRAY[x]);
			HashMap<String, Holder> map = getHolderMap(x);
			
			BufferedWriter writer =  new BufferedWriter(new FileWriter(new File(ConfigReader.getTopeFeb2016Dir() 
					+ File.separator + "spreadsheets" +
					File.separator + "pivoted_" + 
					NewRDPParserFileLine.TAXA_ARRAY[x] + "mds1PivotedBySample.txt")));
			
			writer.write("key\tset1_1\tnumSeqs1_1\tset1_4\tnumSeqs1_4\tset3_1\tnumSeqs3_1\tset3_4\tnumSeqs3_4\tminSeqs\n");
			
			for(String s : map.keySet())
			{
				Holder h = map.get(s);
				writer.write(s + "\t");
				writer.write( getValOrNone(h.set1_1) + "\t" + h.numSeqs_set1_1 + "\t" );
				writer.write( getValOrNone(h.set1_4) + "\t" + h.numSeqs_set1_4 + "\t" );
				writer.write( getValOrNone(h.set3_1) + "\t" + h.numSeqs_set3_1 + "\t");
				writer.write( getValOrNone(h.set3_4) + "\t" + h.numSeq_set3_4 + "\t");
				writer.write(h.getNumSeqs() + "\n");
			}
			
			writer.flush();  writer.close();
			
		}
	}
}
