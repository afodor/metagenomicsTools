package yagb;

public class RangeSettingTextFieldLowY extends RangeSettingTextField
{
	public RangeSettingTextFieldLowY(FragmentRecruiterApp parent)
	{
		super(parent);
	}

	@Override
	boolean acceptNewValue(float newValue)
	{
		if( newValue > parent.getFragmentPanel().getSelectedYAxis().getHighVal())
			return false;
		
		return true;
	}

	@Override
	float getCurrentValue()
	{
		return parent.getFragmentPanel().getSelectedYAxis().getLowVal();
	}

	@Override
	void setNewValue(float newValue)
	{
		parent.getFragmentPanel().getSelectedYAxis().setLowVal(newValue);
	}

	@Override
	boolean forceToInteger()
	{
		return false;
	}
}
