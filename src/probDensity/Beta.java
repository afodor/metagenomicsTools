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


package probDensity;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import utils.Functions;

public class Beta
{
	public static double getPDF(double x,double alpha, double beta) throws Exception
	{
		return Math.pow(x,alpha-1) * Math.pow(1-x,beta-1) / Math.exp( Functions.lnfbeta(alpha, beta));
	}
	
	public static void main(String[] args) throws Exception
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File("c:\\temp\\blah.txt")));
		System.out.println( Beta.getPDF(0.3, 1.0, 2.0));

		System.out.println( Beta.getPDF(0.6, 2.0, 1.0));
		
		double sum=0;
		
		double step =0.00001;
		
		for( double x=0; x <= 1; x+=step)
		{
			writer.write( x + "\t" +  Beta.getPDF(x, 3.0, 5.0) / (1/step) + "\n" );
			sum += Beta.getPDF(x, 5.0, 5.0);
		}
		
		System.out.println(sum / (1/step));
		writer.flush();  writer.close();
	}
}
