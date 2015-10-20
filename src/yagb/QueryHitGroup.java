package yagb;

import java.awt.Color;
import java.io.File;
import java.util.List;

import parsers.HitScores;

public class QueryHitGroup
{
	private Color color;
	private final String queryDescription;
	private final int minQueryAlignmentLength;
	private final File resultsFile;
	private boolean display = true;
	
	final private List<HitScores> hitList;

	public QueryHitGroup(Color color, File resultsFile, boolean fileGZipped,
			int minQueryAlignmentLength, String queryDescription)
		throws Exception
	{
		this.color = color;
		this.minQueryAlignmentLength = minQueryAlignmentLength;
		this.resultsFile = resultsFile;
		this.hitList = HitScores.getAsList(resultsFile, fileGZipped, minQueryAlignmentLength);
		this.queryDescription = queryDescription;
	}
	
	public QueryHitGroup(Color color, List<HitScores> list, int minQueryAlignmentLength,
			String queryDescription)
		throws Exception
	{
		this.color = color;
		this.minQueryAlignmentLength = minQueryAlignmentLength;
		//this.resultsFile = resultsFile;
		//this.hitList = HitScores.getAsList(resultsFile, fileGZipped, minQueryAlignmentLength);
	    this.hitList=list;
	    this.resultsFile=null;
		this.queryDescription = queryDescription;
	}
	
	
	public String getQueryDescription()
	{
		return queryDescription;
	}
	
	public boolean displayThisData()
	{
		return display;
	}
	
	public void setDisiplayThisData(boolean display)
	{
		this.display = display;
	}
	
	public List<HitScores> getHitList()
	{
		return hitList;
	}

	public Color getColor()
	{
		return color;
	}

	public void setColor(Color color)
	{
		this.color = color;
	}


	public int getMinQueryAlignmentNum()
	{
		return minQueryAlignmentLength;
	}

	public File getResultsFile()
	{
		return resultsFile;
	}	
	
}
