package com.hello.forum.bbs.web;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;

import com.hello.forum.bbs.service.BoardService;
import com.hello.forum.bbs.vo.BoardListVO;
import com.hello.forum.bbs.vo.BoardVO;
import com.hello.forum.bbs.vo.SearchBoardVO;
import com.hello.forum.beans.FileHandler;
import com.hello.forum.beans.security.SecurityUser;
import com.hello.forum.exceptions.MakeXlsxFileException;
import com.hello.forum.exceptions.PageNotFoundException;
import com.hello.forum.member.vo.MemberVO;
import com.hello.forum.utils.AjaxResponse;
import com.hello.forum.utils.ValidationUtils;

import io.github.seccoding.excel.option.WriteOption;
import io.github.seccoding.excel.write.ExcelWrite;
import jakarta.servlet.http.HttpSession;

//import jakarta.validation.Valid;

@Controller
public class BoardController {
	
	private Logger logger = LoggerFactory.getLogger(BoardController.class);

	@Autowired
	private FileHandler fileHandler;

	/*
	 * Bean Container에서 BoardService 타입의 객체를 찾아 아래 멤버변수에게 할당한다(DI: Dependency
	 * Injection)
	 */
	@Autowired
	private BoardService boardService;

//	@GetMapping("/board/list") // 브라우저에서 링크를 클릭, 브라우저 URL을 직접 입력.
//	public String viewBoardListPage(Model model) {
//
//		// 1. 게시글의 건수와 게시글의 목록을 조회해서
//		BoardListVO boardListVO = this.boardService.getAllBoard();
//
//		// 2. /WEB-INF/views/board/boardlist.jsp 페이지에게 게시글의 건수와 게시글의 목록을 전달하고
//		model.addAttribute("boardList", boardListVO);
//
//		// 3. 화면을 보여준다.
//		return "board/boardlist";
//	}
	
	@GetMapping("/board/search") // 브라우저에서 링크를 클릭, 브라우저 URL을 직접 입력.
	public String viewBoardListPage(Model model, SearchBoardVO searchBoardVO) {
		BoardListVO boardListVO = this.boardService.searchAllBoard(searchBoardVO);
		model.addAttribute("boardList", boardListVO);
		model.addAttribute("searchBoardVO", searchBoardVO);
		return "board/boardlist";
	}

	/**
	 * 게시글 작성페이지를 보여주는 URL
	 * 
	 * @return
	 */
	@GetMapping("/board/write")
	public String viewBoardWritePage(HttpSession session) {
		// 보여주는 기능을 할 때에는 view

//		// 로그인을 하지 않은 상태로 URL로만 게시글 작성 페이지로 접근하려고 할 때
//		// 로그인 페이지로 이동하도록 설정.
//		MemberVO memberVO = (MemberVO)session.getAttribute("_LOGIN_USER_");
//		if(memberVO == null) {
//			return "redirect:/member/login";
//		}
		return "board/boardwrite";
	}

