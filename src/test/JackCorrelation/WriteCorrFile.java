package test.JackCorrelation;

import java.io.File;

import scripts.JamesCorCheck_Jan2022.CheckCorrs;

public class WriteCorrFile
{
	public static void main(String[] args) throws Exception
	{
		//File loggedFile = new File("C:\\Jack_correlation\\pivotedGenusLogged.txt");
		//File corrFile = new File("C:\\Jack_correlation\\corrFileLogged.txt");
		
		//CheckCorrs.writeCorrFile(loggedFile, corrFile, 1, "\t");
		
		File rawFile = new File("C:\\Jack_correlation\\pivotedGenus.txt");
		File corrFileRaw = new File("C:\\Jack_correlation\\corrFileRaw.txt");
				
		CheckCorrs.writeCorrFile(rawFile, corrFileRaw, 1, "\t");
	}
}
