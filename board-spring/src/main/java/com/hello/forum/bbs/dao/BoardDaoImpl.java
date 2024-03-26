package com.hello.forum.bbs.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hello.forum.bbs.vo.BoardVO;

@Repository
public class BoardDaoImpl extends SqlSessionDaoSupport implements BoardDao {

	@Autowired
	@Override
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		super.setSqlSessionTemplate(sqlSessionTemplate);
	}
	
	@Override
	public int getBoardAllCount() {
		// BoardDaoMapper.xml에 있는 namespace에서 동작하고 싶은 쿼리의 id를 적어줘야 한다.
		// 그 결과를 return
		// 하나의 목록을 조회할 것이기 때문에 selectOne을 사용해준다.
		return getSqlSession().selectOne(BoardDao.NAME_SPACE + ".getBoardAllCount");
	}

	@Override
	public List<BoardVO> getAllBoard() {
		return getSqlSession().selectList(BoardDao.NAME_SPACE + ".getAllBoard");
	}

	@Override
	public int createNewBoard(BoardVO boardVO) {
		return getSqlSession().insert(BoardDao.NAME_SPACE + ".createNewBoard");
	}

}
