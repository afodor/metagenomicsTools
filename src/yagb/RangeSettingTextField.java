package yagb;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;

public abstract class RangeSettingTextField extends JTextField
{
	protected FragmentRecruiterApp parent;
	
	abstract boolean forceToInteger();
	
	// sets the new value in the FragmentPanel
	abstract void setNewValue(float newValue);
	
	// returns true if the newValue is acceptable to the FragmentPanel
	abstract boolean acceptNewValue(float newValue);
	
	// gets the current value from the FragmentPanel
	abstract float getCurrentValue();
	
	RangeSettingTextField(FragmentRecruiterApp parent)
	{
		this.parent = parent;
		
		if( forceToInteger())
			setText("" + (int)getCurrentValue());
		else
			setText("" + getCurrentValue());
		
		addActionListener(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						perform();
					}
				}
			);
		
		addFocusListener
		(
				new FocusListener()
				{
					public void focusGained(FocusEvent e)
					{
					}
					
					public void focusLost(FocusEvent e)
					{
						perform();
					}
				}
		);
	}
	
	protected void perform()
	{
		float oldVal = getCurrentValue();
		boolean failed = false;
		
		float newVal=-1;
		try
		{
			newVal = Float.parseFloat(getText());
		}
		catch(Exception ex)
		{
			failed = true;
			// Fail silently here.  The user typed an invalid value.
		}
		
		if( ! acceptNewValue(newVal))
			failed = true;
		
		if( failed)
		{
			setText("" + oldVal);
			
			if( forceToInteger() )
				setText("" + ((int)newVal) );
		}
		else
		{
			setNewValue(newVal);
			parent.updateYAxisScrollPaneFromCurrentlySelectedAxis();
			parent.updateXAxisScrollPaneFromXAxis();
			parent.getFragmentPanel().repaint();
			
			if( forceToInteger() )
				setText("" + ((int)newVal) );
		}			
	}
	
	
}
