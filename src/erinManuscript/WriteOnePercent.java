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


package erinManuscript;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import parsers.OtuWrapper;
import utils.ConfigReader;

public class WriteOnePercent
{
	private static class Holder implements Comparable<Holder>
	{
		String name;
		double count;
		
		@Override
		public int compareTo(Holder o)
		{
			return Double.compare( o.count , this.count);
		}
	}
	
	public static void main(String[] args) throws Exception
	{
		OtuWrapper wrapper = new OtuWrapper(ConfigReader.getErinDataDir() + File.separator + 
						"erinHannaHuman_raw_gen.txt");
		
		for (int sampleNum =0 ; sampleNum < wrapper.getSampleNames().size(); sampleNum++)
		{
			List<Holder> list = new ArrayList<Holder>();
			System.out.println(wrapper.getSampleNames().get(sampleNum));
			
			double numSeqs = wrapper.getNumberSequences(wrapper.getSampleNames().get(sampleNum));
			
			for( int x=0; x < wrapper.getOtuNames().size(); x++)
			{
				double val=  wrapper.getDataPointsUnnormalized().get(sampleNum).get(x);
				
				if( val / numSeqs >= 0.01)
				{
					Holder h =new Holder();
					h.count = val/numSeqs;
					h.name = wrapper.getOtuNames().get(x);
					list.add(h);
				}
			}
			
			Collections.sort(list);
			for(Holder h : list)
				System.out.println("\t" + h.name.replace("human", "sample") + "\t" + h.count );
		}
		
	}
}
