package scripts.KeHospitalPivot;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import parsers.OtuWrapper;
import utils.Avevar;

public class MergeMetadata
{
	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception
	{
		OtuWrapper wrapper = new OtuWrapper("C:\\Ke_Hospital\\brackenTransposed.txt");
		
		List<Boolean> keepList = new ArrayList<>();
		
		for( int y=0; y <  wrapper.getOtuNames().size(); y++)
		{
			List<Double> innerList = new ArrayList<>();
			
			for( int x=0; x < wrapper.getSampleNames().size(); x++)
			{
				innerList.add(wrapper.getDataPointsNormalizedThenLogged().get(x).get(y));
			}
			
			double aveVal = new Avevar(innerList).getAve();
			
			if( aveVal >= 2)
				keepList.add(true);
			else
				keepList.add(false);
		}
		
		System.out.println("Keep list size " + keepList.size() + " Number of taxa " + wrapper.getOtuNames().size());
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File("C:\\Ke_Hospital\\bracken_LogNormPlusMeta.txt")));
		
		HashMap<String, MetaMapFileLine> map = MetaMapFileLine.getMetaMap();
		
		writer.write("sample\tpatientID\ttimepoint\tpatientInOut\tdonor\tbin\treadDepth\tshannonDiversity");
		
		for( int x=0; x < wrapper.getOtuNames().size(); x++)
			if( keepList.get(x))
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
				writer.write("\t" + mfl.getBin());
				writer.write("\t" + wrapper.getCountsForSample(x) + "\t" +  wrapper.getShannonEntropy(x));
				
				for( int y=0; y < wrapper.getOtuNames().size(); y++)
					if( keepList.get(y))
						writer.write("\t" + wrapper.getDataPointsNormalizedThenLogged().get(x).get(y));
				
				writer.write("\n");
			}
				
			
		}

		writer.flush();  writer.close();
	}
}
