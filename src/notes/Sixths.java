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
			List<String[]> prefix = new ArrayList<String[]>();
			
			String[] s = {"   ", "   " , "   "};
			prefix.add(s);
			list.add(chords);
			
			String[] s2 = {"   ", "" + (char)0x00A1 + "  ", "   "};
			prefix.add(s2);
			chords = lowerMiddle(chords);
			
			list.add(chords);
			
			String[] s3 = {"" + (char)94 + "  " , "   " , "   "};
			chords = raiseUpper(chords);
			prefix.add(s3);
			
			String[] s4 = {"   " , "" + (char)94 + "  " , "   " };
			prefix.add(s4);
			list.add(chords);
			chords = raiseMiddle(chords);
			
			
			list.add(chords);
			chords = raiseUpper(chords);
			prefix.add(s3);
			
			list.add(chords);
			chords = raiseMiddle(chords);
			prefix.add(s4);
			
			list.add(chords);
			String[] s7 = {(char)0x00A1+"  ", "   ", "   " };
			chords = lowerUpper(chords);
			prefix.add(s7);
			
			list.add(chords);
			
			String[] s8 = {"   " , "   ", "" + (char)94 + "  " };
			
			prefix.add(s8);
			chords = raiseLower(chords);
			list.add(chords);
			
			
			for( int y=0; y < 3; y++)
			{
				for( int z=0; z < list.size(); z++)
				{
					System.out.print( prefix.get(z)[y] +  NOTES[ list.get(z)[y]] + "\t" );
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
	
	
	private static Integer[] lowerUpper( Integer[] a)
	{
		Integer[] newA = new Integer[3];
		
		newA[0] = lower(a[0]);
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
	
	private static Integer[] raiseLower( Integer[] a)
	{
		Integer[] newA = new Integer[3];
		
		newA[0] = a[0];
		newA[1] = a[1];
		newA[2] = raise( a[2]);
		
		return newA;
		
	}
	
}
