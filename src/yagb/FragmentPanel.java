package yagb;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JPanel;

import org.jibble.epsgraphics.EpsGraphics2D;

import parsers.GenbankAnnotations;
import parsers.HitScores;

public class FragmentPanel extends JPanel
{
	private static final long serialVersionUID = -7590747409025879150L;
	

	private Color[] colors = { Color.BLACK, Color.BLUE, Color.CYAN,
									Color.DARK_GRAY, Color.GRAY, Color.GREEN,
									Color.LIGHT_GRAY, Color.MAGENTA, Color.ORANGE,
									Color.PINK, Color.RED, Color.YELLOW};
	
	private int colorIndex = 0;

	private FragmentRecruiterApp parent;
	private AxisInfo xAxis = new XAxisAxisInfo();
	private int numPixelsReservedForGeneAnnotations = 31;
	private boolean showGeneAnnotations = true;
	private boolean showGeneLabels = false;
	private int jitterVal =0;
	
	public int getJitterVal()
	{
		return jitterVal;
	}
	
	public void setJitterVal(int jitterVal)
	{
		this.jitterVal =  jitterVal;
	}
	
	final PercentIdentityAxisInfo percentIdentityAxis = 
		new PercentIdentityAxisInfo();

	final QueryAlignmentLengthAxisInfo queryLengthAxis = 
			new QueryAlignmentLengthAxisInfo();
	
	final MinusLog10EScoreAxisInfo minusLog10Axis = 
		new MinusLog10EScoreAxisInfo();

	final BitScoreAxisInfo bitScoreAxis = 
		new BitScoreAxisInfo();
	
	 private List<QueryHitGroup> hits = new ArrayList<QueryHitGroup>();
	
	public void setHits(List<QueryHitGroup> hits)
	{
		this.hits = hits;
	}
	
	public List<QueryHitGroup> getHits()
	{
		return hits;
	}
	
	List<GenbankAnnotations> genbankAnnotationList = null;
	
	public int getNumPixelsReservedForGeneAnnotations() 
	{
		return numPixelsReservedForGeneAnnotations;
	}
	
	public void setNumPixelsReservedForGeneAnnotations(int numPixelsReservedForGeneAnnotations) 
	{
		this.numPixelsReservedForGeneAnnotations = numPixelsReservedForGeneAnnotations;
	}
	
	public void setShowGeneAnnotations(boolean showGeneAnnotations) 
	{
		this.showGeneAnnotations = showGeneAnnotations;
	}
	
	public boolean isCurrentlyShowingGeneAnnotations()
	{
		return showGeneAnnotations;
	}
	
	public void setShowGeneLabels(boolean showGeneLabels) 
	{
		this.showGeneLabels = showGeneLabels;
	}
	
	public boolean isCurrentlyShowingGeneLabels() 
	{
		return showGeneLabels;
	}
	
	public AxisInfo getXAxis()
	{
		return xAxis;
	}
	
	public List<GenbankAnnotations> getGenbankAnnotationList()
	{
		return genbankAnnotationList;
	}
	
	public void setGenbankAnnotationList(List<GenbankAnnotations> genbankAnnotationList)
	{
		this.genbankAnnotationList = genbankAnnotationList;
	}
	
	public List<AxisInfo> getAllYAxes()
	{
		List<AxisInfo> list = new ArrayList<AxisInfo>();
		
		list.add(percentIdentityAxis);
		list.add(queryLengthAxis);
		list.add(minusLog10Axis);
		list.add(bitScoreAxis);
		
		return list;
	}
	
