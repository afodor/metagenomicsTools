package parsers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.StringTokenizer;

import utils.ConfigReader;
import utils.ProcessWrapper;

public class FastaSequence implements Comparable<FastaSequence>
{
	public static final String BLAST_SUFFIX = "_BLAST";
	
	private String header;
	private StringBuffer sequence = new StringBuffer();
	
	public void setHeader(String header)
	{
		this.header= header;
	}
	
	public boolean equals(Object obj)
	{
		FastaSequence o = (FastaSequence) obj;
		
		if( ! o.getHeader().equals(getHeader()))
			return false;
		
		if( ! o.getSequence().equals(getSequence()))
			return false;
		
		return true;
	}
	
	public void setSequence(StringBuffer sequence)
	{
		this.sequence = sequence;
	}
	
	public String getHeader()
	{
		return header;
	}
	
	public FastaSequence( String header, String sequence ) throws Exception
	{
		if ( ! header.startsWith(">") ) 
			header = ">" + header;
		
		this.header = header;
		this.sequence = new StringBuffer( sequence);
	}
	

	public static HashMap<String, FastaSequence> getFirstTokenSequenceMap(String filePath)
		throws Exception
		{
			return getFirstTokenSequenceMap(new File(filePath));
		}
	
	
	public static HashMap<String, FastaSequence> getFirstTokenSequenceMap(File file)
		throws Exception
	{
		List<FastaSequence> list = readFastaFile( file);
		
		HashMap<String, FastaSequence> map = new LinkedHashMap<String, FastaSequence>();
		
		for( FastaSequence fs : list)
		{
			String key = new StringTokenizer(fs.getHeader()).nextToken();
			
			if( key.startsWith(">"))
				key = key.substring(1);
			
			if( map.containsKey(key) )
				throw new Exception("Duplicate key " + key);
			
			map.put(key, fs);
		}
		
		return map;
	}
	
	public boolean isOnlyACGT()
	{
		for (char c : this.sequence.toString().toCharArray())
		{
			if ( c != 'A' &&  c != 'C' && c != 'G' && c != 'T')
				return false;
		}
		
		return true;
	}
	
	public String getSecondTokenOfHeader()
	{
		StringTokenizer sToken = new StringTokenizer(header);
		
		sToken.nextToken();
		String s = sToken.nextToken();
		
		return s;
	}
	
	public String getFirstTokenOfHeader()
	{
		StringTokenizer sToken = new StringTokenizer(header);
		
		String s = sToken.nextToken();
		
		if( s.startsWith(">"))
			s = s.substring(1);
		
		return s;
	}
	
	public static HashSet<String> getSeqsAsHashSet( String filePath ) throws Exception
	{
		List<FastaSequence> list= FastaSequence.readFastaFile(filePath);
		HashSet<String> set = new HashSet<String>();
		
		for(FastaSequence fs : list)
			set.add(fs.getSequence());
		
		return set;
	}
	
	/**  Not thread safe even from separate VMs
	 */
	public synchronized File ensureBlastDatabaseForNucleotideSequence() throws Exception
	{
		File inFile = new File( ConfigReader.getBlastDirectory() + File.separator + 
								 "FastaSequenceDatabase" );
		
		if ( inFile.exists() ) 
			inFile.delete();
		
		writeFastaFile(inFile);
			
		String[] args = new String[5];
		args[0] = ConfigReader.getBlastDirectory() + File.separator + "formatdb";
		args[1] = "-i";
		args[2] = inFile.getAbsolutePath();
		args[3] = "-p";
		args[4] = "f";
		
		for ( int x=0; x< args.length; x++ ) 
		{
			System.out.print( args[x] + " " );
		}
		
		System.out.println();
		
		new ProcessWrapper(args);
		
		File checkFile = new File( ConfigReader.getBlastDirectory() + File.separator + 
								"FastaSequenceDatabase.nsq");
								
		if ( ! checkFile.exists() ) 
			throw new Exception("Blast error.  Could not find " + checkFile.getAbsolutePath() );
		
		return inFile;
	}
	

	public static String stripWhiteSpace( String inString ) throws Exception
	{
		StringBuffer buff = new StringBuffer();
		
		for ( int x=0; x< inString.length(); x++ ) 
		{
			char c = inString.charAt(x);
			
			if ( ! Character.isWhitespace(c) ) 
				buff.append(c);
		}
		
		return buff.toString();
	}
	
	public FastaSequence()
	{
		
	}

	public String getSequence()
	{
		return sequence.toString();
	}
	
