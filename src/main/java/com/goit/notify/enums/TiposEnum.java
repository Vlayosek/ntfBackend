package com.goit.notify.enums;

public enum TiposEnum {
	
	GRUPO("G"),
	ESTRACTO("E");

	private String name;

	TiposEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
