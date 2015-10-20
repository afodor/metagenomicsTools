package yagb;

import parsers.HitScores;

public class QueryAlignmentLengthAxisInfo extends AxisInfo
{
	@Override
	public String getAxisName()
	{
		return "Query Length In Alignment";
	}
	
	@Override
	public float getVal(HitScores hs)
	{
		return hs.getQueryAlignmentLength();
	}
}
