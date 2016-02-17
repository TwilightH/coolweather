package com.coolweather.app.activity;

import java.lang.Character.UnicodeBlock;
import java.security.PublicKey;

import com.coolweather.app.util.HttpCallbackListener;
import com.coolweather.app.util.HttpUtil;
import com.coolweather.app.util.Utility;
import com.example.coolweather.R;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WeatherActivity extends Activity{
	
	private LinearLayout ll_weather_info;
	private TextView tv_city_name,tv_publish_time,tv_weatherdesp,tv_temp1,tv_temp2,tv_current_data;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weather_layout);
		tv_city_name=(TextView)findViewById(R.id.tv_city_name);
		tv_current_data=(TextView)findViewById(R.id.tv_current_data);
		tv_publish_time=(TextView)findViewById(R.id.tv_publish_time);
		tv_temp1=(TextView)findViewById(R.id.tv_temp1);
		tv_temp2=(TextView)findViewById(R.id.tv_temp2);
		tv_weatherdesp=(TextView)findViewById(R.id.tv_weather_desp);
		ll_weather_info=(LinearLayout)findViewById(R.id.ll_weather_info);
		String countyCode=getIntent().getStringExtra("county_code");
		if(!TextUtils.isEmpty(countyCode)){
			//县级代号不为空
			tv_publish_time.setText("同步中...");
			ll_weather_info.setVisibility(View.VISIBLE);
			tv_city_name.setVisibility(View.VISIBLE);
			queryWeatherCode(countyCode);
		}else{
			//没有县级代号直接显示本地天气
			showWeather();
		}
	}
	
	/*
	 * 查询县级代号所对应的天气代号
	 */
	private void queryWeatherCode(String countyCode){
		String address="http://www.weather.com.cn/data/list3/city"+countyCode+".xml";
		queryFromServer(address,"countyCode");
		//Log.d("!!!!!!!!!!!!!!", countyCode);
	}
	
	/*
	 * 查询天气代号所对应的天气
	 */
	private void queryWeatherInfo(String weatherCode){
		String address="http://www.weather.com.cn/data/cityinfo/"+weatherCode+".html";
		queryFromServer(address,"weatherCode");
	}
	
	/*
	 * 根据传入的地址和类型去服务器查询天气代号或天气信息
	 */
	private void queryFromServer(final String address,final String type){
		//Log.d("!!!!!!!!!!!!!!", type);
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			
			@Override
			public void onFinish(String response) {
				// TODO Auto-generated method stub
				if("countyCode".equals(type)){
					if(!TextUtils.isEmpty(response)){
						//从服务器中解析出天气代号
						//Log.d("!!!!!!!!!!!!!!", response);
						String[]array=response.split("\\|");
						if(array!=null){
							String weatherCode=array[1];
							//Log.d("@@@@@@@@@@@", array[0]);
							//Log.d("###########", array[1]);
							queryWeatherInfo(weatherCode);
							
						}
					}
				}else if("weatherCode".equals(type)){
					Utility.handleWeatherResponse(WeatherActivity.this,response);
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							showWeather();
						}
					});
				}
			}
			
			@Override
			public void onError(Exception e) {
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						tv_publish_time.setText("同步失败");
					}
				});
				
			}
		});
	}
	
	/*
	 * 从sharedPreference文件中读取存储的天气信息并显示
	 */
	private void showWeather(){
		SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(this);
		tv_city_name.setText(prefs.getString("city_name", ""));
		tv_temp1.setText(prefs.getString("temp1", ""));
		tv_temp2.setText(prefs.getString("temp2", ""));
		tv_weatherdesp.setText(prefs.getString("weather_desp", ""));
		tv_publish_time.setText("今天"+prefs.getString("publish_time", "")+"发布");
		tv_current_data.setText(prefs.getString("current_data", ""));
		ll_weather_info.setVisibility(View.VISIBLE);
		tv_city_name.setVisibility(View.VISIBLE);
	}

}
