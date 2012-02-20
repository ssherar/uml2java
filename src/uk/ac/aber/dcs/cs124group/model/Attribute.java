package uk.ac.aber.dcs.cs124group.model;

import java.awt.*;
import java.util.Hashtable;
import java.util.*;

public class Attribute extends DocumentElement {


	private static final long serialVersionUID = -2402890557766473597L;
	private IVisibility visibility;
	private AttributeType type;
	
	private String name;
	private Hashtable<String, String> args;
	private String returnType = "void";
	private boolean flagStatic = false;
	private boolean flagAbstract = false;
	private boolean flagTransient = false;
	private boolean flagFinal = false;
	
	public void Atrribute(AttributeType type, IVisibility visibility) {
		this.visibility = visibility;
		this.type = type;
	}
	
	
	public void addArg(String key, String value) {
		if(this.isInArray(key)) {
			this.editArg(key, value);
		} else {
			this.args.put(key,value);
		}
	}
	
	public void editArg(String key, String value) {
		if(this.isInArray(key)) {
			this.args.put(key, value);
		} else {
			this.addArg(key ,value);
		}
	}
	
	public void removeArg(String key) {
		if(this.isInArray(key)) this.args.remove(key);
	}
	
	

	@Override
	public void move(Point newPos) {
		

	}
	
	/** Block of Get/Set */

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	public boolean isFlagStatic() {
		return flagStatic;
	}

	public void setFlagStatic(boolean flagStatic) {
		this.flagStatic = flagStatic;
	}

	public boolean isFlagAbstract() {
		return flagAbstract;
	}

	public void setFlagAbstract(boolean flagAbstract) {
		this.flagAbstract = flagAbstract;
	}

	public boolean isFlagTransient() {
		return flagTransient;
	}

	public void setFlagTransient(boolean flagTransient) {
		this.flagTransient = flagTransient;
	}

	public boolean isFlagFinal() {
		return flagFinal;
	}

	public void setFlagFinal(boolean flagFinal) {
		this.flagFinal = flagFinal;
	}

	public IVisibility getVisibility() {
		return visibility;
	}

	public void setVisibility(IVisibility visibility) {
		this.visibility = visibility;
	}

	public AttributeType getType() {
		return type;
	}

	public void setType(AttributeType type) {
		this.type = type;
	}
	
	/**Private methods */
	
	private boolean isInArray(String key) {
		return (args.get(key) == null) ? false : true;
	}
	
	

}
