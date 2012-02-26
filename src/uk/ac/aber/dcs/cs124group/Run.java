package uk.ac.aber.dcs.cs124group;
import javax.swing.SwingUtilities;

import uk.ac.aber.dcs.cs124group.controller.Manager;

public class Run {

	/** Launches the program */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
	         public void run() {
	            new Manager();
	         }
	      });
	   }
	}


