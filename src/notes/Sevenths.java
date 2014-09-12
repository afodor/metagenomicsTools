package notes;

public class Sevenths
{
	public static final String[] NOTES = { "C", "Db", "D", "Eb", "E", "F", "Gb", "G", "Ab",
																"A", "Bb"};
	
	public static void main(String[] args)
	{
		

		int[] diminished = new int[4];
		diminished[0] = 0;
		diminished[1] =3;
		diminished[2] = 6;
		diminished[3] = 8;
		
		for(int x=0; x < NOTES.length; x++)
		{
			int[] halfDiminished = new int[4];
			halfDiminished[0] = diminished[0];
			halfDiminished[1] = diminished[1];
			halfDiminished[2] = diminished[2];
			halfDiminished[3] = addOne(diminished[3]);
			
			int[] minor = new int[4];
			minor[0] = halfDiminished[0];
			minor[1] = halfDiminished[1];
			minor[2] = addOne( halfDiminished[2]);
			minor[3] = halfDiminished[3];
			
			int[] major = new int[4];
			major[0] = minor[0];
			major[1] = addOne(minor[1]);
			major[2] =  minor[2];
			major[3] = minor[3];
			
			printResults(diminished, halfDiminished, minor, major);
		}
		
	}
	
	private static void printResults( int[] diminished, int[] halfDiminished, int [] minor,
				int [] major)
	{
		System.out.println( " " + NOTES[ diminished[3]] + "  " + (char)(94) +  "\t  " + NOTES[halfDiminished[3]] + "\t\t " + NOTES[minor[3]] + "\t\t" + NOTES[major[3]]);
		System.out.println( " " + NOTES[ diminished[2]] + "\t  " + NOTES[halfDiminished[2]]+ "\t\t " + NOTES[minor[2]]  + "\t" + (char)(94)+ "\t" + NOTES[ major[2]] );
		System.out.println( " " + NOTES[ diminished[1]] + "\t  " + NOTES[halfDiminished[1]] + "   " + (char)(94) + "\t " + NOTES[minor[1]] + "\t\t" + NOTES[major[1]] );
		System.out.println( " " + NOTES[ diminished[0]] + "\t  " + NOTES[halfDiminished[0]] + "\t\t " + NOTES[minor[0]] + "\t\t" + NOTES[major[0]]);
		//0x00A1
		System.out.println("dim\thalfDim\t\tminor\t\tmajor\t");
		System.out.println("\n\n");
	}
	
	private static int addOne(int i)
	{
		i++;
		
		if( i > NOTES.length -1)
			i =0;
		
		return i;
	}
	
	private static int subtractOne(int i)
	{
		i--;
		
		if( i < 0)
			i = NOTES.length -1;
		
		return i;
	}
	
}
