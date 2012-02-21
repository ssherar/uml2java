package uk.ac.aber.dcs.cs124group.model;

import java.util.*;

public class Attribute implements java.io.Serializable {


	private static final long serialVersionUID = -2402890557766473597L;
	private IVisibility visibility;
	private AttributeType type;
	
	private String representation; //e.g. +addElement(element : Element) : void
	private String name;
	private ArrayList<String> args;
	private String returnType = "void";
	private boolean flagStatic = false;
	private boolean flagAbstract = false;
	private boolean flagTransient = false;
	private boolean flagSyncronised = false;
	private boolean flagVolatile = false;
	private boolean flagFinal = false;
	
	public void Attribute(String representation) {
		this.representation = representation;
		initializeFields();		
	}
	
	public void addArgsElement(String argType, String argName){
		args.add(argType + " " + argName);
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
	
	public String getName() {
		return name;
	}

	private void setName(String name) {
		this.name = name;
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

	private void setType(AttributeType type) {
		this.type = type;
	}
	
	
	public void initializeFields() {
		//TODO define
	}
	
	

}
