package com.example.coolweather.model;

public class County {

	private int mId;
	private String mName;
	private String mCode;
	private int mCityId;
	
	public int getId() {
		return mId;
	}
	public void setId(int pId) {
		mId = pId;
	}
	public String getName() {
		return mName;
	}
	public void setName(String pName) {
		mName = pName;
	}
	public String getCode() {
		return mCode;
	}
	public void setCode(String pCode) {
		mCode = pCode;
	}
	public int getCityId() {
		return mCityId;
	}
	public void setCityId(int pCityId) {
		mCityId = pCityId;
	}	
}