	/*public void resetAllAxis(ArrayList<AxisInfo> list)
	{  		System.out.println(list.get(0).getLowVal()+"*");

		this.percentIdentityAxis.resetAxisInfo(list.get(0));
		this.queryLengthAxis.resetAxisInfo(list.get(1));
		this.minusLog10Axis.resetAxisInfo(list.get(2));
		this.bitScoreAxis.resetAxisInfo(list.get(3));
		
	}
	*/
	
	
	private void drawXAxis(Graphics g, Font font)
	{
		if(! xAxis.isDisplayed() )
			return;
		
		Color oldColor = g.getColor();
		g.setColor(Color.BLACK);
		
		float leftBorder = getLeftBorder();
		int yVal = getHeight() - getNumPixelsReservedOnBottom();
		
		g.drawLine((int)leftBorder, yVal, getWidth(), yVal);
		
		float xInterval = ( getWidth() - leftBorder ) / xAxis.getNumberTicks();
		FontMetrics fm = g.getFontMetrics(font);
		
		for( int x=1; x < xAxis.getNumberTicks() ; x++)
		{
			float xPos =  leftBorder +  (xInterval * x);
			g.setColor(Color.GRAY);	
			
			g.drawLine( (int)xPos, yVal, (int)(xPos), yVal+5);
			
			float axisValue = xAxis.getLowVal() 
					+  (x/((float)xAxis.getNumberTicks())) * 
							(xAxis.getHighVal() - xAxis.getLowVal());
			String axisString = new String("" + (int)(axisValue+0.5));
			float tickLabelPos = 
				xPos - fm.stringWidth(axisString) /2;
			g.setColor(Color.BLACK);
			g.drawString(axisString, (int)tickLabelPos , yVal + 18);
		}
	
		
		g.setColor(oldColor);
	}
	
	private AxisInfo selectedYAxis = percentIdentityAxis;
	
	public void setSelectedYAxis( AxisInfo ai )
	{
		if( ai == selectedYAxis )
			return;
		
		selectedYAxis = ai;
		repaint();
	}
	
	public AxisInfo getSelectedYAxis()
	{
		return selectedYAxis;
	}
	
	private int getLeftBorder()
	{
		int numPixelsToReserve = selectedYAxis.getNumPixelsReserved();
		
		if( ! selectedYAxis.isDisplayed )
			numPixelsToReserve = 0;
		
		return numPixelsToReserve;
	}
	
	private int getNumPixelsReservedOnBottom()
	{
		int numPixelsReserved = 0;
		
		if( showGeneAnnotations )
			numPixelsReserved += numPixelsReservedForGeneAnnotations;
		
		if( xAxis.isDisplayed)
			numPixelsReserved += xAxis.getNumPixelsReserved();
		
		return numPixelsReserved;
	}
	
	private int getXValue(int xPos, int xSize)
	{
		if( xPos < xAxis.getLowVal() )
			return 0;
		
		if( xPos > xAxis.getHighVal())
			return xSize;
		
		int numPixelsToReserve = getLeftBorder();
		
		float xVal = numPixelsToReserve + (xSize-numPixelsToReserve) 
						* ( xPos - xAxis.getLowVal()) 
						/ ( xAxis.getHighVal() - xAxis.getLowVal());
		return (int) xVal;
	}
	
	private String getYAxisTickLabel( float axisValue )
	{
		NumberFormat nf = NumberFormat.getInstance();
		
		if( axisValue == ((int)axisValue) )
			nf.setMaximumFractionDigits(0);
		else
			nf.setMaximumFractionDigits(1);
		
		return nf.format(axisValue);
	}
	
	public FragmentPanel(FragmentRecruiterApp parentPanel)
	{
		this.parent = parentPanel;
		
		addMouseMotionListener( new MouseMotionListener()
		{
			public void mouseMoved(MouseEvent e)
			{
				if( e.getY() < getHeight() - numPixelsReservedForGeneAnnotations ||
						genbankAnnotationList == null || ! showGeneAnnotations)
					return;
				
				StringBuffer buff = null;
				
				int mouseX = e.getX();
				for( GenbankAnnotations ga : genbankAnnotationList)
				{
					if( mouseX >= ga.getLowestXPixel() &&  
							mouseX <= ga.getHighestXPixel())
					{
						buff = new StringBuffer();
						
						for( String s : ga.getDescriptionLines() )
							buff.append(s + "\n");
					}
				}
				
				if( buff != null)
					parent.getRightTextArea().setText(buff.toString());
					
			}
			
			public void mouseDragged(MouseEvent e)
			{	
			}
		} );
	}
	
