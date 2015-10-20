package yagb;

import parsers.HitScores;

public class MinusLog10EScoreAxisInfo extends AxisInfo
{
	@Override
	public String getAxisName()
	{
		return "Log EScore";
	}
	
	@Override
	public float getVal(HitScores hs)
	{
		double logScore = -Math.log10(hs.getEScore());
		
		//todo:  Is 0 the right score to return here?
		if( Double.isInfinite(logScore) || Double.isNaN(logScore) )
			logScore = 0;
		
		return (float)logScore;
	}
}
