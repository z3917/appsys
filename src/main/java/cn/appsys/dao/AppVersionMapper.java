package cn.appsys.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.AppVersion;

public interface AppVersionMapper {

	List<AppVersion> findAppVersionList(Map<String, Object> map);
	
	Integer addappVersion(AppVersion appVersion);
	
	Integer findVersionIdByAppId(@Param("appId")Integer appId);
	
	Integer deleteApk(@Param("id")Integer id);
	
	Integer updateAppVersion(AppVersion appVersion);
	
	Integer deleteAppVersion(@Param("id")String id);
}

