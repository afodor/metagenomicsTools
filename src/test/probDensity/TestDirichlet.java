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


package test.probDensity;

import java.util.ArrayList;
import java.util.List;

import probDensity.Dirchlet;
import junit.framework.TestCase;

public class TestDirichlet extends TestCase
{
	public void test() throws Exception
	{
		List<Double> list = new ArrayList<Double>();
		list.add(2.0);list.add(1.0);list.add(0.8);list.add(0.4);
		
		
		Dirchlet d = new Dirchlet(list);
		double x[] = new double[4];
		
		x[0] = .4; x[1] = .3; x[2]= .2; x[3] =.1;
		
		System.out.println(d.getPDF(x));
		
		System.out.println(d.getNormalizingConstant());
		
		//double x[] = new double[4];
		
		double sum =0;
		double step = 0.05;
		double num =0;
		
		for( double x1 =0; x1 <= 1; x1+=step)
			for( double x2 =0; x2 <= 1; x2+=step)
				for( double x3 =0; x3 <= 1; x3+=step)
					for( double x4 =0; x4 <= 1; x4+=step)
					{
						x[0] = x1;
						x[1] = x2;
						x[2] = x3;
						x[3] = x4;
						double pdf = d.getPDF(x);
						sum += pdf;
						System.out.println(pdf + " " + sum);
						num++;
					}
		
		System.out.println("Finished " + sum / num);
	}
}
