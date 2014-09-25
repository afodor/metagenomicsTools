package scottPilot;

import java.io.File;

import parsers.OtuWrapper;
import utils.ConfigReader;

public class PivotSpreadsheet
{
	public static void main(String[] args) throws Exception
	{
		OtuWrapper.transpose(ConfigReader.getScottPilotDataDir() + File.separator + 
						"taxaAsRowsPilot.txt", 
						ConfigReader.getScottPilotDataDir() + File.separator + 
						"taxaAsColumnsPilot.txt"
						, false);
	}
}
