package com.qianfeng.adapter;

import java.util.List;

import pl.droidsonroids.gif.GifImageView;

import com.qianfeng.bean.TextEntity;
import com.qianfeng.bean.UserEntity;
import com.qianfeng.neihan.R;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class EssayAdapter extends BaseAdapter {
	private Context context;
	private List<TextEntity> entities;
	private LayoutInflater inflater;
	public EssayAdapter(Context context,List<TextEntity> entities){
		this.context=context;
		this.entities=entities;
		inflater=LayoutInflater.from(context);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return entities.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return entities.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		View ret=arg1;
		if(arg1==null){
			ret=inflater.inflate(R.layout.item_essay, arg2,false);
			if(ret!=null){
			ViewHolder holder=(ViewHolder) ret.getTag();
			if(holder==null){
				holder=new  ViewHolder();
				holder.btnGifPlay=(TextView)ret.findViewById(R.id.btnGifPlay);
				holder.btnShare=(ImageButton)ret.findViewById(R.id.item_share);
				holder.chbBuryCount=(CheckBox)ret.findViewById(R.id.item_bury_count);
				holder.chbDiggCount=(CheckBox)ret.findViewById(R.id.item_digg_count);
				holder.gifImageView=(GifImageView)ret.findViewById(R.id.gifview);
				holder.imgProfileImage=(ImageView)ret.findViewById(R.id.item_profile_iamge);
				holder.pbDownlodProgress=(ProgressBar)ret.findViewById(R.id.item_image_download_progress);
				holder.textCommentCount=(TextView)ret.findViewById(R.id.item_commente_count);
				holder.textContent=(TextView)ret.findViewById(R.id.item_content);
				holder.textProfileNick=(TextView)ret.findViewById(R.id.item_profile_nick);
				ret.setTag(holder);
				}
			TextEntity entity=entities.get(arg0);
			//先设置文本内容的数据，
			UserEntity user=entity.getUser();
			String nick="";
			if(user!=null){
				nick=user.getName();
				
			}
			holder.textProfileNick.setText(nick);
			
			String comment=entity.getContent();
			holder.textContent.setText(comment);
			int diggCount=entity.getDiggCount();
			
			
			holder.chbDiggCount.setText(diggCount+"");
			
			int usrDigg=entity.getUserDigg();
			//如果userdigg是1 的话，代表已经赞过，那么禁用，所以！=1
			holder.chbDiggCount.setEnabled(usrDigg!=1);
			
			int buryCount=entity.getBuryCount();
			
			holder.chbBuryCount.setText(buryCount+"");
			
			int userBury=entity.getUserBury();
			//如果userBury是1 的话，代表已经cai过，那么禁用，所以！=1
			holder.chbBuryCount.setEnabled(userBury!=1);
			
			int commentCount=entity.getCommentCount();
			
			holder.textCommentCount.setText(commentCount+"");
			
			//在设置图片的数据
			//TODO 需要加载各种图片信息
			}
		}
		return ret;
	}
	private static class ViewHolder{
	public ImageView imgProfileImage;
	/**
	 * 文本北融
	 */
	public TextView textProfileNick;
	
	public TextView textContent;
	
	/**
	 * 下载进度
	 */
	public ProgressBar pbDownlodProgress;
	public GifImageView gifImageView;
	public TextView btnGifPlay;
	/**
	 * 赞，包含赞的个数，如果赞过了，旧金庸控件
	 */
	public CheckBox chbDiggCount;
	public CheckBox chbBuryCount;
	/**
	 * 评论的个数
	 */
	public TextView textCommentCount;
	public ImageButton btnShare;
	}
}
