package com.qianfeng.bean;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class EntityList {
	private boolean hasMore;
	private long minTime;
	private String tip;
	private long maxTime;
	
	private List<TextEntity> entities;
	
	public void parseJson(JSONObject json) throws JSONException {
		if (json != null) {
			hasMore = json.getBoolean("has_more");// shi fou hai yao geng duo
			tip = json.getString("tip");
			if(hasMore==true){
				minTime = json.getLong("min_time");
			}
			maxTime = json.optLong("max_time");
			JSONArray jsonArray = json.getJSONArray("data");
			int len = jsonArray.length();
			if (len > 0) {
				entities=new LinkedList<TextEntity>();
				for (int i = 0; i < len; i++) {
					JSONObject item = jsonArray.getJSONObject(i);
					int type = item.getInt("type");// 1 shi duanzi 5 shi guang
													// gao
					if (type == 5) {

						AdEntity entity = new AdEntity();
						entity.parseJson(item);
						String DownLoadUrl = entity.getDownloadUrl();
						Log.i("TestActivity", "------------------->>"
								+ DownLoadUrl);
						// guang gao
					} else if (type == 1) {
						// duan zi
						JSONObject group = item.getJSONObject("group");
						int cid = group.getInt("category_id");
						TextEntity entity = null;
						if (cid == 1) {
							// jie xi wen ben
							entity = new TextEntity();

						} else if (cid == 2) {
							// jie xi tu pain
							entity = new ImageEntity();
						}
						entity.parseJson(item);
						entities.add(entity);
						long groupId = entity.getGroupId();
						Log.i("TestActivity", "------------------->>" + groupId);
					}

				}
			}
		}
	}
	public boolean isHasMore() {
		return hasMore;
	}
	public long getMinTime() {
		return minTime;
	}
	public String getTip() {
		return tip;
	}
	public long getMaxTime() {
		return maxTime;
	}
	public List<TextEntity> getEntities() {
		return entities;
	}
	
	
}
