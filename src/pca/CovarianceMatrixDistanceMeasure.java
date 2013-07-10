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


package pca;

import utils.Pearson;

public class CovarianceMatrixDistanceMeasure implements DistanceMeasureInterface
{

	public double[][] getDistanceMatrix(double[][] d) throws Exception
	{
		double[][] returnArray = new double[d[0].length][d[0].length];
		//System.out.println(returnArray.length + " " + returnArray.length + " huh");
		
		for( int x=0; x < d[0].length; x++ )
		{
			double[] xArray = PCA.getColumn(d, x);
			
			for( int y=0; y < d[0].length; y++)
			{
				double[] yArray = PCA.getColumn(d, y);
				//System.out.println(xArray.length + " " + yArray.length);
				returnArray[x][y] = Pearson.getCovariance(xArray, yArray);
			}
		}
		
		return returnArray;
	}
	
	@Override
	public String getName()
	{
		return "Covariance Matrix";
	}
}
