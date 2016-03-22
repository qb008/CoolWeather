package com.example.coolweather.activity;

import java.util.ArrayList;
import java.util.List;

import com.example.coolweather.R;
import com.example.coolweather.db.CoolWeatherDB;
import com.example.coolweather.model.City;
import com.example.coolweather.model.County;
import com.example.coolweather.model.Province;
import com.example.coolweather.util.HttpCallbackListener;
import com.example.coolweather.util.HttpUtil;
import com.example.coolweather.util.Utility;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChooseAreaActivity extends Activity implements OnItemClickListener{

	public static final int LEVEL_PROVINCE = 0;
	public static final int LEVEL_CITY = 1;
	public static final int LEVEL_COUNTY = 2;
	private int mCurrentLevel;
	private TextView tvTitle;
	private ListView lvArea;
	private ProgressDialog pdShowProgress;
	private ArrayAdapter mAdapter;
	private List<String> mDataList = new ArrayList<String>();
	private List<Province> mProvinceList;
	private List<City> mCityList;
	private List<County> mCountyList;
	private CoolWeatherDB mCoolWeatherDB;
	private Province mSelectedProvince;
	private City mSelectedCity;
	private boolean mBackHome;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		mBackHome = getIntent().getBooleanExtra("back_home", false);
		SharedPreferences _sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		if(_sharedPreferences.getBoolean("city_selected", false) && !mBackHome){
			Intent _intent = new Intent(this,WeatherActivity.class);
			startActivity(_intent);
		}
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose_area);
		initialVariable();
		lvArea.setAdapter(mAdapter);
		lvArea.setOnItemClickListener(this);
		queryProvinces();
	}

	@Override
	public void onBackPressed() {
		
		if(mCurrentLevel == LEVEL_COUNTY){
			queryCities(mSelectedProvince.getId());
		}
		else if(mCurrentLevel == LEVEL_CITY){
			queryProvinces();
		}
		else{
			if(mBackHome){
				Intent _intent = new Intent(this,WeatherActivity.class);
				startActivity(_intent);
			}
			finish();
		}
	}

	private void initialVariable(){
		
		tvTitle = (TextView)findViewById(R.id.tvTitle);
		lvArea = (ListView)findViewById(R.id.lvArea);
		mAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,mDataList);
		mCoolWeatherDB = CoolWeatherDB.getInstance(this);
		pdShowProgress = new ProgressDialog(this);
		pdShowProgress.setMessage("正在加载...");
		pdShowProgress.setCancelable(false);
		pdShowProgress.setCanceledOnTouchOutside(false);
	}
	
	private void queryProvinces(){
		
		mProvinceList = mCoolWeatherDB.loadProvinces();
		if(mProvinceList.size() > 0){
			mDataList.clear();
			for(Province _province : mProvinceList){
				String _provinceName = _province.getName();
				mDataList.add(_provinceName);
			}
			mAdapter.notifyDataSetChanged();
			lvArea.setSelection(0);
			tvTitle.setText("中国");
			mCurrentLevel = LEVEL_PROVINCE;
		}
		else{
			queryFromServer(null,"province");
		}
	}
	
	private void queryCities(int pProvinceId){
		
		mCityList = mCoolWeatherDB.loadCities(pProvinceId);
		if(mCityList.size() > 0){
			mDataList.clear();
			for(City _city : mCityList){
				String _cityName = _city.getName();
				mDataList.add(_cityName);
			}
			mAdapter.notifyDataSetChanged();
			lvArea.setSelection(0);
			tvTitle.setText(mSelectedProvince.getName());
			mCurrentLevel = LEVEL_CITY;
		}
		else{
			queryFromServer(mSelectedProvince.getCode(),"city");
		}
	}
	
	private void queryCounties(int pCityId){
		
		mCountyList = mCoolWeatherDB.loadCounties(pCityId);
		if(mCountyList.size() > 0){
			mDataList.clear();
			for(County _county : mCountyList){
				String _countyName = _county.getName();
				mDataList.add(_countyName);
			}
			mAdapter.notifyDataSetChanged();
			lvArea.setSelection(0);
			tvTitle.setText(mSelectedCity.getName());
			mCurrentLevel = LEVEL_COUNTY;
		}
		else{
			queryFromServer(mSelectedCity.getCode(),"county");
		}
	}
	
	private void queryFromServer(final String pCode, final String pType){
		
		String _address = null;
		if(!TextUtils.isEmpty(pCode)){
			_address = "http://www.weather.com.cn/data/list3/city" + pCode + ".xml";
		}
		else{
			_address = "http://www.weather.com.cn/data/list3/city.xml";
		}
		pdShowProgress.show();
		HttpCallbackListener _listener = new HttpCallbackListener(){

			@Override
			public void onFinish(String pResponse) {

				boolean _result = false;
				if("province".equals(pType)){
					_result = Utility.handleProvincesResponse(mCoolWeatherDB, pResponse);
				}
				else if("city".equals(pType)){
					_result = Utility.handleCitiesResponse(mCoolWeatherDB, pResponse, mSelectedProvince.getId());
				}
				else if("county".equals(pType)){
					_result = Utility.handleCountiesResponse(mCoolWeatherDB, pResponse, mSelectedCity.getId());
				}
				if(_result){
					runOnUiThread(new Runnable(){

						@Override
						public void run() {
							
							pdShowProgress.dismiss();
							if("province".equals(pType)){
								queryProvinces();
							}
							else if("city".equals(pType)){
								queryCities(mSelectedProvince.getId());
							}
							else if("county".equals(pType)){
								queryCounties(mSelectedCity.getId());
							}
						}
						
					});
				}
			}

			@Override
			public void onError(Exception pException) {

				runOnUiThread(new Runnable(){

					@Override
					public void run() {

						pdShowProgress.dismiss();
						Toast.makeText(ChooseAreaActivity.this, "加载失败", Toast.LENGTH_LONG);
					}
					
				});
				
			}
			
		};
		HttpUtil.sendHttpRequest(_address, _listener);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

		if(mCurrentLevel == LEVEL_PROVINCE){
			mSelectedProvince = mProvinceList.get(position);
			int _provinceId = mSelectedProvince.getId();
			queryCities(_provinceId);
		}
		else if(mCurrentLevel == LEVEL_CITY){
			mSelectedCity = mCityList.get(position);
			int _cityId = mSelectedCity.getId();
			queryCounties(_cityId);
		}
		else if(mCurrentLevel == LEVEL_COUNTY){
			String _countyCode = mCountyList.get(position).getCode();
			Intent _intent = new Intent(this,WeatherActivity.class);
			_intent.putExtra("county_code", _countyCode);
			startActivity(_intent);
		}
	}

}
