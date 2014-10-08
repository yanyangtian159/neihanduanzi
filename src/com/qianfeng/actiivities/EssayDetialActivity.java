package com.qianfeng.actiivities;

import java.util.List;

import com.qianfeng.adapter.DetailPagerAdapter;
import com.qianfeng.bean.DataStore;
import com.qianfeng.bean.TextEntity;
import com.qianfeng.neihan.R;
import com.qianfeng.neihan.R.layout;
import com.qianfeng.neihan.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;

public class EssayDetialActivity extends FragmentActivity {

	private ViewPager pager;
	
	private DetailPagerAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_essay_detial);
		
		pager = (ViewPager)findViewById(R.id.detail_pager_content);
		// 设置 FragmentPagerAdapter
		
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		
		int category = 1;
		
		int currentEssayPosition = 0;
		
		if(extras != null){
			category = extras.getInt("category", 1);
			currentEssayPosition = extras.getInt("currentEssayPosition", 0);
		}
		
		DataStore dataStore = DataStore.getoutInstance();
		
		List<TextEntity> entities = null;
		
		if(category == 1){
			entities = dataStore.getTextEntities();
		}else if(category == 2){
			entities = dataStore.getImageEntities();
		}
		
		adapter = new DetailPagerAdapter(getSupportFragmentManager(), entities);
		pager.setAdapter(adapter);
		
		if(currentEssayPosition > 0){
			pager.setCurrentItem(currentEssayPosition);
		}
	}
	
}

