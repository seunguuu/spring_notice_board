package com.hello.forum.bbs.vo;

import java.util.List;

public class BoardListVO {
	
	private int boardCnt;
	private List<BoardVO> boardList;
	
	public int getBoardCnt() {
		return boardCnt;
	}
	
	public List<BoardVO> getBoardList() {
		return boardList;
	}
	
	public void setBoardCnt(int boardCnt) {
		this.boardCnt = boardCnt;
	}
	
	public void setBoardList(List<BoardVO> boardList) {
		this.boardList = boardList;
	}
}
