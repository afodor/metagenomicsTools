package utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**  Calculates mean and variance from an array of doubles
 */
public class Avevar
{
	double ave;
	double var;
	
	public double getAve()
	{
	    return ave;
	}
	
	public double getVar()
	{
	    return var;
	}
	
	public double getSqrt()
	{
		return Math.sqrt(var);
	}
	
	public double getSD()
	{
		return Math.sqrt(var);
	}
	
	@Override
	public String toString()
	{
		return this.getAve() + " " + this.getSD();
	}
	
	public Avevar( double[] data ) 
	{
		double s, ep;
		int j;
		
		ave = 0.0;
		for ( j=0; j < data.length; j++ ) ave += data[j];
		ave /= data.length;
		var = ep = 00;
		
		for ( j = 0; j < data.length; j++) {
			s = data[j] - ave;
			ep += s;
			var += s*s;
		}
		
		var = ( var - ep * ep / data.length ) / ( data.length - 1);
	}
	
	public static double getFirstQuartile( List<Double> vals)
	{
		List<Double> newList = new ArrayList<Double>();
	
		for(Double d : vals)
			newList.add(d);
	
		Collections.sort(newList);
		
		int firstIndex = newList.size() / 4;
		
		if( newList.size() % 4 == 0 )
		{
			firstIndex = firstIndex-1;
			return (  0.25*newList.get(firstIndex) + 0.75*newList.get(firstIndex+1));
		}
			
		return (  0.75*newList.get(firstIndex) + 0.25*newList.get(firstIndex+1));
	}
	
	public static double getThirdQuartile( List<Double> vals)
	{
		List<Double> newList = new ArrayList<Double>();
	
		for(Double d : vals)
			newList.add(d);
	
		Collections.sort(newList);
		
		int index = 3*newList.size()/ 4;
		
		if( 3*newList.size()%  4 ==0 )
		{
			return (  0.75*newList.get(index-1) + 0.25*newList.get(index));
		}
			
		return (  0.25*newList.get(index-1) + 0.75*newList.get(index));
	}
	
	public Avevar( List<? extends Number> data ) 
	{
		double s, ep;
		int j;
		
		ave = 0.0;
		for ( j=0; j < data.size(); j++ ) ave += (data.get(j)).doubleValue();
		ave /= data.size();
		var = ep = 00;
		
		for ( j = 0; j < data.size(); j++) {
			s = (data.get(j)).doubleValue() - ave;
			ep += s;
			var += s*s;
		}
		
		var = ( var - ep * ep / data.size() ) / ( data.size()- 1);
	}
    
	
    public Avevar( float[] data ) 
	{
        double s, ep;
		int j;
		
		ave = 0.0;
		for ( j=0; j < data.length; j++ ) ave += data[j];
		ave /= data.length;
		var = ep = 00;
		
		for ( j = 0; j < data.length; j++) {
			s = data[j] - ave;
			ep += s;
			var += s*s;
		}
		
		var = ( var - ep * ep / data.length ) / ( data.length - 1);
	}
    

	public static double getMedian(List<Double> list)
	{
		List<Double> newList = new ArrayList<Double>();
		
		for(Double d : list)
			newList.add(d);
		
		Collections.sort(newList);
		
		if( list.size() % 2== 1)
		{
			return newList.get(newList.size()/2);
		}
		
		return ( newList.get(newList.size()/2) + newList.get(newList.size()/2 -1))/2 ;
	}
	
}