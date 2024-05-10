package com.hello.forum.member.web;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.hello.forum.member.service.MemberService;
import com.hello.forum.member.vo.MemberVO;
import com.hello.forum.utils.AjaxResponse;
import com.hello.forum.utils.StringUtils;
import com.hello.forum.utils.ValidationUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class MemberController {
	
	private Logger logger = LoggerFactory.getLogger(MemberController.class);
	
	@Autowired
	private MemberService memberService;
	
	
	/**
	 * 회원가입 페이지를 보여주는 URL
	 * @return
	 */
	@GetMapping("/member/regist")
	public String viewMemberRegistPage() {
		// 보여주는 기능을 할 때에는 view
		return "member/memberregist";
	}
	
	
	//http://localhost:8080/member/regist/available?email=aaa@aaa.com
	@ResponseBody // 응답하는 데이터를 JSON으로 변환해 클라이언트에게 반환한다.
	@GetMapping("/ajax/member/regist/available")
	public Map<String, Object> checkAvailableEmail(@RequestParam String email){
		// 사용자가 입력한 이메일 정보가 email 변수로 들어온다.
		
		// 사용 가능한 이메일인지 체크하는 과정이 필요하다.
		// 사용 가능한 이메일이라면 true, 아니라면 false
		boolean isAvailableEmail = this.memberService.checkAvailableEmail(email);
		
		/*
		 * 반환되는 형태
		 * {
		 * 	"email": "aaa@aaa.com",
		 * 	"available": false
		 * }
		 */
		Map<String, Object> responseMap = new HashMap<>();
		responseMap.put("email", email);
		responseMap.put("available", isAvailableEmail);
		
		// String 타입이라면 화면이 반환되지만 responseMap는 Map 타입이기 때문에
		return responseMap;
		
	}
	
	
	@PostMapping("/member/regist")
	public String doRegistMember(Model model, MemberVO memberVO) {
		
		// 내용 확인.
		logger.info("이메일: " + memberVO.getEmail());
		logger.info("이름: " + memberVO.getName());
		logger.info("비밀번호: " + memberVO.getPassword());
		
		
		// 수동 검사 시작.
		boolean isNotEmptyEmail = ValidationUtils.notEmpty(memberVO.getEmail());
		boolean isEmailFormat = ValidationUtils.email(memberVO.getEmail());
		boolean isNotEmptyName = ValidationUtils.notEmpty(memberVO.getName());
		boolean isNotEmptyPassword = ValidationUtils.notEmpty(memberVO.getPassword());
		boolean minPasswordLength = ValidationUtils.size(memberVO.getPassword(), 10);
		boolean isNotEmptyConfirmPassword = ValidationUtils.notEmpty(memberVO.getConfirmPassword());
		boolean isEqualPassword = ValidationUtils.isEquals(memberVO.getPassword(), memberVO.getConfirmPassword());
		boolean isPasswordFormat = StringUtils.correctPasswordFormat(memberVO.getPassword());
		
		if( !isNotEmptyEmail ) {
			// 이메일을 입력하지 않았다면.
			model.addAttribute("errorMessage", "이메일은 필수 입력값입니다.");
			model.addAttribute("memberVO", memberVO);
			return "member/memberregist";
		}
		
		if( !isEmailFormat ) {
			// 이메일을 입력하지 않았다면.
			model.addAttribute("errorMessage", "이메일을 올바른 형태로 작성해주세요.");
			model.addAttribute("memberVO", memberVO);
			return "member/memberregist";
		}
		
		if( !isNotEmptyName ) {
			// 이름을 입력하지 않았다면.
			model.addAttribute("errorMessage", "이름은 필수 입력값입니다.");
			model.addAttribute("memberVO", memberVO);
			return "member/memberregist";
		}
		
		if( !isNotEmptyPassword ) {
			// 비밀번호를 입력하지 않았다면.
			model.addAttribute("errorMessage", "비밀번호는 필수 입력값입니다.");
			model.addAttribute("memberVO", memberVO);
			return "member/memberregist";
		}
		
		if( !minPasswordLength ) {
			// 비밀번호의 길이가 최소 10자리 이상 입력하지 않았다면.
			model.addAttribute("errorMessage", "비밀번호는 최소 10자리 이상 입력해야 합니다.");
			model.addAttribute("memberVO", memberVO);
			return "member/memberregist";
		}
		
		if (!isPasswordFormat) {
			model.addAttribute("errorMessage",
					"비밀번호는 영어 대/소문자, 숫자를 포함하여 10자리 이상을 입력해주세요.");
			model.addAttribute("memberVO", memberVO);
			return "member/memberregist";
		}
		
		if( !isNotEmptyConfirmPassword ) {
			// 비밀번호 확인을 입력하지 않았다면.
			model.addAttribute("errorMessage", "비밀번호 확인은 필수 입력값입니다.");
			model.addAttribute("memberVO", memberVO);
			return "member/memberregist";
		}
		
		if( !isEqualPassword ) {
			// 비밀번호가 일치하지 않는다면
			model.addAttribute("errorMessage", "비밀번호가 일치하지 않습니다.");
			model.addAttribute("memberVO", memberVO);
			return "member/memberregist";
		}
		
		
		
		boolean isSuccess = this.memberService.createNewMember(memberVO);
		if(isSuccess) {
			logger.info("회원가입 성공!");
			return "redirect:/member/login";
		}
		
		return "member/memberlogin";
	}
	
	
	@GetMapping("/member/login")
	public String viewLoginPage() {
		return "member/memberlogin";
	}
	
//	@ResponseBody
//	@PostMapping("/ajax/member/login")
//	public AjaxResponse doLogin(MemberVO memberVO, HttpSession session,
//			@RequestParam(defaultValue = "/board/search") String nextUrl){
//		
//		logger.info("Next: " + nextUrl);
//		
//		// Validation Check (파라미터 유효성 검사)
//		// MemberVO 타입을 유효성 체크 하겠다
//		Validator<MemberVO> validator = new Validator<>(memberVO);
//		validator.add("email", Type.NOT_EMPTY, "이메일을 입력해주세요")
//				 .add("email", Type.EMAIL, "이메일 형식이 아닙니다.")
//				 .add("password", Type.NOT_EMPTY, "비밀번호를 입력해주세요")
//				 .start();
//		
//		if(validator.hasErrors()) {
//			// 검사를 했을때 에러가 있으면
//			Map<String, List<String>> errors = validator.getErrors();
//			return new AjaxResponse().append("errors", errors);
//		}
//		
//		
////		try {
//			MemberVO member = this.memberService.getMember(memberVO);
//			// 로그인이 정상적으로 이루어졌다면 세션을 생성한다.
//			// ${sessionScope._LOGIN_USER_.adminYn eq 'Y'} 관리자라면 'Y'
//			session.setAttribute("_LOGIN_USER_", member);
//			// 세션이 유지될 수 있는 시간(세션 만료시간) 설정. default: 30분
//			// () 안에 는 second 단위로 숫자를 넣으면 된다.
//			session.setMaxInactiveInterval(20 * 60);
////		}
////		catch (IllegalArgumentException iae) {
////			// 로그인에 실패했다면 화면으로 실패 사유를 보내준다.
////			return new AjaxResponse().append("errorMessage", iae.getMessage());
////		}
//		
//		return new AjaxResponse().append("next", nextUrl);
//	}
	
	
	@GetMapping("/member/logout")
	public String doLogout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		// 로그인 할때에는 세션에 데이터를 집어넣었으므로
		// 로그아웃 할때에는 세션에서 데이터를 삭제해야 하므로
		// 세션의 정보가 필요하다.
		
		// Logout 처리.
		// SessionID로 전달된 세션의 모든 정보를 삭제.
//		session.invalidate();
		
		// Spring Security Logout!
		LogoutHandler logoutHandler = new SecurityContextLogoutHandler();
		logoutHandler.logout(request, response, authentication);
		
		return "redirect:/board/search";
	}
	
	
	@ResponseBody
	@GetMapping("/ajax/member/delete-me")
	public AjaxResponse doDeleteMe(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		// 현재 로그인 되어있는 사용자의 정보
//		 getAttribute의 반환타입이 Object 타입이기 때문에 MemberVO로 캐스팅해준다.
//		MemberVO memberVO = (MemberVO) session.getAttribute("_LOGIN_USER_");
		boolean isSuccess = this.memberService.deleteMe(authentication.getName());
		
		if(isSuccess) {
//			session.invalidate();
			LogoutHandler logoutHandler = new SecurityContextLogoutHandler();
			logoutHandler.logout(request, response, authentication);
		}
		
		// isSuccess가 true라면 /member/success-delete-me,
		// false하면 /member/fail-delete-me 로 이동
		return new AjaxResponse().append("next", 
				isSuccess ? "/member/success-delete-me" : "/member/fail-delete-me");
	}
	
	
	@GetMapping("/member/{result}-delete-me")
	public String viewDeleteMePage(@PathVariable String result) {
		result = result.toLowerCase();
		if( !result.equals("fail") && !result.equals("success")) {
			// result의 값이 fail, success가 아니면 404페이지를 보여준다.
			return "error/404";
		}
		
		return "member/" + result + "deleteme";
		
	}
	
	
	
	
	
	
	
	
	
	
	
}
