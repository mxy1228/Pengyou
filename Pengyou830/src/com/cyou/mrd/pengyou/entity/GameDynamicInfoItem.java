package com.cyou.mrd.pengyou.entity;

public class GameDynamicInfoItem {
    private Integer id;
    private Integer pid = Integer.valueOf(-1);//人工赋予的一个不同值的ID
    private String content;//发布动态文字内容
    private String picture;//发布动态图片
    private Long date;//发布动态时间
    private Integer status;//发布的这条动态的状态 1：发布成功，0：发布失败
    private float score ; //用户评分
    private String gamecode ; //
    private String gameCircleId;
    private Integer uid;
    private Integer width;
    private Integer height;
    
    public String getGamecode() {
		return gamecode;
	}
	public void setGamecode(String gamecode) {
		this.gamecode = gamecode;
	}
    
    public float getScore() {
		return score;
	}
	public void setScore(float score) {
		this.score = score;
	}
	public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    
    public Integer getPid() {
        return pid;
    }
    public void setPid(Integer pid) {
        this.pid = pid;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getPicture() {
        return picture;
    }
    public void setPicture(String picture) {
        this.picture = picture;
    }
    public Long getDate() {
        return date;
    }
    public void setDate(Long date) {
        this.date = date;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
	public String getGameCircleId() {
		return gameCircleId;
	}
	public void setGameCircleId(String gameCircleId) {
		this.gameCircleId = gameCircleId;
	}
	public Integer getUid() {
		return uid;
	}
	public void setUid(Integer uid) {
		this.uid = uid;
	}
	public Integer getWidth() {
		return width;
	}
	public void setWidth(Integer width) {
		this.width = width;
	}
	public Integer getHeight() {
		return height;
	}
	public void setHeight(Integer height) {
		this.height = height;
	}
    
}
