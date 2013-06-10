package pca;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import utils.Pearson;
import Jama.EigenvalueDecomposition;
import Jama.Matrix;

public class PCA
{
	/*
	 * the data array should be in the format
	 * 
	 * 			Taxa1	Taxa2	Taxa3	Taxa4
	 * Site1
	 * Site2
	 * Site3
	 * This is a double[Site][Taxa]
	 * 
	 * This has the effect of clustering Sites by replacing Taxa with their principal components
	 * via a covariance array that is [Taxa] by [Taxa]
	 */
	
	public static class EigenValueHolder implements Comparable<EigenValueHolder>
	{
		final double eigenValue;
		final int rank;
		
		public double getEigenValue()
		{
			return eigenValue;
		}
		
		public int getRank()
		{
			return rank;
		}
		
		public EigenValueHolder(double eigenValue, int rank)
		{
			this.eigenValue = eigenValue;
			this.rank = rank;
		}
		
		public int compareTo(EigenValueHolder o)
		{
			return Double.compare( Math.abs( o.eigenValue), Math.abs( this.eigenValue));
		}
	}
	
    public static Matrix getReorderedMatrix( Matrix eigenVectors,
    		List<EigenValueHolder> sortedEigenValues )
    		               throws Exception
    {
    	if( eigenVectors.getRowDimension() != eigenVectors.getColumnDimension())
    		throw new Exception("Eigen value should be square matrix");

    	if ( sortedEigenValues.size() != eigenVectors.getRowDimension())
    		throw new Exception("Wrong number of eigen values");

    	double[][] oldArray = eigenVectors.getArray();
    	double[][] newArray = new
    	double[eigenVectors.getRowDimension()][eigenVectors.getColumnDimension()];

       for( int x=0; x < sortedEigenValues.size(); x++ )
       {
               for( int y=0; y < sortedEigenValues.size(); y++)
               {
                     newArray[y][x] = oldArray[y][sortedEigenValues.get(x).rank];
               }
       }

       return new Matrix( newArray);
    }
    
    public static List<EigenValueHolder> getRankedEigenValues( EigenvalueDecomposition evd)
    		throws Exception
    {
    	return getRankedEigenValues(evd,null);
    }
    	
	
	public static List<EigenValueHolder> getRankedEigenValues( EigenvalueDecomposition evd, File baseFile )
		throws Exception
	{
		
		BufferedWriter writer = null;
		
		if( baseFile != null)
		{
			writer = new BufferedWriter(new FileWriter(baseFile.getAbsolutePath() + "_COMPONENENTS.txt"));
			writer.write("componenet\tsum\tcumulativeSum\n");
		}
		
		List<EigenValueHolder> list = new ArrayList<EigenValueHolder>();
		
		double[] eigenValues = evd.getRealEigenvalues();
		
		double sum = 0;
		
		for( int x=0; x < eigenValues.length; x++)
		{
			EigenValueHolder evh = new EigenValueHolder(eigenValues[x], x);
			list.add(evh);
			sum+=evh.eigenValue;
		}
		
		Collections.sort(list);
	
		double cumulativeSum = 0;
		for(int x=0; x < list.size(); x++)
		{
			cumulativeSum += list.get(x).eigenValue;
			System.out.println("Comp. " + (x+1) + " cumulatively explains " + cumulativeSum/sum );
			
			if(baseFile != null)
			{
				writer.write((x+1) + "\t" + list.get(x).eigenValue + "\t" + cumulativeSum/sum + "\n");
			}
		}
				
		if( baseFile != null)
		{
			writer.flush();  writer.close();
		}
		
		return list;
		
	}
	
	public static void writePCAFile(List<String> keys,
					double[][] d, File outFile) 
			throws Exception
	{
		writePCAFile(keys, null,null, d, outFile);
	}

	
	public static void writePCAFile(List<String> keys,
			List<String> categoryHeaders,
			List<List<String>> categories,
			double[][] d, File outFile) 
			throws Exception
	{
		writePCAFile(keys,
				 categoryHeaders,
				 categories,
				d, outFile, new CovarianceMatrixDistanceMeasure());
	}
	
