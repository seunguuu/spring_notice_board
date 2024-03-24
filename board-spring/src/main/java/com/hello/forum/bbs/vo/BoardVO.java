package com.hello.forum.bbs.vo;

public class BoardVO {
	
	private int id;
	private String subject;
	private String content;
	private String email;
	private int viewCnt;
	private String crtDt;
	private String mdfyDt;
	private String fileName;
	private String originFileName;
	
	public int getId() {
		return id;
	}
	
	public String getSubject() {
		return subject;
	}
	
	public String getContent() {
		return content;
	}
	
	public String getEmail() {
		return email;
	}
	
	public int getViewCnt() {
		return viewCnt;
	}
	
	public String getCrtDt() {
		return crtDt;
	}
	
	public String getMdfyDt() {
		return mdfyDt;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public String getOriginFileName() {
		return originFileName;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public void setViewCnt(int viewCnt) {
		this.viewCnt = viewCnt;
	}
	
	public void setCrtDt(String crtDt) {
		this.crtDt = crtDt;
	}
	
	public void setMdfyDt(String mdfyDt) {
		this.mdfyDt = mdfyDt;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public void setOriginFileName(String originFileName) {
		this.originFileName = originFileName;
	}
}
