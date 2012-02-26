To-do
=====

1. Finalise the class diagram
2. Mock up GUI Skeleton
3. Exporter
4. Listeners
5. Manager


Attribute newDataField = new Attribute(
					((ClassRectangle)model).getNextDataFieldPoint(-1), 
					"- dataField : Type",
					AttributeType.DATA_FIELD);
			newDataField.setFont(this.model.getFont());
			newDataField.repaint();
			model.add(newDataField);
			model.revalidate();
			model.repaint();