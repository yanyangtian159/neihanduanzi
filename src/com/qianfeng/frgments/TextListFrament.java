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
import com.qianfeng.adapter.EssayAdapter;
import com.qianfeng.bean.EntityList;
import com.qianfeng.bean.TextEntity;
import com.qianfeng.neihan.R;
import com.qianfeng.neihan.client.ClientAPI;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
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
		OnScrollListener, OnRefreshListener2<ListView> {
	private TextView textnotify;
	private View quickTools;
	private View header;
	private EssayAdapter adapter;
	private List<TextEntity> entities;
	private static final int CATEGORY_TEXT = 1;
	public static final int CATEGORY_IMAGE = 2;
	private RequestQueue queue;
	private long lastTime;
	private int requestCategory = CATEGORY_TEXT;

	public TextListFrament() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		if(queue==null){
			queue = Volley.newRequestQueue(getActivity());
			}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		if(savedInstanceState!=null){
			lastTime=savedInstanceState.getLong("lastTime");
			Log.d("TextFragment", "lastTime:::::"+lastTime);
		}
		
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_textlist, container,
				false);
		// huo qu biao ti kong jain 增加点即，进行 新消息悬浮窗显示的功能
		View titleView = view.findViewById(R.id.textlist_title);
		titleView.setOnClickListener(this);

		PullToRefreshListView refreshListView = (PullToRefreshListView) view
				.findViewById(R.id.textlist_listview);
		// 设置上拉与下拉的事件监听

		// OnRefreshListener2<listview>
		refreshListView.setOnRefreshListener(this);
		refreshListView.setMode(Mode.BOTH);

		ListView listView = refreshListView.getRefreshableView();

		// 添加列表上面的快速工具条（header）

		header = inflater.inflate(R.layout.textlist_header_tools, listView,
				false);
		listView.addHeaderView(header);

		View quickPublicView = header.findViewById(R.id.quick_tools_publish);

		quickPublicView.setOnClickListener(this);

		View quickReview = header.findViewById(R.id.quick_tools_review);

		quickReview.setOnClickListener(this);
		
		if(entities==null){
			
			entities = new ArrayList<TextEntity>();
			
			}
		
		adapter = new EssayAdapter(getActivity(), entities);

		listView.setAdapter(adapter);

		listView.setOnScrollListener(this);

		// ＴＯＤＯ　获取快速的工具条（发布审核）　用于列表滚动的娴熟和隐藏

		quickTools = view.findViewById(R.id.textlist_quick_tools);

		quickTools.setVisibility(View.VISIBLE);
		// she zhi xuan fu de gong ju tiao laing ge ming ling shi jian
		quickPublicView = quickTools.findViewById(R.id.quick_tools_publish);

		quickPublicView.setOnClickListener(this);

		quickReview = quickTools.findViewById(R.id.quick_tools_review);

		quickReview.setOnClickListener(this);
		// ＴＯＤＯ　获取新的
		// 新消息提醒
		textnotify = (TextView) view.findViewById(R.id.textlist_new_notify);
		textnotify.setVisibility(View.INVISIBLE);
		return view;
	}

	// 列表滚动显示工具条
	private int lastIndex = 0;

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		int offset = lastIndex - firstVisibleItem;
		

		if (offset < 0 || firstVisibleItem == 0) {
			if (quickTools != null) {
				quickTools.setVisibility(View.INVISIBLE);
			}

		} else if (offset > 0) {

			quickTools.setVisibility(View.VISIBLE);

			
		}
		lastIndex = firstVisibleItem;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub

	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			int what = msg.what;
			if (what == 1) {
				// 待表有新消息提醒
				textnotify.setVisibility(View.INVISIBLE);
			}
		};
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		switch (id) {
		case R.id.textlist_title:
			textnotify.setVisibility(View.VISIBLE);
			// handler.obtainMessage(1);
			handler.sendEmptyMessageDelayed(1, 3000);
			break;

		default:
			break;
		}
	}
	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		outState.putLong("lastTime", lastTime);
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
		this.adapter=null;
		this.header=null;
		this.quickTools=null;
		this.textnotify=null;
	}

	// ///////////////////////////////////////////////////////////////////////////
	// 列表下拉刷新，上拉加载
	/**
	 * 这个方法，在Volley联网响应返回的时候调用，它是在朱先生执行的，直接更新ui
	 * 
	 * @param arg0
	 */
	public void listOnResponse(String arg0) {
		// TODO Auto-generated method stub

		try {
			JSONObject jsonObject = new JSONObject(arg0);

			// 获取根节点下面的data对象
			JSONObject obj = jsonObject.getJSONObject("data");
			// 解析段子列表的完整数据

			EntityList entityList = new EntityList();
			// 这个方法是解析json的方法，其中报班的支持 图片、文本、广告的解析
			entityList.parseJson(obj);// 相当于获取真实的数据
			// 如果isHasMore 返回TRUE，代表还可以更新一次数据
			if (entityList.isHasMore()) {
				lastTime = entityList.getMinTime();// 获取更新时间标识
				Log.i("TextFragment", "lastTime"+lastTime);

			} else {
				// 没有更多的数据，休息一会
				String tip = entityList.getTip();
				
			}

			// 获取内容段子列表
			// TODO 把entityList这个段子的数据集合体，传递给listvie之类的Adapter 即可显示。
			List<TextEntity> ets = entityList.getEntities();
			if (ets != null) {
				if (!ets.isEmpty()) {
					// 把ets中内容按照迭代器的顺序添加，需要验证一下
					entities.addAll(0, ets);
					int len = ets.size();
					for (int index = len - 1; index > 0; index--) {
						// 把objext 添加到制定的位置，原有location以及以后的内容往后移动

						entities.add(0, ets.get(index));
					}
					adapter.notifyDataSetChanged();
				} else {
					// TODO没有更多的数据，需要提示一下
				}
			} else {
				// 没有获取到网络数据，可能市局解析错误，或网络错误，提示一下。
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 从上向下拉动列表，那么就要进行加载新的数据
	 * 
	 */
	@Override
	public void onPullDownToRefresh(
			final PullToRefreshBase<ListView> refreshView) {
		// TODO Auto-generated method stub
		ClientAPI.getList(queue, 30, lastTime, requestCategory,
				new Response.Listener<String>() {

					@Override
					public void onResponse(String arg0) {
						// TODO 加载新数据
						refreshView.onRefreshComplete();
						listOnResponse(arg0);
					}
				});
	}

	/**
	 * 从下往上拉动，那么就要考虑是否加载旧的数据
	 */
	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO Auto-generated method stub

	}

	// /////////////////////////////////////////////////////////////////////////////////
	
}
