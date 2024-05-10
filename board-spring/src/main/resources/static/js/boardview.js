$().ready(function () {
  var pageNumber = 0;
  $(document).on("scroll", function () {
    console.log("스크롤 함!!!");

    // 스크롤바의 윗부분 위치
    var scrollHeight = $(window).scrollTop();
    // console.log("스크롤 위치" + scrollHeight);

    var documentHeight = $(document).height();
    // console.log("문서(내용물)의 높이: " + documentHeight);

    var browserHeight = $(window).height();
    // console.log("브라우저의 높이: " + browserHeight);

    // 스크롤 바의 밑부분 위치
    var scrollBottomPoint = scrollHeight + browserHeight + 30;
    // console.log("스크롤바 밑 부분의 위치: " + scrollBottomPoint);

    var willFetchReply = scrollBottomPoint > documentHeight;
    if (willFetchReply) {
      loadReplies(boardId, pageNumber);
      console.log("댓글을 10개만 더 불러옵니다.");
    }
  });

  $(".delete-board").on("click", function () {
    var chooseValue = confirm(
      "이 게시글을 정말 삭제하시겠습니까?\n삭제 작업은 복구할 수 없습니다."
    );

    var id = $(this).closest(".grid").data("id");

    if (chooseValue) {
      location.href = "/board/delete/" + id;
    }
  });

  var modifyReply = function (event) {
    console.log("수정을 클릭함.", event);
    // 내가 선택한 이벤트의 타겟을 알기 위함
    var target = event.currentTarget;
    // 클래스가 reply(댓글)인 것에게 타겟을 주기 위함.
    var reply = $(target).closest(".reply");
    // 몇번 댓글을 클릭했는지 알기 위함.
    var replyId = reply.data("reply-id");
    console.log("replyId", replyId);

    // 클릭한 댓글 내용 받아오기
    var content = reply.find(".content").text();
    // 댓글 입력창에 기존의 댓글 내용 출력
    $("#txt-reply").val(content);
    // 댓글 입력창 활성화
    $("#txt-reply").focus();

    // 댓글 입력창에 수정 데이터 할당
    $("#txt-reply").data("mode", "modify");
    $("#txt-reply").data("target", replyId);
  };

  var deleteReply = function (event) {
    console.log("삭제를 클릭함.", event);
    // 내가 선택한 이벤트의 타겟을 알기 위함
    var target = event.currentTarget;
    // 클래스가 reply(댓글)인 것에게 타겟을 주기 위함.
    var reply = $(target).closest(".reply");
    // 몇번 댓글을 클릭했는지 알기 위함.
    var replyId = reply.data("reply-id");
    console.log("replyId", replyId);

    // 댓글 입력창의 mode와 target 데이터 삭제
    $("#txt-reply").removeData("mode");
    $("#txt-reply").removeData("target");

    // 해당 댓글을 삭제한 뒤에 댓글 정보들을 다시 불러오고
    // 댓글 입력창의 내용을 비워두기
    if (confirm("댓글을 삭제하시겠습니까?")) {
      $.get("/ajax/board/reply/delete/" + replyId, function (response) {
        var result = response.data.result;
        if (result) {
          loadReplies(boardId);
          $("#txt-reply").val("");
        }
      });
    }
  };

  var reReply = function (event) {
    console.log("답글달기를 클릭함.", event);
    // 내가 선택한 이벤트의 타겟을 알기 위함
    var target = event.currentTarget;
    // 클래스가 reply(댓글)인 것에게 타겟을 주기 위함.
    var reply = $(target).closest(".reply");
    // 몇번 댓글을 클릭했는지 알기 위함.
    var replyId = reply.data("reply-id");
    console.log("replyId", replyId);

    // 댓글 입력창에 대댓글 데이터 할당
    $("#txt-reply").data("mode", "re-reply");
    $("#txt-reply").data("target", replyId);
    $("#txt-reply").focus();
  };

  var recommendReply = function (event) {
    console.log("추천하기를 클릭함.", event);
    // 내가 선택한 이벤트의 타겟을 알기 위함
    var target = event.currentTarget;
    // 클래스가 reply(댓글)인 것에게 타겟을 주기 위함.
    var reply = $(target).closest(".reply");
    // 몇번 댓글을 클릭했는지 알기 위함.
    var replyId = reply.data("reply-id");
    console.log("replyId", replyId);

    // 댓글 입력창의 mode와 target 데이터 삭제
    $("#txt-reply").removeData("mode");
    $("#txt-reply").removeData("target");

    // 해당 댓글을 추천한 후에 댓글 정보들을 다시 불러오고
    // 댓글 입력창의 내용을 비워두기
    $.get("/ajax/board/reply/recommend/" + replyId, function (response) {
      var result = response.data.result;
      console.log(result);
      if (result) {
        loadReplies(boardId);
        $("#txt-reply").val("");
      }
    });
  };

  // 댓글 조회하기
  var loadReplies = function (boardId, pageNo) {
    var isNotUndefinedPageNo = pageNo !== undefined;
    var params = { pageNo: -1 };
    if (isNotUndefinedPageNo) {
      params.pageNo = pageNo;
    }

    $.get("/ajax/board/reply/" + boardId, params, function (response) {
      if (!isNotUndefinedPageNo) {
        //$(".reply-items").html("");
        pageNumber = response.data.paginate.pageCount - 1;
      }

      var count = response.data.count;
      var replies = response.data.replies;

      if (isNotUndefinedPageNo && count == response.data.paginate.listSize) {
        pageNumber++;
      }

      for (var i in replies) {
        var reply = replies[i];

        /***********************이미 불러온 댓글 수정*************************/
        // 이미 불러온 댓글인지 확인
        var appendedReply = $(".reply[data-reply-id=" + reply.replyId + "]");
        var isAppendedReply = appendedReply.length > 0;
        // 이미 불러온 댓글이며, 삭제가 안된 댓글일 경우
        if (isAppendedReply && reply.delYn === "N") {
          appendedReply.find(".content").text(reply.content);
          appendedReply
            .find(".recommend-count")
            .text("추천수: " + reply.recommendCnt);
          var modifyDate = appendedReply.find(".mdfydt");
          if (modifyDate) {
            modifyDate.text("(수정: " + reply.mdfyDt + ")");
          } else {
            var mdfyDtDom = $("<span></span>");
            mdfyDtDom.addClass("mdfydt");
            mdfyDtDom.text("(수정: " + reply.mdfyDt + ")");
            appendedReply.find(".datetime").append(mdfyDtDom);
          }
          continue;
        }
        // 이미 불러온 댓글인데, 삭제가 된 댓글일 경우
        else if (isAppendedReply && reply.delYn === "Y") {
          appendedReply.text("삭제된 댓글입니다.");
          appendedReply.css({
            color: "#F33",
          });
          continue;
        }
        // 이미 불러온 댓글인데, 탈퇴한 회원이 작성한 댓글일 경우
        else if (isAppendedReply && reply.memberVO.delYn === "Y") {
          appendedReply.text("탈퇴한 회원의 댓글입니다.");
          appendedReply.css({
            color: "#F33",
          });
          continue;
        }

        var appendedParentReply = $(
          ".reply[data-reply-id=" + reply.parentReplyId + "]"
        );

        /***********************새로운 댓글 추가*************************/
        /* <div class="reply" data-reply-id="댓글번호"
             style="padding-left: (level - 1) * 40px"> */
        var replyDom = $("<div></div>");
        replyDom.addClass("reply");
        replyDom.attr("data-reply-id", reply.replyId);
        replyDom.data("reply-id", reply.replyId);
        replyDom.css({
          "padding-left": (reply.level === 1 ? 0 : 1) * 40 + "px",
          color: "#333",
        });

        // 삭제된 댓글, 탈퇴한 회원의 댓글, 일반 댓글 구분
        if (reply.delYn === "Y") {
          replyDom.css({
            color: "#F33",
          });
          replyDom.text("삭제된 댓글입니다.");
        } else if (reply.memberVO.delYn === "Y") {
          replyDom.css({
            color: "#F33",
          });
          replyDom.text("탈퇴한 회원의 댓글입니다.");
        } else {
          /* <div class="author">사용자명 (사용자이메일)</div> */
          var authorDom = $("<div></div>");
          authorDom.addClass("author");
          authorDom.text(reply.memberVO.name + " (" + reply.email + ")");
          replyDom.append(authorDom);

          /* <div class="recommend-count">추천수: 실제 추천수</div> */
          var recommendCountDom = $("<div></div>");
          recommendCountDom.addClass("recommend-count");
          recommendCountDom.text("추천수: " + reply.recommendCnt);
          replyDom.append(recommendCountDom);

          /* <div class="datetime"> */
          var datetimeDom = $("<div></div>");
          datetimeDom.addClass("datetime");

          /* span class="crtdt">등록: 등록날짜</span> */
          var crtDtDom = $("<span></span>");
          crtDtDom.addClass("crtdt");
          crtDtDom.text("등록: " + reply.crtDt);
          datetimeDom.append(crtDtDom);

          /* 날짜가 다르면 (수정: 수정날짜)가 보이도록 구성 */
          if (reply.crtDt != reply.mdfyDt) {
            /* <span class="mdfydt">(수정: 수정날짜)</span> */
            var mdfyDtDom = $("<span></span>");
            mdfyDtDom.addClass("mdfydt");
            mdfyDtDom.text("(수정: " + reply.mdfyDt + ")");
            datetimeDom.append(mdfyDtDom);
          }

          replyDom.append(datetimeDom);

          /* <pre class="content">댓글 내용</pre> */
          var contentDom = $("<pre></pre>");
          contentDom.addClass("content");
          contentDom.text(reply.content);
          replyDom.append(contentDom);

          /* ${sessionScope._LOGIN_USER_.email} 를 통해 
          현재 로그인 되어있는 사용자의 이메일 정보를 얻어올 수 있다.
           */
          var loginEmail = $("#login-email").text();


          /* 관리자일때도 댓글을 수정, 삭제가 가능하도록 */
          var roleAdmin = $("#role-admin");
          var isAdmin = roleAdmin && roleAdmin.length > 0;


          var controlDom = $("<div></div>");

          /* 내가 작성한 댓글일 경우 */
          if (reply.email == loginEmail || isAdmin) {
            /* <span class="modify-reply">수정</span> */
            var modifyReplyDom = $("<span></span>");
            modifyReplyDom.addClass("modify-reply");
            modifyReplyDom.text("수정");

            modifyReplyDom.on("click", modifyReply);

            controlDom.append(modifyReplyDom);

            /* <span class="delete-reply">삭제</span> */
            var deleteReplyDom = $("<span></span>");
            deleteReplyDom.addClass("delete-reply");
            deleteReplyDom.text("삭제");

            deleteReplyDom.on("click", deleteReply);

            controlDom.append(deleteReplyDom);

            /* <span class="re-reply">답변하기</span> */
            var reReplyDom = $("<span></span>");
            reReplyDom.addClass("re-reply");
            reReplyDom.text("답변하기");

            reReplyDom.on("click", reReply);

            controlDom.append(reReplyDom);
          } else {
            /* 내가 작성한 댓글이 아닐 경우 */
            /* <span class="recommend-reply">추천하기</span> */
            var recommendReplyDom = $("<span></span>");
            recommendReplyDom.addClass("recommend-reply");
            recommendReplyDom.text("추천하기");

            recommendReplyDom.on("click", recommendReply);

            controlDom.append(recommendReplyDom);

            /* <span class="re-reply">답변하기</span> */
            var reReplyDom = $("<span></span>");
            reReplyDom.addClass("re-reply");
            reReplyDom.text("답변하기");

            reReplyDom.on("click", reReply);

            controlDom.append(reReplyDom);
          }

          replyDom.append(controlDom);
        }

        // 일반 댓글은 reply-items의 자식으로 추가한다.
        if (!appendedParentReply.length > 0) {
          $(".reply-items").append(replyDom);
        }
        // 대댓글은 원 댓글의 자식으로 추가한다.
        else {
          appendedParentReply.append(replyDom);
        }

        /*
        <div class="reply" data-reply-id="댓글번호"
             style="padding-left: (level - 1) * 40px">
          <div class="author">사용자명 (사용자이메일)</div>
          <div class="recommend-count">추천수: 실제 추천수</div>
          <div class="datetime">
            <span class="crtdt">등록: 등록날짜</span>
            <span class="mdfydt">(수정: 수정날짜)</span>
          </div>
          <pre class="content">댓글 내용</pre>
          <div>
            <span class="modify-reply">수정</span>
            <span class="delete-reply">삭제</span>
            <span class="re-reply">답변하기</span>

            <span class="recommend-reply">추천하기</span>
            <span class="re-reply">답변하기</span>
          </div>
        </div>
        */
      }
    });
  };

  // 현재 보고있는 게시글의 댓글들을 받아온다.
  var boardId = $(".grid").data("id");
  // 0번 페이지에 있는 댓글들(10개)을 들고와라
  loadReplies(boardId, 0);

  // 모든 댓글 불러오기 버튼을 클릭하면 댓글을 불러오도록 설정
  $("#get-all-replies-btn").on("click", function () {
    loadReplies(boardId);
  });

  $("#btn-save-reply").on("click", function () {
    // 댓글 입력창의 내용을 불러오고 mode, target 데이터 GET
    var reply = $("#txt-reply").val();
    var mode = $("#txt-reply").data("mode");
    var target = $("#txt-reply").data("target");

    // 공백을 제거한 뒤에 내용이 있다면
    // content에 내용 입력
    if (reply.trim() != "") {
      var body = { content: reply.trim() };
      var url = "/ajax/board/reply/" + boardId;

      // mode가 '대댓글'이라면 parentReplyId를 타겟
      if (mode === "re-reply") {
        body.parentReplyId = target; // 원래 댓글의 아이디
      }

      // mode가 '수정'이라면 해당 댓글에 타겟
      if (mode === "modify") {
        url = "/ajax/board/reply/modify/" + target;
      }

      // 댓글 입력창의 mode와 target 데이터 지우기
      $("#txt-reply").removeData("mode");
      $("#txt-reply").removeData("target");

      // CSRF token을 찾아 body에 넣어준다.
      // body {content, parentReplyId, _csrf}
      var csrfParameterName = $("meta[name=_csrf_parameter]").attr("content");
      var csrfToken = $("meta[name="+csrfParameterName+"]").attr("content");

      // body["_csrf"] = csrfToken;
      // body._csrf = csrfToken;
      body[csrfParameterName] = csrfToken;

      $.post("http://localhost:9090" + url, body, function (response) {
        var result = response.data.result;
        if (result) {
          loadReplies(boardId);
          $("#txt-reply").val("");
        } else {
          alert("댓글을 등록할 수 없습니다. 잠시 후 시도해주세요.");
        }
      });
    }
  });
  $("#btn-cancel-reply").on("click", function () {
    $("#txt-reply").val("");
    $("#txt-reply").removeData("mode");
    $("#txt-reply").removeData("target");
  });
});
