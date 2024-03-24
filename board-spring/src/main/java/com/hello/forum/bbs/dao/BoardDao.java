package com.hello.forum.bbs.dao;

import java.util.List;

import com.hello.forum.bbs.vo.BoardVO;

public interface BoardDao {
	
	/**
	 * DB에 저장된 모든 게시글의 수를 조회 
	 * @return
	 */
	public int getBoardAllCount();
	
	/**
	 * DB에 저장된 모든 게시글 목록을 조회 
	 * @return
	 */
	public List<BoardVO> getAllBoard();
}
