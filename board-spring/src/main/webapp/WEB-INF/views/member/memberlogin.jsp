<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title>로그인</title>
    <jsp:include page="../commonheader.jsp"></jsp:include>
    <style type="text/css">
      div.grid {
        display: grid;
        grid-template-columns: 120px 1fr;
        grid-template-rows: 28px 28px 1fr;
        row-gap: 10px;
      }

      .error {
        grid-column: 1 / -1;
        color: #f00;
        padding-left: 1rem;
        margin: 0;
      }
      .naver-btn {
        background-color: rgb(88, 255, 54);
      }
      .google-btn {
        background-color: rgb(133, 206, 255);
      }
      .github-btn {
        background-color: rgb(82, 82, 82);
      }
      #btn-login, .naver-btn, .google-btn, .github-btn {
        padding: 10px;
        margin-right: 10px;
        border-radius: 5px;
        font-weight: 900;
      }
    </style>
    <script type="text/javascript" src="/js/memberregist.js"></script>
  </head>
  <body>
    <h1>로그인</h1>

    <div>${message}</div>

    <form id="loginForm">
      <sec:csrfInput />
      <input type="hidden" name="next" id="nextUrl" value="${nextUrl}" />
      <div class="grid">
        <label for="email">이메일</label>
        <input type="email" name="email" id="email" />

        <label for="password">비밀번호</label>
        <input type="password" name="password" id="password" />

        <div class="btn-group">
          <div class="right-align">
            <a class="naver-btn" style="color: #fafafa;" href="/oauth2/authorization/naver">Naver 로그인</a>
            <a class="google-btn" style="color: #fafafa;" href="/oauth2/authorization/google">Google 로그인</a>
            <a class="github-btn" style="color: #fafafa;" href="/oauth2/authorization/github">Github 로그인</a>
            <button id="btn-login" style="color: #fafafa;" type="button">로그인</button>
          </div>
        </div>

      </div>
    </form>
  </body>
</html>
