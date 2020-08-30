package scripts.FarnazManualCross;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.LinkedHashMap;

import utils.ConfigReader;

public class ParseAssalMeta
{
	private static class Assal_Meta implements SurgeryMetadataInterface
	{
		private String participantID;
		private String sampleID;
		private Integer timepoint;
		
		private Assal_Meta(String fileLine) throws Exception
		{
			String[] splits = fileLine.split("\t");
			
			this.sampleID = splits[0];
			
			int lastTokenIndex = splits.length -1;
			
			this.participantID = splits[lastTokenIndex];
			
			String timepointString = splits[lastTokenIndex-1];
			
			
			if( timepointString.equals("2Y"))
			{
				this.timepoint =24;
			}
			else if( timepointString.equals("1Y"))
			{
				this.timepoint =12;
			}
			else if( timepointString.equals("Pre"))
			{
				this.timepoint =0;
			}
			else if( timepointString.equals("3M"))
			{
				this.timepoint =3;
			}
			else if( timepointString.equals("3Y"))
			{
				this.timepoint =36;
			}
			else
				throw new Exception("Parsing error " + timepointString);
		}
		
		
		@Override
		public String getParticipantID()
		{
			return participantID;
		}
		
		@Override
		public String getSampleID()
		{
			return sampleID;
		} 
		
		@Override
		public Integer getTimepoint()
		{
			return timepoint;
		}
	}
	
	public static HashMap<String, SurgeryMetadataInterface> parseMetaFile() throws Exception
	{
		HashMap<String, SurgeryMetadataInterface> map = new LinkedHashMap<String, SurgeryMetadataInterface>();
		
		@SuppressWarnings("resource")
		BufferedReader reader = new BufferedReader(
				new FileReader(ConfigReader.getFarnazCrossDirBS() + File.separator+ "16S"
						+ File.separator 
						+ "metaData_Assal.txt"));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null && s.trim().length() > 0 ; s= reader.readLine())
		{
			
			Assal_Meta bsm = new Assal_Meta(s);
				
			if( map.containsKey(bsm.getSampleID()))
				throw new Exception("Error: duplicate " + bsm.getSampleID());
				
			map.put(bsm.getSampleID(), bsm);
		}
		
		reader.close();
		return map;
	}
	
	public static void main(String[] args) throws Exception
	{
		 HashMap<String, SurgeryMetadataInterface> map = parseMetaFile();
		 
		 for(SurgeryMetadataInterface smi : map.values())
			 System.out.println(smi.getSampleID() + " " + smi.getParticipantID() + " " + smi.getTimepoint());
		 
		 
	}
}
