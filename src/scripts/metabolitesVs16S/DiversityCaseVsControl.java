/** 
 * Author:  anthony.fodor@gmail.com
 * 
 * This code is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 2
* of the License, or (at your option) any later version,
* provided that any use properly credits the author.
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details at http://www.gnu.org * * */

package scripts.metabolitesVs16S;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import utils.Avevar;
import utils.ConfigReader;
import utils.TTest;

public class DiversityCaseVsControl
{
	public static void main(String[] args) throws Exception
	{
		String[] vals = { "fam" , "gen", "ord", "phy" , "cls", "counts" };
		
		for(String s : vals)
			writeALevel(s);
	}
	
	private static void writeALevel(String level) throws Exception
	{
		List<Double> caseVals =new ArrayList<Double>();
		List<Double> controlVals = new ArrayList<Double>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File( 
			ConfigReader.getMetabolitesCaseControl() + File.separator + 
			"topeFeb2014_raw_" + level +  ".txt")));
		
		reader.readLine();
		for( String s= reader.readLine(); s != null; s = reader.readLine())
		{
			StringTokenizer sToken = new StringTokenizer(s);
			sToken.nextToken(); sToken.nextToken();
			
			List<Double> aList = null;
			
			String aString = sToken.nextToken().replaceAll("\"", "");
			
			if( aString.equals("case"))
				aList = caseVals;
			else if( aString.equals("control"))
				aList = controlVals;
			else 
				throw new Exception("no");
			aList.add(getEntropy(sToken));
		}
		
		double caseAvg = new Avevar(caseVals).getAve();
		double controlAvg = new Avevar(controlVals).getAve();
		
		System.out.println(caseAvg + " " + controlAvg + " " + caseAvg / controlAvg + " " +
					TTest.ttestFromNumberUnequalVariance(caseVals, controlVals).getPValue());
		
		reader.close();
	}
	
	private static double getEntropy(StringTokenizer sToken)
		throws Exception
	{
		List<Double> counts = new ArrayList<Double>();
		double sum =0;
		
		while(sToken.hasMoreTokens())
		{
			Double d = Double.parseDouble(sToken.nextToken());
			
			if( d < 0 )
			{
				throw new Exception("No");
			}
			else
			{
				counts.add(d);
				sum += d;
			}
		}
		
		double entropy = 0;
		
		for( Double d: counts)
			if( d > 0 )
			{
				double p = d / sum;
				entropy += p * Math.log(p);
			}
		
		return -entropy;
	}
}
