package scripts.goranLab;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import utils.ConfigReader;
import utils.TabReader;

public class PhenotypeDataLine
{
	//SUBJNUM	Study	FRANCESequencedPlasma	NAFLD	PNPLA3CODEDGRP	mTotSugarMedianSplit	mAddedSugarMedianSplit	mFructoseMedianSplit	Visit	TotalFat	TotalLTM	ToPerFat	Age	sex	bmiz	bmi	bmipct	m_tsug	m_asug	m_fruc
	private final int subjectNumber;
	private final String study;
	private final Integer franceSequencePlasma;
	private final Integer nafld;
	
	public int getSubjectNumber()
	{
		return subjectNumber;
	}

	public String getStudy()
	{
		return study;
	}

	public Integer getFranceSequencePlasma()
	{
		return franceSequencePlasma;
	}


	public Integer getNafld()
	{
		return nafld;
	}


	private Integer getInOrNull(String s)
	{
		if( s.trim().length() == 0 )
			return null;
		
		return Integer.parseInt(s);
	}
	
	
	private PhenotypeDataLine(String s)
	{
		TabReader tReader = new TabReader(s);
		this.subjectNumber = Integer.parseInt(tReader.nextToken());
		this.study = tReader.nextToken();
		this.franceSequencePlasma = Integer.parseInt(tReader.nextToken());
		this.nafld = getInOrNull(tReader.nextToken());
	}
	
	public static HashMap<Integer, PhenotypeDataLine> getMap() throws Exception
	{
		HashMap<Integer, PhenotypeDataLine> map = new HashMap<Integer, PhenotypeDataLine>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getGoranTrialDir() + File.separator + 
						"SOL SANO Phenotype 021715 1020AM.txt")));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s = reader.readLine())
		{
			PhenotypeDataLine pdl = new PhenotypeDataLine(s);
			
			if( map.containsKey(pdl.getSubjectNumber()))
				throw new Exception("No");
			
			map.put(pdl.getSubjectNumber(), pdl);
		}
		
		return map;
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<Integer, PhenotypeDataLine> map = getMap();
		System.out.println(map.size());
	}
	
}
