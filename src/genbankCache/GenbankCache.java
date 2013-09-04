 
package genbankCache;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.URL;


import utils.ConfigReader;

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
public class GenbankCache
{

	public static void main(String[] args) throws Exception
	{
    	writeFastaSequenceForNucleotideIDToGenbankCahce("D37875");
    	writeAnntoationsForNucleotideIDToGenbankCache("D37875");
	}
	
	public static void writeAnntoationsForNucleotideIDToGenbankCache(String nucleotideID)
		throws Exception
	{
		File outFile = new File(ConfigReader.getGenbankCacheDir() + File.separator + 
				nucleotideID + ".xml");
		
	    String urlString =
	    	"http://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=nuccore&id=" + 
	      nucleotideID+ "&retmode=xml";
	    
		System.out.println(urlString);
	    writeHTTPGet(outFile, urlString);
	}
	
	public static void writeFastaSequenceForNucleotideIDToGenbankCahce(String nucleotideID)
		throws Exception
	{
		File outFile = new File(ConfigReader.getGenbankCacheDir() + File.separator + 
				nucleotideID + ".fasta");
		
		String urlString =
           	"http://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=nuccore&id=" + 
            nucleotideID + "&rettype=fasta_cds_na&retmode=text";
	    
		System.out.println(urlString);
	    writeHTTPGet(outFile, urlString);
	}
	
	private static void writeHTTPGet(File outFile, String urlString) throws Exception
    {
		BufferedWriter writer = new BufferedWriter( new FileWriter(outFile));
            
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
