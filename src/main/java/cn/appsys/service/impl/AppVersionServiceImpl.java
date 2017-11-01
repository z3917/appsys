package cn.appsys.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import cn.appsys.dao.AppInfoMapper;
import cn.appsys.dao.AppVersionMapper;
import cn.appsys.pojo.AppVersion;
import cn.appsys.service.AppVersionService;

@Service
public class AppVersionServiceImpl implements AppVersionService {
	@Resource
	private AppVersionMapper appVersionMapper;
	@Resource
	private AppInfoMapper appInfoMapper;

	@Override
	public List<AppVersion> findAppVersionList(Map<String, Object> map) {
		return appVersionMapper.findAppVersionList(map);
	}

	@Override
	public Integer addappVersion(AppVersion appVersion) {
		Integer i=0;
		if (appVersionMapper.addappVersion(appVersion)==1) {
			Integer versionId=appVersionMapper.findVersionIdByAppId(appVersion.getAppId());
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("versionId", versionId);
			map.put("id", appVersion.getAppId());
			System.out.println(versionId+"**************"+appVersion.getAppId());
			if (appInfoMapper.updateVersionId(map)==1) {
				i=1;
			}
		}
		return i; 
	}

	@Override
	public Integer deleteApk(@Param("id")Integer id) {
		return appVersionMapper.deleteApk(id);
	}

	@Override
	public Integer updateAppVersion(AppVersion appVersion) {
		return appVersionMapper.updateAppVersion(appVersion);
	}
}
