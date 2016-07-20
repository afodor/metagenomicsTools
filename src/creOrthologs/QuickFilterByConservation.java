package creOrthologs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Random;

public class QuickFilterByConservation
{
	public static void main(String[] args) throws Exception
	{
		Random r = new Random(32345);
		
		int x=0;
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				"C:\\biolockJProjects\\resistantAnnotation\\carolinaVsResistant_kneu.txt")));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				"C:\\biolockJProjects\\resistantAnnotation\\carolinaVsResistant_kneuFiltered.txt")));
		
		writer.write("pValue" + "\t" + "ratioConserved" + "\n");
		reader.readLine();
		
		for(String s= reader.readLine();  s != null; s= reader.readLine())
		{
			String[] splits =s.split("\t");
			
			float val = Float.parseFloat(splits[6]);
			
			if( val >=0.05 && val <=0.95 && r.nextFloat() < 0.15)
				writer.write(splits[5]  + "\t" + splits[6] +  "\n");
			
			x++;
			
			if( x % 100000 == 0)
				System.out.println(x);
		}
		
		writer.flush();  writer.close();
		
		reader.close();
	}
}
