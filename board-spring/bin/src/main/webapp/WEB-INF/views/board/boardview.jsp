<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 조회</title>
<style type="text/css">
    a:link, a:hover, a:visited, a:active {
        color: #333;
        text-decoration: none;
    }
    div.grid {
        display: grid;
        grid-template-columns: 80px 1fr;
        grid-template-rows: repeat(28px, 5) 320px 1fr;
        row-gap: 10px;
    }
    div.grid > div.btn-group {
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
        // 삭제 링크를 클릭하면
        var deleteAnchor = document.querySelector(".delete-board");
        deleteAnchor.addEventListener("click", function () {
            // 사용자에게 진짜 삭제할 것이냐 물어보고
            var chooseValue = confirm("이 게시글을 정말 삭제하시겠습니까?\n삭제 작업은 복구할 수 없습니다.");

            if(chooseValue){
                location.href = "/board/delete/${boardVO.id}";
            }
        })
    }
</script>
</head>
<body>
    <h1>게시글 조회</h1>
    <div class="grid">
        <label for="subject">제목</label>
        <div>${boardVO.subject}</div>

        <label for="email">이메일</label>
        <div>${boardVO.email}</div>

        <label for="viewCnt">조회수</label>
        <div>${boardVO.viewCnt}</div>

        <label for="crtDt">등록일</label>
        <div>${boardVO.crtDt}</div>

        <label for="mdfyDt">수정일</label>
        <div>${boardVO.mdfyDt}</div>

        <label for="content">내용</label>
        <div>${boardVO.content}</div>

        <div class="btn-group">
            <div class="right-align">
                <a href="/board/modify/${boardVO.id}">수정</a>
                <a class="delete-board" href="javascript:void(0);">삭제</a>
            </div>
        </div>
    </div>
    
</body>
</html>