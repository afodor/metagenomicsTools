package yagb;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

import parsers.GenbankAnnotations;
import utils.ConfigReader;


public class FragmentRecruiterApp extends JFrame
{
	private static final long serialVersionUID = -667941602261399207L;
	private FragmentPanel fragmentPanel;
	private FragmentRecruiterApp thisApp;
	private JMenu axisMenu;
	private JTextArea rightTextArea = new JTextArea();
	
	static List<QueryHitGroup> list = new ArrayList<QueryHitGroup>();

	Bookmark bookMark;
	
	private JScrollBar xScrollBar = new JScrollBar(JScrollBar.HORIZONTAL);
	private JScrollBar yScrollBar = new JScrollBar(JScrollBar.VERTICAL);
	private AxisOptionsDialog lastOptionDialog = null;
	private File lastFile = null;

	
	public List<QueryHitGroup> getList()
	{
		return list;
	}
	
	public static void setList(List<QueryHitGroup> myList)
	{
		list=myList;
	}
	public JScrollBar getYScrollBar()
	{
		return yScrollBar;
	}
	
	public JScrollBar getXScrollBar()
	{
		return xScrollBar;
	}
	
	public JTextArea getRightTextArea()
	{
		return rightTextArea;
	}
	
	public JMenu getAxisMenu()
	{
		return axisMenu;
	}
	
	private JPanel getLeftPanel()
	{
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BorderLayout());
		leftPanel.add(fragmentPanel, BorderLayout.CENTER);
		leftPanel.add(xScrollBar, BorderLayout.SOUTH);
		leftPanel.add(yScrollBar, BorderLayout.WEST);
		
		yScrollBar.setVisibleAmount(yScrollBar.getMaximum());
		yScrollBar.setValue(0);
		
		xScrollBar.setMaximum(10000);
		xScrollBar.setVisibleAmount(xScrollBar.getMaximum());
		xScrollBar.setValue(0);
		
		yScrollBar.addAdjustmentListener(new AdjustmentListener()
		{
			public void adjustmentValueChanged(AdjustmentEvent e)
			{
				fragmentPanel.getSelectedYAxis().updateWithNewValuesFromScrollBar(yScrollBar);
				fragmentPanel.repaint();
				
				if( lastOptionDialog != null && lastOptionDialog.dialogIsStillActive())
				{
					lastOptionDialog.layoutMainPanel();
					lastOptionDialog.validate();
				}
			}
		}
		
		);
		
		xScrollBar.addAdjustmentListener(new AdjustmentListener()
		{
			public void adjustmentValueChanged(AdjustmentEvent e)
			{
				fragmentPanel.getXAxis().updateWithNewValuesFromScrollBar(xScrollBar);
				fragmentPanel.repaint();
				
				if( lastOptionDialog != null && lastOptionDialog.dialogIsStillActive())
				{
					lastOptionDialog.layoutMainPanel();
					lastOptionDialog.validate();
				}
			}
		}
		
		);
		
