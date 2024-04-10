<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 작성하기</title>
<style type="text/css">
    div.grid {
        display: grid;
        grid-template-columns: 80px 1fr;
        grid-template-rows: 28px 28px 320px 1fr;
        row-gap: 10px;
    }
    div.grid > div.btn-group {
        display: grid;
        grid-column: 1 / 3;
    }
    div.grid div.right-align {
        text-align: right;
    }
    label {
        padding-left: 10px;
    }
    button, input, textarea {
        padding: 10px;
    }
</style>
<script type="text/javascript">
    window.onload = function () {
        var dialog = document.querySelector(".alert-dialog");
        dialog.showModal();
    };
</script>
</head>
<body>
    <c:if test="${not empty errorMessage}">
        <dialog class="alert-dialog">
            <h1>${errorMessage}</h1>
        </dialog>
    </c:if>
	<h1>게시글 작성</h1>
    <form action="/board/write" method="post">
        <div class="grid">
            <!-- dialog가 실행되면 errorMEssage를 발생시킨 후에 입력했던 값들이 그대로 남아있어야 하므로
            input 태그에 value값을 춰서 model.addattribute로 넘겨받았던 boardVO 를 통해
            boardVO.subject, boardVO.email, boardVO.content를 통해
            입력했었던 내용을 그대로 남겨둔다.
            -->
            <label for="subject">제목</label>
            <input type="text" name="subject" id="subject" value="${boardVO.subject}">

            <label for="email">이메일</label>
            <input type="email" name="email" id="email" value="${boardVO.email}">
            
            <label for="content">내용</label>
            <textarea name="content" id="content" style="height: 300px;">
                ${boardVO.subject}
            </textarea>

            <div class="btn-group">
                <div class="right-align">
                    <input type="submit" value="저장">
                </div>
            </div>
        </div>
    </form>
</body>
</html>