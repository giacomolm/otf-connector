package visualtester;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextPane;

import core.compoundterm.primitiveterm.Cons;

public class ReceiverDialog {
	static JFrame frame;
	static JPanel panel;
	static JTextPane tp;
    static File file = new File("./log/report.txt");
    static FileInputStream fis = null;
    static BufferedInputStream bis = null;
    static DataInputStream dis = null;
	static Cons cons;
    
	public static void main(String[] args) {
		frame = new JFrame("Receiver Dialog");
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(200, 100);
		frame.setLocation(450, 350);
		
		panel = new JPanel();
		
		tp = new JTextPane();
		tp.setEditable(false);
		
		Timer timer = new Timer();

		timer.scheduleAtFixedRate(new TimerTask() {
		        public void run() {
		        	refreshText();
		        }
		    }, 0, 500);
		
		panel.add(tp);
		
		frame.add(panel);
		frame.setVisible(true);
	}
	
	public static void refreshText(){

	    try {
	      fis = new FileInputStream(file);
	      bis = new BufferedInputStream(fis);
	      dis = new DataInputStream(bis);
	      
	      String s = "";
	      while (dis.available() != 0) {
	        s+=dis.readLine();
	      }
	      tp.setText(s);
	      frame.repaint();
	      fis.close();
	      bis.close();
	      dis.close();

	    } catch (FileNotFoundException e) {
	      e.printStackTrace();
	    } catch (IOException e) {
	      e.printStackTrace();
	    }
	}
}
