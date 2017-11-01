package cn.appsys.service;

import java.util.List;
import java.util.Map;

import cn.appsys.pojo.AppCategory;

public interface AppCategoryService {
	List<AppCategory> findAppCategoryList(Map<String, Object> map);
}

