package com.hello.forum.beans.security.oauth2.user;

import java.util.Map;

import org.springframework.security.oauth2.core.user.OAuth2User;

import com.hello.forum.beans.security.SecurityUser;
import com.hello.forum.member.vo.MemberVO;

public class PrincipalDetails extends SecurityUser implements OAuth2User {
	
	private OAuth2UserInfo oAuth2UserInfo;
	
	public PrincipalDetails(MemberVO user, OAuth2UserInfo oAuth2UserInfo) {
		super(user);
		this.oAuth2UserInfo = oAuth2UserInfo;
	}

	@Override
	public Map<String, Object> getAttributes() {
		return oAuth2UserInfo.getAttributes();
	}

	@Override
	public String getName() {
		return oAuth2UserInfo.getName();
	}
	
	

}
