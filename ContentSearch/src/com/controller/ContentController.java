package com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bean.SearchContentList;
import com.bean.SearchCriteria;
import com.service.ContentService;

@RestController
@RequestMapping("/service")
public class ContentController {

	@Autowired
	private ContentService contentService;
	
	@RequestMapping(value="/",method = RequestMethod.GET)
	public String root() {
		return "index";
	}
	
	@RequestMapping(value="/contents",method = RequestMethod.GET,produces ="application/json")
	public SearchContentList getAllContentDetails() {
		SearchContentList list = new SearchContentList();
		list.setContents(contentService.retriveAll());
		return list;
	}
	
	@RequestMapping(value="/contents/{id}",method = RequestMethod.GET,produces="application/json")
	public ResponseEntity<SearchCriteria> getContentDetailsById(@PathVariable("id") String id) {
		try {
			SearchCriteria contents = contentService.retriveContentById(id);		
			return new ResponseEntity<SearchCriteria>(contents,HttpStatus.OK);
		}catch (Exception e) {
			return new ResponseEntity<SearchCriteria>( HttpStatus.NOT_FOUND);
		}

	}
	
	@RequestMapping(value="/contents",method = RequestMethod.POST,consumes = "application/json")
	public ResponseEntity<Void> setContentDetails(@RequestBody SearchCriteria contents) {
		try {
			contentService.storeContent(contents);
			return new ResponseEntity<Void>(HttpStatus.OK);
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR); 
		}
	}
}

