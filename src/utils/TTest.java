package utils;

import java.util.ArrayList;
import java.util.List;

public class TTest
{
	/**  Static methods only
	 */
	private TTest()
	{
		
	}
	
	public static double getAverage( List<Number> list ) throws Exception
	{
		int count =0;
		double sum =0;
		
		for ( int x=0; x< list.size(); x++ ) 
		{
			count++;
			sum += list.get(x).doubleValue();
		}
		
		return sum / count;
	}
	
	public static double getStDev( List<Number> data ) throws Exception
	{
		int j;
		@SuppressWarnings({"unused"}) double ep=0.0,s,p;
		s =0.0;
		
		int n=data.size();
		if (n <= 1) throw new Exception("n must be at least 2 in moment");
		for (j=0;j<n;j++) s += data.get(j).doubleValue();
		double ave=s/n;
		//double adev=0.0;
		double var=0.0;;
		for (j=0;j<n;j++) {
		s=data.get(j).doubleValue()-ave;
		ep += s;
		var += (p=s*s);	
		}
		//adev /= n;
		var=(var-ep*ep/n)/(n-1);
		return Math.sqrt(var);
	}
	
	public static double lnfactorial(double number)
	{
  		return(gammln(number+1));
	}
	
	public static double beta(double z, double w) 
	{ 
		return Math.exp(gammln(z)+gammln(w)-gammln(z+w)); 
	} 

	
	public static double fDensityFunction( double xx, float n, float m )
	{
		double top = Math.pow( m, m/2 );
		top = top * Math.pow( n, n/2 );
		top = top * Math.pow( xx, n/2-1 );
		
		double bottom = Math.pow( m + n * xx, (n+m) /2 );
		bottom = bottom * beta( n /2, m/ 2 );
		
		return top/bottom;
	}
	
	public static double gammln( double xx ) 
	{
		int j;
		double x, y, tmp, ser;
		double[] cof = {
						76.18009172947146,
						-86.50532032941677,
						24.01409824083091,
						-1.231739572450155,
						0.1208650973866179e-2,
						-0.539523938495e-5
					   };
		
		y=x=xx;
		tmp=x+5.5;
		tmp -= (x+0.5)*Math.log(tmp);
		ser=1.000000000190015;
		for ( j=0;j<6;j++) ser += cof[j]/++y;
		return -tmp+Math.log(2.5066282746310005*ser/x);
	}
	
	
	public static double betai( double a, double b, double x ) 
	{
		double bt;
		
		if ( x < 0.0 || x > 1.0 ) 
			throw new RuntimeException("x " + x + " out of bound");
		
		if ( x == 0 || x == 1.0 ) 
			bt = 0.0;
		else
			bt = Math.exp( gammln( a+b ) - gammln(a) - gammln(b) 
						   + a * Math.log( x ) + b * Math.log( 1.0 - x ));
		
		if ( x < (a + 1.0) / (a+b+2.0))
			return bt * betacf(a,b,x)/a;
		else
			return 1.0 - bt * betacf( b,a, 1.0 -x) / b;
	}
	
	public static double betacf( double a, double b, double x )
	{
		int MAXIT=100;
		double EPS = getEpsilon();
		double FPMIN = Double.MIN_VALUE / EPS;
		
		int m, m2;
		double aa, c, d, del, h, qab, qam, qap;
		
		qab = a+b;
		qap = a + 1.0;
		qam = a - 1.0;
		
		c=1.0;
		d=1.0-qab*x/qap;
		
		if ( Math.abs(d) < FPMIN ) d=FPMIN;
		d = 1.0/d;
		h=d;
		
		for( m=1;m<=MAXIT;m++) {
			m2 = 2*m;
			aa = m*(b-m)*x / ((qam+m2)*(a+m2));
			d = 1.0 + aa * d;
			if ( Math.abs(d) < FPMIN ) d = FPMIN;
			c= 1.0 + aa / c;
			if ( Math.abs(c) < FPMIN) c = FPMIN;
			d= 1.0 / d;
			h *= d * c;
			aa = -(a+m) * (qab + m ) * x / ((a+m2) * ( qap + m2));
			d=1.0 + aa * d;
			if ( Math.abs(d) < FPMIN) d = FPMIN;
			c = 1.0 + aa / c;
			if ( Math.abs(c) < FPMIN) c= FPMIN;
			d=1.0/d;
			del=d*c;
			h *= del;
			if ( Math.abs( del - 1.0 ) <= EPS ) break;
		}
		
		if ( m > MAXIT ) 
		{
		    
			throw new RuntimeException("a or b too big, of MAXIT too small");
		}
		
		return h;
	}
	
	/**  This method returns the difference between 1 and the smallest value 
	 *   greater than 1 that is representable for double 
	 */
	public static double getEpsilon()
	{
		long oneLong = Double.doubleToLongBits( 1.0 );
		
		long oneLongPlus = oneLong | Double.doubleToLongBits( Double.MIN_VALUE );
		
		double dblPlus = Double.longBitsToDouble( oneLongPlus );
		double dblOne = Double.longBitsToDouble( oneLong );
		
		return dblPlus - dblOne;
	}
	
	
	@SuppressWarnings("unused")
	private static void printBinary( long l )
	{
		for(int i = 63; i >=0; i--)
			if(((1L << i) & l) != 0)
				System.out.print("1");
			else
				System.out.print("0");
		
		System.out.println();
	}
	
