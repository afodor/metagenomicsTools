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


package exerciseCrossover;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import parsers.OtuWrapper;
import utils.Avevar;
import utils.ConfigReader;

public class Counts
{
	public static void main(String[] args) throws Exception
	{
		OtuWrapper wrapper = new OtuWrapper(ConfigReader.getCrossoverExerciseDir() + File.separator + "asea70_1_silva_raw_counts.txt");
		
		long num =0;
		List<Double> counts = new ArrayList<Double>();
		
		for( int x=0; x < wrapper.getSampleNames().size(); x++)
		{
			num += wrapper.getCountsForSample(x);
			counts.add( new Double( wrapper.getCountsForSample(x)));
		}
		
		System.out.println(num);
		Avevar av = new Avevar(counts);
		System.out.println(av.getAve() + " " + av.getSD());
	}
}
