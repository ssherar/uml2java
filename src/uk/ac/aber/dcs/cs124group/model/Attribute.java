package uk.ac.aber.dcs.cs124group.model;

import java.util.*;
import java.awt.Point;
import java.util.regex.*;
import uk.ac.aber.dcs.cs124group.undo.FlagEdit;

public class Attribute extends TextLabelModel implements java.io.Serializable {


	private static final long serialVersionUID = -2402890557766473597L;
	private final String REGEX_ATTRIB = "^([+#-]) ([a-z][a-zA-Z]*) \\: ([A-Za-z]*)( [=] [a-zA-Z0-9]{1,9})?$";
	private final String REGEX_METHOD_SHELL = "^([+#-]) ([a-zA-Z]*)\\((.*)\\)(( \\: [a-zA-Z]*)?)$";
	private final String REGEX_METHOD_ARGS = "([a-z][a-zA-Z]*(\\[\\])?) \\: ([A-Za-z0-9]*)";
	
	private IVisibility visibility;
	private AttributeType type;
	
	private String representation; //e.g. +addElement(element : Element) : void
	private String attributeName;
	private ArrayList<String> args;
	private String returnType = "void";
	private String attribDefault = null;
	private String attributeType = null;
	private boolean flagStatic = false;
	private boolean flagAbstract = false;
	private boolean flagTransient = false;
	private boolean flagFinal = false;
	
	public Attribute(Point p, String representation, AttributeType type) {
		super(p);
		args = new ArrayList<String>();
		this.representation = representation;
		this.type = type;
		initializeFields();		
		this.setText(representation, false);
	}
	
	public void addArgsElement(String argType, String argName){
		args.add(argType + " " + argName);
	}
	
	@Override
	public void setText(String text, boolean undoable) {
		super.setText(text, undoable);
		this.representation = text;
		initializeFields();
	}
	
	public Matcher checkAttribute(String var) {
		Pattern p = Pattern.compile(REGEX_ATTRIB);
		Matcher m = p.matcher(var);
		return m;
	}
	
	public Matcher checkMethodShell(String var) {
		Pattern p = Pattern.compile(REGEX_METHOD_SHELL);
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
				this.checkVisibility(m.group(1));
				this.attributeName = m.group(2);
				this.attributeType = m.group(3);
				if(m.groupCount() == 5 && m.group(4) == null) {
					this.attribDefault = m.group(4).substring(3);
				}
			}
		} else if(this.type == AttributeType.METHOD) {
			m = this.checkMethodShell(uml);
			if(m.find() && m.groupCount() > 0) {
				this.checkVisibility(m.group(1));
				this.attributeName = m.group(2);
				if(m.group(3) != null) {
				
					Matcher args = this.checkArguements(m.group(3));
					while(args.find()) {
						this.addArgsElement(args.group(3),args.group(1));
					}
				}
				this.returnType = m.group(4).substring(3);
			}
		}
	}
	
	public Matcher checkArguements(String var) {
		args.clear(); //edited to avoid args double up if exported code editted then exported again
		Pattern p = Pattern.compile(REGEX_METHOD_ARGS);
		Matcher m = p.matcher(var);	
		
		return m;
	}
	
	public void setAbstract(boolean set, boolean undo) {
		if(undo) {
			FlagEdit edit = new FlagEdit(this, "flagAbstract", this.flagAbstract, set);
			this.fireUndoableEvent(edit);
		}
		this.flagAbstract = set;
		setChanged();
		notifyObservers("flagChanged");
	}
	
	public void setStatic(boolean set, boolean undo) {
		if(undo) {
			FlagEdit edit = new FlagEdit(this, "flagStatic", this.flagStatic, set);
			this.fireUndoableEvent(edit);
		}
		this.flagStatic = set;
		setChanged();
		notifyObservers("flagChanged");
	}
	
	public void setFinal(boolean set, boolean undo) {
		if(undo) {
			FlagEdit edit = new FlagEdit(this, "flagFinal", this.flagFinal, set);
			this.fireUndoableEvent(edit);
		}
		this.flagFinal = set;
		setChanged();
		notifyObservers("flagChanged");
	}
	
	public void setTransient(boolean set, boolean undo) {
		if(undo) {
			FlagEdit edit = new FlagEdit(this, "flagTransient", this.flagTransient, set);
			this.fireUndoableEvent(edit);
		}
		this.flagFinal = set;
		setChanged();
		notifyObservers("flagChanged");
	}
	
	public void setNone(boolean set, boolean undo) {
		/*if(undo) {
			FlagEdit edit = new FlagEdit(this, "flagNone", this.flagNone, set);
			this.fireUndoableEvent(edit);
		}*/
		cleanFlags(undo);
		setChanged();
		notifyObservers("flagChanged");
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
	
	

}