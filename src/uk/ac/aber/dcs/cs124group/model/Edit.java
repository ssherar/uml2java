package uk.ac.aber.dcs.cs124group.model;

public class Edit {
	private String key;
	private Object value;
	
	public Edit(String key, Object value) {
		this.key = key;
		this.value = value;
	}
	
	public String getKey() {
		return key;
	}

	public Object getValue() {
		return value;
	}

}
