package test.JackCorrelation;

import java.io.File;

import parsers.OtuWrapper;

public class WriteLoggedFile
{
	public static void main(String[] args) throws Exception
	{
		File rawFile = new File("C:\\Jack_correlation\\pivotedGenus.txt");

		File loggedFile = new File("C:\\Jack_correlation\\pivotedGenus.txt");
		
		OtuWrapper wrapper = new OtuWrapper(rawFile);
		wrapper.writeNormalizedLoggedDataToFile(loggedFile);
		
	}
}
