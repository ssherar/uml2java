package uk.ac.aber.dcs.cs124group.prototype;
import java.awt.event.*;
import java.util.regex.*;

import javax.swing.event.*;
import javax.swing.*;

public class Handler implements MouseListener, FocusListener, KeyListener {
	
	private Canvas canvas;
	
	public Handler(Canvas c) {
		this.canvas = c;
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		if(arg0.getClickCount() == 2 && !arg0.isConsumed()) {
			arg0.consume();
			
			int ret = canvas.findLabel((JLabel)arg0.getSource());
			if(ret != -1) {
				//canvas.setVislibility(ret, false);
				canvas.setEditing(ret);
			}
		}
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

	@Override
	public void focusGained(FocusEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void focusLost(FocusEvent e) {
		// TODO Auto-generated method stub

		//Matcher m = Pattern.matcher();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.getKeyCode() == e.VK_ENTER) {
			Pattern p = Pattern.compile("^[+#-] [a-z].[a-zA-Z]* \\: [A-Za-z]*( [=] [a-zA-Z0-9]{0,9})?$");
			JTextField tmp = (JTextField) e.getComponent();
			Matcher m = p.matcher(tmp.getText());
			while(m.find()) {
				int i = tmp.getY() / 25;
				canvas.remove(tmp);
				JLabel modified = canvas.getVar(i);
				modified.setText(tmp.getText());
				canvas.add(modified);
				canvas.repaint();
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}