	/**
	 * Spring 애플리케이션을 개발할 때 같은 URL을 정의할 수 없다. Method가 다를 경우엔 예외적으로 허용한다. GET
	 * /board/write POST /board/write
	 * 
	 * 글 등록 페이지에서 게시글을 작성하고 "저장"버튼을 클릭하면 데이터베이스에 글 정보를 저장(INSERT)해야한다.
	 * 
	 * 사용자가 작성한 글 정보를 알아야 한다. 1. Servlet Like (HttpServletRequest 객체)
	 * 2. @RequestParam (Servlet like -> 조금 더 편하게 사용) 3. Command Object : 보편적으로 많이
	 * 사용하는 방법 > 파라미터 처리가 매우 편하다!!! 4. @ParhVariable
	 * 
	 * @return
	 */
	@PostMapping("/board/write")
	public String doBoardWrite(/* Spring이 안맞은 파라미터를 자동으로 보내준다. Servlet Like: HttpServletRequest request */
	/*
	 * 컨트롤러로 전송된 파라미터를 하나씩 받아오는 방법
	 * 
	 * @RequestParam으로 정의된 파라미터는 필수 파라미터!!! 컨트롤러로 전송되는 파라미터의 개수가 몇개 없을 때, 예: 3개 미만
	 */
//			@RequestParam String subject,
//			@RequestParam String email,
//			@RequestParam String content
	/*
	 * Command Object 파라미터로 전송된 이름과 BoardVO의 멤버변수의 이름과 같은 것이 있다면 해당 멤버변수에 파라미터의 값을
	 * 할당해준다!!! (setter 이용)
	 */
//			@Valid/*@NotEmpty, @Email, @Size, @Min, @Max 이런 것들을 검사하도록 지시. */
			BoardVO boardVO,
			/* @Valid 에 의해 실행된 파라미터 검사 NotEmpty, Email, Size, Min, Max 등)의 결과 */
//			BindingResult bindingResult,
			@RequestParam MultipartFile file, // 사용자가 전송한 파일정보가 들어온다.
			Model model, Authentication authentication // Authentication => Security Context에 저장되어 있는 인증 정보
			) {
		// 처리하는 기능을 할 때에는 do
		logger.info("글 등록 처리를 해야합니다.");

		/*
		 * 1.Servlet 처럼 받아오는 방법 HttpServletRequest에서 니용 Filter에서 이용
		 */
		// html 코드에서 값을 가져오고자 하는 것의 name을 가져와서 getParameter로 받아온다.
//		String subject = request.getParameter("subject");
//		String email = request.getParameter("email");
//		String content = request.getParameter("content");

//		System.out.println("제목: " + subject);
//		System.out.println("이메일: " + email);
//		System.out.println("내용: " + content);

		logger.info("제목: " + boardVO.getSubject());
		logger.info("이메일: " + boardVO.getEmail());
		logger.info("내용: " + boardVO.getContent());
		logger.info("첨부파일명: " + file.getOriginalFilename());

		// 검사 내용 확인.
		// @NotEmpty로 설정한 부분이 하나라도 에러가 나면 해당 if문이 true가 된다.
//		if(bindingResult.hasErrors()) {
//			model.addAttribute("boardVO", boardVO);
//			return "board/boardwrite";
//		}

		// 수동 검사 시작.
		// 제목 검사.
		boolean isNotEmptySubject = ValidationUtils.notEmpty(boardVO.getSubject());
//		// 이메일 검사.
//		boolean isNotEmptyEmail = ValidationUtils.notEmpty(boardVO.getEmail());
		// 내용 검사.
		boolean isNotEmptyContent = ValidationUtils.notEmpty(boardVO.getContent());
//		// 이메일 형식 검사
//		boolean isEmailFormat = ValidationUtils.email(boardVO.getEmail());

		if (!isNotEmptySubject) {
			// 제목을 입력하지 않았다면.
			model.addAttribute("errorMessage", "제목은 필수 입력 값입니다.");
			model.addAttribute("boardVO", boardVO);
			return "board/boardwrite";
		}

//		if( !isNotEmptyEmail) {
//			// 이메일을 입력하지 않았다면.
//			model.addAttribute("errorMessage", "이메일은 필수 입력 값입니다.");
//			model.addAttribute("boardVO", boardVO);
//			return "board/boardwrite";
//		}

		if (!isNotEmptyContent) {
			// 내용을 입력하지 않았다면.
			model.addAttribute("errorMessage", "내용은 필수 입력 값입니다.");
			model.addAttribute("boardVO", boardVO);
			return "board/boardwrite";
		}

//		if( !isEmailFormat) {
//			// 이메일을 이메일 형태로 입력하지 않았다면.
//			model.addAttribute("errorMessage", "이메일을 올바른 형태로 작성해주세요.");
//			model.addAttribute("boardVO", boardVO);
//			return "board/boardwrite";
//		}

		// 글을 등록하기 직전에 세션에 있는 멤버의 이메일 정보를 넣어라. 라는 의미
		boardVO.setEmail(authentication.getName());

		boolean isCreateSuccess = this.boardService.createNewBoard(boardVO, file);
		if (isCreateSuccess) {
			logger.info("글 등록 성공!");
		} else {
			logger.info("글 등록 실패!");
		}

		// board/boardlist 페이지를 보여주는 URL로 이동처리.
		// redirect:/board/search
		// Spring은 브라우저에게 /board/search로 이동하라는 명령을 전송.
		// 명령을 받은 브라우저는 /board/search로 URL을 이동시킨다.
		// /board/search로 브라우저가 요청을 하게 되면
		// Spring 컨트롤러에서 /board/search URL에 알맞은 처리를 진행한다.
		return "redirect:/board/search";
	}

