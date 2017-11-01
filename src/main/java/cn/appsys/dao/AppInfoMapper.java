package cn.appsys.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.AppInfo;

public interface AppInfoMapper {
	
	Integer findAppInfoCount(Map<String, Object> map);
	
	List<AppInfo> findAppInfoList(Map<String, Object> map);
	
	Integer addAppInfo(AppInfo appInfo);
	
	Integer deleteAppLogo(@Param("id")Integer id);
	
	Integer modify(AppInfo appInfo);
	
	Integer updateVersionId(Map<String, Object> map);
	
	Integer deleteApp(@Param("id")String id);
	
	Integer updateSaleStatusByAppId(AppInfo appInfo);
	
	Integer updateSatus(@Param("status")Integer status,@Param("id")Integer id);
}

