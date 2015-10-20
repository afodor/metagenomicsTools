package yagb;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import parsers.HitScores;

public class Bookmark 
{  FragmentPanel parent;
   FragmentRecruiterApp grandFa;
   
   List<QueryHitGroup> listOfFiles = new ArrayList<QueryHitGroup>();
   
	Bookmark(FragmentRecruiterApp parent) throws IOException
	{
	    	this.parent=parent.getFragmentPanel();
    		this.grandFa=parent;
	    	
    }
	
	public float booleanToFloat(boolean a)
	{
		if (a) return 1;
		else return 0;
	}
	
	public void writeBookmarkToFile() throws Exception
	{
		listOfFiles=grandFa.getList();
		
		PrintWriter writer = new PrintWriter("BookMark.txt");

		for(AxisInfo axis : parent.getAllYAxes())
			{
			//  writer.write(axis.getAxisName()+"\t");
			  writer.write(axis.getLowVal()+"\t");
			  writer.write(axis.getHighVal()+"\t");
			  writer.write(axis.getLowValInDataSet()+"\t");
			  writer.write(axis.getHighValInDataSet()+"\t");
			  writer.write(axis.getNumberTicks()+"\t");
			  writer.write(axis.getNumPixelsReserved()+"\t");
			  writer.write(booleanToFloat(axis.isDisplayed())+"\n");
			  
			}

        writer.write(parent.getJitterVal()+"\t");
        writer.write(booleanToFloat(parent.getXAxis().isDisplayed())+"\t");
        writer.write(booleanToFloat(parent.isCurrentlyShowingGeneAnnotations())+"\t");
        writer.write(booleanToFloat(parent.isCurrentlyShowingGeneLabels())+"\t");
        writer.write(parent.getNumPixelsReservedForGeneAnnotations()+"\n");

        writer.write(parent.getLowX()+"\t");
        writer.write(parent.getHighX()+"\n");
		writer.write("**\n");
		
		for(QueryHitGroup qhg: listOfFiles)
		{
		  for (HitScores hs: qhg.getHitList())
			{
		       hs.writeALine(writer,true);		
			}
		  
			writer.write("**\n");

		  
		}
        

        writer.flush();
		writer.close();
			
	}
	
	public ArrayList<Float> getValuesFromRow(String s)
	{  ArrayList<Float> list = new ArrayList<Float>();
	  
	StringTokenizer tokens = new StringTokenizer(s,"\t\n");
    
    while (tokens.hasMoreTokens()) 
          list.add(Float.valueOf(tokens.nextToken()));
	   
		return list;
	}
	
	
	public void loadBookmark() throws Exception 
	{  ArrayList<Float> myList = new ArrayList<Float>();
	   FileReader fileReader = new FileReader(new File("BookMark.txt"));
	   BufferedReader reader = new BufferedReader(fileReader);
	   String s;
	   
	  /* float lowVal,highVal,lowValInDataSet,highValInDataSet;
	   int numberTicks=5,numPixelsReserved=35;*/
		
	   
	   s=reader.readLine();
	   myList=getValuesFromRow(s);
	   parent.percentIdentityAxis.resetAxisInfo(myList.get(0),myList.get(1),myList.get(2),myList.get(3),myList.get(4),myList.get(5),myList.get(6));
	   
	   s=reader.readLine();
	   myList=getValuesFromRow(s);
	   parent.queryLengthAxis.resetAxisInfo(myList.get(0),myList.get(1),myList.get(2),myList.get(3),myList.get(4),myList.get(5),myList.get(6));
	   
	   s=reader.readLine();
	   myList=getValuesFromRow(s);
	   parent.minusLog10Axis.resetAxisInfo(myList.get(0),myList.get(1),myList.get(2),myList.get(3),myList.get(4),myList.get(5),myList.get(6));
	   
	   s=reader.readLine();
	   myList=getValuesFromRow(s);
	   parent.bitScoreAxis.resetAxisInfo(myList.get(0),myList.get(1),myList.get(2),myList.get(3),myList.get(4),myList.get(5),myList.get(6));
	   
	   s=reader.readLine();
	   myList=getValuesFromRow(s);
	   float f=myList.get(0);
	   parent.setJitterVal((int)f);
	   
	   parent.getXAxis().setDisplayed(parent.bitScoreAxis.floatIsDisplayedToBoolean(myList.get(1)));
	   
	   parent.setShowGeneAnnotations(parent.bitScoreAxis.floatIsDisplayedToBoolean(myList.get(2)));
	   
	   parent.setShowGeneLabels(parent.bitScoreAxis.floatIsDisplayedToBoolean(myList.get(3)));
	   
	   f=myList.get(4);
	   parent.setNumPixelsReservedForGeneAnnotations((int)f);
	   
	   s=reader.readLine();
	   myList=getValuesFromRow(s);
	   f=myList.get(0);
	   parent.setLowX((int)f);
	   
	   f=myList.get(1);
	   parent.setHighX((int)f);
	   
	   s=reader.readLine();
	   System.out.println(s.equals("**"));
	   listOfFiles.clear();
	   
	  
		   List<HitScores> listOfHS = new ArrayList<HitScores>();
           while(s.equals("**")) 
        	   {System.out.println(s);
        	   s=reader.readLine();
        	   }
           while(!s.equals("**")) 
        	   {
        	    listOfHS.add(new HitScores(s));
        	    s=reader.readLine();
        	   }
           QueryHitGroup query1 = new QueryHitGroup( Color.BLACK,listOfHS,75, "March 20 Wastewater" );
          
           listOfHS = new ArrayList<HitScores>();
           while(s.equals("**")) s=reader.readLine();
           while(!s.equals("**")) 
        	   {
        	    listOfHS.add(new HitScores(s));
        	    s=reader.readLine();
        	   }
           QueryHitGroup query2 = new QueryHitGroup( Color.RED,listOfHS,250,"Environmental Sequences" );

           listOfHS = new ArrayList<HitScores>();
           while(s.equals("**")) s=reader.readLine();
           while(!s.equals("**")) 
        	   {
        	   listOfHS.add(new HitScores(s));
        	   s=reader.readLine();
        	   }
           
           QueryHitGroup query3 = new QueryHitGroup( Color.BLUE,listOfHS,250, "NC_008752 Acidovorax avenae subsp. citrulli AAC00-1, complete genome");
           
           
           listOfFiles.add(query1);
           listOfFiles.add(query2);
           listOfFiles.add(query3);
           
           grandFa.setList(listOfFiles);

	 
	   
	 reader.close();
	 fileReader.close();
	 
	 parent.repaint();
	}
	
	

}
