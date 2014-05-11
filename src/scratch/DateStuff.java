package scratch;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateStuff
{
	public static void main(String[] args) throws Exception
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy");
		Date date = sdf.parse("10/4/2013");
		System.out.println(date.getTime());
		Date date2 = sdf.parse("1/23/2013");
		System.out.println(date2.getTime());
	}
}
