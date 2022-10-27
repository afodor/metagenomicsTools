package scripts.KePivot;

import java.io.File;

import parsers.OtuWrapper;

public class AddMetadata
{
	public static void main(String[] args) throws Exception
	{
		OtuWrapper wrapper = new OtuWrapper("C:\\kePivot\\genusAsColumns.txt");
		
		File logNormFile = new File("C:\\kePivot\\genusAsColumnsLogNorm.txt");
		
		wrapper.writeNormalizedLoggedDataToFile(logNormFile);
	}
}
