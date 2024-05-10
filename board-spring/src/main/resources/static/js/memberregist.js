$().ready(function () {
  var alertDialog = $(".alert-dialog");

  // 배열.
  if (alertDialog && alertDialog.length > 0) {
    alertDialog[0].showModal();
  }

  $("#email").on("keyup", function () {
    // 서버에게 사용할 수 있는 이메일인지 확인 받는다.
    $.get(
      "/ajax/member/regist/available",
      { email: $(this).val() },
      function (response) {
        var available = response.available;
        if (available) {
          $("#email").addClass("available");
          $("#email").removeClass("unusable");
          $("#btn-regist").removeAttr("disabled");
        } else {
          $("#email").addClass("unusable");
          $("#email").removeClass("available");
          $("#btn-regist").attr("disabled", "disabled");
        }
      }
    );
  });

  $("#btn-login").on("click", function () {
    $(".error").remove();
    $("div.grid").removeAttr("style");

    // $.post(
    //   "/member/login-proc",
    //   {
    //     email: $("#email").val(),
    //     password: $("#password").val(),
    //     nextUrl: $("#nextUrl").val(),
    //   },
    //   function (response) {
    //     var errors = response.data.errors;
    //     var errorMessage = response.data.errorMessage;
    //     var next = response.data.next;

    //     // 파라미터 유효성 검사에 실패했을 경우.
    //     if (errors) {
    //       // 데이터가 있다면

    //       for (var key in errors) {
    //         // 에러 메시지를 담을 div와 클래스를 추가
    //         var errorDiv = $("<div></div>");
    //         errorDiv.addClass("error");
    //         // <div class="error"></div>

    //         var values = errors[key];
    //         for (var i in values) {
    //           var errorValue = values[i];

    //           // 에러 내용을 만들어준 후 errirDiv에 추가
    //           var error = $("<div></div>");
    //           error.text(errorValue);

    //           errorDiv.append(error);
    //         }

    //         // 객체 리터럴 다음에 에러 메시지를 붙여준다.
    //         $("input[name=" + key + "]").after(errorDiv);
    //       }

    //       // 유효성 검사에 따른 에러에 따라 클래스를 추가해준다.
    //       if (errors.email && errors.password) {
    //         var emailFailCount = errors.email.length;
    //         var passwordFailCount = errors.password.length;

    //         // Inline-Style 지정.
    //         $("div.grid").css({
    //           "grid-template-rows":
    //             "28px " +
    //             21 * emailFailCount +
    //             "px 28px " +
    //             21 * passwordFailCount +
    //             "px 1fr",
    //         });
    //       } else if (errors.email) {
    //         var emailFailCount = errors.email.length;
    //         $("div.grid").css({
    //           "grid-template-rows":
    //             "28px " + 21 * emailFailCount + "px 28px 1fr",
    //         });
    //       } else if (errors.password) {
    //         var passwordFailCount = errors.password.length;
    //         $("div.grid").css({
    //           "grid-template-rows":
    //             "28px 28px " + 21 * passwordFailCount + "px 1fr",
    //         });
    //       }
    //     }

    //     // 파라미터 유효성 검사는 패스
    //     // 이메일이나 패스워드가 잘못된 경우.
    //     if (errorMessage) {
    //       console.log(errorMessage);
    //       var errorDiv = $("<div></div>");
    //       errorDiv.addClass("error");
    //       errorDiv.text(errorMessage);

    //       $("#loginForm").after(errorDiv);
    //     }

    //     // 정상적으로 로그인에 성공한 경우.
    //     if (next) {
    //       console.log(next);
    //       location.href = next;
    //     }

    //     /*
    //         response = {
    //             response: {
    //                 errors: {
    //                     "email": []
    //                 },
    //                 errorMessage: "",
    //                 next: "/board/search"
    //             }
    //         }
    //         */
    //   }
    // );

    $("#loginForm")
    .attr({
      action: "/member/login-proc",
      method: "post",
    })
    .submit();
  });
});
