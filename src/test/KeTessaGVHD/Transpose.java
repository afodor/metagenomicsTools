package test.KeTessaGVHD;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SpinnerListModel;

public class Transpose
{
	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(new File("C:\\ke_tessa_test\\GVHDProject-main\\CountsTables\\bracken_species_reads.txt")));
		
		List<List<String>> list = new ArrayList<>();
		
		int length =-1;
		
		for(String s = reader.readLine(); s != null; s=reader.readLine())
		{
			List<String> innerList = new ArrayList<>();
			
			String[] splits = s.split("\t");
			
			for( String s2 : splits)
				innerList.add(s2);
			
			if( length == -1)
				length = innerList.size();
			
			if( length != innerList.size())
				throw new Exception("Parsing error");
			
			list.add(innerList);
		}
		
		reader.close();
		System.out.println(length);
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File("C:\\ke_tessa_test\\GVHDProject-main\\CountsTables\\bracken_species_readsTransposed.txt")));
		
		for( int x=0; x < length; x++)
		{	
			writer.write(list.get(0).get(x));
			
			for( int y=1; y < list.size(); y++ )
			{
				writer.write("\t" + list.get(y).get(x));
			}
			
			writer.write("\n");
		}
		
		writer.flush();  writer.close();
	}
}
