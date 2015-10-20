package yagb;

import java.util.List;

import parsers.HitScores;


public class XAxisAxisInfo extends AxisInfo
{

	@Override
	protected boolean isYAxis()
	{
		return false;
	}
	
	/*
	 * Not thread safe
	 */
	public void zoomOut(List<QueryHitGroup> queryList)
	{
		if( lowVal < 0 || highVal < 0 )
			throw new RuntimeException("Please set range manually with calls to " + 
					"setHighValInDataSet() and setLowValInDataSet()");
		
		lowVal = lowValInDataSet;
		highVal = highValInDataSet;
		currentlyZoomedOut = true;
		zoomCalledOnce = true;
	}
	
	public XAxisAxisInfo()
	{
		super();
		
		// unlike the y-axis, this axis doesn't start zoomed out.
		this.ignoreScrollBarUpdates = false;
		
		this.highValInDataSet = -1;
		this.lowValInDataSet = -1;
		this.numPixelsReserved = 25;
	}
	
	@Override
	public String getAxisName()
	{
		return "Genome Position";
	}

	@Override
	public float getVal(HitScores hs)
	{
		throw new RuntimeException("GetVal is not defined for the x-axis since each " +
				"gene has two x positions");
	}

}
