package yagb;

public class RangeSettingTextFieldHighX extends RangeSettingTextField
{
	RangeSettingTextFieldHighX(FragmentRecruiterApp parent)
	{
		super(parent);
	}

	@Override
	boolean acceptNewValue(float newValue)
	{
		if( newValue < parent.getFragmentPanel().getLowX() )
			return false;
		
		return true;
	}

	@Override
	float getCurrentValue()
	{
		return parent.getFragmentPanel().getHighX();
	}

	@Override
	void setNewValue(float newValue)
	{
		parent.getFragmentPanel().setHighX((int)newValue);
	}
	
	@Override
	boolean forceToInteger()
	{
		return true;
	}
}
