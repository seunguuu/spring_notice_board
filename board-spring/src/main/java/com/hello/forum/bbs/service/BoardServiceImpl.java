package com.hello.forum.bbs.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hello.forum.bbs.dao.BoardDao;
import com.hello.forum.bbs.vo.BoardListVO;
import com.hello.forum.bbs.vo.BoardVO;

@Service
public class BoardServiceImpl implements BoardService {

	@Autowired
	private BoardDao boardDao;
	
	@Override
	public BoardListVO getAllBoard() {
		
		int boardCount = this.boardDao.getBoardAllCount();
		List<BoardVO> boardList = this.boardDao.getAllBoard();
		
		BoardListVO boardListVO = new BoardListVO();
		boardListVO.setBoardCnt(boardCount);
		boardListVO.setBoardList(boardList);
		
		return boardListVO;
	}

	@Override
	public boolean createNewBoard(BoardVO boardVO) {
		
		// DB에 등록한 게시글의 개수를 반환 
		int createCount = this.boardDao.insertNewBoard(boardVO);
		
		return createCount > 0;
	}

	@Override
	public BoardVO getOneBoard(int id, boolean isIncrease) {
		// 게시글 정보 조회하기 
		BoardVO boardVO = this.boardDao.selectOneBoard(id);
		
		// 게시글을 조회한 결과가 null 이라면, 잘못된 접근입니다. 예외를 발생시킨다. 
		if(boardVO == null) {
			throw new IllegalArgumentException("잘못된 접근입니다.");
		}
		
		if(isIncrease) {
			// 게시글의 조회수를 1 증가시키기
			int updatedCount = this.boardDao.increaseViewCount(id);
			if(updatedCount == 0) {
				throw new IllegalArgumentException("잘못된 접근입니다.");
			}
		}
		
		// 게시글 정보 반환 
		return boardVO;
	}

	@Override
	public boolean updateOneBoard(BoardVO boardVO) {
		
		int updatedCount = this.boardDao.updateOneBoard(boardVO);
		
		return updatedCount > 0;
	}

	@Override
	public boolean deleteOneBoard(int id) {
		
		int deletedCount = this.boardDao.deleteOneBoard(id);
		
		return deletedCount > 0;
	}

}
