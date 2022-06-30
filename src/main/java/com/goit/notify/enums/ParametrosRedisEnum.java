package com.goit.notify.enums;

public enum ParametrosRedisEnum {

	JWT_SECRET("JWT_SECRET"),
	GOIT_SEGURIDAD_API("GOIT_SEGURIDAD_API");

	private String name;

	ParametrosRedisEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
