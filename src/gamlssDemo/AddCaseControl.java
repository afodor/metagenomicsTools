package gamlssDemo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashSet;

public class AddCaseControl
{
	private static HashSet<String> getNamesWithCaseControl() throws Exception
	{
		HashSet<String> set = new HashSet<String>();
		
		BufferedReader reader = new BufferedReader(new FileReader(
				new File("c:\\Users\\corei7\\git\\metagenomicsTools\\src\\gamlssDemo\\testData.txt")));
		
		reader.readLine();
		
		for(String s = reader.readLine(); s != null ; s =reader.readLine())
		{
			String[] splits = s.split("\t");
			
			if( set.contains(splits[0]))
				throw new Exception("No");
			
			set.add(splits[0]);
		}
		
		reader.close();
		return set;
	}
	
	public static void main(String[] args) throws Exception
	{
		HashSet<String> set = getNamesWithCaseControl();
		
		BufferedReader reader = new BufferedReader(new FileReader(
				new File(
						"C:\\Users\\corei7\\git\\metagenomicsTools\\src\\gamlssDemo\\genusPivotedTaxaAsColumnsNorm.txt")));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(
			new File("C:\\Users\\corei7\\git\\metagenomicsTools\\src\\gamlssDemo\\genusPivotedTaxaAsColumnsNormCaseContol.txt")));
		
		writer.write(reader.readLine() + "\n");
		
		for(String s= reader.readLine(); s != null ; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			
			String key = splits[0].replace("Tope_", "").replace(".fas", "");
			
			if( set.contains(key+"case"))
			{
				writer.write(key+"case");
				set.remove(key + "case");
			}
			else if (set.contains(key+"control") )
			{
				writer.write(key+"control");
				set.remove(key + "control");
			}
			
			for( int x=1; x < splits.length; x++)
				writer.write("\t" + splits[x]);
			
			writer.write("\n");
		}
		
		for(String s : set )
			System.out.println(s);
		
		if( ! set.isEmpty())
			throw new Exception("No " + set.size());
		
		writer.flush();  writer.close();
	}
}
