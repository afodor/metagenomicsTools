package utils;

import java.util.ArrayList;
import java.util.List;

public class ChisquareTest
{
	public static double getChisquarePValue( 
				List<Double> list1, List<Double> list2) throws Exception
	{
		if( list1.size() != list2.size())
			throw new Exception("No");
		
		List<Double> rowSums = new ArrayList<>();
		List<Double> colSums= new ArrayList<>();
		
		for( int x=0; x <list1.size(); x++)
			rowSums.add(list1.get(x) + list2.get(x));
		
		double sum =0;
		
		for( Double d : list1)
			sum +=d;
		
		colSums.add(sum);
		
		sum =0;
		
		for( Double d : list2)
			sum +=d;
		
		colSums.add(sum);
		
		sum =0;
		
		double N=0;
		
		for( Double d : rowSums)
			N += d;
		
		for( int x=0; x < list1.size(); x++)
		{
			double expected = rowSums.get(x) * colSums.get(0) / N;
			double val = list1.get(x) - expected;
			sum += val * val /expected;
		}
		
		for( int x=0; x < list2.size(); x++)
		{
			double expected = rowSums.get(x) * colSums.get(1)/ N;
			double val = list2.get(x)- expected;
			sum += val * val /expected;
		}
		
		System.out.println(sum);
		return 1-StatFunctions.pchisq(sum, list1.size() -1);
	}
	
	public static void main(String[] args) throws Exception
	{
		List<Double> list1 = new ArrayList<>();
		list1.add(762.0); list1.add(327.0); list1.add(468.0);
		
		List<Double> list2 = new ArrayList<>();
		list2.add(484.0);  list2.add(239.0); list2.add(477.0);
		
		// from R should be 2.953589e-07
		System.out.println(getChisquarePValue(list1, list2));
	}
}
