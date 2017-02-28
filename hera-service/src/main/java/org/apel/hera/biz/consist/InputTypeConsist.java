package org.apel.hera.biz.consist;

public enum InputTypeConsist {

	TEXT("text"),PASSWORD("password"),SELECT("select"),SWITCH("switch"),DATE("date"),
	CHECKBOX("checkbox"),RADIO("radio"),TEXTAREA("textarea");
	
	
	private String name;
	
	private InputTypeConsist(String name){
		this.name = name;
	}
	
	public static InputTypeConsist get(String name){
		InputTypeConsist[] values = InputTypeConsist.values();
		for (InputTypeConsist inputTypeConsist : values) {
			if(inputTypeConsist.toString().equals(name)){
				return inputTypeConsist;
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return this.name;
	}
	
}
