package com.qianfeng.neihan;

import java.util.ArrayList;
import java.util.List;

import com.qianfeng.frgments.HuoDongFragment;
import com.qianfeng.frgments.ImageListFragment;
import com.qianfeng.frgments.MineFragment;
import com.qianfeng.frgments.ReviewFragment;
import com.qianfeng.frgments.TextListFrament;

import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;

import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class MainActivity extends FragmentActivity implements OnCheckedChangeListener{
	private List<Fragment> fragments;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		fragments=new ArrayList<Fragment>();
		fragments.add(new TextListFrament());
		fragments.add(new ImageListFragment());
		fragments.add(new ReviewFragment());
		fragments.add(new HuoDongFragment());
		fragments.add(new MineFragment());
		Fragment fragment=fragments.get(0);
		FragmentManager manager=getSupportFragmentManager();
		FragmentTransaction transaction=manager.beginTransaction();
		transaction.replace(R.id.main_content, fragment);
		transaction.commit();
		RadioGroup group = (RadioGroup) findViewById(R.id.main_tab_bar);
		group.setOnCheckedChangeListener(this);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		int childCount = group.getChildCount();
		int childitem = 0;
		RadioButton button = null;
		for (int i = 0; i < childCount; i++) {
			button = (RadioButton) group.getChildAt(i);
			if (button.isChecked()) {
				childitem = i;
				break;
			}
		}
		Fragment fragment=fragments.get(childitem);
		FragmentManager manager=getSupportFragmentManager();
		FragmentTransaction transaction=manager.beginTransaction();
		transaction.replace(R.id.main_content, fragment);
		transaction.commit();
	}

}
