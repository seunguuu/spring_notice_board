package com.hello.forum.utils;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

public abstract class RequestUtil {
	
	private RequestUtil() {
		
	}
	
	
	public static HttpServletRequest getRequest() {
		// Spring 이 관리하는 Request 객체를 얻어온다.
		// RequestAttributes 에는 Spring이 관리하는 요청 정보들이 모두 들어가 있다.
		RequestAttributes ra = RequestContextHolder.getRequestAttributes();
		
		// Request 객체를 가져오기 위해서 ServletRequestAttributes로 변경한다.
		ServletRequestAttributes sra = (ServletRequestAttributes) ra;
		
		return sra.getRequest();
	}
	
}
