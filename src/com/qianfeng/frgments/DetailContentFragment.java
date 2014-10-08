package com.qianfeng.frgments;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import pl.droidsonroids.gif.GifImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.Volley;
import com.qianfeng.adapter.CommentAdapter;
import com.qianfeng.bean.Comment;
import com.qianfeng.bean.CommentList;
import com.qianfeng.bean.TextEntity;
import com.qianfeng.bean.UserEntity;
import com.qianfeng.neihan.R;
import com.qianfeng.neihan.client.ClientAPI;

import android.os.Bundle;
import android.provider.CalendarContract.Instances;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

public class DetailContentFragment extends Fragment implements OnTouchListener, Listener<String>{
private TextEntity entity;
	
	private ScrollView scrollView;

	private TextView txtHotCommentCount;

	private TextView txtRecentCommentCount;
	
	private RequestQueue queue;
	
	public DetailContentFragment() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if(entity == null){
			Bundle arguments = getArguments();
			Serializable serializable = arguments.getSerializable("entity");
			if(serializable instanceof TextEntity){
				entity = (TextEntity) serializable;
			}
		}
		
		queue = Volley.newRequestQueue(getActivity());
		
	}
	
	private CommentAdapter hotAdapter;
	
	private CommentAdapter recentAdapter;
	
	private List<Comment> hotComments;
	
	private List<Comment> recentComments;
	
	private int offset;

	private boolean hasMore;

	private long groupId;
	
	private LinearLayout scrollContent;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			 ViewGroup container,  Bundle savedInstanceState) {
		View ret = inflater.inflate(R.layout.fragment_detail_content, container, false);
		
		scrollView = (ScrollView)ret.findViewById(R.id.detail_scroll);
		
		scrollView.setOnTouchListener(this);
		
		scrollContent = (LinearLayout) ret.findViewById(R.id.scroll_count);
		
		// 设置主体内容
		setEssayContent(ret);

		txtHotCommentCount = (TextView)ret.findViewById(R.id.text_hot_comments_count);
		
		hotCommentListView = (ListView) ret.findViewById(R.id.hot_comments_list);
		
		hotComments = new LinkedList<Comment>();
		
		
		hotAdapter = new CommentAdapter(getActivity(), hotComments);
		
		hotCommentListView.setAdapter(hotAdapter);
		
		txtRecentCommentCount = (TextView)ret.findViewById(R.id.text_recent_comments_count);
		
		recentCommentListView = (ListView) ret.findViewById(R.id.recent_comments_list);
		
		recentComments = new LinkedList<Comment>();
		
		recentAdapter = new CommentAdapter(getActivity(), recentComments);
		
		recentCommentListView.setAdapter(recentAdapter);
		
		groupId = entity.getGroupId();
		
		ClientAPI.GetComments(queue, groupId, 0, this);
		
		return ret;
	}

	/**
	 * 设置段子主体内容（详情）
	 * @param ret
	 */
	private void setEssayContent(View ret) {
		// 1. 先设置文本内容的数据
		TextView btnGifPlay = (TextView) ret.findViewById(R.id.btnGifPlay);
		ImageButton btnShare = (ImageButton) ret.findViewById(R.id.item_share);
		CheckBox chbBuryCount = (CheckBox) ret.findViewById(R.id.item_bury_count);
		CheckBox chbDiggCount = (CheckBox) ret.findViewById(R.id.item_digg_count);
		GifImageView gifImageView = (GifImageView) ret.findViewById(R.id.gifview);
		ImageView imgProfileImage = (ImageView) ret.findViewById(R.id.item_profile_iamge);
		ProgressBar pbDownloadProgress = (ProgressBar) ret.findViewById(R.id.item_image_download_progress);
		TextView txtCommentCount = (TextView) ret.findViewById(R.id.item_commente_count);
		TextView txtContent = (TextView)ret.findViewById(R.id.item_content);
		TextView txtProfileNick = (TextView) ret.findViewById(R.id.item_profile_nick);				
		
		UserEntity user = entity.getUser();
		String nick = "";
		if(user != null){
			nick = user.getName();
		}
		txtProfileNick.setText(nick);
		
		String content = entity.getContent();
		txtContent.setText(content);
		
		int diggCount = entity.getDiggCount();
		
		chbDiggCount.setText(Integer.toString(diggCount));
		
		int userDigg = entity.getUserDigg(); // 当前用户是否赞过

//		chbDiggCount.setEnabled(userDigg == 1 ? false : true);
		// 如果userDigg 是1的话，代表了，已经赞过，那么 chbDiggCount 必须禁用 所以用 != 1
		chbDiggCount.setEnabled(userDigg != 1);
		
		int buryCount = entity.getBuryCount();
		
		chbBuryCount.setText(Integer.toString(buryCount));
		
		int userBury = entity.getUserBury();
		// 如果userBury 是1的话，代表了，已经踩过，那么 chbBuryCount 必须禁用 所以用 != 1
		chbBuryCount.setEnabled(userBury != 1);
		
		int commentCount = entity.getCommentCount();
		
		txtCommentCount.setText(Integer.toString(commentCount));
		
		// 2. 设置图片的数据
		
		// TODO 需要加载各种图片数据
	}
	
	private boolean hasMove = false;

	private ListView hotCommentListView;

	private ListView recentCommentListView;

	/**
	 * 处理ScrollView 触摸事件，用于在ScrollView滚动到最下面的时候，自动加载数据。
	 */
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		boolean bret = false;
		
		int action = event.getAction();
		if(action == MotionEvent.ACTION_DOWN){
			bret = true;
			hasMove = false;
		}else if(action == MotionEvent.ACTION_MOVE){
			hasMove = true;
		}else if(action == MotionEvent.ACTION_UP){
			if(hasMove){
				int sy = scrollView.getScrollY();
				int mh = scrollView.getMeasuredHeight();
				int ch = scrollContent.getHeight();
				
				if(sy + mh >= ch){
					// TODO 进行评论分页加载
					
					ClientAPI.GetComments(queue, groupId, offset, this);
					
				}
				
			}
		}
		
		return bret;
	}

	@Override
	public void onResponse(String arg0) {
		// TODO Auto-generated method stub
		try {
			JSONObject json = new JSONObject(arg0);
			arg0 = json.toString(4);

			Log.d("TestActivity", "Comment List JSON: " + arg0);

			// 解析获取到的评论列表
			CommentList commentList = new CommentList();
			// 评论列表包含两组数据，一个是 热门评论，一个是新鲜评论
			// 热门评论和新鲜评论都有可能是空的
			commentList.parseJson(json);

//			long groupId = commentList.getGroupId();

			hasMore = commentList.isHasMore();

			// 评论总数
			int totalNumber = commentList.getTotalNumber();

			// 热门评论列表（可能为空，第一次 offset 为0时可能有数据）
			List<Comment> topComments = commentList.getTopComments();
			
			// 新鲜评论，可能有数据
			List<Comment> rtComments = commentList.getRecentComments();

			if(topComments != null){
				hotComments.addAll(topComments);
				hotAdapter.notifyDataSetChanged();
			}
			
			if(rtComments != null){
				recentComments.addAll(rtComments);
				recentAdapter.notifyDataSetChanged();
			}
			
			// TODO 直接把 CommentList 提交给 ListView 的Adapter，这样可以进行是否还有内容的判断
			// 利用Adapter更新数据
			
			// 分页标识，要求服务器每次返回20条评论，通过 hasMore 来进行判断是否还需要分页
			offset += 20;
			
			// TODO 扩充ListView的内容
			
			hotCommentListView.requestLayout();
			recentCommentListView.requestLayout();

			int childCount = hotCommentListView.getChildCount();
			if(childCount > 0){
				int totalHeight = 0;
				for(int i = 0;i<childCount;i++){
					View view = hotCommentListView.getChildAt(i);
					totalHeight += view.getHeight();
				}
				ViewGroup.LayoutParams ps = hotCommentListView.getLayoutParams();
				ps.height = totalHeight;
				hotCommentListView.setLayoutParams(ps);
			}
			
			childCount = recentCommentListView.getChildCount();
			if(childCount > 0){
				int totalHeight = 0;
				for(int i = 0;i<childCount;i++){
					View view = recentCommentListView.getChildAt(i);
					totalHeight += view.getHeight();
				}
				ViewGroup.LayoutParams ps = recentCommentListView.getLayoutParams();
				ps.height = totalHeight;
				recentCommentListView.setLayoutParams(ps);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
