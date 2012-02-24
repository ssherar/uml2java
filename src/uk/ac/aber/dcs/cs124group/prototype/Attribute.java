package uk.ac.aber.dcs.cs124group.prototype;

import java.util.*;
import java.awt.Point;
import java.util.regex.*;
import uk.ac.aber.dcs.cs124group.model.*;

public class Attribute /*extends TextLabel*/ implements java.io.Serializable {


	private static final long serialVersionUID = -2402890557766473597L;
	private final String REGEX_ATTRIB = "^([+#-]) ([a-z].[a-zA-Z]*) \\: ([A-Za-z]*)( [=] [a-zA-Z0-9]{1,9})?$";
	private final String REGEX_METHOD = "^[+#-] [a-z].[a-zA-Z]*\\(([a-z].[a-zA-Z]*(\\[\\])? \\: [A-Za-z]*(, )?)*\\)( \\: [a-zA-Z]*)?$";
	
	private IVisibility visibility;
	private AttributeType type;
	
	private String representation; //e.g. +addElement(element : Element) : void
	private String attributeName;
	private ArrayList<String> args;
	private String returnType = "void";
	private String attribDefault = null;
	private boolean flagStatic = false;
	private boolean flagAbstract = false;
	private boolean flagTransient = false;
	private boolean flagSyncronised = false;
	private boolean flagVolatile = false;
	private boolean flagFinal = false;
	
	public Attribute(Point p, String representation, AttributeType type) {
		//super(p);
		this.representation = representation;
		this.type = type;
		//initializeFields();		
		this.setText(representation);
	}
	
	public void addArgsElement(String argType, String argName){
		args.add(argType + " " + argName);
	}
	
	//@Override
	public void setText(String text) {
		//super.setText(text);
		this.representation = text;
		initializeFields();
	}
	
	public Matcher checkAttribute(String var) {
		Pattern p = Pattern.compile(REGEX_ATTRIB);
		Matcher m = p.matcher(var);
		return m;
	}
	
	public Matcher checkMethod(String var) {
		Pattern p = Pattern.compile(REGEX_METHOD);
		Matcher m = p.matcher(var);
		return m;
	}
	
	/** Block of Get/Set */
	
	public ArrayList<String> getArgs(){
		return args;
	}
	
	public String getRepresentation() {
		return representation;
	}
	
	public void setRepresentation(String representation) {
		this.representation = representation;
	}
	
	public String getAttributeName() {
		return attributeName;
	}

	private void setAttributeName(String name) {
		this.attributeName = name;
	}

	public String getReturnType() {
		return returnType;
	}

	private void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	public boolean isFlagStatic() {
		return flagStatic;
	}

	private void setFlagStatic(boolean flagStatic) {
		this.flagStatic = flagStatic;
	}

	public boolean isFlagAbstract() {
		return flagAbstract;
	}

	private void setFlagAbstract(boolean flagAbstract) {
		this.flagAbstract = flagAbstract;
	}

	public boolean isFlagTransient() {
		return flagTransient;
	}

	private void setFlagTransient(boolean flagTransient) {
		this.flagTransient = flagTransient;
	}

	public boolean isFlagVolatile(){
		return flagVolatile;
	}
	
	private void setFlagVolatile(boolean flagVolatile){
		this.flagTransient = flagVolatile;
	}
	
	public boolean isFlagSyncronised(){
		return flagSyncronised;
	}
	
	private void setFlagSyncronised(boolean flagSyncronised){
		this.flagSyncronised = flagSyncronised;
	}
	
	public boolean isFlagFinal() {
		return flagFinal;
	}

	private void setFlagFinal(boolean flagFinal) {
		this.flagFinal = flagFinal;
	}

	public IVisibility getVisibility() {
		return visibility;
	}

	private void setVisibility(IVisibility visibility) {
		this.visibility = visibility;
	}

	public AttributeType getType() {
		return type;
	}

	public void setType(AttributeType type) {
		this.type = type;
	}
	
	
	private void initializeFields() {
		String uml = this.representation;//super.getText();
		Pattern p;
		Matcher m;
		
		if(this.type == AttributeType.DATA_FIELD) {
			p = Pattern.compile(REGEX_ATTRIB);
			m = p.matcher(uml);
			if(m.find()) {
				System.out.println("Attribute:");
				System.out.println(m.group(0));
				System.out.println(m.group(1));
				System.out.println(m.group(2));
				System.out.println(m.group(3));
			}
		} else if(this.type == AttributeType.METHOD) {
			p = Pattern.compile(REGEX_METHOD);
			m = p.matcher(uml);
			if(m.find()) {
				System.out.println("Method:");
				System.out.println(m.group(0));
			}
		}
	}

	public String getAttribDefault() {
		return attribDefault;
	}

	public void setAttribDefault(String attribDefault) {
		this.attribDefault = attribDefault;
	}
	
	

}
