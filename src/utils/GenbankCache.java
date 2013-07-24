
package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.StringTokenizer;



public class GenbankCache
{
    private static final String NUCLEOTIDE_PREFIX = 
        "http://www.ncbi.nlm.nih.gov/entrez/viewer.fcgi?db=nucleotide&val=";
    
    private static final String GENBANK_XML_PREFIX=
        "http://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=nucleotide&view=fasta_xml&val=";
    
    public static File getGenbankFastXmlRecord(String genbankId, boolean downloadIfNotThere) 
    	throws Exception
    {
        File aFile = new File(ConfigReader.getGenbankCacheDir() + File.separator 
                + genbankId + ".xml" );
        
        if( ! aFile.exists() && ! downloadIfNotThere )
        	throw new Exception(aFile.getAbsolutePath() + " does not exist");
        	
        if ( downloadIfNotThere &&  ! aFile.exists())
        {
            BufferedWriter writer = new BufferedWriter( new FileWriter(aFile));
            
            URL url = new URL(GENBANK_XML_PREFIX + genbankId );
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
        
        return aFile;
    }
    
    private static String stripDefLineStuff(String genbankId) throws Exception
    {
    	genbankId = genbankId.substring("<TSeq_defline>".length());
    	genbankId = genbankId.substring(0, genbankId.indexOf("<"));
    	return genbankId;
    }
    
    public static String getDefLine(String genbankId, boolean downloadIfNotThere) throws Exception
    {
    	File xmlFile = getGenbankFastXmlRecord(genbankId, downloadIfNotThere);
    	
    	BufferedReader reader = new BufferedReader(new FileReader(xmlFile));
    	
    	String nextLine = reader.readLine();
    	System.out.println(genbankId);
    	while( nextLine != null)
    	{
    		nextLine = nextLine.trim();
    		
    		if( nextLine.startsWith("<TSeq_defline>")) 
    			return stripDefLineStuff(nextLine);
    			
    		nextLine = reader.readLine();
    	}
    	
    	throw new Exception("Could not find defline for " + genbankId);
    }
    
    public static File getGenbankRecord(String genbankId) throws Exception
    {
        File aFile = new File(ConfigReader.getGenbankCacheDir() + File.separator 
                + genbankId + ".html" );
        
        if (! aFile.exists())
        {
            BufferedWriter writer = new BufferedWriter( new FileWriter(aFile));
            
            URL url = new URL(NUCLEOTIDE_PREFIX + genbankId);
            BufferedReader reader = new BufferedReader( new InputStreamReader(
                    	url.openStream() ));
            System.out.println(url.getPath());
            
            String nextLine = reader.readLine();
            
            while ( nextLine != null)
            {
                writer.write(nextLine + "\n");
                nextLine = reader.readLine();
            }
            
            writer.flush();  writer.close();
            reader.close();
        }
        
        return aFile;
    }

    
    /*
     * Given a genbankId in the form of AY687928 return a genbankId in the form of 
     */
    public static String getGenbankNumericaId(String genbankId) throws Exception
    {

		File file = getGenbankRecord(genbankId);
    	BufferedReader reader = new BufferedReader(new FileReader(file));
    	
    	String nextLine = reader.readLine();
    	
    	while(true)
    	{
    		if( nextLine.startsWith("VERSION") )
    		{
    			String last = getLastToken(nextLine);
    			
    			if( ! last.startsWith("GI:") )
    				throw new Exception("Parsing error " + nextLine);
    			
    			last = last.substring(last.indexOf(":") +1);
    			return last;
    		}
    		
    		nextLine= reader.readLine();
    	}
    	
    }
    
    public static String getPublicationTitleLines(String genbankId ) throws Exception
    {
    	BufferedReader reader = new BufferedReader(new FileReader(getGenbankRecord(genbankId)));
    	
    	String nextLine= reader.readLine();
    	
    	StringBuffer buff = new StringBuffer();
		while( nextLine != null)
    	{
			nextLine = nextLine.trim();
    		if( nextLine.startsWith("TITLE"))
    		{
    			boolean readLine = true;
    			
    			while( readLine)
    			{
    				buff.append(nextLine + " ");
    				nextLine= reader.readLine().trim();
    				if( nextLine.indexOf("<a name=") != -1)
        				readLine =false;
    			}
    		}
    		
    		nextLine = reader.readLine();
    	}
    	
    	reader.close();
    	return buff.toString();
    }
    
    private static String getLastToken(String inString)
	{
		StringTokenizer sToken = new StringTokenizer(inString);
		
		String lastString = sToken.nextToken();
		
		while( sToken.hasMoreTokens())
			lastString = sToken.nextToken();
		
		return lastString;
	}

    public static void main(String[] args) throws Exception
	{
		
	}

}