	private void drawYAxisAndHorizontalLines(Graphics g, Font fontForAxisLabels)
	{
		Color oldColor = g.getColor();
		g.setColor(Color.BLACK);
	
		float ySize = getHeight() - 
			getNumPixelsReservedOnBottom();
		
		if( selectedYAxis.isDisplayed())
			g.drawLine(selectedYAxis.getNumPixelsReserved(), 0, 
					selectedYAxis.getNumPixelsReserved(), (int) ySize);
		
		if( selectedYAxis.getNumberTicks() < 1)
			return;
		
		g.setFont(fontForAxisLabels);
		int leftBorder = getLeftBorder();
		
		if( selectedYAxis.isDisplayed())
		{
			g.setColor(Color.RED);
			g.drawString(selectedYAxis.getAxisName(), selectedYAxis.numPixelsReserved, 10);
			g.setColor(Color.BLACK);
		}
		
		float yInterval = ySize / selectedYAxis.getNumberTicks();
		FontMetrics fm = g.getFontMetrics(fontForAxisLabels);
		int stringAscent = fm.getAscent();
		
		for( int x=1; x < selectedYAxis.getNumberTicks() ; x++)
		{
			int yVal = (int) (yInterval * x);
			g.setColor(Color.GRAY);	
			
			g.drawLine( leftBorder, yVal, 
					getWidth(), yVal);
			
			float axisValue = selectedYAxis.getHighVal() 
					-  (x/((float)selectedYAxis.getNumberTicks())) * 
							(selectedYAxis.getHighVal() - selectedYAxis.getLowVal());
			String axisString = getYAxisTickLabel(axisValue);
			int tickLabelPos = 
				selectedYAxis.getNumPixelsReserved() - fm.stringWidth(axisString) -2;
			g.setColor(Color.BLACK);
			
			if( selectedYAxis.isDisplayed())
				g.drawString(axisString, tickLabelPos, yVal + stringAscent /2);
		}
		g.setColor(oldColor);
	}
	
	private void drawHits(Graphics g)
	{
		//always draw the jitter in the same way
		Random random =new Random(323452);
		
		Color oldColor = g.getColor();
		int leftBorder = getLeftBorder();
		
		int xSize = getWidth();
		int availableSpaceForY = getHeight() - getNumPixelsReservedOnBottom() ;
		
		for( QueryHitGroup qhg : hits )
		{
			if( qhg.displayThisData())
			{
				g.setColor(qhg.getColor());

				for( HitScores hs : qhg.getHitList() )
				{	
					int yValue = getYValue(hs, availableSpaceForY);
					
					if( jitterVal > 0)
					{
						yValue += random.nextInt(jitterVal ) + jitterVal/2;
						yValue = Math.max(0, yValue);
						yValue = Math.min(availableSpaceForY, yValue);
					}
						
					int x1 = getXValue(hs.getTargetStart(), xSize);
					int x2 = getXValue(hs.getTargetEnd(), xSize);
					
					if( lineIsInFrame(x1, x2, yValue, xSize, availableSpaceForY))
					{
						drawSomeLines(g, x1, x2, yValue, leftBorder, xSize);
					}				
				}
			}
			
		}
		
		g.setColor(oldColor);
				
	}
	
	private void drawSomeLines(Graphics g, int x1, int x2, int yValue, int leftBorder,
			int xSize)
	{	
		x1 = Math.max(x1, leftBorder);
		x1 = Math.min(x1, xSize);
		x2 = Math.max(x2, leftBorder);
		x2 = Math.min(x2, xSize);
		
		g.fillRect(x1, yValue -1, Math.abs( x2-x1), 3);
		
		/*
		g.drawLine(x1, yValue-1, x2, yValue-1);
		g.drawLine(x1, yValue, x2, yValue);
		g.drawLine(x1, yValue+1, x2, yValue+1);
		*/
	}
	
	private boolean lineIsInFrame(int x1, int x2, int yValue, int xSize, int ySize)
	{
		
		int compareX = getLeftBorder();
		
		if ( yValue <= 0 )
			return false;
		
		if( yValue >= ySize)
			return false;
						
		if( x1 <= compareX && x2 <= compareX)
			return false;
		
		if( x1 >= xSize && x2 >= xSize)
			return false;
		
		return true;
	}
	
