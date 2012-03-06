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
 * @author Daniel Maly
 * @author Sam Sherar
 * @author Lee Smith
 * @version 1.0.0
 */

public class Exporter {

	private DocumentModel model;

	private Canvas canvas;
	private ArrayList<String> outputFiles = new ArrayList<String>();
	private ArrayList<String> fileNames = new ArrayList<String>();

	private final static String NL = "\n";
	private final static String TB = "\t";

	private Manager manager;

	/**
	 * Creates a <code>Exporter</code> instance with the specified DocumentModel
	 * and Manager. This constructor is used to complete exporting the diagram
	 * and creating the resulting code
	 * 
	 * @param model
	 *            The current state of the DocumentModel
	 * @param manager
	 *            The diagram Manager
	 * @see uk.ac.aber.dcs.cs124group.model.DocumentModel DocumentModel
	 * @see uk.ac.aber.dcs.cs124group.controller.Manager Manager
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

		manager.setWaitCursor(true);
		JFileChooser fcCode = new JFileChooser();
		fcCode.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fcCode.setAcceptAllFileFilterUsed(false);
		int fcReturnVal = fcCode.showDialog(null, "Select Directory");

		manager.setWaitCursor(false);

		if (fcReturnVal == JFileChooser.APPROVE_OPTION) {
			String chosenDirectory = fcCode.getSelectedFile().getPath() + "/";
			for (int j = fileNames.size() - 1; j >= 0; j--) {
				PrintWriter fileOut = new PrintWriter(new OutputStreamWriter(
						new FileOutputStream(chosenDirectory + ""
								+ fileNames.get(j))));

				fileOut.println(outputFiles.get(j));
				fileOut.close();

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

	private String createClassFileContents(ClassModel classModel)
			throws IOException {
		classModel.cleanUp();
		ClassModel codeModel = classModel;

		if (fileNames.contains(classModel.getClassName() + ".java")) {
			throw new IOException("Export Error, Duplicate Class Names");
		} else {
			fileNames.add(classModel.getClassName() + ".java");

			String contents = "";

			contents = getHeaderString(codeModel);

			// --------------------Attributes/Fields----------------------------
			contents = contents + getAttributesString(codeModel);

			// ----------------------Cardinalities------------------------
			contents = contents + getCardinalitiesString(codeModel);

			// ------------------------Methods---------------------------
			contents = contents + getMethodsString(codeModel);

			return contents;
		}
	}

	/**
	 * Creates the main header for the Java document. This will declare the
	 * class name, any modifiers added to the class, such as
	 * <code>abstract, static and final</code> and any classes it extends or
	 * implements.
	 * 
	 * @param classModel
	 *            - The ClassModel containing information on the visibility of
	 *            the class, and modifiers applied to the class
	 * @return String - String containing the code to be written for the class
	 *         header in the final code.
	 */
	private String getHeaderString(ClassModel classModel) {
		String header = "";

		switch (classModel.getVisibility()) {
		case PUBLIC:
			header = "public ";
			break;
		case PRIVATE:
			header = "private ";
			break;
		case PROTECTED:
			header = "protected ";
			break;
		case PACKAGE:
			header = "package ";
			break;
		}

		if (classModel.isAbstract() && !classModel.isFinal()) {
			header = (header + "abstract ");
		}

		if (classModel.isStatic()) {
			header = (header + "static ");
		}

		header = (header + "class "); // can amend this to include interface
		// and
		// enums etc if we decide to implement them
		header = (header + classModel.getClassName() + " ");

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
			header = (header + "extends " + eXtends);
		}
		if (!iMplements.isEmpty()) {
			iMplements = iMplements.substring(2);
			header = (header + "implements " + iMplements + " ");
		}

