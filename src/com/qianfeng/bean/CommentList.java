package com.qianfeng.bean;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 评论接口放回的 data：{}数据部分的实体定义
 * 包含了top_commnents和recent_comments两个数组
 * json格式如下：<br/>
 * <pre>
 * {
 * 	data:{
 * 		top_commnents:[],
 * 		recent_comments:[]
 * 		
 * 			}
 * 
 * }
 * </pre>
 * 
 * @author hanyang
 *
 */
public class CommentList {
	private List<Comment> topComments;
	private List<Comment> recentComments;
	private long groupId;
	private int totalNumber;
	private boolean hasMore;
	public void parseJson(JSONObject json) throws JSONException {
		if(json!=null){
			groupId=json.getLong("group_id");
			totalNumber=json.getInt("total_number");
			hasMore=json.getBoolean("has_more");
			
			
			JSONObject data=json.getJSONObject("data");
			
			
			
			JSONArray tArray=data.optJSONArray("top_commnents");
			
			if(tArray!=null){
				topComments=new LinkedList<Comment>();
				int len=tArray.length();
				if(len>0){
					for(int index=0;index<len;index++){
						JSONObject obj=tArray.getJSONObject(index);
						Comment comment=new Comment();
						comment.parseJson(obj);
						topComments.add(comment);
					}
				}
			}
			//JSONArray rArray=json.optJSONArray("recent_comments");
			
				JSONArray rArray=data.optJSONArray("recent_comments");
				
				if(rArray!=null){
					recentComments=new LinkedList<Comment>();
					int len=rArray.length();
					if(len>0){
						for(int index=0;index<len;index++){
							JSONObject obj=rArray.getJSONObject(index);
							Comment comment=new Comment();
							comment.parseJson(obj);
							recentComments.add(comment);
						}
					}
				}
			}
		}
	

	public List<Comment> getTopComments() {
		return topComments;
	}

	public List<Comment> getRecentComments() {
		return recentComments;
	}


	public long getGroupId() {
		return groupId;
	}


	public int getTotalNumber() {
		return totalNumber;
	}


	public boolean isHasMore() {
		return hasMore;
	}
	
}
