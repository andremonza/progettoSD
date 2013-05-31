package peer;

import java.awt.Container;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GUI extends JFrame implements MouseListener {
	
	private JPanel panel;
	private Client client;
	
	public GUI(String name, Client client) {
		super(name);
		super.setBounds(100, 100, 300, 100);
		
		panel = new JPanel();
		this.client = client;
		
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    Container con = this.getContentPane(); // inherit main frame
	    con.add(panel); // add the panel to frame
	    // customize panel here
	    // pane.add(someWidget);
	    setVisible(true); // display this frame
	    
	    JButton b = new JButton("click");
	    b.addMouseListener(this);
	    panel.add(b);
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		client.giocata(7698);
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
