package scripts.FarnazManualCross;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.LinkedHashMap;

import utils.ConfigReader;

public class ParsePallejaMeta
{
	private static class Palleja_Meta implements SurgeryMetadataInterface
	{
		private String participantID;
		private String sampleID;
		private Integer timepoint;
		
		private Palleja_Meta(String fileLine) throws Exception
		{
			String[] splits = fileLine.split(",");
			
			this.sampleID = splits[0];
			
			String lastToken = splits[splits.length-1];
			
			String[] lastSplits = lastToken.split("_");
			
			if( lastSplits.length !=2 )
				throw new Exception("Parsing error " + lastSplits);
			
			this.participantID = lastSplits[0];
			
			if( lastSplits[1].equals("Baseline"))
			{
				this.timepoint =0;
			}
			else if (lastSplits[1].equals("3MO"))
			{
				this.timepoint =3;
			}
			else if (lastSplits[1].equals("1Y"))
			{
				this.timepoint = 12;
			}
			else
				throw new Exception("Parsing error");
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
				new FileReader(ConfigReader.getFarnazCrossDirBS() + File.separator+ 
						"metaData_Palleja.txt"));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null && s.trim().length() > 0 ; s= reader.readLine())
		{
			
			Palleja_Meta bsm = new Palleja_Meta(s);
				
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
			 if( smi.getSampleID().equals("ERR1305877"))
			 System.out.println(smi.getSampleID() + " " + smi.getParticipantID() + " " + smi.getTimepoint());
		 
		 
	}
}
