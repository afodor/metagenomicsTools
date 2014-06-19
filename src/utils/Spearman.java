package utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Spearman
{
	private double probrs; // the two-sided significance of the rs from 0
	private double rs; // Spearman's rank correlation
	
	private Spearman( double probrs, double rs ) 
	{
		this.probrs = probrs;
		this.rs = rs;
	}
	
	public static double gammq(double a, double x) throws Exception
	{
		double gln=0;

		if (x < 0.0 || a <= 0.0)
			throw new Exception("Invalid arguments in routine gammq");
		if (x < a+1.0) {
			return 1.0 - gser(a,x,gln);
		} else {
			return gcf(a,x,gln);
	}
}
	
	public static double gammp(double a, double x) throws Exception
	{
		double gln=0;

		if (x < 0.0 || a <= 0.0)
			throw new Exception("Invalid arguments in routine gammp");
		if (x < a+1.0) 
		{
			return gser(a,x,gln);
		} 
		else 
		{
			return 1.0 - gcf(a,x,gln);
		}
	}
	
	public static double gcf(double a, double x, double gln) throws Exception
	{
		int ITMAX=100;
		double EPS=TTest.getEpsilon();
		double FPMIN=Double.MIN_VALUE / EPS;
		int i;
		double an,b,c,d,del,h;

		gln=TTest.gammln(a);
		b=x+1.0-a;
		c=1.0/FPMIN;
		d=1.0/b;
		h=d;
		for (i=1;i<=ITMAX;i++) 
		{
			an = -i*(i-a);
			b += 2.0;
			d=an*d+b;
			if (Math.abs(d) < FPMIN) d=FPMIN;
			c=b+an/c;
			if (Math.abs(c) < FPMIN) c=FPMIN;
			d=1.0/d;
			del=d*c;
			h *= del;
			if (Math.abs(del-1.0) <= EPS) break;
		}
			if (i > ITMAX) 
				throw new Exception("a too large, ITMAX too small in gcf");
	
		return Math.exp(-x+a*Math.log(x)-gln)*h;
}
	
	public static double gser(double a, double x, double gln) throws Exception
	{
		int ITMAX=100;
		double EPS=TTest.getEpsilon();
		int n;
		double sum,del,ap;

		gln=TTest.gammln(a);
		if (x <= 0.0) 
		{
			if (x < 0.0) 
				throw new Exception("x less than 0 in routine gser");
			return 0.0;
		} 
		else 
		{
			ap=a;
			del=sum=1.0/a;
			for (n=0;n<ITMAX;n++) 
			{
				++ap;
				del *= x/ap;
				sum += del;
				if (Math.abs(del) < Math.abs(sum)*EPS) 
				{
					return sum*Math.exp(-x+a*Math.log(x)-gln);
				}
			}
			throw new Exception("a too large, ITMAX too small in routine gser");
		}
	}

	/**  From two lists of Floats
	 */
	public static Spearman getSpear( List<Float> data1, List<Float> data2 ) throws Exception
	{
		if ( data1.size() != data2.size() ) 
			throw new Exception("Error!  lists have unequal sizes");
		
		float[] f1 = new float[ data1.size() ];
		float[] f2 = new float[ data2.size() ];
		
		for ( int x=0; x< data1.size(); x++ ) 
			f1[x] = data1.get(x);
			
		for ( int x=0; x< data2.size(); x++) 
			f2[x] = data2.get(x);
			
		return getSpear(f1, f2);
		
	}
	
		/**  From two lists of doubles
	 */
	public static Spearman getSpearFromDouble( List<Double> data1, List<Double> data2 ) throws Exception
	{
		if ( data1.size() != data2.size() ) 
			throw new Exception("Error!  lists have unequal sizes");
		
		double[] f1 = new double[ data1.size() ];
		double[] f2 = new double[ data2.size() ];
		
		for ( int x=0; x< data1.size(); x++ ) 
			f1[x] = data1.get(x);
			
		for ( int x=0; x< data2.size(); x++) 
			f2[x] = data2.get(x);
			
		return getSpear(f1, f2);
		
	}

	
	/**  Numerical Recipies in C++ - p. 646
	 *   Note that our liscense does not allow us to redisribute this code
	 */
	public static Spearman getSpear(float[] data1, float[] data2)
	{
		int j;
		double vard,t,sg,sf,fac,en3n,en,df,aved;
		@SuppressWarnings("unused")
		double d, zd, probd, rs, probrs;

		int n=data1.length;
		float[] wksp1 = new float[n];
		float[] wksp2 = new float[n];
		for (j=0;j<n;j++) 
		{
			wksp1[j]=data1[j];
			wksp2[j]=data2[j];
		}
		sort2(wksp1,wksp2);
		sf = crank(wksp1);
		sort2(wksp2,wksp1);
		sg = crank(wksp2);
		d=0.0;
		for (j=0;j<n;j++)
			d += (wksp1[j]-wksp2[j]) * (wksp1[j]-wksp2[j]);
		en=n;
		en3n=en*en*en-en;
		aved=en3n/6.0-(sf+sg)/12.0;
		fac=(1.0-sf/en3n)*(1.0-sg/en3n);
		vard=((en-1.0)*en*en*(en+1.0)*(en+1.0)/36.0)*fac;
		zd=(d-aved)/Math.sqrt(vard);
		probd=erfcc(Math.abs(zd)/1.4142136);
		rs=(1.0-(6.0/en3n)*(d+(sf+sg)/12.0))/Math.sqrt(fac);
		fac=(rs+1.0)*(1.0-rs);
		if (fac > 0.0) {
			t=rs*Math.sqrt((en-2.0)/fac);
			df=en-2.0;
			probrs= TTest.betai(0.5*df,0.5,df/(df+t*t));
		} 
		else
			probrs=0.0;
		
		return new Spearman(probrs, rs );
	}
	
	/**  Numerical Recipies in C++ - p. 646
	 *   Note that our liscense does not allow us to redisribute this code
	 */
	public static Spearman getSpear(double[] data1, double[] data2)
	{
		int j;
		double vard,t,sg,sf,fac,en3n,en,df,aved;
		@SuppressWarnings("unused")
		double d, zd, probd, rs, probrs;

		int n=data1.length;
		double[] wksp1 = new double[n];
		double[] wksp2 = new double[n];
		for (j=0;j<n;j++) 
		{
			wksp1[j]=data1[j];
			wksp2[j]=data2[j];
		}
		sort2(wksp1,wksp2);
		sf = crank(wksp1);
		sort2(wksp2,wksp1);
		sg = crank(wksp2);
		d=0.0;
		for (j=0;j<n;j++)
			d += (wksp1[j]-wksp2[j]) * (wksp1[j]-wksp2[j]);
		en=n;
		en3n=en*en*en-en;
		aved=en3n/6.0-(sf+sg)/12.0;
		fac=(1.0-sf/en3n)*(1.0-sg/en3n);
		vard=((en-1.0)*en*en*(en+1.0)*(en+1.0)/36.0)*fac;
		zd=(d-aved)/Math.sqrt(vard);
		probd=erfcc(Math.abs(zd)/1.4142136);
		rs=(1.0-(6.0/en3n)*(d+(sf+sg)/12.0))/Math.sqrt(fac);
		fac=(rs+1.0)*(1.0-rs);
		if (fac > 0.0) {
			t=rs*Math.sqrt((en-2.0)/fac);
			df=en-2.0;
			probrs= TTest.betai(0.5*df,0.5,df/(df+t*t));
		} 
		else
			probrs=0.0;
		
		return new Spearman(probrs, rs );
	}
	
	static double crank(float[] w)
	{
		float s;
		
		int j=1,ji,jt;
		float t,rank;

		int n=w.length;
		s=0.0f;
		while (j < n) 
		{
			if (w[j] != w[j-1]) 
			{
				w[j-1]=j;
				++j;
			} 
			else 
			{
				for (jt=j+1;jt<=n && w[jt-1]==w[j-1];jt++);
				rank=0.5f*(j+jt-1);
				for (ji=j;ji<=(jt-1);ji++)
					w[ji-1]=rank;
				t=jt-j;
				s += (t*t*t-t);
				j=jt;
			}
		}
		if (j == n) w[n-1]=n;
		
		return s;
	}
	
	static double crank(double[] w)
	{
		float s;
		
		int j=1,ji,jt;
		float t,rank;

		int n=w.length;
		s=0.0f;
		while (j < n) 
		{
			if (w[j] != w[j-1]) 
			{
				w[j-1]=j;
				++j;
			} 
			else 
			{
				for (jt=j+1;jt<=n && w[jt-1]==w[j-1];jt++);
				rank=0.5f*(j+jt-1);
				for (ji=j;ji<=(jt-1);ji++)
					w[ji-1]=rank;
				t=jt-j;
				s += (t*t*t-t);
				j=jt;
			}
		}
		if (j == n) w[n-1]=n;
		
		return s;
	}
	
	static double erfcc(double x)
	{
		double t,z,ans;

		z=Math.abs(x);
		t=1.0/(1.0+0.5*z);
		ans=t*Math.exp(-z*z-1.26551223+t*(1.00002368+t*(0.37409196+t*(0.09678418+
							t*(-0.18628806+t*(0.27886807+t*(-1.13520398+t*(1.48851587+
								t*(-0.82215223+t*0.17087277)))))))));
		return (x >= 0.0 ? ans : 2.0-ans);
	}
	
	static void sort2(float[] arr, float[] brr)
	{
		int M=7,NSTACK=50;
		int i,ir,j,k,jstack=-1,l=0;
		float a,b;
		int[] istack = new int[NSTACK];

		int n=arr.length;
		ir=n-1;
		for (;;) {
			if (ir-l < M) {
				for (j=l+1;j<=ir;j++) {
					a=arr[j];
					b=brr[j];
					for (i=j-1;i>=l;i--) {
						if (arr[i] <= a) break;
						arr[i+1]=arr[i];
						brr[i+1]=brr[i];
					}
					arr[i+1]=a;
					brr[i+1]=b;
				}
				if (jstack < 0) break;
				ir=istack[jstack--];
				l=istack[jstack--];
			} else {
				k=(l+ir) >> 1;
				swap( arr, k, l+1); //SWAP(arr[k],arr[l+1]);
				swap( brr, k, l+1); // SWAP(brr[k],brr[l+1]);
				if (arr[l] > arr[ir]) {
					swap( arr, l, ir ); //	SWAP(arr[l],arr[ir]);
					swap( brr, l, ir ); // SWAP(brr[l],brr[ir]);
				}
				if (arr[l+1] > arr[ir]) {
					swap( arr, l+1, ir) ; // SWAP(arr[l+1],arr[ir]);
					swap( brr, l+1, ir) ; // SWAP(brr[l+1],brr[ir]);
				}
				if (arr[l] > arr[l+1]) {
					swap( arr, l, l+1 );  // SWAP(arr[l],arr[l+1]);
					swap( brr, l, l+1 ); // SWAP(brr[l],brr[l+1]);
				}
				i=l+1;
				j=ir;
				a=arr[l+1];
				b=brr[l+1];
				for (;;) {
					do i++; while (arr[i] < a);
					do j--; while (arr[j] > a);
					if (j < i) break;
					swap( arr, i, j) ; // SWAP(arr[i],arr[j]);
					swap( brr, i, j) ; // SWAP(brr[i],brr[j]);
				}
				arr[l+1]=arr[j];
				arr[j]=a;
				brr[l+1]=brr[j];
				brr[j]=b;
				jstack += 2;
				if (jstack >= NSTACK) throw new RuntimeException("NSTACK too small in sort2.");
				if (ir-i+1 >= j-l) {
					istack[jstack]=ir;
					istack[jstack-1]=i;
					ir=j-1;
				} else {
					istack[jstack]=j-1;
					istack[jstack-1]=l;
					l=i;
				}
			}
		}
	}
	
	static void sort2(double[] arr, double[] brr)
	{
		int M=7,NSTACK=50;
		int i,ir,j,k,jstack=-1,l=0;
		double a,b;
		int[] istack = new int[NSTACK];

		int n=arr.length;
		ir=n-1;
		for (;;) {
			if (ir-l < M) {
				for (j=l+1;j<=ir;j++) {
					a=arr[j];
					b=brr[j];
					for (i=j-1;i>=l;i--) {
						if (arr[i] <= a) break;
						arr[i+1]=arr[i];
						brr[i+1]=brr[i];
					}
					arr[i+1]=a;
					brr[i+1]=b;
				}
				if (jstack < 0) break;
				ir=istack[jstack--];
				l=istack[jstack--];
			} else {
				k=(l+ir) >> 1;
				swap( arr, k, l+1); //SWAP(arr[k],arr[l+1]);
				swap( brr, k, l+1); // SWAP(brr[k],brr[l+1]);
				if (arr[l] > arr[ir]) {
					swap( arr, l, ir ); //	SWAP(arr[l],arr[ir]);
					swap( brr, l, ir ); // SWAP(brr[l],brr[ir]);
				}
				if (arr[l+1] > arr[ir]) {
					swap( arr, l+1, ir) ; // SWAP(arr[l+1],arr[ir]);
					swap( brr, l+1, ir) ; // SWAP(brr[l+1],brr[ir]);
				}
				if (arr[l] > arr[l+1]) {
					swap( arr, l, l+1 );  // SWAP(arr[l],arr[l+1]);
					swap( brr, l, l+1 ); // SWAP(brr[l],brr[l+1]);
				}
				i=l+1;
				j=ir;
				a=arr[l+1];
				b=brr[l+1];
				for (;;) {
					do i++; while (arr[i] < a);
					do j--; while (arr[j] > a);
					if (j < i) break;
					swap( arr, i, j) ; // SWAP(arr[i],arr[j]);
					swap( brr, i, j) ; // SWAP(brr[i],brr[j]);
				}
				arr[l+1]=arr[j];
				arr[j]=a;
				brr[l+1]=brr[j];
				brr[j]=b;
				jstack += 2;
				if (jstack >= NSTACK) throw new RuntimeException("NSTACK too small in sort2.");
				if (ir-i+1 >= j-l) {
					istack[jstack]=ir;
					istack[jstack-1]=i;
					ir=j-1;
				} else {
					istack[jstack]=j-1;
					istack[jstack-1]=l;
					l=i;
				}
			}
		}
	}
	
	private static void swap( float[] arr, int i, int j ) 
	{
		float temp = arr[i];
		arr[i] = arr[j];
		arr[j] = temp;
	}		
	
	private static void swap( double[] arr, int i, int j ) 
	{
		double temp = arr[i];
		arr[i] = arr[j];
		arr[j] = temp;
	}		
	
	/**  In the most inefficient way imaginable, returns the ranks of a given array
	 */
	public static int[] getRanks( float[] array ) 
	{
		List<ArrayHolder> list = new ArrayList<ArrayHolder>();		
		
		for ( int x=0; x< array.length; x++ ) 
			list.add( new ArrayHolder( x, array[x]));	
			
		Collections.sort( list );
		
		int[] returnArray = new int[ array.length ];
		
		for ( int x=0; x< list.size(); x++ ) 
		{
			returnArray[((ArrayHolder) list.get(x)).arrayIndex] = x;
		}
		
		return returnArray;
	}
	
	private static class ArrayHolder implements Comparable<ArrayHolder>
	{
		int arrayIndex;
		Float arrayValue;
		
		ArrayHolder( int arrayIndex, float arrayValue) 
		{
			this.arrayIndex = arrayIndex;
			this.arrayValue = new Float( arrayValue);	
		}
		
		
		public int compareTo(ArrayHolder o)
		{
			return arrayValue.compareTo( o.arrayValue );
		}

	}
	
	
	public String toString()
	{
		return "rank correlation = " + this.rs + " prob= " + this.probrs;
	}

	public double getProbrs()
	{
		return probrs;
	}

	public double getRs()
	{
		return rs;
	}

}
