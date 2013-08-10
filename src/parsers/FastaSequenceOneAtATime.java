package parsers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;


public class FastaSequenceOneAtATime
{
	private BufferedReader reader;
	private String nextLine;
	
	public FastaSequenceOneAtATime(String filePath) throws Exception
	{
		this(new File(filePath),filePath.toLowerCase().endsWith("gz"));
	}
	
	public FastaSequenceOneAtATime(File file) throws Exception
	{
		this(file,false);
	}
	
	public FastaSequenceOneAtATime(String filePath, boolean gzipped) throws Exception
	{
		this(new File(filePath), gzipped);
	}
	
	public FastaSequenceOneAtATime(File file, boolean gzipped) throws Exception
	{
		reader = 
			gzipped ?
				new BufferedReader(new InputStreamReader( 
						new GZIPInputStream( new FileInputStream( file) ) ))
				:
					new BufferedReader(new FileReader(file));
		
		nextLine= reader.readLine();
		
		//consume blank lines at the top of the file
		while( nextLine != null && nextLine.trim().length() == 0  )
			nextLine= reader.readLine();

	}
	
	public void close() throws Exception
	{
		reader.close();
	}

	public FastaSequence getNextSequence() throws Exception
	{	
		if(nextLine == null)
		{
			return null;
		}
					
		FastaSequence fs = new FastaSequence();
		fs.setHeader( nextLine);
			
		nextLine = reader.readLine();
		
		StringBuffer buff = new StringBuffer();
		while ( nextLine != null && ! nextLine.startsWith(">") ) 
		{
			buff.append( FastaSequence.stripWhiteSpace( nextLine ) );
			nextLine = reader.readLine();
		}	
		
		fs.setSequence(buff);
			
		//consume blanks that might occur after the ">"
		while( nextLine != null && nextLine.trim().length() == 0  )
			nextLine= reader.readLine();
		
		return fs;
	}
}
