package scripts.KeHospitalPivot;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import parsers.OtuWrapper;

public class MergeMetadata
{
	public static void main(String[] args) throws Exception
	{
		OtuWrapper wrapper = new OtuWrapper("C:\\Ke_Hospital\\genus_transposed.txt");
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File("C:\\Ke_Hospital\\genus_LogNormPlusMeta.txt")));
		
		writer.write("sample\tpatientInOut\tdonor");
		
		for( int x=0; x < wrapper.getOtuNames().size(); x++)
			writer.write("\t" + wrapper.getOtuNames().get(x));
		
		writer.write("\n");
		
		
		
		writer.flush();  writer.close();
	}
	
	
}
