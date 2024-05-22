package com.hello.forum.bbs.api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hello.forum.bbs.service.BoardService;
import com.hello.forum.bbs.vo.BoardListVO;
import com.hello.forum.bbs.vo.BoardVO;
import com.hello.forum.bbs.vo.SearchBoardVO;
import com.hello.forum.beans.security.SecurityUser;
import com.hello.forum.exceptions.PageNotFoundException;
import com.hello.forum.member.vo.MemberVO;
import com.hello.forum.utils.ApiResponse;
import com.hello.forum.utils.ValidationUtils;

@RestController
@RequestMapping("/api/v1")
public class ApiBoardController {
	
	@Autowired
	private BoardService boardService;
	
	@GetMapping("/boards") // 브라우저에서 링크를 클릭, 브라우저 URL을 직접 입력.
	public ApiResponse getBoardListPage(SearchBoardVO searchBoardVO) {
		BoardListVO boardListVO = this.boardService.searchAllBoard(searchBoardVO);
		
		// searchBoardVO.getPageCount(): 총 페이지 수
		// searchBoardVO.getPageNo() < searchBoardVO.getPageCount() - 1: 다음 페이지가 있는지
		return ApiResponse.OK(boardListVO.getBoardList(), boardListVO.getBoardCnt(), 
				searchBoardVO.getPageCount(), searchBoardVO.getPageNo() < searchBoardVO.getPageCount() - 1);
	}
	
	
	@GetMapping("/boards/{id}")
	public ApiResponse getBoard(@PathVariable int id) {
		BoardVO boardVO = this.boardService.getOneBoard(id, true);
		
		return ApiResponse.OK(boardVO, boardVO == null ? 0 : 1);
	}
	
	@DeleteMapping("/boards/{id}")
	public ApiResponse deleteBoard(@PathVariable int id, Authentication authentication) {
		
		// 삭제를 요청한 사람의 인증정보
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		MemberVO memberVO = ((SecurityUser) userDetails).getMemberVO();
		
		BoardVO boardVO = this.boardService.getOneBoard(id, false);
		
		// 로그인한 회원의 이메일과 게시글을 작성한 사람의 이메일이 다를 경우
		if( !memberVO.getEmail().equals(boardVO.getMemberVO().getEmail()) ) {
			return ApiResponse.FORBIDDEN("삭제할 권한이 없습니다.");
		}
		
		boolean isSuccess = this.boardService.deleteOneBoard(id);
		return ApiResponse.OK(isSuccess);
	}
	
	
	@PostMapping("/boards")
	public ApiResponse doBoardWrite(BoardVO boardVO,
			// file 파라미터가 필수가 안되도록 required = false 옵션을 준다.
			@RequestParam(required = false) MultipartFile file, Authentication authentication
			) {
		
		boolean isNotEmptySubject = ValidationUtils.notEmpty(boardVO.getSubject());
		boolean isNotEmptyContent = ValidationUtils.notEmpty(boardVO.getContent());
		
		List<String> errorMessage = null;
		

		if (!isNotEmptySubject) {
			
			if(errorMessage == null) {
				errorMessage = new ArrayList<>();
			}
			
			errorMessage.add("제목을 입력해주세요.");
		}

		if (!isNotEmptyContent) {
			
			if(errorMessage == null) {
				errorMessage = new ArrayList<>();
			}
			
			errorMessage.add("내용을 입력해주세요.");
		}
		
		// 에러 메시지가 있다면 ApiResponse를 return
		if(errorMessage != null) {
			return ApiResponse.BAD_REQUEST(errorMessage);
		}
		
		boardVO.setEmail(authentication.getName());

		boolean isCreateSuccess = this.boardService.createNewBoard(boardVO, file);
		
		return ApiResponse.OK(isCreateSuccess);
	}
	
	
	@PutMapping("/boards/{id}")
	public ApiResponse viewBoardModifyPage(@PathVariable int id, BoardVO boardVO, 
			@RequestParam(required = false) MultipartFile file, Authentication authentication) {

		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		MemberVO memberVO = ((SecurityUser) userDetails).getMemberVO();

		BoardVO originalBoardVO = this.boardService.getOneBoard(id, false);
		if (!originalBoardVO.getEmail().equals(authentication.getName())
				&& memberVO.getAdminYn().equals("N")) {
			return ApiResponse.FORBIDDEN("수정할 권한이 없습니다.");
		}

		// 수동 검사 시작.
		// 제목 검사.
		boolean isNotEmptySubject = ValidationUtils
				.notEmpty(boardVO.getSubject());
		boolean isNotEmptyContent = ValidationUtils
				.notEmpty(boardVO.getContent());

		List<String> errorMessage = null;

		if (!isNotEmptySubject) {
			if (errorMessage == null) {
				errorMessage = new ArrayList<>();
			}
			errorMessage.add("제목을 입력해주세요.");
		}

		if (!isNotEmptyContent) {
			if (errorMessage == null) {
				errorMessage = new ArrayList<>();
			}
			errorMessage.add("내용을 입력해주세요.");
		}

		if (errorMessage != null) {
			return ApiResponse.BAD_REQUEST(errorMessage);
		}

		// Command Object 에는 전달된 ID가 없으므로
		// PathVariable로 전달된 ID를 셋팅해준다.
		boardVO.setId(id);
		boolean isUpdatedSuccess = this.boardService.updateOneBoard(boardVO,
				file);

		return ApiResponse.OK(isUpdatedSuccess);
	}
}