		return leftPanel;
		
	}
	
	public void updateYAxisScrollPaneFromCurrentlySelectedAxis()
	{
		if( fragmentPanel.getSelectedYAxis().currentlyZoomedOut() )
		{
			yScrollBar.setValue(0);
			yScrollBar.setVisibleAmount(yScrollBar.getMaximum());
			yScrollBar.repaint();
			return;
		}
		
		ScrollBarCalculator sbc = new ScrollBarCalculator( fragmentPanel.getSelectedYAxis());
		sbc.setExtendAndValueOnScrollBar(yScrollBar);
	}
	
	public void updateXAxisScrollPaneFromXAxis()
	{
		if( fragmentPanel.getXAxis().currentlyZoomedOut() )
		{
			xScrollBar.setValue(0);
			xScrollBar.setVisibleAmount(xScrollBar.getMaximum());
			xScrollBar.repaint();
			return;
		}
		
		ScrollBarCalculator sbc = new ScrollBarCalculator( fragmentPanel.getXAxis());
		sbc.setExtendAndValueOnScrollBar(xScrollBar);
	}
	
	public JPanel getRightPanel(List<QueryHitGroup> queryList)
	{
		JPanel jPanel = new JPanel();
		
		jPanel.setLayout(new BorderLayout());
		rightTextArea.setEditable(false);
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitPane.add( new JScrollPane( rightTextArea));
		splitPane.add( new JScrollPane( getSelectionPanel(queryList)));
		jPanel.add(splitPane);
		splitPane.setDividerLocation(80);
		
		return jPanel;
	}
	
	public JPanel getSelectionPanel(List<QueryHitGroup> list)
	{
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
	
		panel.setLayout(new GridLayout(list.size(), 1));
		
		for( QueryHitGroup qhg : list )
			panel.add(new DisplayDataCheckBox(qhg));
		
		return panel;
		
	}
	
	private class DisplayDataCheckBox extends JCheckBox
	{
		private final QueryHitGroup aQueryHitGroup;
		
		public DisplayDataCheckBox(QueryHitGroup qhg)
		{
			super(qhg.getQueryDescription());
			this.aQueryHitGroup= qhg;
			setSelected(qhg.displayThisData());
			
			addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					aQueryHitGroup.setDisiplayThisData(isSelected());
					fragmentPanel.repaint();
				}
			} 
			);
		}
	}
	
	public FragmentRecruiterApp(String title, List<QueryHitGroup> queryList)
	{
		super(title);
		this.thisApp = this;
		setSize(500,200);
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLayout(new BorderLayout());
		add(splitPane, BorderLayout.CENTER);
		fragmentPanel = new FragmentPanel(this);
		splitPane.add(getLeftPanel());
		JPanel rightPanel = getRightPanel(queryList);
		splitPane.add( rightPanel);
		splitPane.setDividerLocation(400);
		setJMenuBar(getMyMenuBar());
		setVisible(true);
	}
	
	FragmentPanel getFragmentPanel()
	{
		return this.fragmentPanel;
	}
	
	//**Added by LuisCarlos **//
	private class SaveBookmarkActionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			try {
				bookMark = new Bookmark(thisApp);
				try {
					bookMark.writeBookmarkToFile();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
	private class LoadBookmarkActionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) 
		{
             try {
 				bookMark = new Bookmark(thisApp);

				bookMark.loadBookmark();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}		
		} 
	}
	
	private class SaveEpsActionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			File startDir = lastFile == null ? new File(System.getProperty("user.home")) :
													lastFile.getParentFile();
			
			JFileChooser jfc = new JFileChooser(startDir);
			
			jfc.setSelectedFile(new File(startDir.getAbsolutePath() + File.separator + 
					thisApp.getTitle() + " " + 
					(int) thisApp.getFragmentPanel().getXAxis().getLowVal() + "_" 
					+  (int)thisApp.getFragmentPanel().getXAxis().getHighVal() + ".eps" ));
				
			if( jfc.showSaveDialog(thisApp) != JFileChooser.APPROVE_OPTION)
				return;
			
			if( jfc.getSelectedFile() == null)
				return;
				
			lastFile = jfc.getSelectedFile();
				
			if( jfc.getSelectedFile().exists())
			{
				String message = "File " + jfc.getSelectedFile().getName() + " exists.  Overwrite?";
					
				if( JOptionPane.showConfirmDialog(thisApp, message) != 
						JOptionPane.YES_OPTION)
						return;			
			}
				
			try
			{
				thisApp.fragmentPanel.writeEPSFile(jfc.getSelectedFile());
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
				JOptionPane.showMessageDialog(thisApp, ex.getMessage());
			}
		}		
	}
	
	private JMenuBar getMyMenuBar()
	{
		JMenuBar menuBar = new JMenuBar();
		
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic('F');
		JMenuItem saveEps = new JMenuItem("Save EPS file");
		saveEps.setMnemonic('E');
		fileMenu.add(saveEps);
		saveEps.addActionListener( new SaveEpsActionListener());

		JMenuItem saveBookMark = new JMenuItem("Save this Configuration");
		saveBookMark.setMnemonic('C');
		fileMenu.add(saveBookMark);
		saveBookMark.addActionListener(new SaveBookmarkActionListener());
		
		
		JMenuItem LoadBookMark = new JMenuItem("Load last Configuration Saved");
		LoadBookMark.setMnemonic('C');
		//LoadBookMark.setEnabled(false);
		fileMenu.add(LoadBookMark);
		LoadBookMark.addActionListener(new LoadBookmarkActionListener());
		

		menuBar.add(fileMenu);
		
		this.axisMenu = new JMenu("Axis");
		axisMenu.setMnemonic('A');
		// not enabled until there is data
		this.axisMenu.setEnabled(false);
		
		JMenuItem showYAxisOptionsMenu = 
			new JMenuItem("Axis Options");
		showYAxisOptionsMenu.setMnemonic('A');

		showYAxisOptionsMenu.addActionListener(
				new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						lastOptionDialog = new AxisOptionsDialog(thisApp);
					}
				}
				);
		
		axisMenu.add(showYAxisOptionsMenu);
		menuBar.add(axisMenu);
		
		return menuBar;
	}
	
	private void callAfterDataLoaded(List<QueryHitGroup> list) throws Exception
	{
		// now that we have data, enable the axis menu
		axisMenu.setEnabled(true);
		updateYAxisScrollPaneFromCurrentlySelectedAxis();
		updateXAxisScrollPaneFromXAxis();
		fragmentPanel.zoomOutAllYAxesFromDisplayedData();
		
		long totalNumHits = 0;
		
		for( QueryHitGroup qhg : list )
		{	
			rightTextArea.append(qhg.getQueryDescription() + " " + 
										qhg.getHitList().size() + " hits over " + 
										 qhg.getMinQueryAlignmentNum() + " nucleotides\n");
			
			totalNumHits += qhg.getHitList().size();
		}
		
		rightTextArea.append( "\nTotal "  + totalNumHits);
		
		repaint();
	}
	
	public static void runNC_008766() throws Exception
	{
		List<QueryHitGroup> list = new ArrayList<QueryHitGroup>();
		
		File  wastewaterResultsFile =
			new File( ConfigReader.getFragmentRecruiterSupportDir() + File.separator + 
					"Luis_Wastewater454_TO_NC_008766_TOP_HITS.txt");
	
		QueryHitGroup query1 = new QueryHitGroup( Color.BLACK, 
				wastewaterResultsFile, false, 75, "March 20 Wastewater" );
		
		list.add(query1);
		
		
		File envFile = new File( ConfigReader.getFragmentRecruiterSupportDir() + 
				File.separator + "environmental_sequence_TO_NC_008766_TOP_HITS.txt.gz");
		
		QueryHitGroup query2 = new QueryHitGroup(Color.RED,
				envFile, true, 250, "Environmental Samples");
		
		list.add(query2);
		
		FragmentRecruiterApp fra = new FragmentRecruiterApp(
				"Acidovorax sp. JS42 plasmid pAOVO02, complete sequence", list);
		
		
		fra.fragmentPanel.setHits(list);
		List<GenbankAnnotations> annotations= GenbankAnnotations.getGenbankAnnotations(new File( 
				ConfigReader.getFragmentRecruiterSupportDir() + File.separator + 
				"NC_008766_Description.txt"));
		
		fra.fragmentPanel.getXAxis().setLowValInDataSet(GenbankAnnotations.getLowVal(annotations));
		fra.fragmentPanel.getXAxis().setHighValInDataSet(GenbankAnnotations.getHighVal(annotations));
		
		fra.fragmentPanel.zoomOutAllYAxesFromDisplayedData();
		fra.fragmentPanel.getXAxis().zoomOut(list);
		
			
		fra.fragmentPanel.setGenbankAnnotationList(annotations);
		fra.fragmentPanel.zoomOutAllYAxesFromDisplayedData();
		fra.fragmentPanel.getXAxis().zoomOut(list);
		fra.updateXAxisScrollPaneFromXAxis();
		fra.callAfterDataLoaded(list);
	}
	
	public static void runNC_008765() throws Exception
	{
		List<QueryHitGroup> list = new ArrayList<QueryHitGroup>();

		File  wastewaterResultsFile =
			new File( ConfigReader.getFragmentRecruiterSupportDir() + File.separator + 
					"Luis_Wastewater454_TO_NC_008765_TOP_HITS.txt");
	
		QueryHitGroup query1 = new QueryHitGroup( Color.BLACK, 
				wastewaterResultsFile, false, 75, "March 20 Wastewater" );
		
		list.add(query1);
		
		FragmentRecruiterApp fra = new FragmentRecruiterApp(
				"Acidovorax sp. JS42 plasmid pAOVO01, complete sequence", list);
		fra.fragmentPanel.setHits(list);
		
		List<GenbankAnnotations> annotations= GenbankAnnotations.getGenbankAnnotations(new File( 
				ConfigReader.getFragmentRecruiterSupportDir() + File.separator + 
				"NC_008765_Description.txt"));
		
		fra.fragmentPanel.getXAxis().setLowValInDataSet(GenbankAnnotations.getLowVal(annotations));
		fra.fragmentPanel.getXAxis().setHighValInDataSet(GenbankAnnotations.getHighVal(annotations));
		
		fra.fragmentPanel.zoomOutAllYAxesFromDisplayedData();
		fra.fragmentPanel.getXAxis().zoomOut(list);
			
		fra.fragmentPanel.setGenbankAnnotationList(annotations);
		fra.fragmentPanel.zoomOutAllYAxesFromDisplayedData();
		fra.updateXAxisScrollPaneFromXAxis();
		fra.callAfterDataLoaded(list);
	}
	
	public static void runNC_008782() throws Exception
	{
		List<QueryHitGroup> innerList = new ArrayList<QueryHitGroup>();

		File  wastewaterResultsFile =
			new File( ConfigReader.getFragmentRecruiterSupportDir() + File.separator + 
					"MixedLiquor_First_454_Sample_TO_NC_008782_TOP_HITS.txt");
	
		QueryHitGroup query1 = new QueryHitGroup( Color.BLACK, 
				wastewaterResultsFile, false, 75, "March 20 Wastewater" );
		
		
		File envSequenceResultsFile = 
			new File( ConfigReader.getFragmentRecruiterSupportDir() + File.separator + 
					"EnvSamples_TO_NC_008782_TOP_HITS.txt.gz" );
					
		
		QueryHitGroup query2 = new QueryHitGroup( Color.RED, 
				envSequenceResultsFile, true, 250, 	
				"Environmental Sequences" );
		query2.setDisiplayThisData(false);
		
		innerList.add(query2);
		innerList.add(query1);
		
		File genomeTogenome = new File(  ConfigReader.getFragmentRecruiterSupportDir() 
				+ File.separator + 
				"NC_008752_TO_NC_008782_TOP_HITS.txt");
		
		QueryHitGroup query3 = new QueryHitGroup( Color.BLUE, genomeTogenome, false, 
				250, "NC_008752 Acidovorax avenae subsp. citrulli AAC00-1, complete genome");
		
		innerList.add(query3);
		
		setList(innerList);
		
		FragmentRecruiterApp fra = new FragmentRecruiterApp(
				"NC_008782 Acidovorax sp. JS42, complete genome", list);
		fra.fragmentPanel.setHits(list);
		List<GenbankAnnotations> annotations= GenbankAnnotations.getGenbankAnnotations(new File( 
				ConfigReader.getFragmentRecruiterSupportDir() + File.separator + 
				"NC_008782_GenbankAnnotations.txt"));
		fra.fragmentPanel.getXAxis().setLowValInDataSet(GenbankAnnotations.getLowVal(annotations));
		fra.fragmentPanel.getXAxis().setHighValInDataSet(GenbankAnnotations.getHighVal(annotations));
		
		fra.fragmentPanel.zoomOutAllYAxesFromDisplayedData();
		fra.fragmentPanel.getXAxis().zoomOut(list);
		fra.fragmentPanel.setLowX(100000);
		fra.fragmentPanel.setHighX(400000);
		fra.updateXAxisScrollPaneFromXAxis();
			
		fra.fragmentPanel.setGenbankAnnotationList(annotations);
		
		fra.callAfterDataLoaded(list);
	}
	
	public static void main(String[] args) throws Exception
	{
		runNC_008765();
		//runNC_008782();
		runNC_008766();
	}
	
	
}
