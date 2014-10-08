package com.qianfeng.frgments;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.qianfeng.actiivities.EssayDetialActivity;
import com.qianfeng.adapter.EssayAdapter;
import com.qianfeng.bean.DataStore;
import com.qianfeng.bean.EntityList;
import com.qianfeng.bean.TextEntity;
import com.qianfeng.neihan.R;
import com.qianfeng.neihan.client.ClientAPI;

import android.R.integer;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 1、 列表界面，第一次启动，并且数据为空时，自动加载数据 2、只要列表没有数据，进入这个界面，就尝试加载数据 3、只要有数据旧不进行数据的加载
 * 4、进入这个界面，并且有数据的情况下，查实检查新信息的个数，
 * 
 * @author hanyang
 * 
 */
public class TextListFrament extends Fragment implements OnClickListener,
		OnScrollListener, OnRefreshListener2<ListView>, OnItemClickListener {
	private View quickTools;
	private TextView textNotifiy;
	
	private EssayAdapter adapter;
	
//	private List<TextEntity> entities;
	
	/**
	 * 分类ID, 1 代表文本
	 */
	public static final int CATEGORY_TEXT = 1;

	/**
	 * 分类ID, 2 代表图片
	 */
	public static final int CATEGORY_IMAGE = 2;

	private RequestQueue queue;

	private long lastTime;
	
	/**
	 * 请求的分类类型ID
	 */
	private int requestCategory = CATEGORY_TEXT;

	public TextListFrament() {
		
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		if(queue == null){
			queue = Volley.newRequestQueue(getActivity());
		}
		
		if(savedInstanceState != null){
			lastTime = savedInstanceState.getLong("lastTime");
			Log.d("TextListFragment", "Reload state: lastTime: " + lastTime);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			 ViewGroup container,  Bundle savedInstanceState) {

		
		
		
		View view = inflater.inflate(R.layout.fragment_textlist, container,
				false);
		
		// 获取标题控件，增加点击，进行 新消息悬浮框显示的功能
		
		View titleView = view.findViewById(R.id.textlist_title);
		titleView.setOnClickListener(this);

		// TODO 获取ListView并且设置数据

		
		PullToRefreshListView refreshListView = 
				(PullToRefreshListView)view.findViewById(R.id.textlist_listview);
				
		// 设置上拉与下拉的事件监听
//		OnRefreshListener2<ListView>
		refreshListView.setOnRefreshListener(this);
		
		refreshListView.setMode(Mode.BOTH);
		
		ListView listView = refreshListView.getRefreshableView();
		
		

		
		header = inflater.inflate(R.layout.textlist_header_tools, listView, false);
		listView.addHeaderView(header);

		View quickPublish = header.findViewById(R.id.quick_tools_publish);
		quickPublish.setOnClickListener(this);
		quickPublish.setFocusable(false);
		

		View quickReview = header.findViewById(R.id.quick_tools_review);
		quickReview.setOnClickListener(this);
		quickReview.setFocusable(false);
		
		
		List<TextEntity> entities = DataStore.getoutInstance().getTextEntities();
		
		
		adapter = new EssayAdapter(getActivity(), entities);
		
		adapter.setListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(v instanceof TextView){
					String string = (String)v.getTag();
					
					if(string != null){
						
						int position = Integer.parseInt(string);
						
						Intent intent = new Intent(getActivity(), EssayDetialActivity.class);
						
						intent.putExtra("currentEssayPosition", position);
						
						intent.putExtra("category", requestCategory);
						
						startActivity(intent);
					}
					
				}
			}
		});
		
		listView.setAdapter(adapter);
		
		listView.setOnScrollListener(this);
		
		listView.setOnItemClickListener(this);

		// TODO 获取 快速的工具条（发布和审核），用于列表滚动的显示和隐藏
		
		quickTools = view.findViewById(R.id.textlist_quick_tools); 
		quickTools.setVisibility(View.INVISIBLE);
		
		// 设置 悬浮的工具条 两个 命令的事件
		quickPublish = quickTools.findViewById(R.id.quick_tools_publish);
		quickPublish.setOnClickListener(this);
		

		quickReview = quickTools.findViewById(R.id.quick_tools_review);
		quickReview.setOnClickListener(this);

		// 新消息提醒
		textNotifiy = (TextView)view.findViewById(R.id.textlist_new_notify);//textlist_new_notifiy
		textNotifiy.setVisibility(View.INVISIBLE);
		
		

		return view;
	}
	
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;
			if(what == 1){
				// TODO what = 1 代表有新消息提醒
				textNotifiy.setVisibility(View.INVISIBLE);
			}
		}
		
	};

	/////////////////////////////////////////////////////
	//   列表 滚动，显示 工具条
	
	private int lastIndex = 0;
	private View header;
	
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		int offset = lastIndex - firstVisibleItem;
		
		if(offset < 0 || firstVisibleItem == 0){
			// 证明现在移动是向上移动
			if(quickTools != null){
				quickTools.setVisibility(View.INVISIBLE);
			}
		}else if(offset > 0){
			if(quickTools != null){
				quickTools.setVisibility(View.VISIBLE);
			}
		}
		lastIndex = firstVisibleItem;
	}
	
	
	/////////////////////////////////////////////////////

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		
		switch(id){
			case R.id.textlist_title:
				textNotifiy.setVisibility(View.VISIBLE);
//				Message handler.obtainMessage(1);
				handler.sendEmptyMessageDelayed(1, 3000);
				break;
		}
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		Log.d("TextListFragment", "Save state: lastTime: " + lastTime);
		outState.putLong("lastTime", lastTime);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		
		this.adapter = null;
		
		this.header = null;
		
		this.quickTools = null;
		
		this.textNotifiy = null;
		
	}
	
	/////////////////////////////////////////////////
	// 列表下拉刷新与上拉加载
	
	/**
	 * 列表网络获取回调部分，这个方法，是在Volley联网响应返回的时候调用，
	 * 它是在主线程执行的，因此可以直接更新UI
	 * 
	 * @param arg0 列表JSON数据字符串
	 */
	public void listOnResponse(String arg0) {
		try {
			JSONObject json = new JSONObject(arg0);

			// 获取 根节点下面的 data 对象
			JSONObject obj = json.getJSONObject("data");

			// 解析段子列表的完整数据
			EntityList entityList = new EntityList();
			// 这个方法是解析JSON的方法，其中包含的支持 图片、文本、广告的解析
			entityList.parseJson(obj); // 相当于获取数据内容

			// 如果 isHasMore 返回 true, 代表还可以更新一次数据
			if (entityList.isHasMore()) {
				lastTime = entityList.getMinTime(); // 获取更新时间标识
			} else { // 没有更多数据了，休息一会儿
				String tip = entityList.getTip();
				Log.d("TestActivity", "Tip = " + tip);
			}

			// 获取段子内容列表

			// TODO 把 entityList 这个段子的数据集合体，传递给 ListView 之类的 Adapter 即可显示
			
			List<TextEntity> ets = entityList.getEntities();
			
			if(ets != null){
				if(!ets.isEmpty()){
					// 把ets中内容按照迭代器的顺序添加，需要验证一下。
					
					DataStore.getoutInstance().addTextEntities(ets);
					
//					entities.addAll(0, ets);
					
					// 手动添加
//					int len = ets.size();
//					for(int index = len - 1; index >= 0; index--){
//						// 把object添加到指定的location位置，原有location以及以后的内容，向后移动
//						entities.add(0, ets.get(index));
//					}
					
					adapter.notifyDataSetChanged();
					
				}else{
					// TODO 没有更多的数据了，需要提示一下
				}
			}else{
				// TODO 没有获取到网络数据，可能是数据解析错误、网络错误，需要提示一下
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 从上向下拉动列表，那么就要进行加载新数据的操作
	 */
	@Override
	public void onPullDownToRefresh(final PullToRefreshBase<ListView> refreshView) {
		
		ClientAPI.getList(
				queue,
				30,
				
				
				lastTime,
				requestCategory,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String arg0) {
						// TODO 加载新数据，
						refreshView.onRefreshComplete();
						listOnResponse(arg0);
						
					}
				}
			);
	}

	/**
	 * 从下向上拉动，那么就要考虑是否进行加载旧的数据
	 */
	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
		position--;
		
		Intent intent = new Intent(getActivity(), EssayDetialActivity.class);
		
		intent.putExtra("currentEssayPosition", position);
		
		intent.putExtra("category", requestCategory);
		
		startActivity(intent);
	}

}
