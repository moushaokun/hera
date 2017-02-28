package org.apel.hera.biz.consist;

public enum DataTypeConsist {
	
	STRING("String"),INTEGER("Integer"),BOOLEAN("Boolean"),DOUBLE("Double"),
	FLOAT("Float"),DATE("Date"),LONG("Long"),SHORT("Short");
	
	
	private String name;
	
	private DataTypeConsist(String name){
		this.name = name;
	}
	
	public static DataTypeConsist get(String name){
		DataTypeConsist[] values = DataTypeConsist.values();
		for (DataTypeConsist dataTypeConsist : values) {
			if(dataTypeConsist.toString().equals(name)){
				return dataTypeConsist;
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return this.name;
	}
	
}
