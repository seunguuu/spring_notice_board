<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title>게시글 작성</title>
    <jsp:include page="../commonheader.jsp"></jsp:include>
    <style type="text/css">
      /* div인데 클래스가 grid인것 */
      div.grid {
        display: grid;
        grid-template-columns: 80px 1fr;
        grid-template-rows: 28px 28px 320px 1fr;
        row-gap: 10px;
      }
    </style>
    <script type="text/javascript" src="/js/boardwrite.js"></script>
  </head>
  <body>
    <c:if test="${not empty errorMessage}">
      <dialog class="alert-dialog">
        <h1>${errorMessage}</h1>
      </dialog>
    </c:if>

    <jsp:include page="../layout/layout.jsp" />>

    <h1>게시글 작성</h1>
    <!-- /board/write 라는곳에 post방식으로 전송하라는 의미이다. -->
    <!-- enctype="multipart/form-data" 를 추가해야 파일을 보낼 수 있다. -->
    <form action="/board/write" method="post" enctype="multipart/form-data">
      <sec:csrfInput />
      <div class="grid">
        <label for="subject">제목</label>
        <input
          id="subject"
          type="text"
          name="subject"
          value="${boardVO.subject}"
        />

        <label for="file">첨부파일</label>
        <input id="file" type="file" name="file" />

        <label for="content">내용</label>
        <textarea id="content" name="content" style="height: 300px">
${boardVO.content}</textarea
        >

        <div class="btn-group">
          <div class="right-align">
            <input type="submit" value="저장" />
          </div>
        </div>
      </div>
    </form>
    <jsp:include page="../layout/layout_close.jsp" />
  </body>
</html>
