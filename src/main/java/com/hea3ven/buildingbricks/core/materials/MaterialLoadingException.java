package com.hea3ven.buildingbricks.core.materials;

import com.google.gson.JsonSyntaxException;

public class MaterialLoadingException extends RuntimeException {

	public MaterialLoadingException(String message, JsonSyntaxException cause) {
		super(message, cause);
	}

}
