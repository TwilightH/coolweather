package com.coolweather.app.activity;


import com.coolweather.app.db.CoolWeatherOpenHelper;
import com.example.coolweather.R;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

public class MainActivity extends Activity{
	private CoolWeatherOpenHelper coolWeatherDB;
	private SQLiteDatabase db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		coolWeatherDB=new CoolWeatherOpenHelper(this, "cool", null, 1);
		coolWeatherDB.getWritableDatabase();
	}

}
