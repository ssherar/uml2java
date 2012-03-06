package uk.ac.aber.dcs.cs124group.export;

import java.awt.*;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import javax.swing.RepaintManager;

/**
 * This class allows the user to print their class diagram directly to an external printer
 * 
 * @see Printable
 * 
 * @author Daniel Maly
 * @author Sam Sherar
 * @author Lee Smith
 * @version 1.0.0
 * 
 * 
 * 
 */

public class PrinterDriver extends Object implements Printable {

	private Component canvas;
	
	public PrinterDriver(Component com){
		this.canvas = com;
	}

	public void print() throws PrinterException{
		PrinterJob printJob = PrinterJob.getPrinterJob();
		
		printJob.setPrintable(this);
			
		if(printJob.printDialog()){
			printJob.print();
		}
	}
	
	@Override
	public int print(Graphics graphics, PageFormat pageFormat, int pageIndex)
			throws PrinterException {
		
		if(pageIndex > 0){
			return NO_SUCH_PAGE;
		}
		
		RepaintManager repaint = RepaintManager.currentManager(canvas);
		Graphics2D graphicsCreator = (Graphics2D)graphics;

		double resizeWidth = canvas.getWidth() / pageFormat.getWidth();
		double resizeHeight = canvas.getHeight() / pageFormat.getHeight();
		
		graphicsCreator.scale(1/resizeWidth, 1/resizeHeight);
		
		graphicsCreator.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
		repaint.setDoubleBufferingEnabled(false);
		canvas.paint(graphicsCreator);
		repaint.setDoubleBufferingEnabled(true);
		return PAGE_EXISTS;
	}
	
}
