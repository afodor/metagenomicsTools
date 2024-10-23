package classExamples;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.Timer;

public class AWT_TimerExample extends JFrame
{
	private int count =0;
	
	public AWT_TimerExample()
	{
		setTitle("Timer Example");
		setSize(200,200);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
		
		final JLabel label = new JLabel("Iteration " + count );
		setLayout(new BorderLayout());
		add(label, BorderLayout.CENTER);
		Timer timer = new Timer(1000, new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				count++;
				label.setText("Interation " + count);
			}
		});
		
		timer.start();
	}
	
	public static void main(String[] args)
	{
		new AWT_TimerExample();
	}
}
