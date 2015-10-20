package yagb;

import parsers.HitScores;


public class PercentIdentityAxisInfo extends AxisInfo
{
	@Override
	public String getAxisName()
	{
		return "Percent Identity";
	}

	@Override
	public float getVal(HitScores hs)
	{
		return hs.getPercentIdentity();
	}
	
	public PercentIdentityAxisInfo()
	{
		this.setDisplayed(true);
	}

}
