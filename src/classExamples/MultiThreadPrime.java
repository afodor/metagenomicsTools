package classExamples;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class MultiThreadPrime extends JFrame
{
	private JTextArea myArea = new JTextArea();
	
	private JButton cancelButton = new JButton("Cancel All");
	private JButton spawnButton = new JButton("Spawn thread");
	private JButton clear = new JButton("Clear");
	private HashSet<PrimeUpdater> threadset = new HashSet<PrimeUpdater>();
	
	private static Random RANDOM = new Random();
	int threadIdCounter =0;
	
	public MultiThreadPrime() throws Exception
	{
		super("Multi thread cancel demo");
		setLayout(new BorderLayout());
		getContentPane().add( new JScrollPane( myArea), BorderLayout.CENTER);
		getContentPane().add(getBottomPanel(), BorderLayout.SOUTH);
		setSize(400,300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	private void threadTerminated(PrimeUpdater p)
	{
		synchronized( threadset )
		{
			threadset.remove(p);
			addToArea(threadset.size() + " threads still running ");
			
			if( threadset.size() == 0 )
			{
				cancelButton.setEnabled(false);
			}
		}
	}
	
	private JPanel getBottomPanel() throws Exception
	{
		JPanel panel = new JPanel();
		
		panel.setLayout(new GridLayout(0,3));
		
		cancelButton.setEnabled(false);
		panel.add(spawnButton);
		panel.add(cancelButton);
		panel.add(clear);
		clear.setEnabled(false);
		
		clear.addActionListener( new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				myArea.setText("");
				clear.setEnabled(false);
			}
		} );
		
		cancelButton.addActionListener( new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				//todo:  Implement this...
			}
		} );
		
		spawnButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				threadIdCounter++;
				PrimeUpdater pu = new PrimeUpdater(threadIdCounter);
				
				synchronized(threadset)
				{
					threadset.add(pu);
					addToArea("Spawning concurrent thread " + threadset.size());					
				}
				
				cancelButton.setEnabled(true);
				new Thread(pu).start();
				
			}
		});
		
		return panel;
	}
	
	
	
	private class PrimeUpdater implements Runnable
	{
		private final int threadID;
		
		public PrimeUpdater(int threadID)
		{
			this.threadID = threadID;
		}
		
		@Override
		public void run()
		{	
			addToArea("Starting " + threadID);
			
			try
			{
				int aPrime = findAPrime();
				addToArea("Thread " + threadID + " Found a prime " + aPrime);
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
			finally
			{	
				
				threadTerminated(this);
			}
		}
		
		private int findAPrime() throws Exception
		{
			int numDone=0;
			
			while( true)
			{
				int anInt = RANDOM.nextInt(Integer.MAX_VALUE);
				
				boolean thisIsPrime = true;
				
				int stopPoint = (int) (Math.sqrt(anInt) + 1);
				
				// alternatively, if you want to spin the CPUs
				// do this and comment out the Thread.sleep(...) below
				//stopPoint = anInt;
				
				for( int x=2; x <  stopPoint && thisIsPrime; x++)
				{
					if( anInt % x == 0)
					{
						thisIsPrime = false;
					}
					
					numDone++;
					
					if(numDone % 10000==0)
						Thread.yield();
				}
				
				if( thisIsPrime)
				{
					// this is to simulate doing work if we've used the square root optimization
					Thread.sleep(2000);
					return anInt;
				}		
			}
		}
	}
	
	private void addToArea(final String s)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run() 
			{
				myArea.append(s + "\n");
				clear.setEnabled(true);
			}
		}
		);
	}

	public static void main(String[] args) throws Exception
	{
		new MultiThreadPrime();	
	}
}
