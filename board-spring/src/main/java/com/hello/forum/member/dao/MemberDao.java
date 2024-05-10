package com.hello.forum.member.dao;

import com.hello.forum.member.vo.MemberVO;

public interface MemberDao {
	
	String NAME_SPACE = "com.hello.forum.member.dao.MemberDao";
	
	/**
	 * 파라미터로 전달 된 이메일이 DB에 몇 건 존재하는지 확인한다.
	 * @param email 사용자가 가입 요청한 이메일
	 * @return 동일한 이메일로 등록된 회원의 수
	 */
	public int getEmailCount(String email);
	
	/**
	 * 회원가입 쿼리를 실행한다.
	 * @param memberVO 사용자가 입력한 회원 정보
	 * @return DB에 INSERT한 회원의 개수
	 */
	public int createNewMember(MemberVO memberVO);

//	public String selectSalt(String email);
//
//	public MemberVO selectMemberByEmailAndPassword(MemberVO memberVO);

	public int deleteMemberByEmail(String email);
	
	/**
	 * Spring Security에서 사용할 회원 정보 조회 기능.
	 * @param email 로그인 이메일
	 * @return 로그인 이메일과 일치하는 사용자 정보
	 */
	public MemberVO getMemberByEmail(String email);

	/**
	 * OAuth2로 가입한 회원들의 정보를 작성한다.
	 * @param memberVO OAuth로 가입한 memberVO 회원 정보
	 * @return DB에 INSERT / UPDATE 한 회원의 개수
	 */
	public int createOrUpdate(MemberVO memberVO);
	
}
