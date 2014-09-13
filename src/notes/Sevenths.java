package notes;

public class Sevenths
{
	public static final String[] NOTES = { "C", "Db", "D", "Eb", "E", "F", "Gb", "G", "Ab",
																"A", "Bb", "B"};
	
	public static void main(String[] args)
	{
		

		int[] diminished = new int[4];
		diminished[0] = 0;
		diminished[1] =3;
		diminished[2] = 6;
		diminished[3] = 9;
		
		for(int x=0; x <= NOTES.length; x++)
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
			
			int[] dom7 = new int[4];
			dom7[0] = minor[0];
			dom7[1] = addOne(minor[1]);
			dom7[2] =  minor[2];
			dom7[3] = minor[3];
			
			int[] major7 = new int[4];
			major7[0] = dom7[0];
			major7[1] = dom7[1];
			major7[2] = dom7[2];
			major7[3] = addOne( dom7[3]);
			
			
			printResults(diminished, halfDiminished, minor, dom7, major7);
			
			diminished[0] = addOne(major7[0]);
			diminished[1] = major7[1];	
			diminished[2] = major7[2];
			diminished[3] = subtractOne(major7[3]);
		}
		
	}
	
	private static void printResults( int[] diminished, int[] halfDiminished, int [] minor,
				int [] dom7, int [] major7)
	{
		System.out.println( " " + NOTES[ diminished[3]] + "\t" + (char)(94) +  "\t  " + NOTES[halfDiminished[3]] + "\t\t " + NOTES[minor[3]] + "\t\t" + NOTES[dom7[3]]
								+ "\t" + (char)94 + "\t" +  NOTES[major7[3]] + "\t" + (char)0x00A1);
		
		System.out.println( " " + NOTES[ diminished[2]] + "\t\t  " 
		+ NOTES[halfDiminished[2]]+ "\t" + (char)94 + "\t " + NOTES[minor[2]]  + "\t\t" + NOTES[ dom7[2]] 
				+ "\t\t" +  NOTES[major7[2]]);
		System.out.println( " " + NOTES[ diminished[1]] + "\t\t  " + NOTES[halfDiminished[1]] 
				+ "\t\t " + NOTES[minor[1]] + "\t"  + (char)(94)+ "\t" + NOTES[dom7[1]] 
				 + "\t\t" +  NOTES[major7[1]]);
		System.out.println( " " + NOTES[ diminished[0]] + "\t\t  " + NOTES[halfDiminished[0]] + "\t\t " + NOTES[minor[0]] + "\t\t"
				 + NOTES[dom7[0]]+ "\t\t" +  NOTES[major7[0]] + "\t" + (char)94);
		//0x00A1
		System.out.println("dim\t\thalfDim\t\tminor\t\tdom\t\tmajor");
		System.out.println("\n");
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
