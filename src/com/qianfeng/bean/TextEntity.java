package com.qianfeng.bean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * wen ben duan zi shi ti
 * @author hanyang
 *
 */
public class TextEntity {
	
	private int type;
	private long createTime;
	private int favoriteCount;
	
	/**
	 * 代表当前用户是否踩了，0代表没有，1代表踩了
	 */
	private int userBury;
	
	/**
	 * 代表当前用户是否赞了，0代表没有，1代表赞了
	 */
	private int userFavorite;
	
	/**
	 * 代表踩的个数
	 */
	private int buryCount;
	
	/**
	 * 用于第三方分享，提交的网址参数
	 */
	private String sharUrl;
	
	//TODO 分析这个字段的含义
	private int label;
	
	/**
	 * 文本段子的内容部分（完整的内容）
	 */
	private String content;
	
	/**
	 * 评论的个数
	 */
	private int commentCount;
	
	/**
	 * 状态，其中的可选值3需要分析是什么类型
	 */
	private int status;
	
	/**
	 * 状态描述信息<br/>
	 * 可选值<br/>
	 * <ul>
	 * <li>"已发表到论文列表"</li>
	 * </ul>
	 */
	private String statusDesc;
	
	/**
	 * 当前是否评论
	 */
	private int hasComments;
	
	// TODO 需要分析这个字段的含义
	private int goDetailCount;

	//TODO 需要去了解idgg到底是什么含义
	private int userDigg;
	
	/**
	 * digg的个数
	 */
	private int diggCount;
	
	/**
	 * 段子的ID，访问详情和评论时，用这个作为接口的参数
	 */
	private long groupId;
	
	/**
	 * 需要分析是什么含义，现在有两处地方出现
	 * 1、获取列表接口里面有一个level = 6
	 * 2、文本段子实体中，level = 4
	 */
	private int level;
	
	//TODO 分析含义
	private int repinCount;
	
	/**
	 * 是否repin了，0代表没有
	 */
	private int userRepin;
	
	/**
	 * 是否是热门评论
	 */
	private int hasHotComments;
	
	/**
	 * 内容分类类型，1文本，2图片
	 */
	private int categoryId;
	
	//TODO 需要去分析comments这个JSON数组中的内容是什么？
	
	/**
	 * 上线时间
	 */
	private long onlineTime;
	
	/**
	 * 显示时间
	 */
	private long displayTime;
	
	private UserEntity user;
	
	public void parseJson(JSONObject json)throws JSONException{
		if(json!=null){
			onlineTime = json.getLong("online_time");
			displayTime = json.getLong("display_time");
			
			JSONObject group = json.getJSONObject("group");
			createTime = group.getLong("create_time");
			favoriteCount = group.getInt("favorite_count");
			userBury = group.getInt("user_bury");
			userFavorite = group.getInt("user_favorite");
			buryCount = group.getInt("bury_count");
			sharUrl = group.getString("share_url");
			label = group.optInt("label",0);
			content = group.getString("content");
			commentCount = group.getInt("comment_count");
			status = group.getInt("status");
			hasComments = group.getInt("has_comments");
			goDetailCount = group.getInt("go_detail_count");
			statusDesc = group.getString("status_desc");
			
			JSONObject userObj = group.getJSONObject("user");
			user = new UserEntity();
			user.parseJson(userObj);
			
			userDigg = group.getInt("user_digg");
			groupId = group.getLong("group_id");
			level = group.getInt("level");
			repinCount = group.getInt("repin_count");
			diggCount = group.getInt("digg_count");
			hasHotComments = group.optInt("has_hot_comments",0);
			userRepin = group.getInt("user_repin");
			categoryId = group.getInt("category_id");
		}
	}

	public int getType() {
		return type;
	}

	public long getCreateTime() {
		return createTime;
	}

	public int getFavoriteCount() {
		return favoriteCount;
	}

	public int getUserBury() {
		return userBury;
	}

	public int getUserFavorite() {
		return userFavorite;
	}

	public int getBuryCount() {
		return buryCount;
	}

	public String getSharUrl() {
		return sharUrl;
	}

	public int getLabel() {
		return label;
	}

	public String getContent() {
		return content;
	}

	public int getCommentCount() {
		return commentCount;
	}

	public int getStatus() {
		return status;
	}

	public String getStatusDesc() {
		return statusDesc;
	}

	public int getHasComments() {
		return hasComments;
	}

	public int getGoDetailCount() {
		return goDetailCount;
	}

	public int getUserDigg() {
		return userDigg;
	}

	public int getDiggCount() {
		return diggCount;
	}

	public long getGroupId() {
		return groupId;
	}

	public int getLevel() {
		return level;
	}

	public int getRepinCount() {
		return repinCount;
	}

	public int getUserRepin() {
		return userRepin;
	}

	public int getHasHotComments() {
		return hasHotComments;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public long getOnlineTime() {
		return onlineTime;
	}

	public long getDisplayTime() {
		return displayTime;
	}

	public UserEntity getUser() {
		return user;
	}

}
