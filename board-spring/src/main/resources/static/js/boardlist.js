$().ready(function () {
  $("#deleteMassiveBoard").on("click", function () {
    // 선택된 체크박스만 가져온다.
    var checkedItems = $(".target-board-id:checked");

    // 선택된 체크박스만 반복하며 서버로 보낼 파라미터를 생성한다.
    var itemsArray = [];
    checkedItems.each(function (index, data) {
      itemsArray.push($(data).val());
    });


    // _csrf
    var csrfParameterName = $("meta[name=_csrf_parameter]").attr("content");
    // token 값
    var csrfToken = $("meta[name="+csrfParameterName+"]").attr("content");

    // 서버로 전송한다. (ajax)
    $.post(
      "/ajax/board/delete/massive",
      // [csrfParameterName]: csrfToken
      // csrfParameterName의 값이 변경될 수도 있기 때문에 동적으로 불러오기 위해서
      // csrfParameterName에 []를 씌워주면
      // csrfParameterName에 들어있는 값을 사용한다는 의미이다.
      { deleteItems: itemsArray, [csrfParameterName]: csrfToken },
      function (response) {
        var result = response.data.result;
        if (result) {
          // 삭제가 완료되면 현재 페이지를 새로고침 한다.
          location.reload();
        }
      }
    );
  });

  $("#list-size").on("change", function () {
    search(0);
  });

  $("#search-btn").on("click", function () {
    search(0);
  });

  $("#cancel-search-btn").on("click", function () {
    location.href = "/board/search";
  });

  $("#uploadExcelfile").on("click", function () {
    $("#excelfile").click();
  });

  /** 파일이 선택되면 수행해라. */
  $("#excelfile").on("change", function () {
    // 선택된 파일의 정보를 출력.
    var file = $(this)[0].files[0];
    var filename = file.name;

    if (!filename.endsWith(".xlsx")) {
      alert("엑셀 파일을 선택해주세요 !!");
      // 엑셀 파일을 선택하지 않았을 경우
      // 함수 실행을 종료시킨다.
      return;
    }


    // _csrf
    var csrfParameterName = $("meta[name=_csrf_parameter]").attr("content");
    // token
    var csrfToken = $("meta[name="+csrfParameterName+"]").attr("content");


    // 엑셀 파일을 잘 선택한 경우
    // 파일을 서버로 전송시킨다.
    // 파일을 보낼 때에는 FormData라는 객체가 필요하다.
    var formData = new FormData();

    // formData에 파일 정보를 첨부시킨다.
    // ServiceImpl 파라미터의 명과 일치시켜야됨
    formData.append("excelFile", file);
    formData.append(csrfParameterName, csrfToken);

    // 파일 전송은 $.post로 할 수 없다.
    // $.post();
    $.ajax({
      url: "/ajax/board/excel/write", // 요청을 보낼 주소
      type: "POST", // 요청을 보낼 HttpMethod
      data: formData, // 요청을 보낼 데이터 (FormData)
      processData: false,
      contentType: false,
      success: function (response) {
        console.log(response);
        var data = response.data;
        if (data.result && data.next) {
          location.href = data.next;
        }
      },
    });
  });
});
