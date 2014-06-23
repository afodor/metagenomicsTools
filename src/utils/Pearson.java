package utils;

import java.util.ArrayList;
import java.util.List;

public class Pearson
{
	public static final double TINY=1.0e-20;
    
    public static double getCovariance(double[] x, double[] y)
    	throws Exception
    {
    	if( x.length != y.length)
    		throw new Exception("Expecting equal vector lengths");
    	
    	int j;
        double yt,xt;
        double sxy=0.0,ay=0.0,ax=0.0;

        int n=x.length;
        for (j=0;j<n;j++) {
                ax += x[j];
                ay += y[j];
        }
        ax /= n;
        ay /= n;
        for (j=0;j<n;j++) {
                xt=x[j]-ax;
                yt=y[j]-ay;
                sxy += xt*yt;
        }
        return sxy/(n-1);
    }
	
    public static double getPearsonR(List<? extends Number> x, List<? extends Number> y)
    {
           
            int j;
            double yt,xt;
            double syy=0.0,sxy=0.0,sxx=0.0,ay=0.0,ax=0.0;

            int n=x.size();
            for (j=0;j<n;j++) {
                    ax += x.get(j).doubleValue();
                    ay += y.get(j).doubleValue();
            }
            ax /= n;
            ay /= n;
            for (j=0;j<n;j++) {
                    xt=x.get(j).doubleValue()-ax;
                    yt=y.get(j).doubleValue()-ay;
                    sxx += xt*xt;
                    syy += yt*yt;
                    sxy += xt*yt;
            }
            return sxy/(Math.sqrt(sxx*syy)+TINY);
    }
    
    public static double getPearsonR(ArrayList<Double> x, ArrayList<Double> y)
    {
           
            int j;
            double yt,xt;
            double syy=0.0,sxy=0.0,sxx=0.0,ay=0.0,ax=0.0;

            int n=x.size();
            for (j=0;j<n;j++) {
                    ax += x.get(j).doubleValue();
                    ay += y.get(j).doubleValue();
            }
            ax /= n;
            ay /= n;
            for (j=0;j<n;j++) {
                    xt=x.get(j).doubleValue()-ax;
                    yt=y.get(j).doubleValue()-ay;
                    sxx += xt*xt;
                    syy += yt*yt;
                    sxy += xt*yt;
            }
            return sxy/(Math.sqrt(sxx*syy)+TINY);
    }
    

    public static double getPearsonR(double[] x, double[] y)
    {
           
            int j;
            double yt,xt;
            double syy=0.0,sxy=0.0,sxx=0.0,ay=0.0,ax=0.0;

            int n=x.length;
            for (j=0;j<n;j++) {
                    ax += x[j];
                    ay += y[j];
            }
            ax /= n;
            ay /= n;
            for (j=0;j<n;j++) {
                    xt=x[j]-ax;
                    yt=y[j]-ay;
                    sxx += xt*xt;
                    syy += yt*yt;
                    sxy += xt*yt;
            }
            return sxy/(Math.sqrt(sxx*syy)+TINY);
    }
 
    
    /*
     * NEED TO TEST
     */
    public static double getPearsonR(double[] x, 
    						double[] y, 
    						int leftCutoff, 
    						int rightCutoff,
    						int xOffset) throws Exception
    {
    	
    	if( x.length != y.length )
    		throw new Exception("Error!  Cannot generate Pearson for arrays of different lengths " + 
    				x.length + " " + y.length + " "+ leftCutoff + " " + rightCutoff + " " + xOffset);
           
            int j;
            double yt,xt;
            double syy=0.0,sxy=0.0,sxx=0.0,ay=0.0,ax=0.0;

            int n=x.length - leftCutoff - rightCutoff;
            for (j=leftCutoff;j<x.length - rightCutoff;j++) {
                    ax += x[j+xOffset];
                    ay += y[j];
            }
            ax /= n;
            ay /= n;
            for (j=leftCutoff;j<x.length - rightCutoff;j++) {
                    xt=x[j+xOffset]-ax;
                    yt=y[j]-ay;
                    sxx += xt*xt;
                    syy += yt*yt;
                    sxy += xt*yt;
            }
            return sxy/(Math.sqrt(sxx*syy)+TINY);
    }
    
    /*
     * NEED TO TEST
     */
    public static double getPearsonR(float[] x, 
    						float[] y, 
    						int leftCutoff, 
    						int rightCutoff,
    						int xOffset)
    {
           
            int j;
            double yt,xt;
            double syy=0.0,sxy=0.0,sxx=0.0,ay=0.0,ax=0.0;

            int n=x.length - leftCutoff - rightCutoff;
            for (j=leftCutoff;j<x.length - rightCutoff;j++) {
                    ax += x[j+xOffset];
                    ay += y[j];
            }
            ax /= n;
            ay /= n;
            for (j=leftCutoff;j<x.length - rightCutoff;j++) {
                    xt=x[j+xOffset]-ax;
                    yt=y[j]-ay;
                    sxx += xt*xt;
                    syy += yt*yt;
                    sxy += xt*yt;
            }
            return sxy/(Math.sqrt(sxx*syy)+TINY);
    }
   
    public static double getPearsonRFromFloat(List<Float> x, List<Float> y)
    {
           
            int j;
            double yt,xt;
            double syy=0.0,sxy=0.0,sxx=0.0,ay=0.0,ax=0.0;

            int n=x.size();
            for (j=0;j<n;j++) {
                    ax += x.get(j).doubleValue();
                    ay += y.get(j).doubleValue();
            }
            ax /= n;
            ay /= n;
            for (j=0;j<n;j++) {
                    xt=x.get(j).doubleValue()-ax;
                    yt=y.get(j).doubleValue()-ay;
                    sxx += xt*xt;
                    syy += yt*yt;
                    sxy += xt*yt;
            }
            return sxy/(Math.sqrt(sxx*syy)+TINY);
    } 
    
    public static double getPearsonRFromNumber(List<Number> x, List<Number> y)
    {
           
            int j;
            double yt,xt;
            double syy=0.0,sxy=0.0,sxx=0.0,ay=0.0,ax=0.0;

            int n=x.size();
            for (j=0;j<n;j++) {
                    ax += x.get(j).doubleValue();
                    ay += y.get(j).doubleValue();
            }
            ax /= n;
            ay /= n;
            for (j=0;j<n;j++) {
                    xt=x.get(j).doubleValue()-ax;
                    yt=y.get(j).doubleValue()-ay;
                    sxx += xt*xt;
                    syy += yt*yt;
                    sxy += xt*yt;
            }
            return sxy/(Math.sqrt(sxx*syy)+TINY);
    } 
    
    public static void main(String[] args) throws Exception
	{
		double[] a = {2, 3, 4, 5, 6, 7, 8, 2, 3, 3, 2};
		double[] b = {2, 3, 4, 1, 4, 5, 6, 7, 8, 9, 1};
		
		System.out.println(getPearsonR(a, b));
	}
}
