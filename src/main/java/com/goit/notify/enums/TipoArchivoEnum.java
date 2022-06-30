package com.goit.notify.enums;

public enum TipoArchivoEnum {
	
	EXCEL("EXCEL"),
	CSV("CSV"),
	JSON("JSON");
	
	private String name;

	TipoArchivoEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
