package uk.ac.aber.dcs.cs124group.gui;

import uk.ac.aber.dcs.cs124group.model.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class DiagramListener implements KeyListener, MouseMotionListener, MouseListener, java.io.Serializable {
	
	protected ListeningMode mode = ListeningMode.LISTEN_TO_ALL;
	protected JPanel diagram;
	private TextLabel currentEdited;
	
	public DiagramListener() {
		
	}
	
	public DiagramListener(JPanel panel) {
		this.diagram = panel;
	}
	
	protected void assignTo(JPanel panel) {
		this.diagram = panel;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getComponent() != null) {
			if(e.getComponent() instanceof TextLabel) {
				if(e.getClickCount() == 2 && !e.isConsumed() && mode == ListeningMode.LISTEN_TO_ALL) {
					e.consume();
					TextLabel l = (TextLabel)(e.getComponent());
					if(l != null) {
						mode = ListeningMode.EDITING_TEXT;
						enableLabelEdit(l);
					}
				
				}
			}
		}
		else {
			//TODO: Validate text edit
		}
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseDragged(MouseEvent e) {

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override 
	public void keyPressed(KeyEvent e) {
		if(e.getComponent().getName() == "EditingDiagramLabel" && e.getKeyCode() == KeyEvent.VK_ENTER) {
			exitLabelEdit((JTextArea) e.getComponent());
			/*if(tmp.getText().length() < 1) {
				//status.setText("A label cannot be set to nothing. To delete please Right Click and click Delete."); TODO: Fixme
				e.consume();
				return;
			}

			//status.setText("Label modified successfully!"); TODO: Fixme*/
		}
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {}

	@Override
	public void keyTyped(KeyEvent arg0) {}
	
	protected void enableLabelEdit(TextLabel label) {
		//status.setText("Editing a label! When finished, please press ENTER to commit."); TODO: Fixme
		currentEdited = label;
		diagram.remove(currentEdited);
			
		final JTextArea labelTextArea = new JTextArea();
		
		int x = label.getLocation().x;
		int y = label.getLocation().y;
		
		int diagramWidth = diagram.getPreferredSize().width;
		int diagramHeight = diagram.getPreferredSize().height;
		
		labelTextArea.setPreferredSize(new Dimension(diagramWidth - x, diagramHeight - y));
		labelTextArea.setLocation(new Point(x,y));
		labelTextArea.setName("EditingDiagramLabel");
		labelTextArea.setOpaque(false);
		labelTextArea.setFont(diagram.getFont());
		labelTextArea.setLineWrap(true);
		labelTextArea.setWrapStyleWord(true);
		
		//labelTextArea.setFont(document.getPreferences().getFont()); TODO: Fixme
		labelTextArea.setText(currentEdited.getText());
		labelTextArea.addKeyListener(this);
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				labelTextArea.requestFocus();
				labelTextArea.selectAll();
			}
			
		});
		diagram.add(labelTextArea);
		diagram.revalidate();
		diagram.repaint();
	}
	
	protected void exitLabelEdit(JTextArea a) {
		diagram.remove(a);
		currentEdited.setText(a.getText());
		diagram.add(currentEdited);
		diagram.repaint();
		mode = ListeningMode.LISTEN_TO_ALL;
		
	}

}
