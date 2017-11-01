package cn.appsys.service;

import java.util.List;
import java.util.Map;

import cn.appsys.pojo.AppVersion;

public interface AppVersionService {

	List<AppVersion> findAppVersionList(Map<String, Object> map);
	
	Integer addappVersion(AppVersion appVersion);
	
	Integer deleteApk(Integer id);
	
	Integer updateAppVersion(AppVersion appVersion);
}

