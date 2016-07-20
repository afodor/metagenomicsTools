package adenomasRelease;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import utils.ConfigReader;

public class SeparateColumns
{
	public static void main(String[] args) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(new File(
			ConfigReader.getAdenomasReleaseDir() + File.separator + 
				"caseControl.txt")));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				ConfigReader.getAdenomasReleaseDir() + File.separator + 
				"caseControlTwoColumn.txt")));
		
		writer.write("sample\tcaseContol\n");
		reader.readLine();
		
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			if( s.endsWith("case"))
			{
				writer.write(s.replace("case","") + "\tcase\n");
			}
			else if ( s.endsWith("control"))
			{
				writer.write(s.replace("control","") + "\tcontrol\n");
			}
			else
				throw new Exception("No");
		}
		
		writer.flush();  writer.close();
	}
}
