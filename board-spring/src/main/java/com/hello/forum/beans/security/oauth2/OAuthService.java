package com.hello.forum.beans.security.oauth2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.hello.forum.beans.security.oauth2.user.OAuth2UserInfo;
import com.hello.forum.beans.security.oauth2.user.PrincipalDetails;
import com.hello.forum.beans.security.oauth2.user.providers.GithubUserInfo;
import com.hello.forum.beans.security.oauth2.user.providers.GoogleUserInfo;
import com.hello.forum.beans.security.oauth2.user.providers.NaverUserInfo;
import com.hello.forum.member.dao.MemberDao;
import com.hello.forum.member.vo.MemberVO;

@Service
public class OAuthService implements OAuth2UserService<OAuth2UserRequest, OAuth2User>{

	@Autowired
	private MemberDao memberDao;
	
	/**
	 * OAuth가 Spring Security가 Naver, Kakao, Google 등에게 로그인 정보를 요청하는 역할
	 */
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		
		OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
		
		// OAuth 서비스(naver)에서 가져온 유저 정보를 담고있음
		OAuth2User oAuth2User = delegate.loadUser(userRequest);
		
		OAuth2UserInfo oAuth2UserInfo = null;
		// OAuth 서비스 이름(naver)
		String provider = userRequest.getClientRegistration()
									 .getRegistrationId();
		
		if(provider.equals("naver")) {
			oAuth2UserInfo = new NaverUserInfo(oAuth2User.getAttributes());
		}
		else if(provider.equals("google")) {
			oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
		}
		else if(provider.equals("github")) {
			oAuth2UserInfo = new GithubUserInfo(oAuth2User.getAttributes());
		}
		
		MemberVO memberVO = new MemberVO();
		memberVO.setEmail(oAuth2UserInfo.getEmail());
		memberVO.setName(oAuth2UserInfo.getName());
		memberVO.setProvider(provider);
		memberVO.setAdminYn("N");
		// DB에 정보 등록
		memberDao.createOrUpdate(memberVO);
		
		return new PrincipalDetails(memberVO, oAuth2UserInfo);
	}

	
	
}