	public static StatisticReturnObject ttest(double[] data1, double[] data2)
	{
		double t;
		double prob;
		
		double var1, var2, svar, df, ave1, ave2;
		
		int n1 = data1.length;
		int n2 = data2.length;
		
		Avevar av1 = new Avevar( data1 );
		ave1 = av1.ave;
		var1 = av1.var;
		
		Avevar av2 = new Avevar( data2 );
		ave2 = av2.ave;
		var2 = av2.var;
		
		df = n1 + n2 -2;
		svar = ((n1-1) * var1 + (n2-1)*var2)/df;
		t=(ave1-ave2)/Math.sqrt( svar * (1.0/n1 + 1.0/n2));
		prob = betai( 0.5 * df, 0.5, df / ( df + t * t ));
		
		return new StatisticReturnObject(t, prob);
	}
	
	public static StatisticReturnObject ttest(List<Float> data1, List<Float> data2)
	{
		double t;
		double prob;
		
		double var1, var2, svar, df, ave1, ave2;
		
		int n1 = data1.size();
		int n2 = data2.size();
		
		Avevar av1 = new Avevar( data1 );
		ave1 = av1.ave;
		var1 = av1.var;
		
		Avevar av2 = new Avevar( data2 );
		ave2 = av2.ave;
		var2 = av2.var;
		
		df = n1 + n2 -2;
		svar = ((n1-1) * var1 + (n2-1)*var2)/df;
		t=(ave1-ave2)/Math.sqrt( svar * (1.0/n1 + 1.0/n2));
		prob = betai( 0.5 * df, 0.5, df / ( df + t * t ));
		
		return new StatisticReturnObject(t, prob);
	}
	
	/*
	 * 
	 */
	public static StatisticReturnObject safeTTestFromNumber(List<? extends Number> data1, List<? extends Number> data2 )
	{
		try
		{
			return ttestFromNumber(data1, data2);
		}
		catch(Exception e)
		{
			System.out.println("Warning t-test failed");
		}
		
		return new StatisticReturnObject(0, 1);
	}
	 
	public static StatisticReturnObject ttestFromNumberUnequalVariance(List<? extends Number> data1, List<? extends Number> data2)
	{
		double t;
		double prob;
		
		double var1, var2, df, ave1, ave2;
		
		int n1 = data1.size();
		int n2 = data2.size();
		
		Avevar av1 = new Avevar( data1 );
		ave1 = av1.ave;
		var1 = av1.var;
		
		Avevar av2 = new Avevar( data2 );
		ave2 = av2.ave;
		var2 = av2.var;
		
		double top = ave1 - ave2;
		double bottom = Math.sqrt(var1/n1 + var2/n2);
		t = top / bottom;
		
		df = var1 / n1 + var2 / n2;
		df = df * df;
		double dfBottom = (var1/n1) * (var1/n1) / (n1-1);
		dfBottom = dfBottom + (var2/n2) * (var2/n2) /(n2-1);
		df = df / dfBottom;
		
		prob = betai( 0.5 * df, 0.5, df / ( df + t * t ));
		
		return new StatisticReturnObject(t, prob);
	}
	
	
	public static StatisticReturnObject ttestFromNumber(List<? extends Number> data1, List<? extends Number> data2)
	{
		double t;
		double prob;
		
		double var1, var2, svar, df, ave1, ave2;
		
		int n1 = data1.size();
		int n2 = data2.size();
		
		Avevar av1 = new Avevar( data1 );
		ave1 = av1.ave;
		var1 = av1.var;
		
		Avevar av2 = new Avevar( data2 );
		ave2 = av2.ave;
		var2 = av2.var;
		
		df = n1 + n2 -2;
		svar = ((n1-1) * var1 + (n2-1)*var2)/df;
		t=(ave1-ave2)/Math.sqrt( svar * (1.0/n1 + 1.0/n2));
		prob = betai( 0.5 * df, 0.5, df / ( df + t * t ));
		
		return new StatisticReturnObject(t, prob);
	}
	

	public static StatisticReturnObject ttestAndP(List<Float> data1, List<Float> data2)
	{

		double t;
		double prob;

		

		double var1, var2, svar, df, ave1, ave2;

		int n1 = data1.size();
		int n2 = data2.size();

		

		Avevar av1 = new Avevar( data1 );
		ave1 = av1.ave;
		var1 = av1.var;

		

		Avevar av2 = new Avevar( data2 );
		ave2 = av2.ave;
		var2 = av2.var;

		

		df = n1 + n2 -2;
		svar = ((n1-1) * var1 + (n2-1)*var2)/df;
		t=(ave1-ave2)/Math.sqrt( svar * (1.0/n1 + 1.0/n2));
		prob = betai( 0.5 * df, 0.5, df / ( df + t * t ));


		return new StatisticReturnObject(t, prob);
	}
    
