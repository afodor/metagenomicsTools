package mbqc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import utils.ConfigReader;
import utils.TabReader;

public class MBQC_SampleParser
{
	private final String MBQC_ID;
	private final String NCI_Label;
	private final String NCI_Subj;
	private final String UC_ID;
	private final String Sample_ID;
	private final String Sample_type;
	private final String Health_status;
	private final String Visit;
	private final String Sex;
	private final Integer Age;
	private final Double BMI;
	private final String Extracted_DNA;
	
	public String getMBQC_ID()
	{
		return MBQC_ID;
	}


	public String getNCI_Label()
	{
		return NCI_Label;
	}


	public String getNCI_Subj()
	{
		return NCI_Subj;
	}


	public String getUC_ID()
	{
		return UC_ID;
	}


	public String getSample_ID()
	{
		return Sample_ID;
	}


	public String getSample_type()
	{
		return Sample_type;
	}


	public String getHealth_status()
	{
		return Health_status;
	}


	public String getVisit()
	{
		return Visit;
	}

	public String getSex()
	{
		return Sex;
	}

	public Integer getAge()
	{
		return Age;
	}

	public Double getBMI()
	{
		return BMI;
	}


	public String getExtracted_DNA()
	{
		return Extracted_DNA;
	}


	private MBQC_SampleParser(String s)
	{
		TabReader tr = new TabReader(s);
		this.MBQC_ID = tr.nextToken();
		this.NCI_Label = tr.nextToken();
		this.NCI_Subj = tr.nextToken();
		this.UC_ID = tr.nextToken();
		this.Sample_ID = tr.nextToken();
		this.Sample_type = tr.nextToken();
		this.Health_status = tr.nextToken();
		this.Visit = tr.nextToken();
		this.Sex = tr.nextToken();
		
		String ageString = tr.nextToken();
		
		this.Age = ageString == null ? null : Integer.parseInt(ageString);
		
		String bmiString = tr.nextToken();
		this.BMI = bmiString == null ? null : Double.parseDouble(bmiString);
		
		this.Extracted_DNA = tr.nextToken();
	}
	
	
	public static HashMap<String, MBQC_SampleParser> getMetaMap() throws Exception
	{
		HashMap<String, MBQC_SampleParser> map = new HashMap<String, MBQC_SampleParser>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getMbqcDir() + File.separator + "MBQC samples.txt")));

		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			TabReader tReader = new TabReader(s);
			
			String key = tReader.nextToken();
			
		}
		
		return map;
	}
}
