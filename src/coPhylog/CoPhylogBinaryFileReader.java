/** 
 * Author:  anthony.fodor@gmail.com    
 * This code is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 2
* of the License, or (at your option) any later version,
* provided that any use properly credits the author.
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details at http://www.gnu.org * * */


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
		return readBinaryFile(file,-1);
	}
	
	
	public static HashMap<Long, ContextCount> readBinaryFile(File file, int maxNum) throws Exception
	{	
		System.out.println("Reading " + file.getAbsolutePath());
		DataInputStream in =new DataInputStream( 
				new BufferedInputStream(new GZIPInputStream(new FileInputStream(
					file))));
		
		int numRecords = in.readInt();
		System.out.println(numRecords);

		HashMap<Long, ContextCount> map = new HashMap<Long, ContextCount>(numRecords);
	
		for( int x=0; x < numRecords; x++)
		{
			long aLong = in.readLong();
			
			if( map.containsKey(aLong))
				throw new Exception("Duplicate");
			
			ContextCount cc = new ContextCount(in.readByte(), in.readByte(), in.readByte(), in.readByte());
			map.put(aLong,cc);
			
			//if( x % 100000==0)
			//	System.out.println("Reading " + x);
			
			if( maxNum > -1 && x >= maxNum)
			{
				in.close();
				return map;
			}
			
		}
		
		return map;
	}
	
	public static void main(String[] args) throws Exception
	{

		HashMap<Long, ContextCount> map2 = 
				CoPhylogBinaryFileReader.readBinaryFile(new File(
		File.separator + "results" + File.separator + 
		"AS130-2_ATCACG_s_2_2_sequence.txt.gz_CO_PhylogBin.gz"),100);
		
		HashMap<Long, ContextCount> map1 = 
				CoPhylogBinaryFileReader.readBinaryFile(new File(ConfigReader.getBurkholderiaDir() +
						File.separator + "results" + File.separator + 
						"AS130-2_ATCACG_s_2_1_sequence.txt.gz_CO_PhylogBin.gz"),100);
		
		for(Long l : map2.keySet())
		{
			ContextCount cc = map1.get(l);
			System.out.println(l + " " + cc.getNumA()+ " " + cc.getNumC()+ " " + cc.getNumG() + " " + cc.getNumT());
			
			cc = map2.get(l);
			System.out.println(l + " " + cc.getNumA()+ " " + cc.getNumC()+ " " + cc.getNumG() + " " + cc.getNumT());
			
			System.out.println("\n\n\n\n");
		}
	}
}
