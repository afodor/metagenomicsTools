package scripts.goranLab;

import utils.TabReader;

public class PhenotypeDataLine
{
	//SUBJNUM	Study	FRANCESequencedPlasma	NAFLD	PNPLA3CODEDGRP	mTotSugarMedianSplit	mAddedSugarMedianSplit	mFructoseMedianSplit	Visit	TotalFat	TotalLTM	ToPerFat	Age	sex	bmiz	bmi	bmipct	m_tsug	m_asug	m_fruc
	private final int subjectNumber;
	private final String study;
	private final Integer franceSequencePlasma;
	private final Integer nafld;
	
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
		this.nafld = Integer.parseInt(tReader.nextToken());
	}
	
	
}
