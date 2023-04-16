package test.KeTessaGVHD;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class AddMeta
{
	public static void main(String[] args) throws Exception
	{
		File inFile = new File("C:\\ke_tessa_test\\GVHDProject-main\\CountsTables\\bracken_Genus_Transposed.txt");
		List<Boolean> list = getZeroColList(inFile);
		
		for(int x=0; x < list.size() ; x++ )
			System.out.println( x + " "+ list.get(x));
	}
	
	public static List<Boolean> getZeroColList(File file) throws Exception
	{
		List<Boolean> isZeroCol = new ArrayList<>();
		
		BufferedReader reader = new BufferedReader(new FileReader(file));
		
		String[] topLine= reader.readLine().split("\t");
		
		isZeroCol.add(false);
		
		for( int x=1; x < topLine.length; x++) 
			isZeroCol.add(true);
		
		for(String s= reader.readLine(); s != null; s = reader.readLine())
		{
			String[] splits = s.split("\t");
			
			for(int x=1; x < splits.length; x++)
				if(Integer.parseInt(splits[x]) >0)
					isZeroCol.set(x, false);
		}
		
		reader.close();
		return isZeroCol;
	}
}
