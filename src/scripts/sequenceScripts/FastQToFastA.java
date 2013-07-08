package scripts.sequenceScripts;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashSet;

public class FastQToFastA
{
	public static void main(String[] args) throws Exception
	{
		BufferedReader reader=
			new BufferedReader( new FileReader(
					new File("G:\\RawBurkholderiaSequences\\sequences\\temp.txt")));
		
		BufferedWriter writer=  new BufferedWriter(new FileWriter(new File( 
				"G:\\RawBurkholderiaSequences\\sequences\\tempFasta.txt")));
		
		float numSequences=0;
		long numBases =0;
		HashSet<String> sequences = new HashSet<String>();
		
		for( String firstLine = reader.readLine(); 
					firstLine != null;
						firstLine = reader.readLine())
		{
			if( ! firstLine.startsWith("@"))
				throw new Exception("No");
			
			String seqLine = reader.readLine();
			
			if( ! reader.readLine().startsWith("+") )
				throw new Exception("No");
			
			if( reader.readLine().length() != seqLine.length() )
				throw new Exception("No");
			
			
			if( numSequences %1000 == 0 )
			{
				System.out.println(numSequences + " " + numBases + " " + (numBases/numSequences));
				writer.flush();  
			}
			
			if( ! sequences.contains(seqLine))
			{
				writer.write( ">" + firstLine + "\n");
				writer.write(  seqLine + "\n" );
				numSequences++;
				numBases += seqLine.length();
				sequences.add(seqLine);
			}
		}
		
		System.out.println(numSequences + " " + numBases+ " " + (numBases/numSequences));
		writer.flush();  writer.close();
	}
}
