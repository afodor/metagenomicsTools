package yagb;

import javax.swing.JScrollBar;

public class ScrollBarCalculator
{
	private final float maxValToShow;
	private final float minValToShow;
	private final float currentDataWidth;
	private final float fullDataWidth;
	private final AxisInfo axisInfo;
	
	public float getMaxValToShow()
	{
		return maxValToShow;
	}

	public float getMinValToShow()
	{
		return minValToShow;
	}

	public float getCurrentDataWidth()
	{
		return currentDataWidth;
	}

	public ScrollBarCalculator(AxisInfo axis)
	{
		this.axisInfo = axis;
		
		this.maxValToShow = Math.max( axis.getHighValInDataSet(), axis.getHighVal()  );
		this.minValToShow = Math.min( axis.getLowValInDataSet(), axis.getLowVal());
		
		currentDataWidth = axisInfo.getHighVal() - axisInfo.getLowVal();
		fullDataWidth = maxValToShow - minValToShow;	
		
	}
	
	private void setValueOnScrollBar(JScrollBar scrollBar)
	{
		if( scrollBar.getVisibleAmount() == scrollBar.getMaximum())
		{
			scrollBar.setValue(0);
			return;
		}
			
		float numIncrements = scrollBar.getMaximum() - scrollBar.getVisibleAmount();
		
		float value = numIncrements * ( maxValToShow - axisInfo.getHighVal() )
							/ ( maxValToShow - (minValToShow+currentDataWidth) );
		
		if ( value < 0)
			value =0;
		
		if( value > numIncrements)
			value = numIncrements;
		
		if( axisInfo.isYAxis() )
			scrollBar.setValue((int)value);
		else
			scrollBar.setValue( (int) (numIncrements - value) );
	}
	
	private void setExtentOnScrollBar(JScrollBar scrollBar)
	{
		float maxValue = scrollBar.getMaximum();
		
		float extent = maxValue * (currentDataWidth / fullDataWidth);
		
		if( extent < 0 )
			extent = 0;
		
		if( extent > maxValue )
			extent = maxValue;
		
		//System.out.println("Setting extent " + extent);
		scrollBar.setVisibleAmount((int)extent);
	}
	
	public void setExtendAndValueOnScrollBar(JScrollBar scrollBar)
	{
		setExtentOnScrollBar(scrollBar);
		setValueOnScrollBar(scrollBar);
	}
	
	
	
	
	
}