	public void drawGenbankAnnotations( Graphics g)
	{
		this.colorIndex = 0;
		int yOffset = 3;
		
		g.setFont(new Font("SansSerif", Font.BOLD, 12));
		
		if( genbankAnnotationList == null || ! showGeneAnnotations)
			return;
		
		int leftBorder = selectedYAxis.isDisplayed() ? selectedYAxis.getNumPixelsReserved() :0;
		int xSize = getWidth();
	
		Color oldColor = g.getColor();
		int offSetSize = numPixelsReservedForGeneAnnotations / 5;
		
		for( GenbankAnnotations ga : genbankAnnotationList )
		{
			this.colorIndex++;
			yOffset += offSetSize;
			if( yOffset > numPixelsReservedForGeneAnnotations)
				yOffset = 3;
				
			if( colorIndex >= colors.length)
				colorIndex = 0;
			
			g.setColor(colors[colorIndex]);
			
			int x1 = getXValue(ga.getBeginPos(), xSize);
			int x2 = getXValue(ga.getEndPos(), xSize);
			
			// dummy values of 1 and 100 as the y axis is always ok for annotations
			if(lineIsInFrame(x1, x2,1,xSize,100))
			{
				int yValue = getHeight() - yOffset;
				
				drawSomeLines(g, x1, x2, yValue, leftBorder, xSize);
				ga.setXPixelStart(x1);
				ga.setXPixelEnd(x2);
				
				if(showGeneLabels)
				{
					g.setColor(Color.BLACK);
					g.drawString(ga.getProduct(), x1, yValue);
				}
					
			}
			else
			{
				ga.setXPixelStart(-1);
				ga.setXPixelEnd(-1);
			}
		}
		
		g.setColor(oldColor);
	}
	
	
	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		drawToGraphics(g);
	}
	
	/*
	 * Does not check to see if the file exists
	 * before over writing
	 */
	public void writeEPSFile(File fileToSave) throws Exception
	{	
		
		EpsGraphics2D g = new EpsGraphics2D();
	    g.setAccurateTextMode(false);
	    drawToGraphics(g);
	    
	    BufferedWriter writer = new BufferedWriter(new FileWriter(fileToSave));
	    writer.write(g.toString());
	    writer.flush();  writer.close();
	}
	
	
	private void drawToGraphics(Graphics g)
	{
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		if( hits.size() > 0)
		{
			Font f= new Font("SansSerif", Font.BOLD, 12);
			drawHits(g);
			drawGenbankAnnotations(g);
			drawYAxisAndHorizontalLines(g,f);
			drawXAxis(g, f);
		}
	}
	
	private int getYValue(HitScores hs, int ySize)
	{
		float val = selectedYAxis.getVal(hs);
		
		if( val > selectedYAxis.getHighVal())
			return 0;
		
		if ( val < selectedYAxis.getLowVal())
			return ySize;
		
		float yVal = ySize - ySize * (val -selectedYAxis.getLowVal()) 
									/ (selectedYAxis.getHighVal() - selectedYAxis.getLowVal());
		return (int) yVal;
	}
	
	public void zoomOut()
	{
		zoomOutY(false);
		zoomOutX(true);
	}
	
	public void zoomOutY(boolean repaint)
	{
		selectedYAxis.zoomOut(hits);
		
		if(repaint)
			repaint();
	}
	
	public void zoomOutX(boolean repaint)
	{
	
		xAxis.zoomOut(hits);
		
		if(repaint)
			repaint();
	}
	
	public void zoomOutAllYAxesFromDisplayedData() throws Exception
	{
		percentIdentityAxis.resetZoomCache();
		percentIdentityAxis.zoomOut(hits);
		
		minusLog10Axis.resetZoomCache();
		minusLog10Axis.zoomOut(hits);
		
		bitScoreAxis.resetZoomCache();
		bitScoreAxis.zoomOut(hits);
		
		queryLengthAxis.resetZoomCache();
		queryLengthAxis.zoomOut(hits);
	}

	public int getLowX()
	{
		return (int) xAxis.getLowVal();
	}

	public void setLowX(int lowX)
	{
		xAxis.setLowVal(lowX);
	}

	public int getHighX()
	{
		return (int)xAxis.getHighVal();
	}

	public void setHighX(int highX)
	{
		xAxis.setHighVal(highX);
	}
}
