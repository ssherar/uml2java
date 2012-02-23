package uk.ac.aber.dcs.cs124group.prototype;
import java.awt.*;
import java.util.regex.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;


public class Canvas extends JPanel {
	
	private ArrayList<JLabel> vars;
	private JTextArea edit;
	private int currentEdit;
	private int offset = 0;
	private Handler h = new Handler(this);
	private boolean editing = false;
	
	public Canvas() {
		setLayout(null);
		setBackground(Color.white);
		
		/*JTextField edit = new JTextField("Hello World");
		edit.setLocation(new Point(200, 250));
		this.add(edit);*/
		
		vars = new ArrayList<JLabel>();
		this.createVar("+ hello : String");
		this.createVar("+ trevor : String");
		this.createVar("+ lol : String");
		repaint();
	}
	
	private void createVar(String uml) {
		JLabel tmp = new JLabel(uml);
		tmp.setBounds((int)(Math.random() *500), (int)(Math.random()*500) , 400, 20);
		//offset += 25;
		tmp.addMouseListener(h);
		this.add(tmp);
		this.vars.add(tmp);
		repaint();
	}
	
	public int findLabel(JLabel find) {
		for(int i = 0; i < vars.size(); i++) {
			JLabel tmp = (JLabel)vars.get(i);
			if(find.equals(tmp)) return i;
		}
		return -1;
	}
	
	public int findLabel(Rectangle r) {
		for(int i = 0; i < vars.size(); i++) {
			JLabel tmp = (JLabel)vars.get(i);
			if(r.equals(tmp.getBounds())) return i;
		}
		return -1;
	}
	
	public void setVislibility(int i, boolean v) {
		vars.get(i).setVisible(v);
	}
	
	public void setEditing(int i) {
		editing = true;
		edit = null;
		edit = new JTextArea();
		edit.addFocusListener(h);
		edit.addKeyListener(h);
		
		currentEdit = i;
		
		edit.setBounds(this.vars.get(i).getBounds());
		edit.setText(this.vars.get(i).getText());
		this.remove(this.vars.get(i));
		this.add(edit);
		repaint();
	}
	
	public JLabel getVar(int i) {
		return vars.get(i);
	}
	
	public boolean isEditing() {
		return this.editing;
	}
	
	public void finishEditing() {
		editing = false;
	}
}
