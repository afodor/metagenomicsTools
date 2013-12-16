package utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import rExecute.Rexecute;

public class OneWayAnova
{
	private final double totalSumSqaured;
	private final double modelSumSquared;
	
	private final int r;
	private final int n;
	
	public int getR()
	{
		return r;
	}
	
	public int getN()
	{
		return n;
	}
	
	public static final String NA_STRING = "NA";
	
	public double getTotalSumSqaured()
	{
		return totalSumSqaured;
	}
	
	public double getModelSumSquared()
	{
		return modelSumSquared;
	}
	
	public double getErrorSumSquared()
	{
		return totalSumSqaured - modelSumSquared;
	}
	
	public double getFractionExplainedByModel() 
	{
		return modelSumSquared / totalSumSqaured;
	}
	
	public OneWayAnova(List<Number> data, List<String> factors)
		throws Exception
	{
		List<Number> dataCopy = new ArrayList<Number>();
		List<String> factorCopy = new ArrayList<String>();
		
		if( data.size() != factors.size())
			throw new Exception("Data list and factor list must be matched");
		
		for(int x=0; x < factors.size(); x++)
			if( ! factors.get(x).equals(NA_STRING))
			{
				factorCopy.add(factors.get(x));
				dataCopy.add(data.get(x));
			}
		
		this.totalSumSqaured = getTotalSumSquared(dataCopy);
		this.modelSumSquared = getModelSummedSquared(dataCopy, factorCopy);
		this.n = dataCopy.size();
		
		HashSet<String> set = new HashSet<String>();
		
		for( String s : factorCopy)
			set.add(s);
		
		this.r = set.size();
	}
	
	public double getPValue()
	{
		double x = (getModelSumSquared()/(r-1) ) 
		/ (getErrorSumSquared()/(n-r));
			
		return 1- StatFunctions.pf(x , r - 1, n-r);
	}
	
	public double getPValueFromR() throws Exception
	{
		
		double x = (getModelSumSquared()/(r-1) ) 
		/ (getErrorSumSquared()/(n-r));
		
		String command = "pf(" + x + "," + (r - 1) + "," + (n-r) + ")";
		
		return 1-  Rexecute.getOneDoubleFromOneCommand(command);
	}
	
	private static double getModelSummedSquared( List<Number> data, List<String> factors )
	{
		double sum =0;
		
		HashSet<String> factorSet = new HashSet<String>();
		
		for( String s : factors)
			factorSet.add(s);
		
		System.out.println("Total factors:" + factorSet);
		
		Avevar var = new Avevar(data);
		
		for( String s: factorSet )
		{
			List<Number> subList = new ArrayList<Number>();
			
			for(int x=0; x < factors.size(); x++)
				if( factors.get(x).equals(s))
					subList.add(data.get(x));
			
			Avevar subVar = new Avevar(subList);
			double aVal = subVar.getAve() - var.getAve();
			sum+= subList.size() * (aVal * aVal);
		}
		
		return sum;
	}
	
	private static double getTotalSumSquared(List<Number> data)
	{
		double sum =0;
		
		Avevar var = new Avevar(data);
		
		for(Number n : data)
		{
			double aVal = n.doubleValue() - var.getAve();
			sum += aVal * aVal;
		}
		
		return sum;
	}
	
	public static void main(String[] args) throws Exception
	{
		List<Number> data = new ArrayList<Number>();
		
		data.add(1.0);data.add(9.0);data.add(12.0);data.add(11.0);data.add(4.0);
		data.add(6.0);data.add(3.0);data.add(-2.0);data.add(2.0);data.add(7.0);
		
		List<String> factors =new ArrayList<String>();
		
		for( int x=0; x < 5; x++)
			factors.add("A");
		
		for( int x=0; x < 5; x++)
			factors.add("B");
		
		OneWayAnova own = new OneWayAnova(data, factors);
		
		System.out.println(own.getModelSumSquared());
		System.out.println(own.getErrorSumSquared());
		System.out.println(own.getTotalSumSqaured());
		
		System.out.println(own.n);
		System.out.println(own.getPValue());
		
	}
	
}
