package com.qianfeng.bean;

import org.json.JSONException;
import org.json.JSONObject;

public class DuanZiEntity {
	protected int type;

	protected long createTime;
	
	protected long onlineTime;//上线时间
	
	protected long displayTime;//显示时间
	
	protected int commentCount; //评论的个数
	
	protected int diggCount;//digg的个数
	
	protected int status;//状态，其中的可选值3  ，分析中。。。
	//TODO 需要去了解digg到底是什么含义
	protected int userDigg;
	
	protected long groupId;//段子的id，访问详情和评论时，用这个作为借口的参数
	
	protected int categoryId;//内容分类类型，1文本，2图片
	
	protected int buryCount;//cai de ge shu
	
	protected String content;//文本段子的内容（完整的内容）      换行问题
	
	protected int userRepin;//代表用户是否
	
	protected int userBury;//dai biao dang qian yong hu shu fou cai le 0 dai biao mei you 1dai biao cai le
	
	protected int label;//TODO 分析这个字段的含义
	
	protected int goDetailCount;//TODO需要分析这个字段的含义
	
	protected int hasComments;//当前用户是否评论
	
	protected String statusDesc;//状态的描述，可选值
	
	protected int favoriteCount;//zan de ge shu 
	
	protected int userFavorite;//dai baio de shi dang qian de yong hu shi fou zan le 0 dai biao mei you 1dai biao zan le 
	
	protected int level;
	
	protected int repinCount;//TODO 分析含义
	
	protected int hasHotComments;//是否含有热门评论
	
	protected String shareUrl;//用于第三方分享，提交的网址参数
	public void parseJson(JSONObject item) throws JSONException{
		type = item.getInt("type");
	}
}
