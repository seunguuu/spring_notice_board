package com.hello.forum.bbs.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
	public String viewBoardDetailPage(@RequestParam int id) {
		System.out.println("ID: " + id);
		return "board/boardview";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
