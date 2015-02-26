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
	private final Integer PNPLA3CODEDGRP;
	private final Integer mTotSugarMedianSplit;
	private final Integer mAddedSugarMedianSplit;
	private final Integer mFructoseMedianSplit;
	private final Double mTotalSugar;
	
	public Double getmTotalSugar()
	{
		return mTotalSugar;
	}
		
	public Integer getPNPLA3CODEDGRP()
	{
		return PNPLA3CODEDGRP;
	}

	public Integer getmTotSugarMedianSplit()
	{
		return mTotSugarMedianSplit;
	}

	public Integer getmAddedSugarMedianSplit()
	{
		return mAddedSugarMedianSplit;
	}

	public Integer getmFructoseMedianSplit()
	{
		return mFructoseMedianSplit;
	}

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
	
	private Double getDoubleOrNull(String s)
	{
		if( s.trim().length() == 0)
			return null;
		
		return Double.parseDouble(s);
	}
	
	
	private PhenotypeDataLine(String s) throws Exception
	{
		System.out.println(s);
		TabReader tReader = new TabReader(s);
		this.subjectNumber = Integer.parseInt(tReader.nextToken());
		this.study = tReader.nextToken();
		this.franceSequencePlasma = Integer.parseInt(tReader.nextToken());
		this.nafld = getInOrNull(tReader.nextToken());
		this.PNPLA3CODEDGRP = getInOrNull(tReader.nextToken());
		this.mTotSugarMedianSplit = getInOrNull(tReader.nextToken());
		this.mAddedSugarMedianSplit = getInOrNull(tReader.nextToken());
		this.mFructoseMedianSplit = getInOrNull(tReader.nextToken());
		
		for( int x=0; x < 9; x++)
			tReader.nextToken();
		
		if( tReader.hasMore())
		{
			this.mTotalSugar = getDoubleOrNull(tReader.nextToken());
			
			tReader.nextToken();  tReader.nextToken();
			
			if( tReader.hasMore())
				throw new Exception("No");
		}
		else
		{
			this.mTotalSugar =  null;
		}
		
		
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
