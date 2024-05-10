package com.hello.forum.bbs.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.hello.forum.bbs.vo.BoardListVO;
import com.hello.forum.bbs.vo.BoardVO;
import com.hello.forum.bbs.vo.SearchBoardVO;

public interface BoardService {
	
	/**
	 * 게시글의 목록과 게시글의 건수를 모두 조회한다.
	 * @return
	 */
	public BoardListVO getAllBoard();
	
	/**
	 * DB에 저장된 모든 게시글의 목록을 조회 (Pagination을 위한 메소드)
	 * @param searchBoardVO 검색할 조건 (페이지 번호, 노출할 목록 개수 등)
	 * @return DB에서 조회된 게시글의 목록
	 */
	public BoardListVO searchAllBoard(SearchBoardVO searchBoardVO);
	
	/**
	 * 새로운 게시글을 등록한다.
	 * @param boardVO 사용자가 입력한 게시글의 내용
	 * @param file 사용자가 업로드한 파일
	 * @return 게시글 등록 성공 여부
	 */
	public boolean createNewBoard(BoardVO boardVO, MultipartFile file);
	
	/**
	 * 전달받은 파라미터의 게시글 정보를 조회해 반환한다.
	 * 게시글 조회시, 게시글 조회수도 1 증가해야 한다.
	 * @param id 사용자가 조회하려는 게시글의 번호
	 * @param isIncrease 이 값이 true일 때, 조회수가 증가한다.
	 * @return 게시글 정보
	 */
	public BoardVO getOneBoard(int id, boolean isIncrease);
	
	/**
	 * 전달받은 게시글 정보로 게시글을 수정한다.
	 * @param boardVO 수정할 게시글의 정보
	 * @param 사용자가 업로드한 파일 (이미 파일이 첨부된 게시글에 새로운 파일을 업로드하면 기존 파일은 사라진다.
	 * @return 수정 성공 여부
	 */
	public boolean updateOneBoard(BoardVO boardVO, MultipartFile file);

	
	/**
	 * 전달받은 게시글의 번호로 게시글을 삭제한다.
	 * 단, 물리적인 삭제가 아니라 논리적 삭제를 수행해야 한다
	 * @param id 삭제할 게시글의 번호
	 * @return 삭제 성공 여부
	 */
	public boolean deleteOneBoard(int id);
	
	public boolean deleteManyBoard(List<Integer> deleteItems);

	/**
	 * 엑셀 파일로 게시글을 대량 등록한다.
	 * @param excelFile
	 * @return
	 */
	public boolean createMassiveBoard(MultipartFile excelFile);
	
	/**
	 * 엑셀 파일로 게시글을 대량 등록한다.
	 * @param excelFile
	 * @return
	 */
	public boolean createMassiveBoard2(MultipartFile excelFile);

	

	
	
}
