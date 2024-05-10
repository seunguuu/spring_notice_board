package com.hello.forum.bbs.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hello.forum.bbs.vo.BoardVO;
import com.hello.forum.bbs.vo.SearchBoardVO;

/**
 * DB에 쿼리를 전송 및 실행하고 결과를 받아오는 클래스.
 * 
 * SqlSessionDaoSupport : MyBatis가 Database에 접속을 해서 데이터들을 제어하는 역할.
 * 						  INSERT, UPDATE, SELECT, DELETE
 * 
 * @Repository : Bean Container에 적재를 시키기 위한 힌트. (데이터 접근 제어)
 * @Controller : Spring이 클래스를 객체화 시켜서 Bean Container에 적재.
 */
@Repository
public class BoardDaoImpl extends SqlSessionDaoSupport implements BoardDao {
	
	// 연결정보가 SqlSessionTemplate에 다 들어있는데 Spring은 알고있지만 MyBatis 프레임워크는 모른다.
	// 따라서 이러한 정보들을 전달해줘야 한다.
	
	/*
	 * @Autowired: Bean Container / DI(Dependency Injection)
	 * Bean Container : Spring Framework 가 객체들을 생성해서 보관하는 메모리 공간.
	 * 					@Controller, @Repository
	 * DI (Dependency Inejction)
	 * Bean Container 에 보관되어 있는 객체를 필요한 곳에 자동으로 주입하는 기술
	 * 		실행하는 방법
	 * 		1. 생성자를 이용하는 방법
	 * 		2. Setter를 이용하는 방법 --> Autowired
	 * 		3. 멤버변수를 이용하는 방법
	 */
	@Autowired
	@Override
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		super.setSqlSessionTemplate(sqlSessionTemplate);
	}
	
	
	@Override
	public int getBoardAllCount() {
		// session: 서버와 클라이언트가 연결되어있는 정보.
		// 서버: Database 서버.
		// 클라이언트: Spring 애플리케이션
		// 이것을 Sqlsession이라고 부른다.
		// sqlSession을 받아오는 명령어가 getSqlSession()
		return getSqlSession().selectOne(BoardDao.NAME_SPACE + ".getBoardAllCount");
	}
	
	@Override
	public int searchBoardAllCount(SearchBoardVO searchBoardVO) {
		return getSqlSession().selectOne(BoardDao.NAME_SPACE + ".searchBoardAllCount", searchBoardVO);
	}
	
	@Override
	public SqlSessionTemplate getSqlSessionTemplate() {
		return super.getSqlSessionTemplate();
	}
	
	@Override
	public List<BoardVO> getAllBoard() {
		return getSqlSession().selectList(BoardDao.NAME_SPACE + ".getAllBoard");
	}
	
	
	@Override
	public List<BoardVO> searchAllBoard(SearchBoardVO searchBoardVO) {
		return getSqlSession().selectList(BoardDao.NAME_SPACE + ".searchAllBoard", searchBoardVO);
	}

	@Override
	public int insertNewBoard(BoardVO boardVO) {
		
		/*
		 * MyBatis를 호출해서 쿼리가 실행될 수 있도록 코드
		 * 코드에 파라미터가 필요할 경우에 쿼리셀렉터 이후에 파라미터를 전달
		 * 전달할 수 있는 파라미터의 개수 1개
		 */
		return getSqlSession().insert(BoardDao.NAME_SPACE + ".insertNewBoard", boardVO);
	}


	@Override
	public BoardVO selectOneBoard(int id) {
		return getSqlSession().selectOne(BoardDao.NAME_SPACE + ".selectOneBoard", id);
	}


	@Override
	public int increaseViewCount(int id) {
		return getSqlSession().update(BoardDao.NAME_SPACE + ".increaseViewCount", id);
	}


	@Override
	public int updateOneBoard(BoardVO boardVO) {
		return getSqlSession().update(BoardDao.NAME_SPACE + ".updateOneBoard", boardVO);
	}


	@Override
	public int deleteOneBoard(int id) {
		return getSqlSession().update(BoardDao.NAME_SPACE + ".deleteOneBoard", id);
	}


	@Override
	public List<BoardVO> selectManyBoard(List<Integer> deleteItems) {
		return getSqlSession().selectList(BoardDao.NAME_SPACE + ".selectManyBoard", deleteItems);
	}


	@Override
	public int deleteManyBoard(List<Integer> deleteItems) {
		return getSqlSession().update(BoardDao.NAME_SPACE + ".deleteManyBoard", deleteItems);
	}
	
	
}
