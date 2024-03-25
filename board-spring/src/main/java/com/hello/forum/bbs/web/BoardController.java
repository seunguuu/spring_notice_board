package com.hello.forum.bbs.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.hello.forum.bbs.service.BoardService;
import com.hello.forum.bbs.vo.BoardListVO;

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
	
}
