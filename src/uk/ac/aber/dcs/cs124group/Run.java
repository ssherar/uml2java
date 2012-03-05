package uk.ac.aber.dcs.cs124group;
import javax.swing.SwingUtilities;

import uk.ac.aber.dcs.cs124group.controller.Manager;

/** 
 * This is the main class of the application and contains the program entry point.
 * @author Daniel Maly
 * @author Samuel B Sherar
 * @author Lee Smith
 * @version 1.0.0
 */
public class Run {

	/** Launches the program. */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
	         public void run() {
	            new Manager();
	         }
	      });
	   }
	}


