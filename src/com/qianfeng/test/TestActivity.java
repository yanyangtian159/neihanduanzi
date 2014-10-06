package com.qianfeng.test;




import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.qianfeng.bean.Comment;
import com.qianfeng.bean.CommentList;
import com.qianfeng.bean.EntityList;
import com.qianfeng.bean.TextEntity;
import com.qianfeng.neihan.R;
import com.qianfeng.neihan.client.ClientAPI;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

/**
 * 
 * @author hanyang
 * 
 */
public class TestActivity extends Activity implements Response.Listener<String>{
	private static final int CATEGORY_TEXT = 1;
	public static final int CATEGORY_IMAGE = 2;
	private RequestQueue queue;
	private Button button,button1;
	private long lastTime=0;
	long groupId=3551461874l;//对应文本段子的Id
	int offset=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_test);
		button=(Button)this.findViewById(R.id.testbutton1);
		button1=(Button)this.findViewById(R.id.testbutton2);
		final int itemCount = 30;
		queue = Volley.newRequestQueue(this);
		//ClientAPI.getList(queue,itemCount,0,CATEGORY_IMAGE,this);
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ClientAPI.getList(queue,itemCount,lastTime,CATEGORY_TEXT,TestActivity.this);
			}
		});
		button1.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			//ClientAPI.getList(queue,itemCount,lastTime,CATEGORY_TEXT,TestActivity.this);
			ClientAPI.GetComments(queue, groupId, offset,TestActivity.this);
		}
	});
		
		
		//ClientAPI.GetComments(queue,groupId, offset,this);
		
	}
	@Override
	public void onResponse(String arg0) {
		// TODO Auto-generated method stub
		try {
			JSONObject json=new JSONObject(arg0);
			arg0=json.toString(4);
			Log.d("TestActivity", "arg0:"+arg0);
		
			CommentList commentList=new CommentList();
			commentList.parseJson(json);
			// 包含两种数据 一个是热门评论  一个是新鲜评论  且这两者都有可能是空的
			
			long groupId=commentList.getGroupId();
			
			boolean hasMore=commentList.isHasMore();
			
			offset=offset+20;
			//评论总数
			int totalNumber=commentList.getTotalNumber();
			Log.d("TestActivity", "groupId:"+groupId);
			Log.d("TestActivity", "hasMore:"+hasMore);
			Log.d("TestActivity", "totalNumber:"+totalNumber);
			//热门评论列表，（可能为空，第一次为0时 可能有数据）
			List<Comment> topComments=commentList.getTopComments();
			
			//新鲜评论  可能有数据
			List<Comment> reComments=commentList.getRecentComments();
			Log.d("TestActivity", "topComments:"+topComments);
			Log.d("TestActivity", "reComments:"+reComments);
			//TODO 直接把commentsList提交给Listview的Adapter，这样可以经行是否还有内容的判断
			//利用Adapter更新数据
			
			//分页标识，需要服务器每次返回20条评论，通过hasmore来判断是否还需要分页
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.test, menu);
		return true;
	}

	//列表网络获取毁掉部分
	/**
	 * 
	 * @param arg0	
	 */
	public void listOnResponse(String arg0) {
		// TODO Auto-generated method stub
		
			// TODO Auto-generated method stub
			//Log.d("TestActivity", "list:" + arg0);
			try {
				JSONObject jsonObject=new JSONObject(arg0);
				arg0=jsonObject.toString(4);

				System.out.print("list:"+arg0);
				//获取根节点下面的data对象
				JSONObject obj=jsonObject.getJSONObject("data");
				//解析段子列表的完整数据 
				
				
				EntityList entityList=new EntityList();
				//这个方法是解析json的方法，其中报班的支持 图片、文本、广告的解析
				entityList.parseJson(obj);//相当于获取真实的数据
				//如果isHasMore 返回TRUE，代表还可以更新一次数据
				if(entityList.isHasMore()){
					lastTime = entityList.getMinTime();//获取更新时间标识
					Log.d("TestActivity", "lastTime:"+lastTime);
				}else{
					//没有更多的数据，休息一会
					String tip=entityList.getTip();
					Log.d("TestActivity", "tip:"+tip);
				}
				
				//获取内容段子列表
				//TODO 把entityList这个段子的数据集合体，传递给listvie之类的Adapter 即可显示。
				//List<TextEntity> entities=entityList.getEntities();
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
}
