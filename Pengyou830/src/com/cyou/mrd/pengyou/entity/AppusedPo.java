package com.cyou.mrd.pengyou.entity;


public class AppusedPo{
	private String appName;			//应用名称
    private int count;				//使用次数
    private Integer id;				//主键
    private Long lastUseDate;		//最后使用时间
    private String packageName;		//包名
    private String version;			//版本号
    
    public AppusedPo(){
    	
    }
    
    public AppusedPo(Integer integer, String s, String s1, String s2, Long long1, int i){
        id = integer;
        appName = s;
        packageName = s1;
        version = s2;
        lastUseDate = long1;
        count = i;
    }

    public String getAppName()
    {
        return appName;
    }

    public int getCount()
    {
        return count;
    }

    public Integer getId()
    {
        return id;
    }

    public Long getLastUseDate()
    {
        return lastUseDate;
    }

    public String getPackageName()
    {
        return packageName;
    }

    public String getVersion()
    {
        return version;
    }

    public void setAppName(String s)
    {
        appName = s;
    }

    public void setCount(int i)
    {
        count = i;
    }

    public void setId(Integer integer)
    {
        id = integer;
    }

    public void setLastUseDate(Long long1)
    {
        lastUseDate = long1;
    }

    public void setPackageName(String s)
    {
        packageName = s;
    }

    public void setVersion(String s)
    {
        version = s;
    }

    
}