	public static double ttestT(List<Float> data1, List<Float> data2)
	{
		double t;
		
		double var1, var2, svar, df, ave1, ave2;
		
		int n1 = data1.size();
		int n2 = data2.size();
		
		Avevar av1 = new Avevar( data1 );
		ave1 = av1.ave;
		var1 = av1.var;
		
		Avevar av2 = new Avevar( data2 );
		ave2 = av2.ave;
		var2 = av2.var;
		
		df = n1 + n2 -2;
		svar = ((n1-1) * var1 + (n2-1)*var2)/df;
		t=(ave1-ave2)/Math.sqrt( svar * (1.0/n1 + 1.0/n2));
		
		return t;
	}
	
	
	public static double getPooledVariance(List<Float> data1, List<Float> data2)
	{
		double var1, var2, svar, df;
		
		int n1 = data1.size();
		int n2 = data2.size();
		
		Avevar av1 = new Avevar( data1 );
		var1 = av1.var;
		
		Avevar av2 = new Avevar( data2 );
		var2 = av2.var;
		
		df = n1 + n2 -2;
		svar = ((n1-1) * var1 + (n2-1)*var2)/df;
		return svar * (1.0/n1 + 1.0/n2);
	}
	
	
	public static StatisticReturnObject tptest(float[] data1, float[] data2)
    {
        int j;
        double var1,var2,ave1,ave2,sd,df,cov=0.0;
        double t, prob;

        int n=data1.length;
        
        Avevar av1 = new Avevar( data1 );
		ave1 = av1.ave;
		var1 = av1.var;
		
		Avevar av2 = new Avevar( data2 );
		ave2 = av2.ave;
		var2 = av2.var;
        
        for (j=0;j<n;j++)
        cov += (data1[j]-ave1)*(data2[j]-ave2);
        cov /= df=n-1;
        sd=Math.sqrt((var1+var2-2.0*cov)/n);
        t=(ave1-ave2)/sd;
        prob=betai(0.5*df,0.5,df/(df+t*t));
        
        return new StatisticReturnObject(t, prob);
    }

	public static StatisticReturnObject pairedTTest(List<? extends Number> list1, List<? extends Number> list2) 
		throws Exception
	{
		
		if( list1.size() != list2.size())
			throw new Exception("Lists must be of equal size");
		
		List<Double> diffs = new ArrayList<Double>();
		
		for( int x=0; x < list1.size(); x++ )
		{
			if( list1.get(x) != null && list2.get(x) != null)
				diffs.add(list1.get(x).doubleValue() - list2.get(x).doubleValue());
		}
		
		Avevar av = new Avevar(diffs);
		
		double t= av.getAve() / (av.getSqrt() / Math.sqrt(diffs.size()));
		double df = diffs.size() -1;
		double p= betai( 0.5 * df, 0.5, df / ( df + t * t ));
		
		return new StatisticReturnObject(t,p);
	}
	

    /**
     * if ( forceDegreesOfFreedom <= 0), calculate degegrees of freedom the normal way
     */
    public static StatisticReturnObject ttest(float[] data1, float[] data2, float forceDegreesOfFreedom)
	{
		boolean nonZero = false;
		
		for ( int x=0; x < data1.length && ! nonZero; x++ ) 
			if ( data1[x] != 0 ) 
				nonZero = true;
		
		for ( int x=0; x < data2.length && ! nonZero; x++ ) 
			if ( data2[x] != 0 ) 
				nonZero = true;
		
		if ( ! nonZero ) 
			return new StatisticReturnObject(0, 1);
		
		double t;
		double prob;
		
		double var1, var2, svar, df, ave1, ave2;
		
		int n1 = data1.length;
		int n2 = data2.length;
		
		Avevar av1 = new Avevar( data1 );
		ave1 = av1.ave;
		var1 = av1.var;
		
		Avevar av2 = new Avevar( data2 );
		ave2 = av2.ave;
		var2 = av2.var;
		
		if ( forceDegreesOfFreedom> 0 ) 
			df = forceDegreesOfFreedom;
		else
			df = n1 + n2 -2;
		
		svar = ((n1-1) * var1 + (n2-1)*var2)/df;
		t=(ave1-ave2)/Math.sqrt( svar * (1.0/n1 + 1.0/n2));
		prob = betai( 0.5 * df, 0.5, df / ( df + t * t ));
		
		return new StatisticReturnObject(t, prob);
	}
    
	public static void main(String[] args) throws Exception
	{
		List<Float> l1 = new ArrayList<Float>();
		List<Float> l2 = new ArrayList<Float>();

		l1.add(10f);l1.add(20f);l1.add(30f);l1.add(40f);l1.add(60f);
		l2.add(40f);l2.add(100f);l2.add(120f);l2.add(100f);l2.add(100f);l2.add(90f);

		StatisticReturnObject results = TTest.ttest(l1,l2);

		// the score here is the T value.  According to R it should be -4.1139
		System.out.println(results.getScore());

		//  According to R it should be 0.002622
		System.out.println(results.getPValue());
	}
}
