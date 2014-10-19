package ianAnorexia;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import java.util.HashMap;

import utils.ConfigReader;
import utils.TabReader;

public class HumanMetadataParser
{
	//Patient	Age	Race	Height (cm)	Weight (kg) T1	Weight (kg) T2	BMI (T1)	BMI (T2)	BDI	BAI	Restraint	Eating	Shape	WeightConc	EDEQTotal
	private final int patientID;
	private final int age;
	private final String race;
	private final float height;
	private final Float weightT1;
	private final Float weightT2;
	private final Float bdi;
	private final Float bai;
	
	private Float getFloatOrNull(String s)
	{
		try
		{
			return Float.parseFloat(s);
		}
		catch(Exception ex)
		{
			
		}
		
		return null;
	}
	
	public int getPatientID()
	{
		return patientID;
	}



	public int getAge()
	{
		return age;
	}

	public String getRace()
	{
		return race;
	}

	public float getHeight()
	{
		return height;
	}
	
	public Float getWeightT1()
	{
		return weightT1;
	}

	public Float getWeightT2()
	{
		return weightT2;
	}
	
	public Float getWeightDiff()
	{
		if( weightT1 == null || weightT2 == null)
			return null;
		
		return weightT2 - weightT1;
	}

	public Float getBdi()
	{
		return bdi;
	}

	public Float getBai()
	{
		return bai;
	}

	private HumanMetadataParser(String s)
	{
		TabReader tr = new TabReader(s);
		
		this.patientID = Integer.parseInt(tr.nextToken());
		this.age = Integer.parseInt(tr.nextToken());
		this.race = tr.nextToken();
		this.height = Float.parseFloat(tr.nextToken());
		this.weightT1 = getFloatOrNull(tr.nextToken());
		this.weightT2 = getFloatOrNull(tr.nextToken());
		tr.nextToken(); tr.nextToken();
		this.bdi = getFloatOrNull(tr.nextToken());
		this.bai = getFloatOrNull(tr.nextToken());
	}
	
	public static HashMap<Integer, HumanMetadataParser> getAsMap() throws Exception
	{
		HashMap<Integer, HumanMetadataParser> map = new HashMap<Integer, HumanMetadataParser>();
		
		BufferedReader reader = new BufferedReader(new FileReader(new File(
				ConfigReader.getIanAnorexiaDir() + File.separator + "AN Data_07.29.14_with clinical data.txt")));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null; s= reader.readLine())
		{
			HumanMetadataParser meta = new HumanMetadataParser(s);
			
			if( map.containsKey(meta.getPatientID()))
				throw new Exception("No");
			
			map.put(meta.getPatientID(), meta);
		}
		
		reader.close();
		
		return map;
	}
	
	public static void main(String[] args) throws Exception
	{
		HashMap<Integer, HumanMetadataParser> meta = 
				getAsMap();
		
		for(Integer i : meta.keySet())
			System.out.println(i  + " " + meta.get(i).getAge());
	} 
}
