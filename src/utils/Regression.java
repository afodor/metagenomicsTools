package utils;

import java.util.ArrayList;
import java.util.List;

public class Regression
{
	private double a, b, stdErrorA, stdErrorB, chi2, q,df;
	
	public void fitFromList(List<? extends Number> xList, List<? extends Number> yList) throws Exception
	{
		fitFromList(xList, yList, new ArrayList<Number>(), false);
	}
	
	public void fitFromList( List<? extends Number> xList, List<? extends Number> yList, 
			List<Number> sigList, boolean mwt ) throws Exception
	{
		if ( xList.size() != yList.size() ) 
			throw new Exception("Unequal sizes");
			
		if ( mwt && yList.size() != sigList.size() ) 
			throw new Exception("Unequal sizes");
			
		df = xList.size() -2;
		
		double[] x = new double[xList.size()];
		double[] y=  new double[yList.size()];
		double [] sigs = sigList==null? null : new double[sigList.size()];
		
		for ( int i = 0; i < xList.size(); i++ ) 
		{
			x[i] = xList.get(i).doubleValue();
			y[i] = yList.get(i).doubleValue();
			
			if ( mwt ) 
			{
				sigs[i] = ((Float) sigList.get(i)).doubleValue();	
			}
		}	
		
		fit( x, y, sigs, mwt );
	}
	
	public void fit(double[] x, double[] y, double[] sig, boolean mwt) throws Exception
	{
		int i;
		double wt,t,sxoss,sx=0.0,sy=0.0,st2=0.0,ss,sigdat;

		int ndata=x.length;
		b=0.0;
		if (mwt) 
		{
			ss=0.0;
			for (i=0;i<ndata;i++) 
			{
				wt=1.0/(sig[i]* sig[i]);
				ss += wt;
				sx += x[i]*wt;
				sy += y[i]*wt;
			}
		} 
		else 
		{
			for (i=0;i<ndata;i++) 
			{
				sx += x[i];
				sy += y[i];
			}
			ss=ndata;
		}
		
		sxoss=sx/ss;
		
		if (mwt) 
		{
		
			for (i=0;i<ndata;i++) 
			{
				t=(x[i]-sxoss)/sig[i];
				st2 += t*t;
				b += t*y[i]/sig[i];
			}
		} 
		else 
		{
			for (i=0;i<ndata;i++) 
			{
				t=x[i]-sxoss;
				st2 += t*t;
				b += t*y[i];
			}
		}
		b /= st2;
		a=(sy-sx*b)/ss;
		stdErrorA=Math.sqrt((1.0+sx*sx/(ss*st2))/ss);
		stdErrorB=Math.sqrt(1.0/st2);
		chi2=0.0;
		q=1.0;
		if (!mwt) 
		{
			for (i=0;i<ndata;i++)
			chi2 += (y[i]-a-b*x[i]) * (y[i]-a-b*x[i]);
			sigdat=Math.sqrt(chi2/(ndata-2));
			stdErrorA *= sigdat;
			stdErrorB *= sigdat;
		} 
		else 
		{
			for (i=0;i<ndata;i++)
				chi2 += ((y[i]-a-b*x[i])/sig[i]) * ((y[i]-a-b*x[i])/sig[i]);
			
			if (ndata>2) q=Spearman.gammq(0.5*(ndata-2),0.5*chi2);
	}
}
	public double getA()
	{
		return a;
	}

	public double getB()
	{
		return b;
	}

	public double getChi2()
	{
		return chi2;
	}

	public double getQ()
	{
		return q;
	}

	public double getStdErrorA()
	{
		return stdErrorA;
	}

	public double getStdErrorB()
	{
		return stdErrorB;
	}
	
	public double getPValueForSlope()
	{
		double t = this.b / this.stdErrorB;
		
		double returnVal = 1;
		
		try
		{
			returnVal = TTest.betai( 0.5 * df, 0.5, df / ( df + t * t ));
		}
		catch(Exception e)
		{
			System.out.println("Warning!  p-value not calculable");
		}
		
		return returnVal;
	}

	
	public static void main(String[] args) throws Exception
	{
		Regression r = new Regression();
		
		List<Number> xList = new ArrayList<Number>();
		List<Number> yList = new ArrayList<Number>();
		
		xList.add(1);xList.add(12);xList.add(14);xList.add(17);xList.add(19);xList.add(21);
		yList.add(2);yList.add(14);yList.add(15);yList.add(16);yList.add(21);yList.add(22.5);
		
		r.fitFromList(xList, yList);
		
		System.out.println(r.a);
		System.out.println(r.b);
		System.out.println(r.stdErrorB);
		System.out.println(r.q);
		System.out.println(r.chi2);
		System.out.println(r.getPValueForSlope());
	}
}
