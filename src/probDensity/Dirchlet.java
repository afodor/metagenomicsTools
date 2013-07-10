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

import java.util.List;

import utils.Functions;

public class Dirchlet
{
	private final double[] alphas;
	
	private final double normalizingConstant;
	
	public double getNormalizingConstant()
	{
		return normalizingConstant;
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
		
		
		double top =1;
		double sum =0;
		
		for( Double d : alphas )
		{
			top = top * Math.exp(Functions.lnfgamma(d));
			sum += d;
		}
		
		this.normalizingConstant =  Math.exp(Functions.lnfgamma(sum)) / top;
	}
	
	public double getPDF( double[] x) throws Exception
	{
		if( x.length != alphas.length)
			throw new Exception("Wrong # of parameters");
		
		double product = normalizingConstant;
		
		for( int i =0; i < x.length; i++) 
		{
			product = product * Math.pow(x[i], alphas[i]-1);
		}
		
		if( Double.isInfinite(product) || Double.isNaN(product))
			return 0;
		
		return product;
	}
	
	public static void main(String[] args) throws Exception
	{
		
	}
}
