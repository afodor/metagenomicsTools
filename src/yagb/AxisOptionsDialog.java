package yagb;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class AxisOptionsDialog extends JDialog
{
	private static final long serialVersionUID = 7826945436332181692L;
	private FragmentRecruiterApp parent;
	private JPanel mainPanel = new JPanel();
	private AxisOptionsDialog thisDialog;
	private boolean isActive = true;
	private JCheckBox showGeneLabelsCheckBox;
	private JTextField numPixelsReservedForLabelsTextArea;
	
	public boolean dialogIsStillActive()
	{
		return isActive;
	}
	
	AxisOptionsDialog(FragmentRecruiterApp applicationParent)
	{
		super(applicationParent, false);
		setTitle("Axis Options for" + applicationParent.getTitle());
		this.parent = applicationParent;
		parent.getFragmentPanel().getSelectedYAxis().setIgnoreScrollBarUpdates(true);
		parent.getFragmentPanel().getXAxis().setIgnoreScrollBarUpdates(true);
		parent.getAxisMenu().setEnabled(false);
		
		addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosed(WindowEvent e)
			{
				parent.getFragmentPanel().getSelectedYAxis().setIgnoreScrollBarUpdates(false);
				parent.getFragmentPanel().getXAxis().setIgnoreScrollBarUpdates(false);
				
				parent.getAxisMenu().setEnabled(true);
				isActive = false;
			}
			
			@Override
			public void windowActivated(WindowEvent e)
			{
				parent.getFragmentPanel().getSelectedYAxis().setIgnoreScrollBarUpdates(true);
				parent.getFragmentPanel().getXAxis().setIgnoreScrollBarUpdates(true);
			
			}
			
			@Override
			public void windowDeactivated(WindowEvent e)
			{
				parent.getFragmentPanel().getSelectedYAxis().setIgnoreScrollBarUpdates(false);
				parent.getFragmentPanel().getXAxis().setIgnoreScrollBarUpdates(false);
			}
			
		}
		);
		
		thisDialog = this;
		setLayout(new BorderLayout());
		add(new AxisSelectorJComboBox(), BorderLayout.NORTH);
		add(mainPanel, BorderLayout.CENTER);
		setAlwaysOnTop(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(400,300);
		setLocationRelativeTo(applicationParent);
		layoutMainPanel();
		setVisible(true);
	}
	
	private class YaxisSelectedCheckBox extends JCheckBox
	{
		YaxisSelectedCheckBox()
		{
			super("Show Y Axis");
			
			setSelected( parent.getFragmentPanel().getSelectedYAxis().isDisplayed() );
			setMnemonic('S');
			
			addActionListener(
					new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
						{
							AxisInfo axis = parent.getFragmentPanel().getSelectedYAxis();
							axis.setDisplayed(isSelected());
							parent.getFragmentPanel().repaint();
						}
					}
					);
			
		}
	}
	
	private class XAxisSelectedCheckBox extends JCheckBox
	{	
		XAxisSelectedCheckBox()
		{
			super("Show X Axis");
			
			setSelected( parent.getFragmentPanel().getSelectedYAxis().isDisplayed() );
			setMnemonic('X');
			
			addActionListener(
					new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
						{
							AxisInfo axis = parent.getFragmentPanel().getXAxis();
							axis.setDisplayed(isSelected());
							parent.getFragmentPanel().repaint();
						}
					}
					);
			
		}
	}
	
	private class YJitterSlider extends JSlider
	{
		YJitterSlider()
		{
			setMaximum(20);
			setValue(parent.getFragmentPanel().getJitterVal());
			
			addChangeListener(new ChangeListener()
			{
					public void stateChanged(ChangeEvent e)
					{
						parent.getFragmentPanel().setJitterVal(getValue());
						parent.getFragmentPanel().repaint();
						
					}
			}
		);
		}
	}
	
	
	private class ReZoomY extends JButton
	{
		public ReZoomY()
		{
			super("Zoom out Y Axis");
			
			addActionListener(
					new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
						{
							//only zoom to the currently displayed data
							parent.getFragmentPanel().getSelectedYAxis().resetZoomCache();
							parent.getFragmentPanel().zoomOutY(true);
							layoutMainPanel();
							parent.updateYAxisScrollPaneFromCurrentlySelectedAxis();
							thisDialog.validateTree();
						}
					}
							 );
		}
	}
	
	private class ReZoomX extends JButton
	{
		public ReZoomX()
		{
			super("Zoom out X Axis");
			
			addActionListener(
					new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
						{
							parent.getFragmentPanel().getXAxis().resetZoomCache();
							parent.getFragmentPanel().zoomOutX(true);
							layoutMainPanel();
							parent.updateXAxisScrollPaneFromXAxis();
							thisDialog.validateTree();
						}
					}
							 );
		}
	}
	
	@SuppressWarnings("rawtypes")
	private class AxisSelectorJComboBox extends JComboBox
	{
		@SuppressWarnings("unchecked")
		public AxisSelectorJComboBox()
		{	
			for( AxisInfo ai : parent.getFragmentPanel().getAllYAxes() )
				addItem(ai);
			
			setSelectedItem(parent.getFragmentPanel().getSelectedYAxis());
			
			addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					AxisInfo ai = (AxisInfo) getSelectedItem();
					parent.getFragmentPanel().setSelectedYAxis(ai);
					layoutMainPanel();
					parent.updateYAxisScrollPaneFromCurrentlySelectedAxis();
					thisDialog.validateTree();
				}
			});
			
		}
	}
	
	private class ShowGeneLabelsCheckBox extends JCheckBox
	{
		public ShowGeneLabelsCheckBox()
		{
			super("Show gene labels");
			setSelected(parent.getFragmentPanel().isCurrentlyShowingGeneLabels());
			addActionListener( 
					new ActionListener()
					{
						public void actionPerformed(ActionEvent e) 
						{
							parent.getFragmentPanel().setShowGeneLabels(isSelected());
							parent.getFragmentPanel().repaint();
						}
					}
			);
		}
	}
	
	private class NumPixelsReservedForTextArea extends JTextField
	{
		void perform()
		{
			float newValue = -1;
			
			try
			{
				newValue = Float.parseFloat(getText());
			}
			catch(Exception e)
			{
				
			}
			
			int intNewValue = (int)newValue;
			
			if( intNewValue <= 0 || intNewValue > 1000)
			{
				setText("" + parent.getFragmentPanel().getNumPixelsReservedForGeneAnnotations());
			}
			else
			{
				parent.getFragmentPanel().setNumPixelsReservedForGeneAnnotations(intNewValue);
				parent.getFragmentPanel().repaint();
				setText("" + intNewValue);
			}
				
		}
		
		NumPixelsReservedForTextArea()
		{
			
			
			setText("" + parent.getFragmentPanel().getNumPixelsReservedForGeneAnnotations());
			
			addActionListener(
					new ActionListener()
					{
						public void actionPerformed(ActionEvent e)
						{
							perform();
						}
					}
				);
			
			addFocusListener
			(
					new FocusListener()
					{
						public void focusGained(FocusEvent e)
						{
						}
						
						public void focusLost(FocusEvent e)
						{
							perform();
						}
					}
			);
		}
	}
	
	private class ShowGeneAnnotationsCheckBox extends JCheckBox
	{
		public ShowGeneAnnotationsCheckBox()
		{
			super("Show Gene Annotations");
			setSelected(parent.getFragmentPanel().isCurrentlyShowingGeneAnnotations());
			
			addActionListener( 
					new ActionListener()
					{
						public void actionPerformed(ActionEvent e) 
						{
							showGeneLabelsCheckBox.setEnabled(isSelected());
							numPixelsReservedForLabelsTextArea.setEnabled(isSelected());
							parent.getFragmentPanel().setShowGeneAnnotations(isSelected());
							parent.getFragmentPanel().repaint();
						}
					}
			);
			
		}
	}
	
	private void layoutYAxisInfo()
	{
		mainPanel.add(new YaxisSelectedCheckBox());
		
		JPanel lowPanel = new JPanel();
		lowPanel.setLayout(new GridLayout(1,2));
		lowPanel.add(new JLabel("Low Y-Axis Val"));
		RangeSettingTextFieldLowY lowRangeTextField = new RangeSettingTextFieldLowY(parent);
		lowPanel.add(lowRangeTextField);
		mainPanel.add(lowPanel);
		
		JPanel highPanel = new JPanel();
		highPanel.setLayout(new GridLayout(1,2));
		highPanel.add(new JLabel("High Y-Axis Val"));
		RangeSettingTextFieldHighY highRangeTextField = new RangeSettingTextFieldHighY(parent);
		highPanel.add(highRangeTextField);
		mainPanel.add(highPanel);
		
		JPanel jitterPanel = new JPanel();
		jitterPanel.setLayout(new GridLayout(1,2));
		jitterPanel.add(new JLabel("Random Noise in Y-axis data:"));
		jitterPanel.add(new YJitterSlider());
		mainPanel.add(jitterPanel);
		
		JPanel zoomButtonPanel = new JPanel();
		zoomButtonPanel.setLayout(new GridLayout(1,3));
		zoomButtonPanel.add(new JLabel(""));
		zoomButtonPanel.add(new ReZoomY());
		zoomButtonPanel.add(new JLabel(""));
		mainPanel.add(zoomButtonPanel);
		

	}
	
	
	void layoutMainPanel()
	{
		mainPanel.removeAll();
		mainPanel.setLayout(new GridLayout(14,1));
		layoutYAxisInfo();
		
		mainPanel.add(new JLabel());
		mainPanel.add(new JLabel("X axis"));
		mainPanel.add(new XAxisSelectedCheckBox());
		showGeneLabelsCheckBox = new ShowGeneLabelsCheckBox();
		mainPanel.add(new ShowGeneAnnotationsCheckBox());
		mainPanel.add(showGeneLabelsCheckBox);
		
		JPanel numPixelsPanel = new JPanel();
		numPixelsPanel.setLayout(new GridLayout(1,2));
		numPixelsPanel.add(new JLabel("Num pixels for text area:"));
		numPixelsReservedForLabelsTextArea = new NumPixelsReservedForTextArea();
		numPixelsPanel.add(numPixelsReservedForLabelsTextArea);
		mainPanel.add(numPixelsPanel);
		
		JPanel lowPanel = new JPanel();
		lowPanel.setLayout(new GridLayout(1,2));
		lowPanel.add(new JLabel("Low X-Axis Val"));
		lowPanel.add(new RangeSettingTextFieldLowX(parent));
		mainPanel.add(lowPanel);
		
		JPanel highPanel = new JPanel();
		highPanel.setLayout(new GridLayout(1,2));
		highPanel.add(new JLabel("High X-Axis Val"));
		highPanel.add(new RangeSettingTextFieldHighX(parent));
		mainPanel.add(highPanel);	
		
		JPanel zoomButtonPanel = new JPanel();
		zoomButtonPanel.setLayout(new GridLayout(1,3));
		zoomButtonPanel.add(new JLabel(""));
		zoomButtonPanel.add(new ReZoomX());
		zoomButtonPanel.add(new JLabel(""));
		mainPanel.add(zoomButtonPanel);
		
	}
}
