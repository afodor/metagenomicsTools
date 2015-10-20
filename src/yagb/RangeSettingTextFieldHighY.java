package yagb;

public class RangeSettingTextFieldHighY extends RangeSettingTextField
{
	public RangeSettingTextFieldHighY(FragmentRecruiterApp parent)
	{
		super(parent);
	}

	@Override
	boolean acceptNewValue(float newValue)
	{
		if( newValue < parent.getFragmentPanel().getSelectedYAxis().getLowVal())
			return false;
		
		return true;
	}

	@Override
	float getCurrentValue()
	{
		return parent.getFragmentPanel().getSelectedYAxis().getHighVal();
	}

	@Override
	void setNewValue(float newValue)
	{
		parent.getFragmentPanel().getSelectedYAxis().setHighVal(newValue);
	}
	
		
	@Override
	boolean forceToInteger()
	{
		return false;
	}
}
