package com.example.coolweather.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.util.Log;

public class HttpUtil {

	public static void sendHttpRequest(final String pAddress, final HttpCallbackListener pListener){
		
		Thread _thread = new Thread(new Runnable(){

			@Override
			public void run() {
				HttpURLConnection _connection = null;
				try {
					
					URL _url = new URL(pAddress);
					_connection = (HttpURLConnection) _url.openConnection();
					_connection.setRequestMethod("GET");
					_connection.setConnectTimeout(8000);
					_connection.setReadTimeout(8000);
					InputStream _inputStream = _connection.getInputStream();
					BufferedReader _reader = new BufferedReader(new InputStreamReader(_inputStream));
					StringBuilder _response = new StringBuilder();
					String _line = null;
					while((_line = _reader.readLine()) != null){
						_response.append(_line);
					}
					
					if(pListener != null){
						pListener.onFinish(_response.toString());
					}
				} catch (Exception e) {

					if(pListener != null){
						pListener.onError(e);
					}
				}finally{
					if(_connection != null){
						_connection.disconnect();
					}
		        }
			}
		});
		
	    _thread.start();
		
	}
}
