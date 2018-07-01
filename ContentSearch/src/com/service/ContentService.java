package com.service;

import java.util.List;

import com.bean.SearchCriteria;

public interface ContentService {
	public List<SearchCriteria> retriveAll();
	public SearchCriteria retriveContentById(String id);
	public void storeContent(SearchCriteria content) throws Exception;
}
