package com.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.bean.MetaData;
import com.bean.SearchCriteria;

public class ContentRepositoryImpl implements ContentRepository {
	
	private final class SearchCriteriaRowMapper implements RowMapper<SearchCriteria>{

		@Override
		public SearchCriteria mapRow(ResultSet rs, int index) throws SQLException {
			SearchCriteria cri = new SearchCriteria();
			
			cri.setId(rs.getString("id"));
			cri.setTitle(rs.getString("title"));
			cri.setStatus(rs.getString("pub_status"));
			cri.setDate(rs.getString("release_date"));
			
			String query1 = "select * from metadata where id="+rs.getString("id");
			MetaData metaData = (MetaData) jdbcTemplate.query(query1, new MetaDataRowMapper());
			cri.setMetaData(metaData);
			
			return cri;
		}
		
	}
	
	private final class MetaDataRowMapper implements RowMapper<MetaData>{

		@Override
		public MetaData mapRow(ResultSet rs, int index) throws SQLException {

			MetaData metaData = new MetaData(); 
			metaData.setRegion(rs.getString("region"));
			metaData.setSynopsis(rs.getString("synopsis"));
			metaData.setDescription(rs.getString("description"));
			
			String query2 = "select * from tags where id=" + rs.getString("id");
			metaData = (MetaData)jdbcTemplate.query(query2, new MetaDataRowMapper());
			
			metaData.setAsemicwriting(rs.getString("asemicwriting"));
			metaData.setComedy(rs.getString("comedy"));
			metaData.setDrama(rs.getString("drama"));
			metaData.setHorrorfiction(rs.getString("horrorfiction"));
			metaData.setRomance(rs.getString("romance"));
			metaData.setFantasy(rs.getString("fantasy"));
			metaData.setMythology(rs.getString("mythology"));			
			return metaData;
		}
		
	}

	
	@Autowired
	JdbcTemplate jdbcTemplate; 
	
	@Override
	public List<SearchCriteria> retriveAll() {
		String query  = "select * from search";				
		List<SearchCriteria> list = jdbcTemplate.query(query, new SearchCriteriaRowMapper());		
		return list;
	}

	@Override
	public SearchCriteria retriveContentById(String id) {
		String query = "select * from search where id = "+id;
		List<SearchCriteria> list = jdbcTemplate.query(query, new SearchCriteriaRowMapper());
		
		if(list.isEmpty()) {
			return null;
		}else if(list.size() == 1) {		
			return list.get(0);
		}else {
			throw new IncorrectResultSizeDataAccessException("Incorrect Result size", 1);
		}
	}

	@Override
	public void storeContent(SearchCriteria content) throws Exception {
		String insertIntoSearch = "insert into search(id,title,pub_status,release_date) values (?,?,?,?)";
		String insertIntoMetadata = "insert into metadata(id,region,synopsis,description) values (?,?,?,?)";
		String insertIntoTags = "insert into tags(id,asemicwriting,comedy,drama,horrorfiction,romance,fantasy,mythology) values (?,?,?,?,?,?,?,?)";
		
		int rowsAffected = jdbcTemplate.update(insertIntoSearch, content.getId(),content.getTitle(),content.getStatus(),content.getDate());
		if(rowsAffected!=1) {
			throw new Exception("Insert Failed for Search");
		}
		
		rowsAffected = jdbcTemplate.update(insertIntoMetadata, content.getId(),content.getMetaData().getRegion(), content.getMetaData().getSynopsis(),content.getMetaData().getDescription());
		if(rowsAffected!=1) {
			throw new Exception("Insert Failed for Metadata");
		}
		
		rowsAffected = jdbcTemplate.update(insertIntoTags, content.getMetaData().getAsemicwriting(),content.getMetaData().getComedy(),content.getMetaData().getDrama(),content.getMetaData().getHorrorfiction(),content.getMetaData().getRomance(),content.getMetaData().getFantasy(),content.getMetaData().getMythology());
		if(rowsAffected!=1) {
			throw new Exception("Insert Failed for Tags");
		}
	}
	
}
