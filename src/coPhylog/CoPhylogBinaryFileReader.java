package coPhylog;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.zip.GZIPInputStream;

import utils.ConfigReader;

public class CoPhylogBinaryFileReader
{
	public static void main(String[] args) throws Exception
	{
		DataInputStream in =new DataInputStream( 
				new BufferedInputStream(new GZIPInputStream(new FileInputStream(
					new File(ConfigReader.getBurkholderiaDir()+ File.separator + "results"+
				File.separator + "AS130-2_ATCACG_s_2_1_sequence.txt.gz_CO_PhylogBin.gz")))));
		
		
		long aLong = in.readLong();
		
		byte numA = in.readByte();
		byte numC = in.readByte();
		byte numG = in.readByte();
		byte numT = in.readByte();
		
		System.out.println( aLong + "\t" + (numA+128) + "\t" + (numC+128)  + "\t" +  (numG+128) + "\t" + (numT+128)   );
		
		
	}
}
