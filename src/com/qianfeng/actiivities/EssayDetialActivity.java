package com.qianfeng.actiivities;

import com.qianfeng.neihan.R;
import com.qianfeng.neihan.R.layout;
import com.qianfeng.neihan.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class EssayDetialActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_essay_detial);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.essay_detial, menu);
		return true;
	}

}
