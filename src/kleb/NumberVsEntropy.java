package kleb;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import parsers.FastaSequence;
import parsers.FastaSequenceOneAtATime;
import utils.ConfigReader;

public class NumberVsEntropy
{
	private static int A_POS=0;
	private static int C_POS=1;
	private static int G_POS=2;
	private static int T_POS=3;
	
	/*
	 * Run the main method in this.
	 * 
	 *  The key is the position number in the alignment.
	 *  The value is the number of total SNP chagnes in the entire column.
	 *  (This map is sparse so perfectly conserved columns are not present).
	 */
	public static HashMap<Integer, Integer> getPositionVsNumChangesMap(  ) throws Exception
	{
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(ConfigReader.getKlebDir() + File.separator+
				"setOf48" + File.separator + "entropyVsNumOut.txt")));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			String[] splits = s.split("\t");
			Integer key = Integer.parseInt(splits[0]);
			
			if( map.containsKey(key))
				throw new Exception("Duplicate");
			
			map.put(key, Integer.parseInt( splits[6]));
		}
		
		reader.close();
		return map;
	}
	
	public static void main(String[] args) throws Exception
	{
		List<String> list = getSequences();
		int length = list.get(0).length();
		
		for(String s : list)
			if( s.length() != length)
				throw new Exception("No");
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(ConfigReader.getKlebDir() + File.separator+
				"setOf48" + File.separator + "entropyVsNumOut.txt")));
		writer.write("position\tnumA\tnumC\tnumG\tnumT\tshannonEntropy\tnumChanges\n");
		
		for(int x=0; x < length; x++)
		{

			int[] a = getAsArray(list, x);
			
			int numChanges = numChanges(a);
			
			if(numChanges > 0 )
			{
				writer.write((x+1) + "");
				
				for(int y=0; y < a.length; y++)
					writer.write("\t" + a[y]);
				
				writer.write("\t" + quickShannon(a));
				writer.write("\t" + numChanges + "\n");
				
			}
			
			if( x % 100000 == 0 )
				System.out.println(x);
		}
		
		writer.flush();  writer.close();
	}
	
	private static int numChanges(int[] a) throws Exception
	{
		int[] b = new int[a.length];
		
		for( int x=0; x < a.length; x++)
			b[x] = a[x];
		
		Arrays.sort(b);
		
		int sum=0;
		
		for( int x=0; x < a.length-1; x++)
			sum+= b[x];
		
		return sum;
	}
	
	private static double quickShannon(int[] a) throws Exception
	{
		if( a.length != 4) 
			throw new Exception("Logic error");
		
		double sum =0;
		double numSeqs= 0;
		
		for( int x=0; x < a.length; x++)
			numSeqs += a[x];
		
		for( int x=0; x < a.length; x++)
			if( a[x] > 0 )
			{
				double p = a[x] / numSeqs;
				sum += p * Math.log(p);
			}
		
		return - sum;
	}
	
	private static List<String> getSequences() throws Exception
	{
		FastaSequenceOneAtATime fsoat = new FastaSequenceOneAtATime(ConfigReader.getKlebDir() + File.separator + 
				"setOf48" + File.separator + "reduced_set_of_48.mfa");
		
		
		List<String> returnList = new ArrayList<String>();
		
		for( FastaSequence fs = fsoat.getNextSequence(); fs != null; fs = fsoat.getNextSequence())
		{
			returnList.add(fs.getSequence());
		}
		
		fsoat.close();
		
		return returnList;
		
	}
	
	private static int[] getAsArray(List<String> list, int position)
		throws Exception
	{
		int[] vals = new int[4];
		
		for( String s : list)
		{
			char c = s.charAt(position);
			
			if( c== 'A')
				vals[A_POS]++;
			else if ( c== 'C')
				vals[C_POS]++;
			else if( c == 'G')
				vals[G_POS]++;
			else if ( c== 'T')
				vals[T_POS]++;
			else throw new Exception("Unexpected char " + c );
		}
		
		return vals;
	}
}
