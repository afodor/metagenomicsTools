package ruralVsUrban.mostWanted;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import utils.ConfigReader;

public class MostWantedSpreadsheetToConsensusSequence
{
	/*
	 * Most wanted spreadsheet from http://hmpdacc.org/most_wanted/
	 */
	public static void main(String[] args) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(ConfigReader.getChinaDir() + File.separator + 
				"mostWanted" + File.separator + 
				"MW_all.txt"));
		
		reader.readLine();
		
		BufferedWriter writer =new BufferedWriter(new FileWriter(new File(ConfigReader.getChinaDir() + File.separator + 
				"mostWanted" + File.separator + 
				"MW_all.txt")));
		
	}
}
