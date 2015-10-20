package yagb;

import java.util.List;

import javax.swing.JScrollBar;

import parsers.HitScores;


public abstract class AxisInfo
{
	abstract public String getAxisName();
	abstract public float getVal( HitScores hs );
	
	protected float lowVal;
	protected float highVal;
	
	protected float lowValInDataSet;
	protected float highValInDataSet;
	boolean zoomCalledOnce = false;
	
	protected int numberTicks=5;
	protected int numPixelsReserved=35;
	protected boolean isDisplayed=true;
	
	protected boolean ignoreScrollBarUpdates = true;
	protected boolean currentlyZoomedOut = false;
	
	
	
	public AxisInfo() {
		super();
	}
	
	   
	  	
	public AxisInfo(float lowVal, float highVal, float lowValInDataSet,
			float highValInDataSet, int numberTicks, int numPixelsReserved) {
		super();
		this.lowVal = lowVal;
		this.highVal = highVal;
		this.lowValInDataSet = lowValInDataSet;
		this.highValInDataSet = highValInDataSet;
		this.numberTicks = numberTicks;
		this.numPixelsReserved = numPixelsReserved;
	}
	
	public void setHighValInDataSet(float highValInDataSet)
	{
		this.highValInDataSet = highValInDataSet;
	}
	
	public void setLowValInDataSet(float lowValInDataSet)
	{
		this.lowValInDataSet = lowValInDataSet;
	}
	
	public boolean currentlyZoomedOut()
	{
		return currentlyZoomedOut;
	}
	
	public boolean ignoringScrollBarUpdates()
	{
		return ignoreScrollBarUpdates;
	}
	
	public void setIgnoreScrollBarUpdates(boolean ignoreScrollBarUpdates)
	{
		this.ignoreScrollBarUpdates = ignoreScrollBarUpdates;
	}
	
	/*
	 * It's a good idea to immediatly call zoomOut() after calling this
	 */
	public void resetZoomCache()
	{
		this.zoomCalledOnce = false;
	}
	
	public float getLowValInDataSet()
	{
		if(! zoomCalledOnce)
			throw new RuntimeException("Must call zoomOut() first");
		
		return lowValInDataSet;
	}
	
	public float getHighValInDataSet()
	{
		if(! zoomCalledOnce)
			throw new RuntimeException("Must call zoomOut() first");
		
		return highValInDataSet;
	}
	
	@Override
	public String toString()
	{
		return getAxisName();
	}
	
	private boolean checkForExtremes(ScrollBarCalculator sbc, JScrollBar jScrollBar) 
	{
		int totalNumberOfIncrements = jScrollBar.getMaximum() - jScrollBar.getVisibleAmount();
		
		if( jScrollBar.getValue() == 0  )
		{
			if( isYAxis() )
			{
				this.lowVal = sbc.getMaxValToShow() - sbc.getCurrentDataWidth();
			}
			else
			{
				this.lowVal = sbc.getMinValToShow();
			}
			
			this.highVal = this.lowVal + sbc.getCurrentDataWidth();
			return true;
		}
		
		if( jScrollBar.getValue() >= totalNumberOfIncrements)
		{
			if( isYAxis())
			{
				this.lowVal = sbc.getMinValToShow();
			}
			else
			{
				this.lowVal = sbc.getMaxValToShow() - sbc.getCurrentDataWidth();
			}
			
			this.highVal = this.lowVal + sbc.getCurrentDataWidth();
			return true;
		}
		
		return false;
	}
	
	public void updateWithNewValuesFromScrollBar(JScrollBar jScrollBar)
	{
		if(ignoreScrollBarUpdates)
			return;
		
		ScrollBarCalculator sbc = new ScrollBarCalculator(this);
		
		if( checkForExtremes(sbc, jScrollBar))
			return;
		
		float totalNumberOfIncrements = jScrollBar.getMaximum() - jScrollBar.getVisibleAmount();
		float lowVal = sbc.getMaxValToShow() - sbc.getCurrentDataWidth();
		
		lowVal = lowVal - 
		    jScrollBar.getValue() 
		    * (sbc.getMaxValToShow() - sbc.getMinValToShow() - sbc.getCurrentDataWidth())/
		    								totalNumberOfIncrements;
		
		if( ! isYAxis())
			lowVal = sbc.getMaxValToShow() - lowVal + sbc.getMinValToShow();
		
		if (lowVal < sbc.getMinValToShow())
			lowVal = sbc.getMinValToShow();
		
		if( lowVal > sbc.getMaxValToShow() - sbc.getCurrentDataWidth())
			lowVal = sbc.getMaxValToShow() - sbc.getCurrentDataWidth();
		
		float highVal = lowVal + sbc.getCurrentDataWidth();
		
		if( highVal > sbc.getMaxValToShow())
			highVal = sbc.getMaxValToShow();
		
		this.lowVal = lowVal;
		this.highVal = highVal;
	}
	
	/*
	 * Not thread safe
	 */
	public void zoomOut(List<QueryHitGroup> queryList)
	{
		if( ! zoomCalledOnce)
		{
			lowValInDataSet = Float.POSITIVE_INFINITY;
			highValInDataSet = Float.NEGATIVE_INFINITY;
			
			for( QueryHitGroup qhg : queryList )
				if( qhg.displayThisData())
					for( HitScores hs : qhg.getHitList())
					{
						float currentVal = getVal(hs);
						lowValInDataSet = Math.min(lowValInDataSet, currentVal);
						highValInDataSet = Math.max(highValInDataSet, currentVal);
					}
			
			zoomCalledOnce = true;
		}
		
		lowVal = lowValInDataSet;
		highVal = highValInDataSet;
		currentlyZoomedOut = true;
		
	}
	
	public boolean isDisplayed()
	{
		return isDisplayed;
	}
	
	public void setDisplayed(boolean isDisplayed)
	{
		this.isDisplayed = isDisplayed;
	}
	
	public float getLowVal()
	{
		return lowVal;
	}
	
	public void setLowVal(float lowVal)
	{
		this.lowVal = lowVal;
		currentlyZoomedOut = false;
	}
	
	public float getHighVal()
	{
		return highVal;
	}
	
	public void setHighVal(float highVal)
	{
		this.highVal = highVal;
		currentlyZoomedOut = false;
	}
	
	public int getNumberTicks()
	{
		return numberTicks;
	}
	
	public void setNumberTicks(int numberTicks)
	{
		this.numberTicks = numberTicks;
	}
	
	public int getNumPixelsReserved()
	{
		return numPixelsReserved;
	}
	
	public void setNumPixelsReserved(int numPixelsReserved)
	{
		this.numPixelsReserved = numPixelsReserved;
	}
	
	public boolean floatIsDisplayedToBoolean(float n)
	{
		if (n>0) return true;
		else return false;
	}
	
	public void resetAxisInfo(float lowVal, float highVal, float lowValInDataSet, float highValInDataSet, float numberTicks, float numPixelsReserved, float yAxisDisplayed) 
	{
		this.setLowVal(lowVal);
		System.out.println(this.getAxisName());
		this.highVal = highVal;
		this.lowValInDataSet = lowValInDataSet;
		this.highValInDataSet = highValInDataSet;
		this.numberTicks = (int)numberTicks;
		this.numPixelsReserved = (int)numPixelsReserved;
		this.setDisplayed(floatIsDisplayedToBoolean(yAxisDisplayed));
	}
	
	protected boolean isYAxis()
	{
		return true;
	}
}
