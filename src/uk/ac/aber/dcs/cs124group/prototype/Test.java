package uk.ac.aber.dcs.cs124group.prototype;
import java.awt.*;
import uk.ac.aber.dcs.cs124group.model.*;

public class Test {
	public static void main(String[] args) {
		Attribute a = new Attribute(new Point(0,0), "+ hello : String", AttributeType.DATA_FIELD);
		a.setType(AttributeType.METHOD);
		a.setText("+ method(arg : String, pow : Int) : void");
	}
}
