package cn.appsys.dao;

import java.util.List;
import java.util.Map;

import cn.appsys.pojo.AppCategory;

public interface AppCategoryMapper {
	List<AppCategory> findAppCategoryList(Map<String, Object> map);
}
