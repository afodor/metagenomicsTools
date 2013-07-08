package coPhylog;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.zip.GZIPInputStream;

import utils.ConfigReader;

public class CoPhylogBinaryFileReader
{
	public static HashMap<Long, ContextCount> readBinaryFile(File file) throws Exception
	{
		HashMap<Long, ContextCount> map = new HashMap<>();
		
		DataInputStream in =new DataInputStream( 
				new BufferedInputStream(new GZIPInputStream(new FileInputStream(
					new File(ConfigReader.getBurkholderiaDir()+ File.separator + "results"+
				File.separator + "AS130-2_ATCACG_s_2_1_sequence.txt.gz_CO_PhylogBin.gz")))));
		
		int numRecords = in.readInt();
		System.out.println(numRecords);
		
		for( int x=0; x < numRecords; x++)
		{
			long aLong = in.readLong();
			
			if( map.containsKey(aLong))
				throw new Exception("Duplicate");
			
			ContextCount cc = new ContextCount(in.readByte(), in.readByte(), in.readByte(), in.readByte());
			map.put(aLong,cc);
			
			if( x % 100000==0)
				System.out.println("Reading " + x);
			
		}
		
		return map;
	}
}
