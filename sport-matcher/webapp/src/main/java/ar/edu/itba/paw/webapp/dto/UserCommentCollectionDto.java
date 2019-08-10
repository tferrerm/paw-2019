package ar.edu.itba.paw.webapp.dto;

import java.util.List;

public class UserCommentCollectionDto {
	
	private List<UserCommentDto> collection;
	private int pageCount;
	
	public static UserCommentCollectionDto ofComments(List<UserCommentDto> collection, int pageCount) {
		UserCommentCollectionDto dto = new UserCommentCollectionDto();
		dto.collection = collection;
		dto.pageCount = pageCount;
		
		return dto;
	}

	public List<UserCommentDto> getCollection() {
		return collection;
	}

	public void setCollection(List<UserCommentDto> collection) {
		this.collection = collection;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

}