	public static void writePCAFile(List<String> keys,
			List<String> categoryHeaders,
			List<List<String>> categories,
			double[][] d, File outFile, DistanceMeasureInterface dmi) 
			throws Exception
	{
		if(categories.size() != categoryHeaders.size())
			throw new Exception("Category size must equal headers size " + categories.size() + " " + categoryHeaders.size());
		
		subtractColumnMeans(d);
		
		double[][] covarianceArray = dmi.getDistanceMatrix(d);
		//writeCovarianceArray(covarianceArray);
		Matrix covarianceMatrix = new Matrix(covarianceArray);
		EigenvalueDecomposition evd = covarianceMatrix.eig();
		
		System.out.println("Covariance array length=" + evd.getRealEigenvalues().length);
		
		List<EigenValueHolder> eigenValues = getRankedEigenValues(evd,outFile);
		
		Matrix eigenVectors = evd.getV();
		
		Matrix rawDataAdjust = new Matrix(d).transpose();
		Matrix rowFeatureVector = eigenVectors.transpose();
		
		Matrix finalData = rowFeatureVector.times(rawDataAdjust);
		System.out.println(finalData.getArray().length + " " + finalData.getArray()[0].length);
		
		writeComponents(outFile, finalData, keys, categoryHeaders, categories,  eigenValues);
		
		Matrix originalData =  new Matrix(d);
		
		Matrix compressedData = rowFeatureVector.inverse().times(finalData).transpose();
		
		if( originalData.getColumnDimension() != compressedData.getColumnDimension())
			throw new Exception("Logic error");
		
		if( originalData.getRowDimension() != compressedData.getRowDimension())
			throw new Exception("Logic error");
		
		for( int i=0; i < originalData.getRowDimension(); i++)
			for( int j=0; j < originalData.getColumnDimension(); j++)
			{
				if( Math.abs( originalData.get(i, j) - compressedData.get(i, j)) > 0.0001)
					throw new Exception("Diff");
			}
	}
	
	public static void writeComponents( File outFile,
			Matrix componentMatrix,  List<String> labels, 
			List<String> categoryHeaders,
			List<List<String>> categories,
			List<EigenValueHolder> eigenValues)
		throws Exception
	{
		double[][] a = componentMatrix.getArray();
		
		if( labels.size() != a[0].length)
			throw new Exception("Logic error " + labels.size() + " " + a[0].length);
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(outFile ));
		
		writer.write( "keys" );
		
		if( categoryHeaders != null)
			for(String s : categoryHeaders)
				writer.write("\t"+ s);
		
		for( int x=0; x < a.length; x++)
			writer.write("\tComp" + x);
		
		writer.write("\n");
		
		for( int y =0; y < labels.size(); y++)
		{
			writer.write(labels.get(y));
			
			if( categories != null)
				for( List<String> cats : categories )
					writer.write("\t" + cats.get(y));
			
			for( int x=0; x < eigenValues.size(); x++)
			{
				writer.write("\t" + a[eigenValues.get(x).rank][y]);
			}
			
			writer.write("\n");
		}
		
		writer.flush();  writer.close();
	}

	
	public static double[][] transposeArray(double[][] d) throws Exception
	{
		double[][] returnA = new double[d[0].length][d.length];
		
		for( int x=0; x< d.length; x++)
			for( int y=0; y < d[0].length; y++)
				returnA[y][x] = d[x][y];
		
		return returnA;
	}
	
	
	
	

	public static double[][] getCorrelationArray(double[][] d)
		throws Exception
	{
		double[][] returnArray = new double[d[0].length][d[0].length];
		
		for( int x=0; x < d[0].length; x++ )
		{
			double[] xArray = getColumn(d, x);
			
			for( int y=0; y < d[0].length; y++)
			{
				double[] yArray = getColumn(d, y);
				
				returnArray[x][y] = Pearson.getPearsonR(xArray, yArray);
			}
		}
		
		return returnArray;
	}
	
	static double[] getRow( double[][] d, int rowIndex ) throws Exception
	{
		double[] r = new double[d[rowIndex].length];
		
		for( int x=0; x < d[rowIndex].length; x++)
			r[x] = d[rowIndex][x];
		
		return r;
	}
	
	public static double[] getColumn( double[][] d, int colIndex) throws Exception
	{
		double[] r = new double[d.length];
		
		for( int x=0; x < d.length; x++)
			r[x] = d[x][colIndex];
		
		return r;
	}
	
	
	public static void subtractColumnMeans(double[][] d) throws Exception
	{
		for( int y=0; y < d[0].length; y++)
		{
			double colSum = 0;
			
			for( int x=0; x < d.length; x++)
				colSum += d[x][y];
			
			colSum = colSum / d.length;
			
			for( int x=0; x < d.length; x++)
				d[x][y] = d[x][y] - colSum;
			
		}
	}
	
	public static double[][] getDataArrayWithTaxaAsColumns(HashMap<String, int[]> countMap,
			List<String> keys, List<String> annotations) 
		throws Exception
	{	
		double[][] a = 
			new double[annotations.size()][keys.size()];
		
			for( int y=0; y < keys.size(); y++)
			{
				int[] innerArray = countMap.get(keys.get(y));
				
				for( int x=0; x< innerArray.length; x++)
					a[x][y] = Math.log10( innerArray[x] + 1);
			}
		
		return a;
	}
	
	public static void percentExplained(List<EigenValueHolder> list) throws Exception
	{	
		double sum = 0;
		double cumulative = 0;
		
		for( EigenValueHolder egv : list )
			sum += egv.eigenValue;
		
		for( EigenValueHolder egv : list)
		{
			cumulative += egv.eigenValue / sum ;
			System.out.println( egv.eigenValue / sum + " " + cumulative);
		}
	}
}
