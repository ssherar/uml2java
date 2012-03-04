package uk.ac.aber.dcs.cs124group.export;

import uk.ac.aber.dcs.cs124group.controller.Manager;
import uk.ac.aber.dcs.cs124group.gui.*;
import uk.ac.aber.dcs.cs124group.model.*;
import java.util.*;
import java.io.*;
import java.awt.Graphics;
import java.awt.image.*;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * 
 * The Exporter class allows the user to create java code documents based on the
 * class diagram they have created. The code exporter {@link #exportCode()}
 * provides a basic outline for the classes created in the diagram, completing
 * fields, methods and the basic outline of the class. It will also add any
 * relationships to other classes.
 * 
 * @author Daniel Maly, Sam Sherar, Lee Smith
 */

public class Exporter {

	private DocumentModel model;
	private MainFrame window;
	private Canvas canvas;
	private ArrayList<String> outputFiles = new ArrayList<String>();
	private ArrayList<String> fileNames = new ArrayList<String>();
	private final String NL = "\n";
	private final String TB = "\t";
	private Manager manager;
	private boolean errorCheck = false;

	/**
	 * Creates a <code>Exporter</code> instance with the specified DocumentModel
	 * and Manager. This constructor is used to complete exporting the diagram
	 * and creating the resulting code
	 * 
	 * @param model
	 *            The current state of the {@link #DocumentModel()}
	 * @param manager
	 *            The diagram {@link #Manager}
	 */

	public Exporter(DocumentModel model, Manager manager) {

		this.model = model;
		this.manager = manager;
	}

	/**
	 * Creates a <code>Exporter</code> instance with the current Canvas and
	 * Manager. This constructor is used to complete exporting the diagram and
	 * creating the resulting code
	 * 
	 * @param model
	 *            The current state of the {@link #Canvas()} and what is on it
	 * @param manager
	 *            The diagram {@link #Manager}
	 * 
	 */
	public Exporter(Canvas c, Manager manager) {

		this.canvas = c;
		this.manager = manager;
	}

	/**
	 * Allows Image export, to create a .png, .jpg, or .gif file from the canvas
	 * image. and allows the user to select where to place the file in their
	 * directory
	 */

	public void exportImage() throws IIOException {

		BufferedImage imageBuffer = new BufferedImage(canvas.getSize().width,
				canvas.getSize().height, BufferedImage.TYPE_INT_RGB);
		Graphics canvasImage = imageBuffer.createGraphics();

		canvas.paint(canvasImage);
		canvasImage.dispose();

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
					ImageIO.write(imageBuffer, "png", new File(saveFile
							+ ".png"));
				} else if (fcImage.getFileFilter() == jpg) {
					ImageIO.write(imageBuffer, "jpg", new File(saveFile
							+ ".jpg"));
				} else if (fcImage.getFileFilter() == gif) {
					ImageIO.write(imageBuffer, "gif", new File(saveFile
							+ ".gif"));
				}
			} catch (Exception e) {
			}
		}
	}

	/**
	 * Allows <code>.java</code> files to be created from the diagram
	 * components. Also allows the user to select the directory to save the
	 * files to.
	 */

	public void exportCode() throws IOException {
		for (int i = 0; i < model.getElements().size(); i++) {
			if (model.getElements().get(i).getClass() == (ClassModel.class)) {
				outputFiles.add(createClassFileContents((ClassModel) model
						.getElements().get(i)));
			}
		}
		if (!errorCheck) {
			manager.setWaitCursor(true);
			JFileChooser fcCode = new JFileChooser();
			fcCode.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			fcCode.setAcceptAllFileFilterUsed(false);
			int fcReturnVal = fcCode.showDialog(null, "Select Directory");

			manager.setWaitCursor(false);

			if (fcReturnVal == JFileChooser.APPROVE_OPTION) {
				String chosenDirectory = fcCode.getSelectedFile().getPath()
						+ "/";
				for (int j = fileNames.size() - 1; j >= 0; j--) {
					PrintWriter fileOut = new PrintWriter(
							new OutputStreamWriter(new FileOutputStream(
									chosenDirectory + "" + fileNames.get(j))));

					fileOut.println(outputFiles.get(j));
					fileOut.close();

				}
			}
		}

	}

	/**
	 * 
	 * Creates the String that is used to create the <code>.java</code> taking
	 * components and extracting information such as class name and return types
	 * from data that the user has entered into the class diagram.
	 * 
	 * @param classModel
	 *            All the components that the user has created in the class
	 *            diagram.
	 * @return String of characters that comprise the contents of the .java
	 *         files that are to be created.
	 */

	private String createClassFileContents(ClassModel classModel) {
		classModel.cleanUp();

		fileNames.add(classModel.getClassName() + ".java");

		String contents = "";
		switch (classModel.getVisibility()) {
		case PUBLIC:
			contents = "public ";
			break;
		case PRIVATE:
			contents = "private ";
			break;
		case PROTECTED:
			contents = "protected ";
			break;
		case PACKAGE:
			contents = "package ";
			break;
		}

		if (classModel.isAbstract() && !classModel.isFinal()) {
			contents = (contents + "abstract ");
		}

		if (classModel.isStatic()) {
			contents = (contents + "static ");
		}

		contents = (contents + "class "); // can amend this to include interface
											// and
		// enums etc if we decide to implement them
		contents = (contents + classModel.getClassName() + " ");

		String eXtends = "";
		String iMplements = "";

		int implementsNum = 0;
		for (int j = 0; j < classModel.getRelationships().size(); j++) {
			if (classModel.getRelationships().get(j).getType() == RelationshipType.IMPLEMENTS) {
				implementsNum++;

			}
		}

		for (int l = 0; l <= classModel.getRelationships().size() - 1; l++) {
			switch (classModel.getRelationships().get(l).getType()) {
			case IMPLEMENTS:
				if (classModel.getClassName() != classModel.getRelationships()
						.get(l).getGoingFrom().getClassName()) {
					iMplements = (iMplements + classModel.getRelationships()
							.get(l).getGoingFrom().getClassName());
				}
				if (implementsNum > 1) {
					iMplements = iMplements + ", ";
					implementsNum--;
				}

				iMplements = (iMplements + ", " + classModel.getRelationships()
						.get(l).getGoingFrom().getClassName());

				break;

			case INHERITANCE:
				if (classModel.getClassName() != classModel.getRelationships()
						.get(l).getGoingFrom().getClassName()) {
					eXtends = (eXtends
							+ classModel.getRelationships().get(l)
									.getGoingFrom().getClassName() + " ");
				}
				break;
			}
		}

		if (!eXtends.isEmpty()) {
			contents = (contents + "extends " + eXtends);
		}
		if (!iMplements.isEmpty()) {
			iMplements = iMplements.substring(2);
			contents = (contents + "implements " + iMplements + " ");
		}

		contents = (contents + "{" + NL + NL);

		// --------------------Attributes/Fields----------------------------
		for (int variables = 0; variables <= classModel.getAttributes().size() - 1; variables++) {

			boolean isAttributeFinal = false;

			if (classModel.getAttributes().get(variables).getType() == AttributeType.DATA_FIELD) {
				switch (classModel.getAttributes().get(variables)
						.getVisibility()) {
				case PUBLIC:
					contents = (contents + TB + "public ");
					break;
				case PRIVATE:
					contents = (contents + TB + "private ");
					break;
				case PROTECTED:
					contents = (contents + TB + "protected ");
					break;
				}

				if (classModel.getAttributes().get(variables).isFlagStatic()) {
					contents = (contents + "static ");
				}

				if (classModel.getAttributes().get(variables).isFlagFinal()) {
					contents = (contents + "final ");
					isAttributeFinal = true;
				}

				if (classModel.getAttributes().get(variables).isFlagTransient()) {
					contents = (contents + "transistent ");
				}

				contents = (contents
						+ classModel.getAttributes().get(variables)
								.getAttributeType() + " ");

				if (isAttributeFinal) {
					contents = (contents
							+ classModel.getAttributes().get(variables)
									.getAttributeName().toUpperCase() + ";");
				} else {
					contents = (contents
							+ classModel.getAttributes().get(variables)
									.getAttributeName() + ";");
				}
				contents = contents + NL;

			}
		}
		// ----------------------Cardinalities------------------------

		// ----------- Many to One -------------
		for (int cardinalities = 0; cardinalities < classModel
				.getRelationships().size(); cardinalities++) {

			if (classModel.getRelationships().get(cardinalities)
					.getCardinalityTo().getText().equals("0..*")
					&& classModel.getRelationships().get(cardinalities)
							.getGoingTo().getClassName() != classModel
							.getClassName()) {

				contents = contents
						+ TB
						+ "private ArrayList<"
						+ classModel.getRelationships().get(cardinalities)
								.getGoingTo().getClassName()
						+ "> "
						+ classModel.getRelationships().get(cardinalities)
								.getLabel().getText() + "; " + NL;
			}
			// ------- Many to Many Extension -------
			if (classModel.getRelationships().get(cardinalities)
					.getCardinalityFrom().getText().equals("0..*")
					&& classModel.getRelationships().get(cardinalities)
							.getGoingFrom().getClassName() != classModel
							.getClassName()) {

				contents = contents
						+ TB
						+ "private ArrayList<"
						+ classModel.getRelationships().get(cardinalities)
								.getGoingFrom().getClassName()
						+ "> "
						+ classModel.getRelationships().get(cardinalities)
								.getLabel().getText() + "; " + NL;
			}
			// ---------- Zero to Something Extension --------
			if (!classModel.getRelationships().get(cardinalities)
					.getCardinalityTo().getText().equals("0..*")
					&& !classModel.getRelationships().get(cardinalities)
							.getCardinalityTo().getText().equals("1")
					&& classModel.getRelationships().get(cardinalities)
							.getGoingTo().getClassName() != classModel
							.getClassName()) {
				String toCardinality = classModel.getRelationships()
						.get(cardinalities).getCardinalityTo().getText();
				String[] values = toCardinality.split("\\.\\.");
				if (!isInteger(values[1].toString())) {
					JOptionPane.showMessageDialog(null,
							"Error, Check Cardinalities",
							"Error in Cardinalities",
							JOptionPane.WARNING_MESSAGE);
					errorCheck = true;
				} else {
					contents = contents
							+ TB
							+ "private ArrayList<"
							+ classModel.getRelationships().get(cardinalities)
									.getGoingTo().getClassName()
							+ "> "
							+ classModel.getRelationships().get(cardinalities)
									.getLabel().getText()
							+ " = new ArrayList<"
							+ classModel.getRelationships().get(cardinalities)
									.getGoingTo().getClassName() + ">("
							+ values[1] + ");" + NL;
				}

			}

			if (!classModel.getRelationships().get(cardinalities)
					.getCardinalityFrom().getText().equals("0..*")
					&& !classModel.getRelationships().get(cardinalities)
							.getCardinalityFrom().getText().equals("1")
					&& classModel.getRelationships().get(cardinalities)
							.getGoingFrom().getClassName() != classModel
							.getClassName()) {
				String toCardinality = classModel.getRelationships()
						.get(cardinalities).getCardinalityFrom().getText();
				String[] values = toCardinality.split("\\.\\.");
				if (!isInteger(values[1].toString())) {
					JOptionPane.showMessageDialog(null,
							"Error, Check Cardinalities",
							"Error in Cardinalities",
							JOptionPane.WARNING_MESSAGE);
					errorCheck = true;
				} else {
					contents = contents
							+ TB
							+ "private ArrayList<"
							+ classModel.getRelationships().get(cardinalities)
									.getGoingFrom().getClassName()
							+ "> "
							+ classModel.getRelationships().get(cardinalities)
									.getLabel().getText()
							+ " = new ArrayList<"
							+ classModel.getRelationships().get(cardinalities)
									.getGoingFrom().getClassName() + ">("
							+ values[1] + ");" + NL;
				}

			}
		}

		// ------------------------Methods---------------------------
		for (int methods = 0; methods < classModel.getAttributes().size(); methods++) {

			if (classModel.getAttributes().get(methods).getType() == AttributeType.METHOD) {
				switch (classModel.getAttributes().get(methods).getVisibility()) {
				case PUBLIC:
					contents = (contents + TB + "public ");
					break;
				case PRIVATE:
					contents = (contents + TB + "private ");
					break;
				case PROTECTED:
					contents = (contents + TB + "protected ");
					break;
				}

				boolean isMethodAbstract = false;
				if (classModel.getAttributes().get(methods).isFlagAbstract()) {
					contents = (contents + "abstract");
					isMethodAbstract = true;
				}

				if (classModel.getAttributes().get(methods).isFlagStatic()) {
					contents = (contents + "static ");
				}

				if (classModel.getAttributes().get(methods).isFlagFinal()) {
					contents = (contents + "final ");
				}

				contents = (contents
						+ classModel.getAttributes().get(methods)
								.getReturnType() + " ");

				contents = (contents
						+ classModel.getAttributes().get(methods)
								.getAttributeName() + "(");

				int numOfArgs = classModel.getAttributes().get(methods)
						.getArgs().size();

				for (int k = 0; k <= numOfArgs - 1; k++) {
					contents = (contents + classModel.getAttributes()
							.get(methods).getArgs().get(k));
					if (k < numOfArgs - 2) {
						contents = (contents + ", ");
					}
				}
				contents = (contents + ")");
				if (isMethodAbstract) {
					contents = (contents + ";");
				} else {
					if (classModel.getAttributes().get(methods).getReturnType()
							.equals("void")) {
						contents = (contents + "{" + NL + NL + TB + "}" + NL + NL);
					} else {
						contents = (contents + "{" + NL + TB + TB
								+ "return null;" + NL + TB + "}" + NL + NL);
					}
				}

			}

		}
		contents = (contents + "}");
		return contents;

	}

	private boolean isInteger(String s) {
		try {
			System.out.println(Integer.parseInt(s));
			Integer.parseInt(s);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
