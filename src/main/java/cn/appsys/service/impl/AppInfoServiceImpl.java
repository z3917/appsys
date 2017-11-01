package cn.appsys.service.impl;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.validation.constraints.Pattern.Flag;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

import cn.appsys.dao.AppInfoMapper;
import cn.appsys.dao.AppVersionMapper;
import cn.appsys.pojo.AppInfo;
import cn.appsys.pojo.AppVersion;
import cn.appsys.service.AppInfoService;

@Service
public class AppInfoServiceImpl implements AppInfoService {
	@Resource
	private AppInfoMapper appInfoMapper;
	@Resource
	private AppVersionMapper appVersionMapper;

	@Override
	public Integer findAppInfoCount(Map<String, Object> map) {
		return appInfoMapper.findAppInfoCount(map);
	}

	@Override
	public List<AppInfo> findAppInfoList(Map<String, Object> map) {
		return appInfoMapper.findAppInfoList(map);
	}

	@Override
	public Integer addAppInfo(AppInfo appInfo) {
		return appInfoMapper.addAppInfo(appInfo);
	}

	@Override
	public Integer deleteAppLogo(Integer id) {
		return appInfoMapper.deleteAppLogo(id);
	}

	@Override
	public Integer modify(AppInfo appInfo) {
		return appInfoMapper.modify(appInfo);
	}

	@Override
	public Boolean deleteApp(String id) {
		Boolean flag = false;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("aid", id);
		map.put("id", id);
		List<AppVersion> appVersionList = null;
		// <1> 删除上传的apk文件
		appVersionList = appVersionMapper.findAppVersionList(map);
		if (appVersionList.size() > 0) {
			for (AppVersion appVersion : appVersionList) {
				if (appVersion.getApkLocPath() != null && !appVersion.getApkLocPath().equals("")) {
					File file = new File(appVersion.getApkLocPath());
					if (file.exists()) {
						if (!file.delete())
							flag = false;
					}
				}
			}
			// <2> 删除app_version表数据
			appVersionMapper.deleteAppVersion(id);
		}
		// 2 再删app基础信息
		// <1> 删除上传的logo图片
		AppInfo appInfo = appInfoMapper.findAppInfoList(map).get(0);
		if (appInfo.getLogoLocPath() != null && !appInfo.getLogoLocPath().equals("")) {
			File file = new File(appInfo.getLogoLocPath());
			if (file.exists()) {
				if (!file.delete())
					flag = false;
			}
		}
		// <2> 删除app_info表数据
		if (appInfoMapper.deleteApp(id) > 0) {
			flag = true;
		}

		return flag;
	}

	@Override
	public Boolean updateSaleStatusByAppId(AppInfo appInfo2) {
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("id", appInfo2.getId());
		AppInfo appInfo=appInfoMapper.findAppInfoList(map).get(0);
		if (null == appInfo) {
			return false;
		} else {
			switch (appInfo.getStatus()) {
			case 2: // 当状态为审核通过时，可以进行上架操作
			case 5:// 当状态为下架时，可以进行上架操作
				appInfo.setOnSaleDate(new Date());
				appInfo.setStatus(4);
				appInfoMapper.modify(appInfo);
				AppVersion appVersion=new AppVersion();
				appVersion.setModifyBy(appInfo.getCreatedBy());
				appVersion.setModifyDate(new Date());
				appVersion.setPublishStatus(2);
				appVersionMapper.updateAppVersion(appVersion);
				return true;
			case 4:// 当状态为上架时，可以进行下架操作
				appInfo.setOffSaleDate(new Date());
				appInfo.setStatus(5);
				appInfoMapper.modify(appInfo);
				return true;
			default:
				return false;
			}
		}

	}
	
	public Integer updateSatus(Integer status,Integer id){
		return appInfoMapper.updateSatus(status,id);
	};
}
