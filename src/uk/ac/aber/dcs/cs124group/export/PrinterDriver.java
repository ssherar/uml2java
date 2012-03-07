package uk.ac.aber.dcs.cs124group.export;

import java.awt.*;
import java.awt.print.*;

import javax.swing.RepaintManager;

/**
 * This class allows the user to print their class diagram directly to an
 * external printer
 * 
 * @see java.awt.print.Printable Printable
 * 
 * @author Daniel Maly
 * @author Sam Sherar
 * @author Lee Smith
 * @version 1.0.0
 * 
 */

public class PrinterDriver implements Printable {

	private Component canvas;

	/**
	 * Constructor that sets the current canvas state as the main component
	 * ready to be printed
	 * 
	 * @param com
	 * @see uk.ac.aber.dcs.cs124group.gui.Canvas Canvas
	 */
	public PrinterDriver(Component com) {
		this.canvas = com;
	}

	/**
	 * Sets the printer ready to print the job, displays the printDialog for the
	 * user to select the printer they wish to print to
	 * 
	 * @throws PrinterException
	 */
	public void print() throws PrinterException {
		PrinterJob printJob = PrinterJob.getPrinterJob();

		printJob.setPrintable(this);

		if (printJob.printDialog()) {
			printJob.print();
		}
	}

	@Override
	public int print(Graphics graphics, PageFormat pageFormat, int pageIndex)
			throws PrinterException {

		if (pageIndex > 0) {
			return NO_SUCH_PAGE;
		}

		RepaintManager repaint = RepaintManager.currentManager(canvas);
		Graphics2D graphicsCreator = (Graphics2D) graphics;

		double resizeWidth = canvas.getWidth() / pageFormat.getWidth();
		double resizeHeight = canvas.getHeight() / pageFormat.getHeight();

		graphicsCreator.scale(1 / resizeWidth, 1 / resizeHeight);

		graphicsCreator.translate(pageFormat.getImageableX(),
				pageFormat.getImageableY());
		repaint.setDoubleBufferingEnabled(false);
		canvas.paint(graphicsCreator);
		repaint.setDoubleBufferingEnabled(true);
		return PAGE_EXISTS;
	}

}
