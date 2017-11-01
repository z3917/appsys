package cn.appsys.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.AppInfo;

public interface AppInfoService {
	
	Integer findAppInfoCount(Map<String, Object> map);
	
	List<AppInfo> findAppInfoList(Map<String, Object> map);
	
	Integer addAppInfo(AppInfo appInfo);
	
	Integer deleteAppLogo(@Param("id")Integer id);
	
	Integer modify(AppInfo appInfo);
	
	Boolean deleteApp(@Param("id")String id);
	
	Boolean updateSaleStatusByAppId(AppInfo appInfo);
	
	Integer updateSatus(Integer status,Integer id);
}

