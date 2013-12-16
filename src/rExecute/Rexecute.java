package rExecute;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import utils.ConfigReader;

public class Rexecute
{
	public static double getOneDoubleFromOneCommand(String command) throws Exception
	{
		File tempFile = new File(ConfigReader.getRDirectory() + File.separator + "temp123.txt");
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
		
		writer.write(command + "\n");
		
		writer.flush();  writer.close();
		
		Runtime r = Runtime.getRuntime();
		
		String[] args = new String[3];
		args[0] = new File(ConfigReader.getRDirectory() + File.separator + "R").getAbsolutePath();
		args[1] = "-f";
		args[2] = tempFile.getAbsolutePath();
		
		for(String s : args)
			System.out.print( s + " " );
		
		System.out.println();
		
		Process p = r.exec(args);
		
		BufferedReader br = new BufferedReader (new InputStreamReader(p.getInputStream ()));
		
		Double returnVal = null;
		
		String s;
		
		while ((s = br.readLine ())!= null)
		{
    		System.out.println (s);
    		
    		if( s.startsWith("[1]") )
    		{
    			StringTokenizer sToken = new StringTokenizer(s);
    			sToken.nextToken();
    			returnVal = Double.parseDouble(sToken.nextToken());
    		}
		}
				
		p.waitFor();
		p.destroy();
		
		if(returnVal == null)
			throw new Exception("Parsing error");
		
		return returnVal;
	}
}
