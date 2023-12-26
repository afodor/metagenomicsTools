package scripts.KeHospitalPivot;

public class MetaMapFileLine
{

	private final String sampleID;
	private final String patientID;
	private final int timepoint;
	private final String patientInOut;
	private final String donor;
	
	
	public String getSampleID()
	{
		return sampleID;
	}

	public String getPatientID()
	{
		return patientID;
	}

	public int getTimepoint()
	{
		return timepoint;
	}

	public String getPatientInOut()
	{
		return patientInOut;
	}

	public String getDonor()
	{
		return donor;
	}


	private MetaMapFileLine(String s )
	{
		String[] splits = s.split("\t");
		
		this.sampleID = splits[0];
		this.patientID = splits[1];
		this.timepoint = Integer.parseInt(splits[2]);
		this.patientInOut = splits[7];
		this.donor = splits[8];
	}
}
