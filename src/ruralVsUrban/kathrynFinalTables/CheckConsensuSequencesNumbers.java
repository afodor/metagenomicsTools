package ruralVsUrban.kathrynFinalTables;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import utils.ConfigReader;

public class CheckConsensuSequencesNumbers
{

	public static void main(String[] args) throws Exception
	{
		HashSet<Integer> set = new HashSet<Integer>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getChinaDir() + File.separator + 
				"Kathryn_update_NCBI_MostWanted" + File.separator + "otuModel_pValues_otu.txt"
				)));
		
		reader.readLine();
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			if( ! s.startsWith("\"unRarifiedRichness") && ! s.startsWith("\"shannon") )
			{

				int anInt= Integer.parseInt(s.split("\t")[0].replaceAll("X" , "").replaceAll("\"", ""));
				
				if( set.contains(anInt))
					throw new Exception("No");
				
				set.add(anInt);

			}
		}
		
		System.out.println(set.size());
		
		List<Integer> list =new ArrayList<Integer>(set);
		
		Collections.sort(list);
		
		
		for( int x=0; x < list.size() -1 ;x++)
		{
			if( list.get(x+1) - list.get(x) != 1)
				System.out.println("Gap " + list.get(x) + " " + list.get(x+1));
		}
		
		reader.close();
	}
	
}
