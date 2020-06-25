package classExamples;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

public class FastChildExample 
{
	
	private static String getMutatedSeq(String seq, int pos, char c)
	{
		StringBuffer buff = new StringBuffer();
		
		for( int x=0; x < seq.length(); x++)
		{
			if( x== pos)
				buff.append(c);
			else
				buff.append(seq.charAt(x));
		}
		
		return buff.toString();			
	}
	
	
	
	private static char[] ACGT = {'A', 'C','G','T'};
	
	private static class Holder
	{
		int counts;
		boolean used =false;
		
		public Holder(int counts)
		{
			this.counts = counts;
		}
	}
	
	public static void main(String[] args)
	{
		
		LinkedHashMap<String, Holder> candidateMap = new LinkedHashMap<String, Holder>();
		LinkedHashMap<String, List<String>> parentsToChildren = new LinkedHashMap<String,List<String>>();
		
		candidateMap.put( "AAACCCG", new Holder(324244));
		candidateMap.put( "AAATCCG", new Holder(5553));
		candidateMap.put( "AGACCCG", new Holder(2534));
		candidateMap.put( "AAACCAG", new Holder(241));
		candidateMap.put( "AAACCCT", new Holder(53));
		candidateMap.put( "GGGGTTT", new Holder(41));
		candidateMap.put( "GGGGTAT", new Holder(31));
		candidateMap.put( "AGACCCT", new Holder(4));

		for( Iterator<String> i= candidateMap.keySet().iterator(); i.hasNext();   )
		{
			String aSeq = i.next();
			Holder parentHolder = candidateMap.get(aSeq);
			i.remove();
			
			if(! parentHolder.used)
			{
				List<String> innerList = new ArrayList<String>();
				parentsToChildren.put(aSeq, innerList);
					
				for( int x=0; x < aSeq.length(); x++)
				{
					for( char c : ACGT)
					{
						String mutatedString =getMutatedSeq(aSeq, x, c);
							
						if( ! mutatedString.contentEquals(aSeq) )
						{
							Holder childSeq = candidateMap.get(mutatedString);
							
							if( childSeq != null)
							{
								childSeq.used = true;
								innerList.add(mutatedString);
							}
						}			
					}
				}
			}
		}
		
		for(String seq : parentsToChildren.keySet())
		{
			System.out.println("Parent : " + seq);
			
			for(String seq2 : parentsToChildren.get(seq))
				System.out.println("\tchild " + seq2 );
			
			System.out.println("\n");
		}
		
	}
	
}
