<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<div class="right-align">
  <ul class="horizontal-list">
    
    <sec:authorize access="!isAuthenticated()">
      <li>
        <a href="/member/regist">회원가입</a>
      </li>
      <li>
        <a href="/member/login">로그인</a>
      </li>
    </sec:authorize>

    <sec:authorize access="isAuthenticated()">
      <li style="margin-right: 1rem">
        <sec:authorize access="hasRole('ADMIN')">
          <span id="role-admin">(Super Admin)</span>
        </sec:authorize>

        <!-- principal.memberVO 라고 선언한 것이
          (UserDetails)authentication.getPrincipal(); 
          MemberVO memberVO = ((SecurityUser) userDetails).getMemberVO();
          까지 선언한 것과 같고, 그 뒤에 가져오고 싶은 값(ex. name, email)을 적어주면 된다.
        -->
        <sec:authentication property="principal.memberVO.name" />
        (<span id="login-email"
          ><sec:authentication property="principal.memberVO.email" /></span
        >)
      </li>
      <li>
        <a href="/member/logout">로그아웃</a>
      </li>
      <li>
        <a class="deleteMe" href="javascript:void(0);">탈퇴</a>
      </li>
    </sec:authorize>

  </ul>
</div>