	// 브라우저에서 URL을 http://localhost:8080/board/view?id=1
	// UTL ? <-- Query Parameter
	// ?id=1 <-- Parameter Key: id, Parameter Value: 1
	// ?id=1&subject=abc <-- Parameter Key: id, Parameter Value: 1 / Parameter Key:
	// subject, Parameter Value: abc

	@GetMapping("/board/view")
	public String viewBoardDetailPage(@RequestParam int id, Model model) {

		// 1. boardService에게 파라미터로 전달받은 id 값을 보내준다.
		// 2. boardService는 파라미터로 전달받은 id의 게시글 정보를 조회해서 반환해주면
		BoardVO boardVO = this.boardService.getOneBoard(id, true);

		// 3. boardview 페이지에 데이터를 전송해준다.
		model.addAttribute("boardVO", boardVO);

		// 4. 화면을 보여준다.
		return "board/boardview";
	}

	@GetMapping("/board/modify/{id}") // /board/modify/1 <-- id 변수의 값은 1
	public String viewBoardModifyPage(@PathVariable int id, Model model,
			Authentication authentication) {

		// 1. 전달받은 id의 값으로 게시글을 조회한다.
		BoardVO boardVO = this.boardService.getOneBoard(id, false);

		UserDetails userDetails = (UserDetails)authentication.getPrincipal();
		MemberVO memberVO = ((SecurityUser) userDetails).getMemberVO();
		
		// 내가 쓴 게시물도 아니고, 관리자도 아니라면 PageNotFoundException 발생
		if ( !authentication.getName().equals(boardVO.getEmail()) && 
						memberVO.getAdminYn().equals("N")) {
			throw new PageNotFoundException();
		}

		// 2. 게시글의 정보를 화면에 보내준다.
		model.addAttribute("boardVO", boardVO);

		// 3. 화면을 보여준다.
		return "board/boardmodify";
	}

	/**
	 * 게시글을 수정한다.
	 * 
	 * @param id      수정할 게시글의 변호
	 * @param boardVO 사용자가 입력한 수정된 게시글의 정보 (제목, 이메일, 내용)
	 * @return
	 */
	@PostMapping("/board/modify/{id}")
	public String doBoardModify(@PathVariable int id, BoardVO boardVO, @RequestParam MultipartFile file, Model model,
			Authentication authentication) {
		
		UserDetails userDetails = (UserDetails)authentication.getPrincipal();
		MemberVO memberVO = ((SecurityUser) userDetails).getMemberVO();

		BoardVO originalBoardVO = this.boardService.getOneBoard(id, false);
		if (!originalBoardVO.getEmail().equals(authentication.getName()) &&
				memberVO.getAdminYn().equals("N")) {
			throw new PageNotFoundException();
		}
		
		

		// 수동 검사 시작.
		// 제목 검사.
		boolean isNotEmptySubject = ValidationUtils.notEmpty(boardVO.getSubject());
//		// 이메일 검사.
//		boolean isNotEmptyEmail = ValidationUtils.notEmpty(boardVO.getEmail());
		// 내용 검사.
		boolean isNotEmptyContent = ValidationUtils.notEmpty(boardVO.getContent());
//		// 이메일 형식 검사
//		boolean isEmailFormat = ValidationUtils.email(boardVO.getEmail());

		if (!isNotEmptySubject) {
			// 제목을 입력하지 않았다면.
			model.addAttribute("errorMessage", "제목은 필수 입력 값입니다.");
			model.addAttribute("boardVO", boardVO);
			return "board/boardmodify";
		}

//		if( !isNotEmptyEmail) {
//			// 이메일을 입력하지 않았다면.
//			model.addAttribute("errorMessage", "이메일은 필수 입력 값입니다.");
//			model.addAttribute("boardVO", boardVO);
//			return "board/boardmodify";
//		}

		if (!isNotEmptyContent) {
			// 내용을 입력하지 않았다면.
			model.addAttribute("errorMessage", "내용은 필수 입력 값입니다.");
			model.addAttribute("boardVO", boardVO);
			return "board/boardmodify";
		}

//		if( !isEmailFormat) {
//			// 이메일을 이메일 형태로 입력하지 않았다면.
//			model.addAttribute("errorMessage", "이메일을 올바른 형태로 작성해주세요.");
//			model.addAttribute("boardVO", boardVO);
//			return "board/boardmodify";
//		}

		// Command Object 에는 전달된 ID가 없으므로
		// PathVariable로 전달된 ID를 셋팅해준다.
		boardVO.setId(id);
//		boardVO.setEmail(memberVO.getEmail());

		// 서비스 처리의 결과는 boolean으로 받아온다.
		boolean isUpdatedSuccess = this.boardService.updateOneBoard(boardVO, file);

		if (isUpdatedSuccess) {
			logger.info("수정 성공했습니다!");
		} else {
			logger.info("수정 실패했습니다!");
		}

		return "redirect:/board/view?id=" + id;
	}

