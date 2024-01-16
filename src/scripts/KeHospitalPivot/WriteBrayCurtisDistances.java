package scripts.KeHospitalPivot;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import parsers.OtuWrapper;

public class WriteBrayCurtisDistances
{
	public static void main(String[] args) throws Exception
	{
		OtuWrapper wrapper = new OtuWrapper("C:\\Ke_Hospital\\bracken_LogNormFilteredMetaRemovedManually.txt");
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File("C:\\Ke_Hospital\\brayCurtisDistances.txt")));
		
		writer.write("xSample\tySample\tbrayCurtisDistance\n");
		
		for( int x=0 ; x < wrapper.getSampleNames().size(); x++)
		{
			for( int y=0; y < wrapper.getSampleNames().size(); y++)
			{
				if( x != y)
				{
					writer.write( wrapper.getSampleNames().get(x) + "\t" + wrapper.getSampleNames().get(y) + "\t" +  
									wrapper.getBrayCurtis(x, y, true) + "\n");	
				}
			}
		}
		
		writer.flush();  writer.close();
	}
}
