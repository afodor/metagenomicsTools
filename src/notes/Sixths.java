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
		
		chords[0] = 7;
		chords[1] = 4;
		chords[2] = 0;
 		
		for( int x=0; x < NOTES.length; x++)
		{
			List<Integer[]> list= new ArrayList<Integer[]>();
			
			list.add(chords);
			chords = lowerMiddle(chords);
			list.add(chords);
			chords = raiseUpper(chords);
			list.add(chords);
			chords = raiseMiddle(chords);
			list.add(chords);
			chords = raiseUpper(chords);
			list.add(chords);
			chords = raiseMiddle(chords);
			list.add(chords);
			
			
			
			for( int y=0; y < 3; y++)
			{
				for( int z=0; z < list.size(); z++)
				{
					System.out.print( NOTES[ list.get(z)[y]] + "\t" );
				}
				
				System.out.println();
			}

			System.out.println();
		}
		
	}	
	
	private static int lower(int a)
	{
		a = a - 1;
		
		if( a < 0)
			a = NOTES.length - 1;
		
		return a;
	}
	
	private static int raise(int a )
	{
		a = a + 1;
		
		if( a == NOTES.length)
			a = 0;
		
		return a;
	}
	
	private static Integer[] raiseUpper( Integer[] a)
	{
		Integer[] newA = new Integer[3];
		
		newA[0] = raise(a[0]);
		newA[1] = a[1];
		newA[2] = a[2];
		
		return newA;
		
	}
	
	private static Integer[] lowerMiddle( Integer[] a)
	{
		Integer[] newA = new Integer[3];
		
		newA[0] = a[0];
		newA[1] = lower(a[1]);
		newA[2] = a[2];
		
		return newA;
		
	}
	
	private static Integer[] raiseMiddle( Integer[] a)
	{
		Integer[] newA = new Integer[3];
		
		newA[0] = a[0];
		newA[1] = raise(a[1]);
		newA[2] = a[2];
		
		return newA;
		
	}
	
}
