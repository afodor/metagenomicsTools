package scripts.FarnazManualCross;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.LinkedHashMap;

import utils.ConfigReader;

public class ParseBSInteface
{
	private static class BS_Meta implements SurgeryMetadataInterface
	{
		private String participantID;
		private String sampleID;
		private Integer timepoint;
		
		private BS_Meta(String fileLine)
		{
			String[] splits = fileLine.split("\t");
			
			this.participantID = splits[0];
			this.sampleID = splits[1];
			
			if( ! splits[3].equals("NA"))
				this.timepoint = Integer.parseInt( splits[3]);
			else
				this.timepoint = null;
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
						"Metadata_BS_RYGB.txt"));
		
		reader.readLine();
		
		for(String s= reader.readLine(); s != null && s.trim().length() > 0 ; s= reader.readLine())
		{
			
			BS_Meta bsm = new BS_Meta(s);
				
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
