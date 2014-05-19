package utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ProcessWrapper
{
	
	public ProcessWrapper( String[] cmdArgs ) throws Exception
	{
		for ( int x=0; x < cmdArgs.length; x++ )
				System.out.print(cmdArgs[x] + " " );
			
			System.out.println();	
		
		Runtime r = Runtime.getRuntime();
		Process p = r.exec(cmdArgs);
		
		BufferedReader br = new BufferedReader (new InputStreamReader(p.getInputStream ()));
		
		String s;
		
		while ((s = br.readLine ())!= null)
		{
    		System.out.println (s);
		}
				
		p.waitFor();
		p.destroy();
	}
	
	public static void main(String[] args) throws Exception
	{
		String[] a= new String[2];
		a[0] = "c:\\temp\\myScript.bat";
		a[1] = "locationOfConfigFile";
		new ProcessWrapper(a);
	}
}
