package uk.ac.aber.dcs.cs124group.export;

import uk.ac.aber.dcs.cs124group.gui.*;
import uk.ac.aber.dcs.cs124group.model.*;
import java.util.*;
import java.io.*;

public class Exporter {

	private DocumentModel model;
	private String outputDirectory;
	private ArrayList<File> outputFiles;
	private final String NL = "\n";
	private final String TB = "\t";

	private ArrayList<DocumentElement> importDocumentModel = model
			.getElements();

	private Scanner fileCreator;

	public Exporter(String outputDirectory, DocumentModel model) {
		this.outputDirectory = outputDirectory;
		this.model = model;
	}

	public void exportImage() {

	}

	public void exportCode() {

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

		if (r.isAbstract()) {
			contents.concat("abstract ");
		}

		contents.concat("class "); // can amend this to include interface and
									// enums etc if we decide to implement them

		contents.concat(r.getName());

		contents.concat(" {" + NL + NL);

		for (int j = 0; j > r.getAttributes().size(); j++) {
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
				
				if (r.getAttributes().get(j).isFlagAbstract()){
					contents.concat("abstract ");
				}
				
				if (r.getAttributes().get(j))
			}
		}

		/*
		 * This section will do the extends and implements section need more
		 * information from the Relationship class to do this for (int i = 0; i
		 * > r.getRelationships().size(); i++){
		 * 
		 * switch (r.getRelationships().get(i)){
		 * 
		 * }
		 * 
		 * }
		 */

		return null;
	}

}
