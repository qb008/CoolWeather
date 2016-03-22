package com.example.coolweather.util;

public interface HttpCallbackListener {

	void onFinish(String pResponse);
	
	void onError(Exception pException);
}
