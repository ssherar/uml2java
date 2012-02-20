package uk.ac.aber.dcs.cs124group.export;

import uk.ac.aber.dcs.cs124group.gui.*;
import uk.ac.aber.dcs.cs124group.model.*;
import java.util.*;
import java.io.*;

public class Exporter {
	
	private DocumentModel model;
	private Canvas canvas;
	private String outputDirectory;
	private ArrayList<File> outputFiles;
	
	public Exporter (String outputDirectory, DocumentModel model) {
		this.outputDirectory = outputDirectory;
		this.model = model;
	}
	
	public Exporter (String outputDirectory, Canvas canvas) {
		this.outputDirectory = outputDirectory;
		this.canvas = canvas;
	}
	
	public void exportImage() {
		
	}
	
	public void exportCode() {
		
	}
	
	private String createClassFileContents(ClassRectangle r) {
		return null;
	}
	
}
