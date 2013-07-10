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

package scripts.sequenceScripts;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import utils.ConfigReader;

/*
 * This has dependencies on CoPhylogOnBurk and then GenerateDistances
 */
public class ApplyWeigthedChiSquare
{
	
	private static class CountHolder
	{
		private long a=0;
		private long c=0;
		private long g=0;
		private long t=0;
	}
	
	
	
	public static void main(String[] args) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(new File( 
			ConfigReader.getBurkholderiaDir() + File.separator + "distances" +
					File.separator + "techReps1.txt")));
		
		reader.readLine();
		
		
		
		reader.close();
	}
}
