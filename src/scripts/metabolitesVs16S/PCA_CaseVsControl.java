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
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;	

import parsers.OtuWrapper;
import pca.PCA;

import utils.ConfigReader;

public class PCA_CaseVsControl
{
	private static HashMap<String, String> getCaseControlMap() throws Exception
	{
		HashMap<String, String> map = new HashMap<String, String>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
					ConfigReader.getMetabolitesCaseControl() + File.separator + 
					"topeFeb2014_raw_fam.txt")));
		
		reader.readLine();
		
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			StringTokenizer sToken = new StringTokenizer(s);
			sToken.nextToken();
			
			String key = sToken.nextToken().replaceAll("\"","");
			
			if( map.containsKey(key))
				throw new Exception("No");
			
			map.put( key, sToken.nextToken().replaceAll("\"", ""));
		}
		
		reader.close();
		
		return map;
	}
	
	/*
	 * Run WriteOTUSpreadsheets first...
	 */
	public static void main(String[] args) throws Exception
	{
		String[] vals = { "fam" , "gen", "ord", "phy" , "cls", "counts" };
		
		for(String s : vals)
			pcaPivotALevel(s);
	}
			
		
	public static void pcaPivotALevel(String level) throws Exception
	{
		System.out.println(level);
		HashMap<String, String> caseControlMap= getCaseControlMap();
			
		List<String> shannon = new ArrayList<String>();
		List<String> evenness = new ArrayList<String>();
		List<String> numSequences = new ArrayList<String>();
		List<String> sampleIds = new ArrayList<String>();
		List<String> caseControl = new ArrayList<String>();
		OtuWrapper wrapper = new OtuWrapper(ConfigReader.getMetabolitesCaseControl() + File.separator + 
				level + "_asColumns.txt");
			
		System.out.println(wrapper.getSampleNames().size() + " " + wrapper.getOtuNames().size());
			
		double[][] d=  wrapper.getNormalizedThenLoggedAsArray();
			
		for(String s : wrapper.getSampleNames())
		{
			System.out.println(s);
			
			sampleIds.add(s);
			shannon.add("" + wrapper.getShannonEntropy(s));
			evenness.add("" + wrapper.getEvenness(s));
			
			String aString = caseControlMap.get(s);
			
			if( aString == null)
				throw new Exception("No");
			
			caseControl.add(aString);
			
			numSequences.add("" + wrapper.getCountsForSample(s));
		}
			
			List<String> catHeaders = new ArrayList<String>();
			catHeaders.add("caseControl");
			catHeaders.add("NumSequences"); catHeaders.add("diversity");
			catHeaders.add("evenness"); 
			
			List<List<String>> categories = new ArrayList<List<String>>();
			
			categories.add(caseControl);
			categories.add(numSequences); categories.add(shannon);
			categories.add(evenness); 
	
		File outFile = new File(ConfigReader.getMetabolitesCaseControl() + File.separator
					+ "PCA_PIVOT_" + level+  ".txt");
			
			PCA.writePCAFile(sampleIds, catHeaders, categories,d, outFile);
	}

}
