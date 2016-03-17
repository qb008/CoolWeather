package com.example.coolweather.model;

public class City {

	private int mId;
	private String mName;
	private String mCode;
	private int mProvinceId;
	
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
	public int getProvinceId() {
		return mProvinceId;
	}
	public void setProviceId(int pProvinceId) {
		mProvinceId = pProvinceId;
	}	
}
