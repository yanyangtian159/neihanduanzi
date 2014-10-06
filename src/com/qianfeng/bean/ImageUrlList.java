package com.qianfeng.bean;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class ImageUrlList {
	
	private List<String> largeImageUrls;
	private String uri;
	private int width;
	private int height;
	public void parseJson(JSONObject json) throws JSONException{
		largeImageUrls = pathImageUrlList(json);
		//Log.i("ImageUrlList", "---------------largeImageUrls---"+largeImageUrls.toString());
		uri = json.getString("uri");
		width = json.getInt("width");
		height = json.getInt("height");
	}
	
	public List<String> getLargeImageUrls() {
		return largeImageUrls;
	}

	public String getUri() {
		return uri;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	private List<String> pathImageUrlList(JSONObject largeimage) throws JSONException {
		JSONArray urls=largeimage.getJSONArray("url_list");
		List<String> largeImageUrls=new LinkedList<String>();
		int ulen=urls.length();
		for(int j=0;j<ulen;j++){
			JSONObject uobj=urls.getJSONObject(j);
			String url=uobj.getString("url");
			largeImageUrls.add(url);
		}
		return largeImageUrls;
	}
}
