package com.hello.forum.bbs.dao;

import java.util.List;

import com.hello.forum.bbs.vo.BoardVO;

public interface BoardDao {
	
	String NAME_SPACE = "com.hello.forum.bbs.dao.BoardDao";
	
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

	/**
	 * DB에 새로운 게시글을 등록한다.
	 * @param boardVO 사용자가 입력한 게시글 정
	 * @return 사용자가 DB에 INSERT한 개수 
	 */
	public int insertNewBoard(BoardVO boardVO);

	/**
	 * 전달받은 파라미터로 게시글에서 데이터베이스를 조회해 반환한다.
	 * @param id 조회하려는 게시글의 번호 
	 * @return 조회된 게시글 정보 
	 */
	public BoardVO selectOneBoard(int id);

	/**
	 * 전달받은 파라미터로 데이터베이스에서 해당 게시글의 조회수를 1 증가시킨다.
	 * @param id 조회수를 증가시키려는 제시글의 번
	 * @return 업데이트 영향을 받ㅇ느 데이터의 건수 
	 */
	public int increaseViewCount(int id);
}
