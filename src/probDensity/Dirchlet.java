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

import java.util.ArrayList;
import java.util.List;

import utils.Functions;

public class Dirchlet
{
	private final double[] alphas;
	
	private final double logNormalizingConstant;
	
	public double getNormalizingConstant()
	{
		return Math.exp(logNormalizingConstant);
	}
	
	/*
	 * Note that a defensive copy of inAlphas is made.
	 * Changes to inAlphas made after calls to this constructor will
	 * not change the results of subsequent calls to this object
	 */
	public Dirchlet( List<Double> inAlphas)
	{
		alphas= new double[inAlphas.size()];
		
		for( int x=0; x < inAlphas.size(); x++)
			alphas[x] = inAlphas.get(x);
		
		
		double top =0;
		double sum =0;
		
		for( Double d : alphas )
		{
			top = top + Functions.lnfgamma(d);
			sum += d;
		}
		
		this.logNormalizingConstant =  Functions.lnfgamma(sum) - top;
		System.out.println("CONSTANT " + this.logNormalizingConstant + " " + Math.exp(logNormalizingConstant));
	}
	
	public double getPDF( double[] x) throws Exception
	{
		if( x.length != alphas.length)
			throw new Exception("Wrong # of parameters");
		
		for( Double d : x)
			System.out.println(d + "\t");
		
		System.out.println();
		
		double product = Math.exp(logNormalizingConstant);
		
		for( int i =0; i < x.length; i++) 
		{
			product = product * Math.pow(x[i], alphas[i]-1);
		}
		
		System.out.println("PRODUCT=" + product);
		
		if( Double.isInfinite(product) || Double.isNaN(product))
			return 0;
		
		
		return product;
	}
	
	public double[] getMeanCenteredAlphas()
	{
		double sum =0;
		
		double[] a = new double[alphas.length];
		
		for( Double d : alphas)
			sum +=d;
		
		for(int x=0; x < alphas.length; x++)
			a[x] = alphas[x] / sum;
		
		return a;
		
	}
	
	public static void main(String[] args) throws Exception
	{
		List<Double> list1 = new ArrayList<>();
		
		list1.add(217.70117659936497);
		list1.add(1.2988234466047284);
		list1.add(1.2988234466047284);
		list1.add(0.7011765074255709);
		
		Dirchlet d1 = new Dirchlet(list1);
		
		double stepSize = 0.001;
		double width = Math.pow(stepSize, 4);
		double interval=0;
		
		double[] xs= d1.getMeanCenteredAlphas();
		
		System.out.println( d1.getPDF(xs) );
		
		/*
		
		for( Double d : xs)
			System.out.println(d + "\t");
		
		System.out.println();
		
		double sum =0;
		
		while(sum <=0.95)
		{
			sum += d1.getPDF(xs) / width;
			
			interval += stepSize;
			
			for( int x=0; x < xs.length; x++)
				xs[x] -= interval;
			
			sum += d1.getPDF(xs) / width;
			
			for( int x=0; x < xs.length; x++)
				xs[x] += 2 *  interval;
			
			sum += d1.getPDF(xs);
			
			for( int x=0; x < xs.length; x++)
				xs[x] -= interval / width;
			
			System.out.println("SUM=" + sum);
			//System.exit(1);
		}
		*/
	}
}
