package scripts.topeOneAtATime;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import parsers.NewRDPParserFileLine;
import utils.Avevar;
import utils.ConfigReader;

public class CountForwardReads
{
	public static void main(String[] args) throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				ConfigReader.getTopeOneAtATimeDir() + File.separator + 
				"countSummary.txt")));
		
		writer.write("level\ttotalSequences\taverage\tsd\tnumCase\tnumControl\tfractionAbove1000\n");
		
		for(int x=1; x < NewRDPParserFileLine.TAXA_ARRAY.length; x++)
		{
			List<Double> sampleCounts = new ArrayList<Double>();
			String taxa = NewRDPParserFileLine.TAXA_ARRAY[x];
			
			BufferedReader reader = new BufferedReader(new FileReader(new File(
					ConfigReader.getTopeOneAtATimeDir() + File.separator + 
					"merged" + File.separator + 
						"pivoted_" + taxa + "asColumnsPlusMetadata.txt")));
			
			
			reader.readLine();
			
			int numCase = 0;
			int numControl = 0;
			
			for(String s= reader.readLine(); s != null; s= reader.readLine())
			{
				
				String[] splits = s.split("\t");
				
				int readNum = Integer.parseInt(splits[10]);
				
				if( readNum == 1 && splits[11].toLowerCase().equals("false") && splits[14].length() > 0)
						if( Integer.parseInt(splits[14]) == 0 || Integer.parseInt(splits[14]) == 1)
					{
						List<Double> list = new ArrayList<Double>();
						
						for( int y=17; y < splits.length; y++)
							list.add(Double.parseDouble(splits[y]));
						
						sampleCounts.add(sum(list));
						
						if( Integer.parseInt(splits[14]) == 0)
							numControl++;
						else if (Integer.parseInt(splits[14]) == 1)
							numCase++;
						else throw new Exception("Parsing error");
					}
				else if ( readNum != 4 && readNum != 1)
						throw new Exception("Parsing error " + readNum);
			}

			Collections.sort(sampleCounts);
			Avevar avevar = new Avevar(sampleCounts);
			
			NumberFormat nf = NumberFormat.getInstance();
			nf.setMaximumFractionDigits(2);
			
			writer.write(taxa + "\t" + (long)sum(sampleCounts) + "\t" + 
						nf.format(avevar.getAve()) + "\t" + nf.format(avevar.getSD())
						+ "\t" + numCase+ "\t" + numControl + "\t" + 
					nf.format(fractionAboveThreshold(sampleCounts, 1000)) + "\n");
			
			writer.flush(); 
		}
		
		 writer.flush();  writer.close();
	}
	
	private static double fractionAboveThreshold(List<Double> list, int threshold ) throws Exception
	{
		double sum =0;
		
		for(  Double d : list )
			if( d >= threshold)
				sum++;
		
		return sum / list.size();
	}
	
	private static double sum(List<Double> list) throws Exception
	{
		int sum = 0;
		
		for(Double i : list)
			sum+=i;
		
		return sum;
	}
}
