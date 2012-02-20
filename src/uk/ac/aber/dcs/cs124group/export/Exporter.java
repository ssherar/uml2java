package uk.ac.aber.dcs.cs124group.export;

import uk.ac.aber.dcs.cs124group.gui.*;
import uk.ac.aber.dcs.cs124group.model.*;
import java.util.*;
import java.io.*;

public class Exporter {
	
	private DocumentModel model;
	private String outputDirectory;
	private ArrayList<File> outputFiles; 
	
	
	private Scanner fileCreator;
	
	public Exporter (String outputDirectory, DocumentModel model) {
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
		
		if (r.isAbstract()){
			contents.concat("abstract ");
		} 
		
		contents.concat("class "); //can amend this to include interface and enums etc if we decide to implement them
		
		for (int i = 0; r.getRelationships().size(); i++)
		
		
		
		
		
		
		
		
		
		return null;
	}
	
}
