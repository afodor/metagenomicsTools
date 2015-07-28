package notes;

import java.util.ArrayList;
import java.util.List;

public class Sixths
{
	public static final String[] NOTES = { "C", "Db", "D", "Eb", "E", "F", "Gb", "G", "Ab",
																"A", "Bb", "B"};
	
	public static void main(String[] args)
	{
		Integer[] chords =  new Integer[3];
		
		chords[0] = 0;
		chords[1] = 4;
		chords[2] = 7;
 		
		for( int x=0; x < NOTES.length; x++)
		{
			List<Integer[]> list= new ArrayList<Integer[]>();
			
			list.add(chords);
			chords = lowerMiddle(chords);
			
			list.add(chords);
			
			for( int y=0; y < 3; y++)
			{
				for( int z=0; z < list.size(); z++)
				{
					System.out.print( NOTES[ list.get(z)[y]] + "\t" );
				}
				
				System.out.println();
			}
		}
		
		System.out.println();
	}	
	
	private static int lower(int a)
	{
		a = a - 1;
		
		if( a < 0)
			a = NOTES.length - 1;
		
		return a;
	}
	
	private static Integer[] lowerMiddle( Integer[] a)
	{
		Integer[] newA = new Integer[3];
		
		newA[0] = a[0];
		newA[1] = lower(a[1]);
		newA[2] = a[2];
		
		return newA;
		
	}
	
}
