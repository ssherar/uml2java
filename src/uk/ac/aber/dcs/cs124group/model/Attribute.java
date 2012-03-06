package uk.ac.aber.dcs.cs124group.model;

import java.util.*;
import java.awt.Point;
import java.util.regex.*;
import uk.ac.aber.dcs.cs124group.undo.FlagEdit;

/**
 * Attribute handles both Data and Method fields in
 * the class views. When passed a string (e.g. + data : Type)
 * it will validate then pull variables out.
 * 
 * @author Daniel Maly
 * @author Samuel B Sherar
 * @author Lee Smith
 * @version 1.0.0
 *
 */

public class Attribute extends TextLabelModel implements java.io.Serializable {
	
	/**
	 * Serialisable ID for saving
	 */
	private static final long serialVersionUID = -2402890557766473597L;
	
	/**
	 * The Regex for validating Attributes of type (+-#) name : Type = null
	 */
	private static final String REGEX_ATTRIB = "^([+#-]) ([a-z][a-zA-Z]*) \\: ([A-Za-z]*)( [=] [a-zA-Z0-9]{1,9})?$";
	
	/**
	 * The Regex for validating the shell of type (+-#) ClassName(*) : void
	 */
	private static final String REGEX_METHOD_SHELL = "^([+#-]) ([a-zA-Z]*)\\((.*)\\)";
	
	/**
	 * The Regex for validating arguments in the MethodShell of type a : Typea, b : Typeb
	 */
	private static final String REGEX_METHOD_ARGS = "([a-z][a-zA-Z]*(\\[\\])?) \\: ([a-zA-Z0-9]*)";
	//private final String REGEX_RETURN_TYPE = "(( \\: [a-zA-Z]*)?)";
	
	/**
	 * Fancypants way of getting rid of whitespace in the Regex
	 */
	static {
		REGEX_ATTRIB.replace(" ", "\\s*");
		REGEX_METHOD_SHELL.replace(" ", "\\s*");
		REGEX_METHOD_ARGS.replace(" ", "\\s*");
	}
	
	/** 
	 * The visibility of the attribute
	 */
	private IVisibility visibility;
	
	/**
	 * The type of Attribute (either AttributeType.DATA_FIELD or AttributeType.METHOD)
	 */
	private AttributeType type;
	
	/**
	 * The UML Representation (e.g e.g. +addElement(element : Element) : void
	 */
	private String representation;
	
	/**
	 * name of the variable/method
	 */
	private String attributeName;
	
	/**
	 * Arguments of the methods stored as strings 
	 */
	private ArrayList<String> args;
	
	/**
	 * The return type of a methods, which is automatically set to void for ease
	 */
	private String returnType = "void";
	
	/**
	 * Default for Datafields, which are not compulsory
	 */
	private String attribDefault = null;
	
	/**
	 * The type of the dataFields which needs to be set unless it will fail to show in the export
	 */
	private String attributeType = null;
	
	/**
	 * The flags for showing modifiers
	 */
	private boolean flagStatic = false;
	
	/**
	 * The flags for showing modifiers
	 */
	private boolean flagAbstract = false;
	
	/**
	 * The flags for showing modifiers
	 */
	private boolean flagTransient = false;
	
	/**
	 * The flags for showing modifiers
	 */
	private boolean flagFinal = false;
	
	/**
	 * For checking if the UML is correct for the exporter.
	 */
	private boolean valid = true;
	
	/**
	 * The constructor for the class: Takes the value of the representation and works upon it when the TextField
	 * is created in the ClassRectangle of a default string.
	 * @param p						To set the location on the diagram
	 * @param representation		The UML which has been typed or loaded into the class
	 * @param type					The type of Attribute (either METHOD or DATA_FIELD)
	 */
	public Attribute(Point p, String representation, AttributeType type) {
		super(p);
		args = new ArrayList<String>();
		this.representation = representation;
		this.type = type;
		initializeFields();		
		this.setText(representation, false);
		//clean up whitespace
	}
	
