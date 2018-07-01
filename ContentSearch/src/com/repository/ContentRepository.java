package com.repository;

import java.util.List;

import com.bean.SearchCriteria;

public interface ContentRepository {
	public List<SearchCriteria> retriveAll();
	public SearchCriteria retriveContentById(String id);
	public void storeContent(SearchCriteria content) throws Exception;	
}
