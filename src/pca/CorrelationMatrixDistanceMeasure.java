package pca;

import utils.Pearson;

public class CorrelationMatrixDistanceMeasure implements DistanceMeasureInterface
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
				returnArray[x][y] = Pearson.getPearsonR(xArray, yArray);
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