	/** Public methods */
	
	/**
	 * Concatenates the Argument Type and and Name together and adds to the Arraylist
	 * @param argType	The type of the argument (eg String)
	 * @param argName	The name of the argument (eg varName)
	 */
	public void addArgsElement(String argType, String argName){
		args.add(argType + " " + argName);
	}
	
	@Override
	/**
	 * Set the text and reset the variables to validate the new representation
	 * @param text		The UML representation
	 * @param undoable	a boolean to check if you want to fire an undoable event
	 */
	public void setText(String text, boolean undoable) {
		valid = true;
		super.setText(text, undoable);
		this.representation = text;
		initializeFields();
	}
	
	/**
	 * Set the abstract flag in the attribute, then tells the observer to act upon this new information
	 * @param set		To set the flag directly
	 * @param undo		<code>true</code> if you want to make an undoableEdit, <code>false</code> for none
	 */
	public void setAbstract(boolean set, boolean undo) {
		if(undo) {
			FlagEdit edit = new FlagEdit(this, "flagAbstract", this.flagAbstract, set);
			this.fireUndoableEvent(edit);
		}
		this.flagAbstract = set;
		setChanged();
		notifyObservers("flagChanged");
	}
	
	/**
	 * Set the static flag in the attribute, then tells the observer to act upon this new information
	 * @param set		To set the flag directly
	 * @param undo		<code>true</code> if you want to make an undoableEdit, <code>false</code> for none
	 */
	public void setStatic(boolean set, boolean undo) {
		if(undo) {
			FlagEdit edit = new FlagEdit(this, "flagStatic", this.flagStatic, set);
			this.fireUndoableEvent(edit);
		}
		this.flagStatic = set;
		setChanged();
		notifyObservers("flagChanged");
	}
	
	/**
	 * Set the final flag in the attribute, then tells the observer to act upon this new information
	 * @param set		To set the flag directly
	 * @param undo		<code>true</code> if you want to make an undoableEdit, <code>false</code> for none
	 */
	public void setFinal(boolean set, boolean undo) {
		if(undo) {
			FlagEdit edit = new FlagEdit(this, "flagFinal", this.flagFinal, set);
			this.fireUndoableEvent(edit);
		}
		this.flagFinal = set;
		setChanged();
		notifyObservers("flagChanged");
	}
	
	/**
	 * Set the transient flag in the attribute, then tells the observer to act upon this new information
	 * @param set		To set the flag directly
	 * @param undo		<code>true</code> if you want to make an undoableEdit, <code>false</code> for none
	 */
	public void setTransient(boolean set, boolean undo) {
		if(undo) {
			FlagEdit edit = new FlagEdit(this, "flagTransient", this.flagTransient, set);
			this.fireUndoableEvent(edit);
		}
		this.flagFinal = set;
		setChanged();
		notifyObservers("flagChanged");
	}
	
	/**
	 * Remove ALL the flags in the attribute, then tells the observer to act upon this new information
	 * @param set		To set the flag directly
	 * @param undo		<code>true</code> if you want to make an undoableEdit, <code>false</code> for none
	 */
	public void setNone(boolean set, boolean undo) {
		cleanFlags(undo);
		setChanged();
		notifyObservers("flagChanged");
	}
	
	/** Private Methods */
	
	/**
	 * Creating a new Matcher with the Attribute regex
	 * @param var	The UML which we need to check against
	 * @return		A fully initialised Matcher 
	 */
	private Matcher checkAttribute(String var) {
		Pattern p = Pattern.compile(REGEX_ATTRIB);
		Matcher m = p.matcher(var);
		return m;
	}
	
	/**
	 * Creating a new Matcher with the Method Shell regex
	 * @param var	The UML which we need to check against
	 * @return		A fully initialised Matcher 
	 */
	private Matcher checkMethodShell(String var) {
		Pattern p = Pattern.compile(REGEX_METHOD_SHELL);
		Matcher m = p.matcher(var);
		return m;
	}
	
