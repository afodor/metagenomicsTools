package classExamples.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Random;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class DoubleOrNothing extends JFrame
{
	private static final long serialVersionUID = 3794059922116115530L;
	
	private JTextField aTextField = new JTextField();
	private int numDollars =1;
	private Random random = new Random();
	private JCheckBox enableCheatItem;

	private JPanel getBottomPanel() 
	{
		JPanel panel = new JPanel();
		
		panel.setLayout(new GridLayout(0,3));
		
		JButton doubleButton = new JButton("Double or nothing");
		doubleButton.setMnemonic('D');
		doubleButton.setToolTipText("Click here");
		
		doubleButton.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				{
					if( random.nextFloat() < 0.5 || ( enableCheatItem.isSelected() &&  (e.getModifiers() & e.SHIFT_MASK) != 0 ) )
						numDollars = numDollars  *2;
					else
						numDollars =0;
					
					updateTextField();
			
				}
			}
		});
		
		JButton addOneButton = new JButton("Add $1");
		addOneButton.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				System.out.println("Got click");
				numDollars++;
				updateTextField();
			}
		});
		
		JButton removeOneButton = new JButton("Remove $1");
		removeOneButton.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				numDollars--;
				updateTextField();
			}
		});
		
		panel.add(addOneButton);
		panel.add(doubleButton);
		panel.add(removeOneButton);
		
		return panel;
		
	}
	
	private void updateTextField()
	{
		aTextField.setText("You have $" + numDollars);
		validate();
	}
	
	public DoubleOrNothing() 
	{
		super("Double your money!");
		
		setLocationRelativeTo(null);
		setSize(400,200);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(getBottomPanel(), BorderLayout.SOUTH);
		getContentPane().add(aTextField, BorderLayout.CENTER);
		setJMenuBar(getMyMenuBar());
		updateTextField();
		setVisible(true);
	}
	
	private void loadFromFile() 
	{
		JFileChooser jfc = new JFileChooser();
		
		if( jfc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION)
			return;
		
		if( jfc.getSelectedFile() == null)
			return;
			
		File chosenFile = jfc.getSelectedFile();
		
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(chosenFile));
			String line = reader.readLine();
			
			if( line == null || reader.readLine() != null)
				throw new Exception("Unexpected file format");
			
			StringTokenizer sToken = new StringTokenizer(line);
			
			if( sToken.countTokens() != 1)
				throw new Exception("Unexpected file format");
			
			try
			{
				this.numDollars = Integer.parseInt(sToken.nextToken());
			}
			catch(Exception ex)
			{
				throw new Exception("Unexpected file format");
			}
			
			updateTextField();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Could not read file", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void saveToFile()
	{
		JFileChooser jfc = new JFileChooser();
			
		if( jfc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION)
			return;
		
		if( jfc.getSelectedFile() == null)
			return;
			
		File chosenFile = jfc.getSelectedFile();
			
		if( jfc.getSelectedFile().exists())
		{
			String message = "File " + jfc.getSelectedFile().getName() + " exists.  Overwrite?";
				
			if( JOptionPane.showConfirmDialog(this, message) != 
					JOptionPane.YES_OPTION)
					return;			
		}
		
		try
		{
			BufferedWriter writer= new BufferedWriter(new FileWriter(chosenFile));
			writer.write(this.numDollars+ "\n");
			writer.flush();  writer.close();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Could not write file", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private JMenuBar getMyMenuBar()
	{
		JMenuBar jmenuBar = new JMenuBar();
		
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic('F');
		jmenuBar.add(fileMenu);
		
		JMenu subMenu = new JMenu("SubMenu");
		fileMenu.add(subMenu);
		subMenu.add(new JMenuItem("SubItem"));
		
		JMenuItem saveItem = new JMenuItem("Save");
		fileMenu.add(saveItem);
		saveItem.setMnemonic('S');
		
		saveItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				saveToFile();
			}
		});
		
		JMenuItem openItem = new JMenuItem("Open");
		openItem.setMnemonic('O');
		fileMenu.add(openItem);
		
		openItem.addActionListener(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					loadFromFile();
				}
			}	
				);
		
		JMenu cheatMenu = new JMenu("Cheat");
		cheatMenu.setMnemonic('C');
		jmenuBar.add(cheatMenu);
		
		this.enableCheatItem =new JCheckBox("Enable Cheat");
		enableCheatItem.setMnemonic('E');
		cheatMenu.add(enableCheatItem);
		return jmenuBar;
	}
	
	public static void main(String[] args)
	{
		new DoubleOrNothing();
	}
	
}
