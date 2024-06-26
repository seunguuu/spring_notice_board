package com.hello.forum.utils;

/**
 * Spring Validator를 사용하지 않고
 * 파라미터 유효성 검사를 하기 위한 유틸리티.
 */
public abstract class ValidationUtils {
	
	private ValidationUtils() {
		
	}
	
	public final static boolean notEmpty(String value) {
		// 비어있으면 false, 비어있지 않으면 true가 되도록 구성
		return ! StringUtils.isEmpty(value);
	}
	
	public final static boolean email(String value) {
		// 이메일 패턴이 아니면 false가 되도록 구성
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
