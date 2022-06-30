package com.goit.notify.enums;

public enum CamposDEnum {
	
	PRIMERNOMBRE("PRIMERNOMBRE"),
	PRIMERAPELLIDO("PRIMERAPELLIDO"),
	CORREO("CORREO"),
	GENERO("GENERO"),
	PAIS("PAIS"),
	FECHANACIMIENTO("FECHANACIMIENTO");

	private String name;

	CamposDEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
