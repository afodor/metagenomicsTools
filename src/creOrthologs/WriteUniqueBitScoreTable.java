package creOrthologs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import utils.ConfigReader;
import creOrthologs.GetDistinctAlignedOrthologs.Holder;


public class WriteUniqueBitScoreTable
{
	public static void main(String[] args) throws Exception
	{
		List<Holder> list = GetDistinctAlignedOrthologs.getList();
		
		HashSet<String> names = new HashSet<String>();
		
		for(Holder h : list)
		{
			String key = "Line_" + h.lineName;
			names.add(key);
		}
		
		List<Boolean> includeList = new ArrayList<Boolean>();
		
		includeList.add(true);
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getCREOrthologsDir() + File.separator + 
					"bitScoreOrthologsAsColumns.txt")));
		
		String[] topSplits = reader.readLine().split("\t");
		
		int numIncluded=0;
		
		for( int x=1; x < topSplits.length; x++ )
			if( names.contains(topSplits[x]) )
			{
				numIncluded++;
				includeList.add(true);
			}
			else
			{
				includeList.add(false);
			}
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
			ConfigReader.getCREOrthologsDir() + File.separator + 
						"bitScoreOrthologsAsColumnsReducedForPCA.txt")));
		
		writer.write("genome");
		
		for( int x=1; x < topSplits.length; x++)
			if( includeList.get(x))
				writer.write("\t" + topSplits[x]);
		
		writer.write("\n");
		
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			
			writer.write(splits[0]);
			
			if( splits.length != includeList.size())
				throw new Exception("No");
			
			for( int x=1; x < splits.length; x++)
				if( includeList.get(x))
					writer.write("\t" + splits[x]);
			
			writer.write("\n");
		}
		
		writer.flush();  writer.close();
		
		reader.close();
		System.out.println(numIncluded);
		
	}
}
