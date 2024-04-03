package com.hello.forum.bbs.service;

import com.hello.forum.bbs.vo.BoardListVO;
import com.hello.forum.bbs.vo.BoardVO;

public interface BoardService {
	
	/**
	 * 게시글의 목록과 게시글의 건수를 모두 조회한다.
	 * @return
	 */
	public BoardListVO getAllBoard();

	/**
	 * 새로운 게시글 등록 처리를 한다.
	 * @param boardVO 사용자가 입력한 게시글 정
	 * @return 정상적으로 등록되었는지 여부 
	 */
	public boolean createNewBoard(BoardVO boardVO);
	
	/**
	 * 전달받은 파라미터의 게시글 정보를 조회해 반환한다.
	 * @param id 사용자가 조회하려는 게시글의 번
	 * @return 게시글의 정보 
	 */
	public BoardVO getOneBoard(int id, boolean isIncrease);
	
}