	/* 
	 * Does not hold the entire fileToParse in RAM, so this is good for large FASTA files.
	 * Will return only the FastaSequences in matchingFirstToken whose header matches
	 * something in that set (not including the ">")
	 */
	public static List<FastaSequence> getOnlyMatching( File fileToParse,  
			HashSet<String> matchingFirstTokens) throws Exception
	{
		List<FastaSequence> returnList = new ArrayList<FastaSequence>();
		
		BufferedReader reader = new BufferedReader( new FileReader( 
				fileToParse));
		
		String nextLine = reader.readLine();
		
		while ( nextLine != null ) 
		{
			String originalHeader = nextLine;
			String header = originalHeader;
			
			if( ! header.startsWith(">"))
				throw new Exception("Parsing error " + header);
			
			header = new StringTokenizer(header).nextToken();
			header = header.substring(1);
			
			nextLine = reader.readLine();
			
			StringBuffer buff = new StringBuffer();
			
			while ( nextLine != null && ! nextLine.startsWith(">") ) 
			{
				buff.append(	FastaSequence.stripWhiteSpace( nextLine ) );
				nextLine = reader.readLine();
			}			
			
			if( matchingFirstTokens.contains(header))
			{
				returnList.add(new FastaSequence(originalHeader, 
						buff.toString()));
			}
		}
		
		return returnList;
	}
	
	public static String getFasta( FastaSequence seq1, FastaSequence seq2 ) throws Exception
	{
		StringBuffer buff = new StringBuffer();
		
		buff.append(seq1.header + "\n");
		buff.append(seq1.sequence + "\n");
		buff.append(seq2.header + "\n" );
		buff.append(seq2.sequence + "\n" );
		
		return buff.toString();
	}
	
	public static FastaSequence getFirstSequence(String filePath) throws Exception
	{
	    return getFirstSequence(new File(filePath));
	}
	
	public static FastaSequence getFirstSequence(File file) throws Exception
	{
	    return (FastaSequence) readFastaFile(file).get(0);
	}
	
	public static List<FastaSequence> readFastaFile(String filePath) throws Exception
	{
	    return readFastaFile(new File(filePath));
	}
	
	public static HashMap<String, String> getHeaderFirstTokenMap(String filepath) throws Exception
	{
		return getHeaderFirstTokenMap(new File(filepath));
	}
	
	public static HashMap<String, String> getHeaderFirstTokenMap(File file) throws Exception
	{
		HashMap<String, String> returnMap = new HashMap<String,String>();
		
		BufferedReader reader = new BufferedReader( new FileReader( 
				file));
		
		String nextLine = reader.readLine();
		
		while ( nextLine != null ) 
		{
			String originalHeader = nextLine;
			String header = originalHeader;
			
			header = new StringTokenizer(header).nextToken();
			
			if( header.startsWith(">"))
				header = header.substring(1);
			
			if( returnMap.containsKey(header))
				throw new Exception("Error!  Duplicate header");
			
			returnMap.put(header, originalHeader);
			//System.out.println(header);
			
			nextLine = reader.readLine();
			
			while ( nextLine != null && ! nextLine.startsWith(">") ) 
			{
				nextLine = reader.readLine();
			}			
			
		}
		
		return returnMap;
		
	}
	
	public static HashMap<String, Integer> getCountMap(List<File> fastaFiles) throws Exception
	{
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		
		for( File f : fastaFiles)
		{
			BufferedReader reader = new BufferedReader(new FileReader(f));
			
			String nextLine = reader.readLine();
			
			while ( nextLine != null ) 
			{
				String header = nextLine;
				header = new StringTokenizer(header).nextToken();
				if( header.startsWith(">"))
					header = header.substring(1);
				
				if( map.keySet().contains(header) )
					throw new Exception("Duplicate key " + header);
				
				nextLine = reader.readLine();
				
				int count = 0;
				
				while ( nextLine != null && ! nextLine.startsWith(">") ) 
				{
					count+=	stripWhiteSpace( nextLine ).length();
					nextLine = reader.readLine();
				}
				
				map.put(header, count);
			}
			
			reader.close();
			
		}
		
		return map;
	}
	
	public static List<FastaSequence> readFastaFile( File file) throws Exception
	{
		return readFastaFile(file, -1);
	}
	
