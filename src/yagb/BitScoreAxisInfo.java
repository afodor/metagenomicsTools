package yagb;

import parsers.HitScores;


public class BitScoreAxisInfo extends AxisInfo
{
	@Override
	public String getAxisName()
	{
		return "Bit Score";
	}
	
	@Override
	public float getVal(HitScores hs)
	{
		return hs.getBitScore();
	}
}
