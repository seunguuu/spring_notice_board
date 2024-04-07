package com.hello.forum.bbs.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hello.forum.bbs.service.BoardService;
import com.hello.forum.bbs.vo.BoardListVO;
import com.hello.forum.bbs.vo.BoardVO;

@Controller
public class BoardController {
	
	@Autowired
	private BoardService boardService;
	
	
	@GetMapping("/board/list")
	public String viewBoardListPage(Model model) {
		
		// 게시글의 건수와 게시글의 목록을 조회
		BoardListVO boardListVO = this.boardService.getAllBoard();
		// /WEB-INF/views/board/boardlist.jsp 페이지에게 게시글의 건수와 게시글의 목록을 전달하고
		model.addAttribute("boardList", boardListVO);
		// 화면을 보여준다.
		return "board/boardlist";
	}
	
	/**
	 * 게시글 작성 페이지를 보여주는 URL
	 * @return
	 */
	@GetMapping("/board/write")
	public String viewBoardWritePage() {
		return "board/boardwrite";
	}
	
	
	@PostMapping("/board/write")
	public String doBoardWrite(BoardVO boardVO) {
		
		System.out.println("제목: " + boardVO.getSubject());
		System.out.println("이메일: " + boardVO.getEmail());
		System.out.println("내용: " + boardVO.getContent());
		
		
		boolean isCreateSuccess = this.boardService.createNewBoard(boardVO);
		
		if(isCreateSuccess) {
			System.out.println("글 등록 성공!");
		}
		else {
			System.out.println("글 등록 실패!");
		}
		
		return "redirect:/board/list";
	}
	
	
	@GetMapping("/board/view")
	public String viewBoardDetailPage(@RequestParam int id, Model model) {
		System.out.println("ID: " + id);
		
		// boardService에게 파라미터로 전달받은 id 값을 보내준다.
		// boardService는 파라미터로 전달받은 id의 게시글 정보를 조회해서 반환해주면
		BoardVO boardVO = this.boardService.getOneBoard(id, true);
		
		// boardview 페이지에 데이터를 전송해준다.		
		model.addAttribute("boardVO", boardVO);
		
		// 화면을 보여준다.
		return "board/boardview";
	}
	
	
	@GetMapping("/board/modify/{id}")
	public String viewBoardModifyPage(@PathVariable int id, Model model) {
		
		// 전달받은 id의 값으로 게시글을 조회한다. 
		BoardVO boardVO = this.boardService.getOneBoard(id, false);
		// 게시글의 정보를 화면에 보내준다. 
		model.addAttribute("boardVO", boardVO);
		
		// 화면을 보여준다.
		return "board/boardmodify";
	}
	
	
	@PostMapping("/board/modify/{id}")
	public String doBoardModify(@PathVariable int id, BoardVO boardVO) {
		
		boardVO.setId(id);
		
		boolean isUpdatedSuccess = this.boardService.updateOneBoard(boardVO);
		
		if(isUpdatedSuccess) {
			System.out.println("수정 성공!");
		}
		else {
			System.out.println("수정 실패!");
		}
		
		return "redirect:/board/view?id=" + id;
	}
	
	
	@GetMapping("/board/delete/{id}")
	public String doDeleteBoard(@PathVariable int id) {
		
		boolean isDeletedSuccess = this.boardService.deleteOneBoard(id);
		
		if(isDeletedSuccess) {
			System.out.println("게시글 삭제 성공.");
		}
		else {
			System.out.println("게시글 삭제 실패.");
		}
		
		return "redirect:/board/list";
	}
	
	
	
	
	
	
	
}
