package malcolmParsers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.StringTokenizer;

public class Table2Parser
{
	private final String isolateNumber;
	private final String species;
	private final HashSet<String> phenotypicProfiles= new HashSet<String>();
	private final HashSet<String> genotypicProfiles =new HashSet<String>();
	private final int year;
	
	@Override
	public String toString()
	{
		return isolateNumber + " " + species + " " + phenotypicProfiles + " " + year + " " + genotypicProfiles;
	}
	
	public String getIsolateNumber()
	{
		return isolateNumber;
	}

	public String getSpecies()
	{
		return species;
	}

	public HashSet<String> getPhenotypicProfiles()
	{
		return phenotypicProfiles;
	}

	public HashSet<String> getGenotypicProfiles()
	{
		return genotypicProfiles;
	}

	public int getYear()
	{
		return year;
	}

	private Table2Parser(String s) throws Exception
	{
		StringTokenizer sToken = new StringTokenizer(s, " ");
		this.isolateNumber = sToken.nextToken();
		this.species = sToken.nextToken();
		
		String phenoString = sToken.nextToken();
		
		if( phenoString.equals("pansusceptible"))
		{
			phenotypicProfiles.add(phenoString);
		}
		else
		{
			for( int x=0; x < phenoString.length(); x= x + 2)
			{
				String substring = phenoString.substring(x, x+2);
				if( phenotypicProfiles.contains(substring) )
					throw new Exception("Parsing error");
				
				phenotypicProfiles.add(substring);
			}
		}
		 
		this.year = Integer.parseInt(sToken.nextToken());
		
		if( ! sToken.hasMoreTokens())
			return;
		
		String[] genoTokenizer = sToken.nextToken().split(",");
		
		for( String s2 :genoTokenizer )
		{
			if( genotypicProfiles.contains(s2))
				throw new Exception("Parisng error");
			
			genotypicProfiles.add(s2);
		}
	}
	
	static public HashMap<String, Table2Parser> parseTable( String filepath ) throws Exception
	{
		HashMap<String, Table2Parser> map = new HashMap<String, Table2Parser>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(filepath)));
		
		for(String s= reader.readLine(); s != null; s = reader.readLine())
		{
			Table2Parser t2p = new Table2Parser(s);
			map.put( t2p.isolateNumber, t2p);
		}
		
		reader.close();
		
		return map;
	}
	
	public static HashSet<String> getAllPhenotypes( Collection<Table2Parser> c ) 
	{
		HashSet<String> set = new HashSet<String>();
		
		for( Table2Parser t2p : c)
			set.addAll(t2p.phenotypicProfiles);
			
		
		return set;
	}
	
	public static HashSet<String> getAllGenotypes( Collection<Table2Parser> c ) 
	{
		HashSet<String> set = new HashSet<String>();
		
		for( Table2Parser t2p : c)
			set.addAll(t2p.genotypicProfiles);
			
		
		return set;
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<String, Table2Parser> map = parseTable("c:\\temp\\Table_S2.txt");
		
		for(String s : map.keySet() )
		{
			System.out.println(map.get(s));
		}
		
		System.out.println("Phenotypes: " + getAllPhenotypes(map.values()));
		System.out.println("genotypes: " + getAllGenotypes(map.values()));
		
		// write json colors
		
		for( String  s :  getAllGenotypes(map.values()))
		{

			for(String s2 : map.keySet() )
			{
				if( map.get(s2).genotypicProfiles.contains(s))
					System.out.println("found " + s + " in " + s2);
			}
		}
		
	}
	
}
