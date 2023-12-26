package scripts.KeHospitalPivot;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;

import parsers.OtuWrapper;

public class MergeMetadata
{
	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception
	{
		OtuWrapper wrapper = new OtuWrapper("C:\\Ke_Hospital\\genus_transposed.txt");
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File("C:\\Ke_Hospital\\genus_LogNormPlusMeta.txt")));
		
		HashMap<String, MetaMapFileLine> map = MetaMapFileLine.getMetaMap();
		
		writer.write("sample\tpatientID\ttimepoint\tpatientInOut\tdonor\treadDepth\tshannonDiversity");
		
		for( int x=0; x < wrapper.getOtuNames().size(); x++)
			writer.write("\t" + wrapper.getOtuNames().get(x));
		
		writer.write("\n");
		
		for( int x=0; x < wrapper.getSampleNames().size(); x++)
		{
			MetaMapFileLine mfl = map.get(wrapper.getSampleNames().get(x));
			
			if( mfl == null)
			{
				System.out.println("Could not find " + wrapper.getSampleNames().get(x));
			}
			else
			{
				writer.write( wrapper.getSampleNames().get(x));
				
				System.out.println(wrapper.getSampleNames().get(x));
				
				writer.write("\t" + mfl.getPatientID() + "\t" + mfl.getTimepoint() + "\t" + mfl.getPatientInOut() + "\t" + mfl.getDonor());
				writer.write("\t" + wrapper.getCountsForSample(x) + "\t" +  wrapper.getShannonEntropy(x));
				
				for( int y=0; y < wrapper.getOtuNames().size(); y++)
					writer.write("\t" + wrapper.getDataPointsNormalizedThenLogged().get(x).get(y));
				
				writer.write("\n");
			}
				
			
		}
		
		writer.flush();  writer.close();
	}
}
