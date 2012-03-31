package visualtester;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import core.compoundterm.primitiveterm.Prod;


public class ClientTester {
	static JFrame frame;
	static JPanel p;
	static Prod prod;
	
	static JTextField transfield;
	static JTextField splitfield;
	static JTextField mergefield;
	static JTextField orderfield;
	
	public static void main(String[] args) {

		frame = new JFrame("Primitive Test");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400, 400);
		frame.setLocation(400, 200);
		//frame.getContentPane().add(emptyLabel, BorderLayout.CENTER);
		
		p = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		
		c.gridx = 0;
		c.gridy = 0;
		JTextPane primitivetext = new JTextPane();
		primitivetext.setText("Terms");
		p.add(primitivetext,c);
		
		c.gridx = 1;
		c.gridy = 0;
		JTextPane messagetext = new JTextPane();
		messagetext.setText("Message Content");
		p.add(messagetext,c);
		
		c.gridx = 2;
		c.gridy = 0;
		JTextPane sizetext = new JTextPane();
		sizetext.setText("Size");
		p.add(sizetext,c);
		
		c.gridx = 0;
		c.gridy = 1;
		JTextPane transtext = new JTextPane();
		transtext.setText("Translator");
		transtext.setEditable(false);
		p.add(transtext,c);
		
		transfield = new JTextField("Ciao");
		c.gridx = 1;
		c.gridy = 1;
		transfield.setColumns(15);
		p.add(transfield,c);
		
		JButton transbutton = new JButton("TransTest>");
		c.gridx = 3;
		c.gridy = 1;
        transbutton.addActionListener(new ActionListener() {
        	 
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				prod = new Prod("mina:tcp://localhost:6886?textline=true&sync=false", String.class, transfield.getText());
				prod.start();
			}
        });
		p.add(transbutton,c);
		
		c.gridx = 0;
		c.gridy = 2;
		JTextPane splittext = new JTextPane();
		splittext.setText("Splitter");
		splittext.setEditable(false);
		p.add(splittext,c);
		
		splitfield = new JTextField("a b c d");
		c.gridx = 1;
		c.gridy = 2;
		p.add(splitfield,c);
		
		JButton splitbutton = new JButton("SplitTest>");
		c.gridx = 3;
		c.gridy = 2;
		
		splitbutton.addActionListener(new ActionListener() {
       	 
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				prod = new Prod("mina:tcp://localhost:6887?textline=true&sync=false", String.class, splitfield.getText());
				prod.start();
			}
        });
		
		p.add(splitbutton,c);
		
		c.gridx = 0;
		c.gridy = 3;
		JTextPane mergetext = new JTextPane();
		mergetext.setText("Split->Merge->Trans");
		mergetext.setEditable(false);
		p.add(mergetext,c);
		
		mergefield = new JTextField("a,b,c,d");
		c.gridx = 1;
		c.gridy = 3;
		p.add(mergefield,c);
		
		JTextField sizemergefield = new JTextField("4");
		c.gridx = 2;
		c.gridy = 3;
		sizemergefield.setEnabled(false);
		p.add(sizemergefield,c);
		
		JButton mergebutton = new JButton("MergeTest>");
		c.gridx = 3;
		c.gridy = 3;
		mergebutton.addActionListener(new ActionListener() {
	       	 
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				prod = new Prod("mina:tcp://localhost:6894?textline=true&sync=false", String.class, mergefield.getText());
				prod.start();
			}
        });
		p.add(mergebutton,c);
		
		c.gridx = 0;
		c.gridy = 4;
		JTextPane ordertext = new JTextPane();
		ordertext.setText("Split->Order->Merge->Trans");
		ordertext.setEditable(false);
		p.add(ordertext,c);
		
		orderfield = new JTextField("1,0,2,4,3");
		c.gridx = 1;
		c.gridy = 4;
		p.add(orderfield,c);
		
		JTextField sizeorderfield = new JTextField("5");
		c.gridx = 2;
		c.gridy = 4;
		sizeorderfield.setEnabled(false);
		p.add(sizeorderfield,c);
		
		JButton orderbutton = new JButton("OrderTest >");
		c.gridx = 3;
		c.gridy = 4;
		orderbutton.addActionListener(new ActionListener() {
        	 
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				prod = new Prod("mina:tcp://localhost:6889?textline=true&sync=false", String.class, orderfield.getText());
				prod.start();
			}
        });
		p.add(orderbutton,c);
		
		frame.add(p);
		frame.pack();
		frame.setVisible(true);
	}
}