	/*
	 * GET / POST
	 * 
	 * GET 데이터 조회. (페이지 보여주기, 게시글 정보 보여주기) POST 데이터 등록. (게시글 등록하기) PUT 데이터 수정 (게시글
	 * 수정하기, 좋아요 처리하기, 추천 처리하기) DELETE 데이터 삭제 (게시글 삭제하기, 댓글 삭제하기)
	 * 
	 * JSP의 경우에는 PUT, DELETE 지원하지 않음. 오로지 GET, POST만 지원 데이터 조회, 등록, 수정, 삭제 GET/POST를
	 * 이용해서 작성.
	 * 
	 * FORM 으로 데이터를 등록하거나 수정할 경우 -> POST URL이나 링크 등으로 데이터를 조회하거나 삭제할 경우 -> GET
	 */

	@GetMapping("/board/delete/{id}")
	public String doDeleteBoard(@PathVariable int id, Authentication authentication) {
		
		UserDetails userDetails = (UserDetails)authentication.getPrincipal();
		MemberVO memberVO = ((SecurityUser) userDetails).getMemberVO();

		BoardVO originalBoardVO = this.boardService.getOneBoard(id, false);
		if (!originalBoardVO.getEmail().equals(authentication.getName()) &&
				memberVO.getAdminYn().equals("N")) {
			throw new PageNotFoundException();
		}

		boolean isDeletedSuccess = this.boardService.deleteOneBoard(id);

		if (isDeletedSuccess) {
			logger.info("게시글 삭제 성공.");
		} else {
			logger.info("게시글 삭제 실패.");
		}

		return "redirect:/board/search";
	}
	
	
	@ResponseBody
	@PostMapping("/ajax/board/delete/massive")
	public AjaxResponse doDeleteMassive(
			// deleteItems[] 로 전달받은 내용을 deleteItems 리스트로 만들어라 라는 의미이다.
			@RequestParam("deleteItems[]") List<Integer> deleteItems,
			Authentication authentication) {
		
		UserDetails userDetails = (UserDetails)authentication.getPrincipal();
		MemberVO memberVO = ((SecurityUser) userDetails).getMemberVO();
		
		if(memberVO.getAdminYn().equals("N")) {
			throw new PageNotFoundException();
		}
		
		boolean deleteResult = this.boardService.deleteManyBoard(deleteItems);
		
		return new AjaxResponse().append("result", deleteResult);
	}
	

	@GetMapping("/board/file/download/{id}")
	public ResponseEntity<Resource> downloadFile(@PathVariable int id) {

		// 파일 다운로드를 위해서 id 값으로 게시글을 조회한다.
		BoardVO boardVO = this.boardService.getOneBoard(id, false);

		// 만약, 게시글이 존재하지 않다면 "잘못된 접근입니다"라는 에러를 사용자에게 보여준다.
		if (boardVO == null) {
			throw new PageNotFoundException();
		}

		// 첨부된 파일이 없을 경우에도 "잘못된 접근입니다"라는 에러를 사용자에게 보여준다.
		if (boardVO.getFileName() == null || boardVO.getFileName().length() == 0) {
			throw new PageNotFoundException();
		}

		// 첨부된 파일이 있을 경우엔 파일을 사용자에게 보내준다. (Download)
		// boardVO.getOriginFileName(): 사용자가 업로드한 파일이름
		// boardVO.getFileName(): 서버에 저장되어 있는 파일 이름
		return this.fileHandler.download(boardVO.getOriginFileName(), boardVO.getFileName());
	}
	
	
	@GetMapping("/board/excel/download2")
	public ResponseEntity<Resource> downloadExcelFile2() {
		// 모든 게시글 조회
		BoardListVO boardListVO = boardService.getAllBoard();
		
		WriteOption<BoardVO> writeOption = new WriteOption<>();
		writeOption.setFileName("게시글_목록.xlsx");
		writeOption.setFilePath("C:\\uploadFiles");
		writeOption.setContents(boardListVO.getBoardList());
		
		File excelFile = ExcelWrite.write(writeOption);
		
		// 엑셀 파일 다운로드 (다운로드파일 명이 한글일 때, URLEncoder 필요)
		return this.fileHandler.download("게시글_목록.xlsx", excelFile.getName());
	}
	

