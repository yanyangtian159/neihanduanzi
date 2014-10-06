package com.qianfeng.frgments;

import java.util.ArrayList;
import java.util.List;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.qianfeng.neihan.R;

import android.R.integer;
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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class TextListFrament extends Fragment 
implements OnClickListener,OnScrollListener,OnRefreshListener2<ListView>{
	private TextView textnotify;
	private View quickTools;
	private View header;
	public TextListFrament() {
		// TODO Auto-generated constructor stub
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view=inflater.inflate(R.layout.fragment_textlist, container,false);
		// huo qu biao ti kong jain 增加点即，进行 新消息悬浮窗显示的功能
		View titleView=view.findViewById(R.id.textlist_title);
		titleView.setOnClickListener(this);
		
		
		PullToRefreshListView refreshListView=(PullToRefreshListView)view.findViewById(R.id.textlist_listview);
		//设置上拉与下拉的事件监听
		
		//OnRefreshListener2<listview>
		refreshListView.setOnRefreshListener(this);
		refreshListView.setMode(Mode.BOTH);
		
		ListView listView=refreshListView.getRefreshableView();
		
		List<String> list=new ArrayList<String>();
		list.add("java");
		list.add("da");
		list.add("daw");
		list.add("ada");
		list.add("javfaa");
		list.add("javdaa");
		list.add("jasava");
		list.add("wew");
		list.add("cacac");
		list.add("jadadva");
		list.add("jaacava");
		list.add("jaaava");
		list.add("jacava");
		list.add("javgfha");
		list.add("javytytwewewa");
		list.add("jawfsddfwva");
		list.add("javewewea");
		list.add("jwdava");
		list.add("jadqdwqrrva");
		list.add("jatrtrta");
		//添加列表上面的快速工具条（header）
		
		header=inflater.inflate(R.layout.textlist_header_tools, listView, false);
		listView.addHeaderView(header);
		
		View quickPublicView=header.findViewById(R.id.quick_tools_publish);
		
		quickPublicView.setOnClickListener(this);
		
		View quickReview=header.findViewById(R.id.quick_tools_review);
		
		quickReview.setOnClickListener(this);
		
		ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1,list);
		listView.setAdapter(adapter);
		
		listView.setOnScrollListener(this);
		
		
		//ＴＯＤＯ　获取快速的工具条（发布审核）　用于列表滚动的娴熟和隐藏
		
		quickTools=view.findViewById(R.id.textlist_quick_tools);
		
		quickTools.setVisibility(View.VISIBLE);
		// she zhi xuan fu de gong ju tiao laing ge ming ling shi jian
		 quickPublicView=quickTools.findViewById(R.id.quick_tools_publish);
		
		quickPublicView.setOnClickListener(this);
		
		 quickReview=quickTools.findViewById(R.id.quick_tools_review);
		
		quickReview.setOnClickListener(this);
		//ＴＯＤＯ　获取新的
		//新消息提醒
		textnotify=(TextView)view.findViewById(R.id.textlist_new_notify);
		textnotify.setVisibility(View.INVISIBLE);
		return view;
	}
	//列表滚动显示工具条
	private int lastIndex=0;
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		int offset=lastIndex-firstVisibleItem;
		Log.d("mainActivity", "firstVisibleItem:"+firstVisibleItem);
		
		if(offset<0||firstVisibleItem==0){
			if(quickTools!=null){
				quickTools.setVisibility(View.INVISIBLE);
			}
			
		}else if(offset>0){
			
				quickTools.setVisibility(View.VISIBLE);
			
//			if(header.getVisibility()==view.VISIBLE){
//				header.setVisibility(View.INVISIBLE);
//			}
		}
		lastIndex=firstVisibleItem;
	}
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	
	private Handler handler=new Handler(){
		@Override
		public void handleMessage(android.os.Message msg) {
			int what=msg.what;
			if(what==1){
				//待表有新消息提醒
				textnotify.setVisibility(View.INVISIBLE);
			}
		};
	};
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id=v.getId();
		switch (id) {
		case R.id.textlist_title:
			textnotify.setVisibility(View.VISIBLE);
			//handler.obtainMessage(1);
			handler.sendEmptyMessageDelayed(1, 3000);
			break;

		default:
			break;
		}
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
	}
	/////////////////////////////////////////////////////////////////////////////
	//列表下拉刷新，上拉加载
	/**
	 * 从上向下拉动列表，那么就要进行加载新的数据
	 */
	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 从下往上拉动，那么就要考虑是否加载旧的数据
	 */
	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO Auto-generated method stub
		
	}

	///////////////////////////////////////////////////////////////////////////////////
}
