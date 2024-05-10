package com.hello.forum.beans.security.oauth2.user.providers;

import java.util.Map;

import com.hello.forum.beans.security.oauth2.user.OAuth2UserInfo;

public class GithubUserInfo implements OAuth2UserInfo {
	
	private Map<String, Object> attributes;
	
	public GithubUserInfo(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	@Override
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	@Override
	public String getEmail() {
		return attributes.get("login").toString();
	}

	@Override
	public String getName() {
		return attributes.get("name").toString();
	}
	
}
