package com.example.coolweather.model;

public class WeatherInfo {

	private String mCityName;
	private String mWeatherCode;
	private String mTemp1;
	private String mTemp2;
	private String mWeatherDes;
	private String mPublishTime;
	
	public String getCityName(){
		
		return mCityName;
	}
	
	public void setCityName(String pCityName){
		
		mCityName = pCityName;
	}
	
    public String getWeatherCode(){
		
		return mWeatherCode;
	}
	
	public void setWeatherCode(String pWeatherCode){
		
		mWeatherCode = pWeatherCode;
	}
	
    public String getTemp1(){
		
		return mTemp1;
	}
	
	public void setTemp1(String pTemp1){
		
		mTemp1 = pTemp1;
	}
	
    public String getTemp2(){
		
		return mTemp2;
	}
	
	public void setTemp2(String pTemp2){
		
		mTemp2 = pTemp2;
	}
	
    public String getWeatherDes(){
		
		return mWeatherDes;
	}
	
	public void setWeatherDes(String pWeatherDes){
		
		mWeatherDes = pWeatherDes;
	}
	
    public String getPublishTime(){
		
		return mPublishTime;
	}
	
	public void setPublishTime(String pPublishTime){
		
		mPublishTime = pPublishTime;
	}
}
