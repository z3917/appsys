package cn.appsys.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.appsys.dao.AppCategoryMapper;
import cn.appsys.pojo.AppCategory;

@Service
public class AppCategoryServiceImpl implements AppCategoryService {
	@Resource
	private AppCategoryMapper appCategoryMapper;

	@Override
	public List<AppCategory> findAppCategoryList(Map<String, Object> map) {
		return appCategoryMapper.findAppCategoryList(map);
	}

}
