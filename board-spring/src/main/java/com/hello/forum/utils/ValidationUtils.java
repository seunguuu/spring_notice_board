package com.hello.forum.utils;

public class ValidationUtils {
	
	private ValidationUtils() {
		
	}

	public final static boolean notEmpty(String value) {
		return ! StringUtils.isEmpty(value);
	}
	
	public final static boolean email(String value) {
		return StringUtils.isEmailFormat(value);
	}
	
}
