package parsers;

import java.io.BufferedReader;
import java.io.BufferedWriter;

public class FastQ
{
	private String sequence;
	private String qualScore;
	private String checkLine;
	private String header;
	
	public FastQ(String header, String sequence, String qualScore) throws Exception
	{
		this.header = header;
		this.sequence = sequence;
		this.qualScore = qualScore;
		
		if( qualScore.length() != sequence.length())
			throw new Exception("Unequal length " +qualScore.length() + " " + sequence.length());
	}
	

	public FastQ(String header, String sequence, String checkLine, String qualScore) throws Exception
	{
		this(header,sequence,qualScore);
		this.checkLine = checkLine;
		
		if( ! this.checkLine.startsWith("+"))
			throw new Exception("parsing error " + checkLine);
	}
	
	public void setSequence(String sequence)
	{
		this.sequence = sequence;
	}

	public void setQualScore(String qualScore)
	{
		this.qualScore = qualScore;
	}

	public String getCheckLine()
	{
		return checkLine;
	}
	
	public void setHeader(String header)
	{
		this.header = header;
	}

	public String getSequence()
	{
		return sequence;
	}

	public String getQualScore()
	{
		return qualScore;
	}

	public String getHeader()
	{
		return header;
	}

	public void writeToFile(BufferedWriter writer) throws Exception
	{
		writer.write(header + "\n");
		writer.write(sequence + "\n");
		writer.write(checkLine + "\n");
		writer.write(qualScore + "\n");
	}
	
	public static FastQ readOneOrNull(BufferedReader reader) throws Exception
	{
		String header= reader.readLine();
		
		if( header == null)
			return null;
		
		String sequence = reader.readLine();
		
		String checkLine = reader.readLine();
		if( ! checkLine.startsWith("+"))
			throw new Exception("Parsing error " + checkLine);
		
		
		String qualScore = reader.readLine();
		
		return new FastQ(header, sequence, checkLine, qualScore);
	}
	
}
