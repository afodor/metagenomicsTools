
package genbankCache;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.URL;


/** 
 * Author:  anthony.fodor@gmail.com    
 * This code is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 2
* of the License, or (at your option) any later version,
* provided that any use properly credits the author.
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details at http://www.gnu.org * * */

public class AnnotationFileGet
{
    public static void main(String[] args) throws Exception
    {
    	for( int x=10; x <=27; x++)
    	{
    		
    		String s = "NZ_AEFA010000" + x;
    		String urlString= "http://www.xbase.ac.uk/genome/escherichia-coli-nc101/" + s + "/features";
    		System.out.println(s);
    		
    	    BufferedWriter writer = new BufferedWriter( new FileWriter("c:\\temp\\" +s + ".html" ));
            
    	    URL url = new URL(urlString);
            BufferedReader reader = new BufferedReader( new InputStreamReader(
                    	url.openStream() ));
            
            String nextLine = reader.readLine();
            
            while ( nextLine != null)
            {
                writer.write(nextLine + "\n");
                nextLine = reader.readLine();
            }
            
            writer.flush();  writer.close();
            reader.close();
        }
        
    }
}
