package uk.ac.aber.dcs.cs124group.export;

import uk.ac.aber.dcs.cs124group.controller.Manager;
import uk.ac.aber.dcs.cs124group.gui.*;
import uk.ac.aber.dcs.cs124group.model.*;
import uk.ac.aber.dcs.cs124group.view.ClassRectangle;

import java.util.*;
import java.io.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.*;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Exporter {

	private DocumentModel model;
	private String outputDirectory;
	private Canvas canvas;
	private ArrayList<String> outputFiles;
	private ArrayList<String> fileNames;
	private final String NL = "\n";
	private final String TB = "\t";
	private String imageSaveLocation;
	private Manager manager;


	public Exporter(DocumentModel model, Manager manager) {
		this.model = model;
		this.manager = manager;
	}

	public Exporter(Canvas c, Manager manager) {
		this.canvas  = c;
		this.manager = manager;
	}
	

	public void exportImage() throws IIOException {
		BufferedImage bi = new BufferedImage(canvas.getSize().width,
				canvas.getSize().height, BufferedImage.TYPE_INT_ARGB);
		Graphics g = bi.createGraphics();

		canvas.paint(g); // this == JComponent
		g.dispose();

		manager.setWaitCursor(true);
		JFileChooser fcImage = new JFileChooser();
		fcImage.setAcceptAllFileFilterUsed(false);
		manager.setWaitCursor(false);

		FileNameExtensionFilter png = new FileNameExtensionFilter("PNG Image",
				"png");
		FileNameExtensionFilter jpg = new FileNameExtensionFilter("JPEG Image",
				"jpg");
		FileNameExtensionFilter gif = new FileNameExtensionFilter("GIF Image",
				"gif");
		fcImage.addChoosableFileFilter(png);
		fcImage.addChoosableFileFilter(jpg);
		fcImage.addChoosableFileFilter(gif);

		int fcReturnVal = fcImage.showSaveDialog(null);

		if (fcReturnVal == JFileChooser.APPROVE_OPTION) {
			File saveFile = fcImage.getSelectedFile();

			try {
				if (fcImage.getFileFilter() == png) {
					ImageIO.write(bi, "png", new File(saveFile + ".png"));
				} else if (fcImage.getFileFilter() == jpg) {
					ImageIO.write(bi, "jpg", new File(saveFile + ".jpg"));
				} else if (fcImage.getFileFilter() == gif) {
					ImageIO.write(bi, "gif", new File(saveFile + ".gif"));
				}
			} catch (Exception e) {
			}
		}
	}

	private void serialise(String path) {
		// TODO Auto-generated method stub

	}

	public void exportCode() throws IOException {
		for (int i = 0; i < model.getElements().size(); i++) {
			if (model.getElements().get(i).equals(ClassRectangle.class)) {
				fileNames.add(model.getElements().get(i).getName() + ".java");
				outputFiles.add(createClassFileContents((ClassRectangle) model
						.getElements().get(i)));
			}
		}

		manager.setWaitCursor(true);
		JFileChooser fcCode = new JFileChooser();
		fcCode.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fcCode.setAcceptAllFileFilterUsed(false);

		File chosenDirectory = fcCode.getCurrentDirectory();

		int fcReturnVal = fcCode.showDialog(null, "Select Directory");
		manager.setWaitCursor(false);

		if (fcReturnVal == JFileChooser.APPROVE_OPTION) {
			System.out.println(fileNames.size());
			for (int j = fileNames.size() - 1; j >= 0; j--) {
				File f;
				f = new File(fileNames.get(j));
				if (!f.exists()) {
					f.createNewFile();
					PrintWriter fileOut = new PrintWriter(
							new OutputStreamWriter(new FileOutputStream(
									chosenDirectory + fileNames.get(j))));
					fileOut.println(outputFiles.get(j));
					fileOut.close();
				}
			}
		}

	}

	private String createClassFileContents(ClassRectangle r) {
		String contents = "";

		switch (r.getVisibility()) {
		case PUBLIC:
			contents = "public ";
		case PRIVATE:
			contents = "private ";
		case PROTECTED:
			contents = "protected ";
		case PACKAGE:
			contents = "package ";
		}

		if (r.isAbstract() && !r.isFinal()) {
			contents.concat("abstract ");
		}

		if (r.isStatic()) {
			contents.concat("static ");
		}

		contents.concat("class "); // can amend this to include interface and
									// enums etc if we decide to implement them

		contents.concat(r.getClassName() + " ");

		String eXtends = "";
		String iMplements = "";

		for (int l = 0; l <= r.getRelationships().size() - 1; l++) {
			switch (r.getRelationships().get(l).getType()) {
			case IMPLEMENTS:
				iMplements.concat(r.getRelationships().get(l).getGoingTo()
						.getName()
						+ " ");
			case INHERITANCE:
				eXtends.concat(r.getRelationships().get(l).getGoingTo()
						.getName()
						+ " ");
			}
		}

		if (!eXtends.isEmpty()) {
			contents.concat("extends " + eXtends);
		}
		if (!iMplements.isEmpty()) {
			contents.concat("implements " + iMplements);
		}

		contents.concat("{" + NL + NL);

		// --------------------Attributes/Fields----------------------------
		for (int j = 0; j <= r.getAttributes().size() - 1; j++) {

			boolean isAttributeFinal = false;
			boolean isMethodAbstract = false;

			switch (r.getAttributes().get(j).getType()) {
			case DATA_FIELD:
				switch (r.getAttributes().get(j).getVisibility()) {
				case PUBLIC:
					contents.concat(TB + "public ");
				case PRIVATE:
					contents.concat(TB + "private ");
				case PROTECTED:
					contents.concat(TB + "protected ");
				}

				if (r.getAttributes().get(j).isFlagStatic()) {
					contents.concat("static ");
				}

				if (r.getAttributes().get(j).isFlagFinal()) {
					contents.concat("final ");
					isAttributeFinal = true;
				}

				if (r.getAttributes().get(j).isFlagTransient()) {
					contents.concat("transistent ");
				}

				if (r.getAttributes().get(j).isFlagVolatile()) {
					contents.concat("volatile ");
				}

				contents.concat(r.getAttributes().get(j).getType().toString());

				if (isAttributeFinal) {
					contents.concat(r.getAttributes().get(j).getAttributeName()
							.toUpperCase()
							+ ";");
				} else {
					contents.concat(r.getAttributes().get(j).getAttributeName()
							+ ";");
				}

				// ------------------------Methods---------------------------
			case METHOD:
				switch (r.getAttributes().get(j).getVisibility()) {
				case PUBLIC:
					contents.concat(TB + "public ");
				case PRIVATE:
					contents.concat(TB + "private ");
				case PROTECTED:
					contents.concat(TB + "protected ");
				}

				if (r.getAttributes().get(j).isFlagAbstract()) {
					contents.concat("abstract");
					isMethodAbstract = true;
				}

				if (r.getAttributes().get(j).isFlagStatic()) {
					contents.concat("static ");
				}

				if (r.getAttributes().get(j).isFlagFinal()) {
					contents.concat("final ");
				}

				if (r.getAttributes().get(j).isFlagSyncronised()) {
					contents.concat("syncronized ");
				}

				contents.concat(r.getAttributes().get(j).getReturnType() + " ");

				contents.concat(r.getAttributes().get(j).getAttributeName()
						+ "(");

				int numOfArgs = r.getAttributes().get(j).getArgs().size();

				for (int k = 0; k <= numOfArgs - 1; k++) {
					contents.concat(r.getAttributes().get(j).getArgs().get(k));
					if (k < numOfArgs - 2) {
						contents.concat(", ");
					}
				}
				contents.concat(")");
				if (isMethodAbstract) {
					contents.concat(";");
				} else {
					if (r.getAttributes().get(j).getReturnType() != "void") {
						contents.concat("{" + NL + TB + TB + "return null;"
								+ NL + TB + "}");
					} else {
						contents.concat("{" + NL + NL + TB + "}");
					}
				}

			}
			contents.concat("}");
		}
		return contents;

	}
}
