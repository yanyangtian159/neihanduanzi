package com.qianfeng.bean;

import org.json.JSONException;
import org.json.JSONObject;

public class UserEntity {
	
	private String avatarUrl;//头像网址
	/**
	 * 用户id
	 */
	private long userId;
	/**
	 * 用户名称（昵称）
	 */
	private String name;
	/**
	 * 用户是否实名认证，即加V
	 */
	private boolean userUerified;
	/**
	 * "user": {
                        "avatar_url": "http://p1.pstatp.com/thumb/1367/2213311454",
                        "user_id": 3080520868,
                        "name": "请叫我梓安哥",
                        "user_verified": false
                    },
	 */
	public void parseJson(JSONObject json) throws JSONException{
		if(json!=null){
			this.avatarUrl=json.getString("avatar_url");
			this.userId=json.getLong("user_id");
			this.name=json.getString("name");
			this.userUerified=json.getBoolean("user_verified");
		}
	}
}
