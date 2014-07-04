/** 
 * Author:  anthony.fodor@gmail.com    
 * This code is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 2
* of the License, or (at your option) any later version,
* provided that any use properly credits the author.
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details at http://www.gnu.org * * */

package parsers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class FastQ
{
	private String sequence;
	private String qualScore;
	private String checkLine;
	private String header;
	
	public static void FastQToFastA(String fastQFilePath, String fastAFilePath)
		throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(fastQFilePath));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				fastAFilePath)));
		
		for(FastQ fq = readOneOrNull(reader); fq != null; fq = readOneOrNull(reader))
		{
			writer.write(">" + fq.header + "\n");
			writer.write(fq.sequence + "\n");
		}
		
		writer.flush();  writer.close();
		
		reader.close();
	}
	
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
	
	@Override
	public String toString()
	{
		return this.header + "\n" + this.sequence + "\n" + this.checkLine + "\n" + this.qualScore + "\n";
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
	
	public static void main(String[] args) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				"D:\\classes\\undergradProgramming_2013\\example.fastq")));
		
		//FastQ fastq = readOneOrNull(reader);
		
		reader.close();
	}
	
}
