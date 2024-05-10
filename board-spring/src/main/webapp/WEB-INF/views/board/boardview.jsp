<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <sec:csrfMetaTags />
    
    <title>게시글 내용</title>
    <jsp:include page="../commonheader.jsp"></jsp:include>
    <style type="text/css">
      div.grid {
        display: grid;
        grid-template-columns: 80px 1fr;
        grid-template-rows: repeat(6, 28px) auto auto 1fr;
        row-gap: 10px;
      }
    </style>
    <script type="text/javascript" src="/js/boardview.js"></script>
  </head>
  <body>
    <jsp:include page="../layout/layout.jsp" />>

    <h1>게시글 조회</h1>
    <div class="grid" data-id="${boardVO.id}">
      <label for="subject">제목</label>
      <div>${boardVO.subject}</div>

      <label for="name">작성자 이름</label>
      <div>${boardVO.memberVO.name}</div>

      <label for="viewCnt">조회수</label>
      <div>${boardVO.viewCnt}</div>

      <label for="originFileName">첨부파일</label>
      <div>
        <a href="/board/file/download/${boardVO.id}">
          ${boardVO.originFileName}
        </a>
      </div>

      <label for="crtDt">등록일</label>
      <div>${boardVO.crtDt}</div>

      <label for="mdfyDt">수정일</label>
      <div>${boardVO.mdfyDt}</div>

      <label for="content">내용</label>
      <div>${boardVO.content}</div>

      <div class="replies">
        <div>
          <button type="button" id="get-all-replies-btn">
            모든 댓글 불러오기
          </button>
        </div>
        <div class="reply-items"></div>
        <div class="write-reply">
          <textarea id="txt-reply"></textarea>
          <button id="btn-save-reply">등록</button>
          <button id="btn-cancel-reply">취소</button>
        </div>
      </div>

        <!-- 게시글 수정/삭제를 할 수 있는 사용자는
              "관리자" 권한을 가진 사용자.
              게시글을 작성한 유저 
        -->
        <!-- <sec:authentication property="principal.memberVO.email" var="email" />
              이런 식으로 쓰면 인증 정보가 담겨있는 authentication에서
              principal.memberVO.email 값을 받아오는데 그 value를 var="email"로 담아서
              EL 문법을 사용할 수 있도록 만들어준다.
         -->
        <sec:authentication property="principal.memberVO.email" var="email" />
        <!-- SecurityUser.class 에 있는 
          public Collection<? extends GrantedAuthority> getAuthorities()에 있는
          정보를 받아와서 그 중에서 첫번쨰 값을 role 변수에 담아서 사용할 것이라는 의미
        -->
        <sec:authentication property="principal.authorities[0]" var="role" />
        <c:if test="${email eq boardVO.email or role eq 'ROLE_ADMIN'}">
          <div class="btn-group">
            <div class="right-align">
              <a href="/board/modify/${boardVO.id}">수정</a>
              <!-- 
                javascript:void(0);
                주로 anchor 태그의 href에 작성하는 코드.
                링크를 클릭했을 때, javascript를 이용해서 처리할 경우 위 처럼 작성을 한다.
                javascript:void(0); 이 코드는 anchor 태그의 링크 이동을 무시한다.
              -->
              <a class="delete-board" href="javascript:void(0);">삭제</a>
            </div>
          </div>
        </c:if>
      
    </div>
    <jsp:include page="../layout/layout_close.jsp" />
  </body>
</html>
