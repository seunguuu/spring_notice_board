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
	
	public final static boolean size(String value, int minSize) {
		return StringUtils.isEnough(value, minSize);
	}
	
	public final static boolean isEquals(String value, String otherValue) {
		if (StringUtils.isEmpty(value) || StringUtils.isEmpty(otherValue)) {
			return false;
		}
		
		return value.equals(otherValue);
	}
	
}