	@GetMapping("/board/excel/download")
	public ResponseEntity<Resource> downloadExcelFile() {
		// 모든 게시글 조회
		BoardListVO boardListVO = boardService.getAllBoard();

		// XLSX 문서 만드릭
		Workbook workbook = new SXSSFWorkbook(-1);

		// 엑셀 시트 만들기
		Sheet sheet = workbook.createSheet("게시글 목록");

		// 행 만들기
		Row row = sheet.createRow(0);

		Cell cell = row.createCell(0);
		cell.setCellValue("번호");

		cell = row.createCell(1);
		cell.setCellValue("제목");

		cell = row.createCell(2);
		cell.setCellValue("첨부파일명");

		cell = row.createCell(3);
		cell.setCellValue("작성자이메일");

		cell = row.createCell(4);
		cell.setCellValue("조회수");

		cell = row.createCell(5);
		cell.setCellValue("등록일");

		cell = row.createCell(6);
		cell.setCellValue("수정일");

		// 데이터 행 만들고 쓰기
		List<BoardVO> boardList = boardListVO.getBoardList();
		int rowIndex = 1;
		for (BoardVO boardVO : boardList) {
			row = sheet.createRow(rowIndex);

			cell = row.createCell(0);
			cell.setCellValue("" + boardVO.getId());

			cell = row.createCell(1);
			cell.setCellValue(boardVO.getSubject());

			cell = row.createCell(2);
			cell.setCellValue(boardVO.getOriginFileName());

			cell = row.createCell(3);
			cell.setCellValue(boardVO.getEmail());

			cell = row.createCell(4);
			cell.setCellValue(boardVO.getViewCnt());

			cell = row.createCell(5);
			cell.setCellValue(boardVO.getCrtDt());

			cell = row.createCell(6);
			cell.setCellValue(boardVO.getMdfyDt());

			rowIndex += 1;
		}

		// 엑셀 파일 만들기
		File storedFile = fileHandler.getStoredFile("게시글_목록.xlsx");
		OutputStream os = null;

		try {
			os = new FileOutputStream(storedFile);
			workbook.write(os);
		} catch (IOException e) {
			throw new MakeXlsxFileException();
		} finally {
			try {
				workbook.close();
			} catch (IOException e) {
			}
			if (os != null) {
				try {
					os.flush();
				} catch (IOException e) {
				}
				try {
					os.close();
				} catch (IOException e) {
				}
			}
		}

		// 엑셀 파일 다운로드 (다운로드파일 명이 한글일 때, URLEncoder 필요)
		return this.fileHandler.download("게시글_목록.xlsx", "게시글_목록.xlsx");
	}

	@ResponseBody
	@PostMapping("/ajax/board/excel/write")
	public AjaxResponse doExcelUpload(@RequestParam MultipartFile excelFile) {

		boolean isSuccess = this.boardService.createMassiveBoard2(excelFile);
		
		if(isSuccess) {
			
			logger.info(excelFile.getOriginalFilename() + " 파일을 업로드 했습니다.");
		}
		else {
			logger.info(excelFile.getOriginalFilename() + " 파일 업로드에 실패했습니다.");
		}

		// Ajax 를 활용하여 성공 여부와 다음 이동 여부를 return 해준다.
		return new AjaxResponse().append("result", isSuccess).append("next", "/board/search");
	}

}
