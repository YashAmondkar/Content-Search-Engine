package com.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.bean.SearchCriteria;
import com.repository.ContentRepository;

public class ContentServiceImpl implements ContentService {

	@Autowired
	private ContentRepository contentRepository;
	
	@Override
	public List<SearchCriteria> retriveAll() {
		return contentRepository.retriveAll();
	}

	@Override
	public SearchCriteria retriveContentById(String id) {
		return contentRepository.retriveContentById(id);
	}

	@Override
	public void storeContent(SearchCriteria content) throws Exception {
		contentRepository.storeContent(content);
	}

}