	@SuppressWarnings("unused")
	public static List<FastaSequence> readFastaFile( File file, int limit ) throws Exception
	{
		List<FastaSequence> list = new ArrayList<FastaSequence>();
		
		BufferedReader reader = new BufferedReader( new FileReader( file ));
		
		String nextLine = reader.readLine();
		
		//consume blank lines at the top of the file
		while( nextLine != null && nextLine.trim().length() == 0  )
			nextLine= reader.readLine();
		
		while ( nextLine != null  ) 
		{
			if( limit != -1 && list.size() >= limit)
			{
				reader.close();
				return list;
			}
				
			FastaSequence fs = new FastaSequence();
			list.add(fs);
			fs.header = nextLine;
			
			nextLine = reader.readLine();
			
			while ( nextLine != null && ! nextLine.startsWith(">") ) 
			{
				fs.sequence.append(	stripWhiteSpace( nextLine ) );
				nextLine = reader.readLine();
			}	
			
			//consume blanks that might occur after the ">"
			while( nextLine != null && nextLine.trim().length() == 0  )
				nextLine= reader.readLine();
			
		}
		
		// There appears to be some kind of bug between the BufferedReader and
		// Linux that, amazingly, seems to sometimes cause this Exception to fire!
		// the two lines of code above that consume blank lines prevent this Exception.
		// this, however,makes no sense to me and is, I think, some kind of subtle
		// threading bug in BufferedReader.readLine(..) that I haven't had the time to track down...
		// with this line, we simply fail the parser if this bug ever rears its head again..
		if( nextLine != null)
			throw new Exception("Logic exception.  Parsing failed");
		
		reader.close();
		return list;
	}
	
	public static List<FastaSequence> readFastaFileForceToUpper( File file ) throws Exception
	{
		List<FastaSequence> list = new ArrayList<FastaSequence>();
		
		BufferedReader reader = new BufferedReader( new FileReader( file ));
		
		String nextLine = reader.readLine();
		
		while ( nextLine != null ) 
		{
			FastaSequence fs = new FastaSequence();
			list.add(fs);
			fs.header = nextLine;
			
			nextLine = reader.readLine();
			
			while ( nextLine != null && ! nextLine.startsWith(">") ) 
			{
				fs.sequence.append(	stripWhiteSpace( nextLine ).toUpperCase() );
				nextLine = reader.readLine();
			}				
		}
		
		return list;
	}
	
	/*
	 *   Does not check for membership in set of { A,C,G,T}
	 *   
	 *   Returns the number of {G,C} over all other characters.
	 */
	public float getGCRatio() throws Exception
	{
		float numGC = 0;
		
		String testString = this.getSequence().toUpperCase();
		
		for( int x=0; x < testString.length(); x++ )
		{
			char c = testString.charAt(x);
			
			if( c=='C' || c=='G' )
				numGC++;
		}
		
		return numGC / testString.length();
	}
	
	public float getRatioValidForDNA() throws Exception
	{
		float numValid = 0;
		
		String testString = this.getSequence().toUpperCase();
		
		for( int x=0; x < testString.length(); x++ )
		{
			char c = testString.charAt(x);
			
			if( c== 'A' || c=='C' || c=='G' || c=='T' )
				numValid++;
		}
		
		return numValid / testString.length();
	}
	
	public void writeFastaFile(File file) throws Exception 
	{
		BufferedWriter writer = new BufferedWriter( new FileWriter( file ));
		
		writer.write(this.header + "\n");
		writer.write(this.sequence + "\n");
		
		writer.flush();  writer.close();
	}
	
	/**  Sort by length
	 */
	public int compareTo(FastaSequence otherFasta)
	{
		
		if ( this.sequence.length() > otherFasta.sequence.length() ) 
			return -1;
			
		if ( this.sequence.length() < otherFasta.sequence.length() ) 
			return 1;
			
		return 0;	
	}
	
	public File ensureBlastDatabase(
	        String sequenceName,
	        boolean isProtein) 
						throws Exception
	{
		File inFile = new File( ConfigReader.getBlastDirectory() 
		        					+ File.separator + 
									System.currentTimeMillis()
									+ BLAST_SUFFIX);
		if ( inFile.exists() ) 
				inFile.delete();
		
		FastaSequence fs = new FastaSequence( 
		        ">" + this.getHeader(), 
		        this.getSequence() );
		
		fs.writeFastaFile(inFile);
			
		String[] args = new String[5];
		args[0] = ConfigReader.getBlastDirectory() + File.separator + "formatdb";
		args[1] = "-i";
		args[2] = inFile.getAbsolutePath();
		args[3] = "-p";
		
		if (  isProtein )
			args[4] = "t";
		else
			args[4] = "f";
		
		for ( int x=0; x< args.length; x++ ) 
		{
			System.out.print( args[x] + " " );
		}
		
		System.out.println();
		
		new ProcessWrapper(args);
								
		return inFile;
	}
	
}
