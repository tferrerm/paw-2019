package ar.edu.itba.paw.webapp.dto;

import java.util.List;

public class UserCommentCollectionDto {
	
	private List<UserCommentDto> comments;
	private int pageCount;
	
	public static UserCommentCollectionDto ofComments(List<UserCommentDto> comments, int pageCount) {
		UserCommentCollectionDto dto = new UserCommentCollectionDto();
		dto.comments = comments;
		dto.pageCount = pageCount;
		
		return dto;
	}

	public List<UserCommentDto> getComments() {
		return comments;
	}

	public void setComments(List<UserCommentDto> comments) {
		this.comments = comments;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

}
