package cn.appsys.dao;

import java.util.List;
import java.util.Map;

import cn.appsys.pojo.DataDictionary;

public interface DataDictionaryMapper {
	List<DataDictionary> finDataDictionaryList(Map<String, Object> map);
}

