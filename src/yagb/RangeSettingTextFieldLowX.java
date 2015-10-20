package yagb;

public class RangeSettingTextFieldLowX extends RangeSettingTextField
{
	RangeSettingTextFieldLowX(FragmentRecruiterApp parent)
	{
		super(parent);
	}

	@Override
	boolean acceptNewValue(float newValue)
	{
		if( newValue > parent.getFragmentPanel().getHighX() )
			return false;
		
		return true;
	}

	@Override
	float getCurrentValue()
	{
		return parent.getFragmentPanel().getLowX();
	}

	@Override
	void setNewValue(float newValue)
	{
		parent.getFragmentPanel().setLowX((int)newValue);
	}
	
	@Override
	boolean forceToInteger()
	{
		return true;
	}
}