		header = (header + "{" + NL + NL);
		return header;
	}

	/**
	 * Creates a String containing the Attributes entered into each class,
	 * including <code>Visibility, AttributeName, and Type</code> and any
	 * default values that the user has assigned to the variables.
	 * 
	 * @param classModel
	 *            - The ClassModel, containing Attributes for each class on the
	 *            diagram
	 * @return String - String containing the code to be written for the
	 *         attributes section of the final code, related to Attributes
	 */

	private String getAttributesString(ClassModel classModel) {
		String attribute = "";
		for (int variables = 0; variables <= classModel.getAttributes().size() - 1; variables++) {

			boolean isAttributeFinal = false;

			// Is it actually valid??
			if (!classModel.getAttributes().get(variables).isValid())
				continue;

			if (classModel.getAttributes().get(variables).getType() == AttributeType.DATA_FIELD) {
				switch (classModel.getAttributes().get(variables)
						.getVisibility()) {
				case PUBLIC:
					attribute = (attribute + TB + "public ");
					break;
				case PRIVATE:
					attribute = (attribute + TB + "private ");
					break;
				case PROTECTED:
					attribute = (attribute + TB + "protected ");
					break;
				}

				if (classModel.getAttributes().get(variables).isFlagStatic()) {
					attribute = (attribute + "static ");
				}

				if (classModel.getAttributes().get(variables).isFlagFinal()) {
					attribute = (attribute + "final ");
					isAttributeFinal = true;
				}

				if (classModel.getAttributes().get(variables).isFlagTransient()) {
					attribute = (attribute + "transistent ");
				}

				attribute = (attribute
						+ classModel.getAttributes().get(variables)
								.getAttributeType() + " ");

				if (isAttributeFinal) {
					attribute = (attribute + classModel.getAttributes()
							.get(variables).getAttributeName().toUpperCase());
				} else {
					attribute = (attribute + classModel.getAttributes()
							.get(variables).getAttributeName());
				}
				if (!(classModel.getAttributes().get(variables)
						.getAttribDefault() == null)) {
					attribute = attribute
							+ " = "
							+ classModel.getAttributes().get(variables)
									.getAttribDefault();
				}
				attribute = attribute + ";" + NL;
			}
		}
		attribute = attribute + NL;
		return attribute;
	}

	/**
	 * Creates a String containing the ArrayLists created from relationships and
	 * cardinalities
	 * 
	 * @param classModel
	 *            - The ClassModel containing required relationships and
	 *            cardinalities
	 * @return String - String containing the cardinalities section of the final
	 *         code, related to Relationships creating ArrayLists
	 * @throws IOException
	 *             - If the user enters a cardinality that contains illegal
	 *             characters
	 */
	private String getCardinalitiesString(ClassModel classModel)
			throws IOException {
		String cardialitiesString = "";

		for (int cardinalities = 0; cardinalities < classModel
				.getRelationships().size(); cardinalities++) {
			if (classModel.getRelationships().get(cardinalities)
					.getCardinalityFrom() != null) {
				String cardinalityFrom = classModel.getRelationships()
						.get(cardinalities).getCardinalityFrom().getText();
				String goingFrom = classModel.getRelationships()
						.get(cardinalities).getGoingFrom().getClassName();
				String cardinalityLabelFrom = classModel.getRelationships()
						.get(cardinalities).getLabel().getText();

				if ((manyCardinality(cardinalityFrom).equals("*") && !cardinalityFrom
						.equals("1")) && goingFrom != classModel.getClassName()) {
					System.out.println("Check 2");
					cardialitiesString = cardialitiesString + TB
							+ "private ArrayList<" + goingFrom + "> "
							+ cardinalityLabelFrom + "; " + NL;
					// Fixed Size
				} else if (isInteger(cardinalityFrom)
						&& goingFrom != classModel.getClassName()
						&& !cardinalityFrom.equals("1")) {
					System.out.println("Check 4");
					cardialitiesString = cardialitiesString + TB
							+ "private ArrayList<" + goingFrom + "> "
							+ cardinalityLabelFrom + " = new ArrayList<"
							+ goingFrom + ">(" + cardinalityFrom + ");" + NL;
					// Erroneous Input

				} else if (!cardinalityFrom.equals("0..*")
						&& !cardinalityFrom.equals("1")
						&& goingFrom != classModel.getClassName()) {
					System.out.println("Check 6");
					String[] values = cardinalityFrom.split("\\.\\.");

					if (!isInteger(values[1].toString())) {
						JOptionPane.showMessageDialog(null,
								"Error, invalid cardinality.",
								"Error in Cardinalities",
								JOptionPane.WARNING_MESSAGE);
						throw new IOException("Export failed");

					} else {
						cardialitiesString = cardialitiesString + TB
								+ "private ArrayList<" + goingFrom + "> "
								+ cardinalityLabelFrom + " = new ArrayList<"
								+ goingFrom + ">(" + values[1] + ");" + NL;
					}
				}

				if (classModel.getRelationships().get(cardinalities)
						.getCardinalityTo() != null) {
					String cardinalityTo = classModel.getRelationships()
							.get(cardinalities).getCardinalityTo().getText();

					String goingTo = classModel.getRelationships()
							.get(cardinalities).getGoingTo().getClassName();

					String cardinalityLabelTo = classModel.getRelationships()
							.get(cardinalities).getLabel().getText();

					// ----------- Many to One -------------
					if ((manyCardinality(cardinalityTo).equals("*") && !cardinalityTo
							.equals("1"))
							&& goingTo != classModel.getClassName()) {
						System.out.println("Check 1");
						cardialitiesString = cardialitiesString + TB
								+ "private ArrayList<" + goingTo + "> "
								+ cardinalityLabelTo + "; " + NL;

					} else if (isInteger(cardinalityTo)
							&& goingTo != classModel.getClassName()
							&& !cardinalityTo.equals("1")) {
						System.out.println("Check 3");
						cardialitiesString = cardialitiesString + TB
								+ "private ArrayList<" + goingTo + "> "
								+ cardinalityLabelTo + " = new ArrayList<"
								+ goingTo + ">(" + cardinalityTo + ");" + NL;

					} else if (!cardinalityTo.equals("0..*")
							&& !cardinalityTo.equals("1")
							&& goingTo != classModel.getClassName()) {
						System.out.println("Check 5");
						String[] values = cardinalityTo.split("\\.\\.");

						if (!isInteger(values[1].toString())) {
							JOptionPane.showMessageDialog(null,
									"Error, invalid cardinality.",
									"Error in Cardinalities",
									JOptionPane.WARNING_MESSAGE);
							throw new IOException("Export failed");

						} else {
							cardialitiesString = cardialitiesString + TB
									+ "private ArrayList<" + goingTo + "> "
									+ cardinalityLabelTo + " = new ArrayList<"
									+ goingTo + ">(" + values[1] + ");" + NL;
						}

					}
				}
			}

		}
		cardialitiesString = cardialitiesString + NL;
		return cardialitiesString;
	}

	/**
	 * Checks whether the cardinality passed to the method is an integer, linked
	 * with {@link #manyCardinality(String))} to get the required information
	 * from cardinalities that may have many instances of the class
	 * 
	 * @param s
	 *            - A String containing the cardinality or part of (drawn from
	 *            {@link #manyCardinality(String)})
	 * @return - <code>true</code> if the parameter is a valid integer,
	 *         <code>false</code> if the parameter is not a valid integer.
	 */
	private boolean isInteger(String s) {
		try {
			System.out.println(Integer.parseInt(s));
			Integer.parseInt(s);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Splits cardinalities up if they are in the form
	 * <code>(#..#, or #..*)</code> and returns the latter value if try, if the
	 * parameter is not in the form <code>(#..#, or #..*)</code> then the method
	 * will return the initial parameter
	 * 
	 * @param s
	 *            - String containing the cardinality to be checked/split
	 * @return String - either the initial parameter if is already an integer,
	 *         or the final value if the string contains ".."
	 */
	private String manyCardinality(String s) {
		if (s.contains("..")) {
			String values[] = s.split("\\.\\.");
			return values[1];
		} else {
			return s;
		}

	}

	/**
	 * Creates a String containing the methods the user has entered into the
	 * class diagram. Including Visibility, method modifiers, return types and any
	 * parameters that the user has specified will be passed into the method
	 * 
	 * @param classModel
	 *            - The ClassModel containing required relationships and
	 *            cardinalities
	 * @return String - String containing the cardinalities section of the final
	 *         code, related to Relationships creating ArrayLists
	 * @throws IOException
	 *             If the user enters a cardinality that contains illegal
	 *             characters
	 *             @see uk.ac.aber.dcs.cs124group.model.Relationship Relationship
	 * 
	 */
	private String getMethodsString(ClassModel classModel) {
		String methodString = "";

		for (int methods = 0; methods < classModel.getAttributes().size(); methods++) {

			Attribute a = classModel.getAttributes().get(methods);

			if (!a.isValid())
				continue;
			if (a.getType() == AttributeType.METHOD) {
				switch (a.getVisibility()) {
				case PUBLIC:
					methodString = (methodString + TB + "public ");
					break;
				case PRIVATE:
					methodString = (methodString + TB + "private ");
					break;
				case PROTECTED:
					methodString = (methodString + TB + "protected ");
					break;
				}

				boolean isMethodAbstract = false;
				if (a.isFlagAbstract()) {
					methodString = (methodString + "abstract");
					isMethodAbstract = true;
				}

				if (a.isFlagStatic()) {
					methodString = (methodString + "static ");
				}

				if (a.isFlagFinal()) {
					methodString = (methodString + "final ");
				}

				if (!a.getReturnType().equals("init")) {
					methodString = (methodString + a.getReturnType() + " ");
				}

				methodString = (methodString + a.getAttributeName() + "(");

				int numOfArgs = a.getArgs().size();

				for (int k = 0; k < numOfArgs; k++) {
					if (!(k == 0)) {
						methodString = (methodString + ", ");
					}
					methodString = (methodString + a.getArgs().get(k));

				}
				methodString = (methodString + ")");
				if (isMethodAbstract) {
					methodString = (methodString + ";");
				} else {

					methodString = (methodString + " {" + NL + NL + TB + "}"
							+ NL + NL);
				}
			}
		}

		methodString = (methodString + "}");
		return methodString;
	}
}