	/**
	 * Creating a new Matcher with the Method Argument regex
	 * @param var	The UML which we need to check against
	 * @return		A fully initialised Matcher 
	 */
	private Matcher checkArguements(String var) {
		args.clear(); //edited to avoid args double up if exported code edited then exported again
		Pattern p = Pattern.compile(REGEX_METHOD_ARGS);
		Matcher m = p.matcher(var);	
		return m;
	}
	
	/*private Matcher checkReturnType(String var) {
		Pattern p = Pattern.compile(REGEX_RETURN_TYPE);
		return p.matcher(var);
	}*/
	
	/**
	 * A worker method to convert the string visibility to the enum
	 * @param var		the visibility symbol
	 */
	private void checkVisibility(String var) {
		if(var.equals("+")) this.visibility = IVisibility.PUBLIC;
		else if(var.equals("-")) this.visibility = IVisibility.PRIVATE;
		else if(var.equals("#")) this.visibility = IVisibility.PROTECTED;
		else this.visibility = IVisibility.PACKAGE;
	}
	
	private void initializeFields() {
		String uml = this.representation;
		Matcher m;
		
		if(this.type == AttributeType.DATA_FIELD) {
			m = this.checkAttribute(uml);
			if(m.find() && m.groupCount() > 0) {
				// Attribute has 3 main variables and 4th is optional
				if(m.groupCount() < 3) {
					this.valid = false;
					return;
				}
				this.checkVisibility(m.group(1));
				this.attributeName = m.group(2);
				this.attributeType = m.group(3);
				if(m.groupCount() == 5 && m.group(4) == null) {
					this.attribDefault = m.group(4).substring(3);
				}
			}
		} else if(this.type == AttributeType.METHOD) {
			m = this.checkMethodShell(uml);
			
			if(m.groupCount() > 3) {
				this.valid = false;
				return;
			}
			if(m.find() && m.groupCount() > 0) {
				this.checkVisibility(m.group(1));
				this.attributeName = m.group(2);
				
				if(m.group(3) != null) {
					Matcher args = this.checkArguements(m.group(3));
					while(args.find()) {
						this.addArgsElement(args.group(3),args.group(1));
					}
				}
				
				String tmp = uml.substring(uml.lastIndexOf(")"));
				if(tmp.lastIndexOf(":") > 0) {
					this.returnType = uml.substring(uml.lastIndexOf(":") + 2, uml.length());
				} else
					this.checkConstructor(m.group(2));
				
			}
		}
	}
	
	private void checkConstructor(String className) {
		System.out.println("HelloWorld");
		char c = className.charAt(0);
		if(Character.isUpperCase(c)) {
			this.returnType = "init";
		}
	}
	
	
	private void cleanFlags(boolean undo) {
		if(this.flagAbstract == true) {
			this.setAbstract(false, undo);
		}
		if(this.flagFinal == true) {
			this.setFinal(false, undo);
		}
		if(this.flagStatic == true) {
			this.setStatic(false, undo);
		}
		if(this.flagTransient == true) {
			this.setTransient(false, undo);
		}
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
	
	public void setAttributeName(String name) {
		this.attributeName = name;
	}
	
	public String getAttributeName() {
		return attributeName;
	}

	public String getAttributeType(){
		return attributeType;
	}
	
	public void setAttributeType(String type) {
		this.attributeType = type;
	}
	
	public String getReturnType() {
		return returnType;
	}

	public boolean isFlagStatic() {
		return flagStatic;
	}

	public boolean isFlagAbstract() {
		return flagAbstract;
	}

	public boolean isFlagTransient() {
		return flagTransient;
	}
	
	public boolean isFlagFinal() {
		return flagFinal;
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
	
	public boolean isValid() {
		return this.valid;
	}
}