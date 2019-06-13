package ar.edu.itba.paw.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ar.edu.itba.paw.exception.UserNotAuthorizedException;
import ar.edu.itba.paw.interfaces.ClubDao;
import ar.edu.itba.paw.interfaces.ClubService;
import ar.edu.itba.paw.interfaces.InscriptionDao;
import ar.edu.itba.paw.interfaces.UserDao;
import ar.edu.itba.paw.model.Club;
import ar.edu.itba.paw.model.ClubComment;
import ar.edu.itba.paw.model.User;

@Service
public class ClubServiceImpl implements ClubService {
	
	@Autowired
	private ClubDao cd;
	
	@Autowired
	private UserDao ud;
	
	@Autowired
	private InscriptionDao idao;
	
	private static final String NEGATIVE_ID_ERROR = "Id must be greater than zero.";
	private static final String NEGATIVE_PAGE_ERROR = "Page number must be greater than zero.";

	@Override
	public Optional<Club> findById(long clubid) {
		if(clubid <= 0) {
			throw new IllegalArgumentException(NEGATIVE_ID_ERROR);
		}
		return cd.findById(clubid);
	}
	
	@Override
	public List<Club> findAll(int pageNum) {
		if(pageNum <= 0) {
			throw new IllegalArgumentException(NEGATIVE_PAGE_ERROR);
		}
		return cd.findAll(pageNum);
	}

	@Transactional(rollbackFor = { Exception.class })
	@Override
	public Club create(String name, String location) {
		return cd.create(name, location);
	}
	
	@Override
	public int getPageInitialClubIndex(final int pageNum) {
		if(pageNum <= 0) {
			throw new IllegalArgumentException(NEGATIVE_PAGE_ERROR);
		}
		return cd.getPageInitialClubIndex(pageNum);
	}
	
	@Transactional(rollbackFor = { Exception.class })
	@Override
	public void deleteClub(final long clubid) {
		if(clubid <= 0) {
			throw new IllegalArgumentException(NEGATIVE_ID_ERROR);
		}
		cd.deleteClub(clubid);
	}

	@Override
	public int countClubPages() {
		return cd.countClubPages();
	}

	@Override
	public List<Club> findBy(Optional<String> clubName, Optional<String> location, int pageNum) {
		if(pageNum <= 0) {
			throw new IllegalArgumentException(NEGATIVE_PAGE_ERROR);
		}
		return cd.findBy(clubName, location, pageNum);
	}
	
	@Override
	public int countFilteredClubs(Optional<String> clubName, Optional<String> location) {
		return cd.countFilteredClubs(clubName, location);
	}

	@Override
	public int countPastEvents(final long clubid) {
		if(clubid <= 0) {
			throw new IllegalArgumentException(NEGATIVE_ID_ERROR);
		}
		return cd.countPastEvents(clubid);
	}
	
	@Transactional(rollbackFor = { Exception.class })
	@Override
	public ClubComment createComment(final long userid, final long clubid, final String comment) 
			throws UserNotAuthorizedException {
		if(userid <= 0 || clubid <= 0) {
			throw new IllegalArgumentException(NEGATIVE_ID_ERROR);
		}
		
		User commenter = ud.findById(userid).orElseThrow(NoSuchElementException::new);
		Club club = cd.findById(clubid).orElseThrow(NoSuchElementException::new);
		
		if(!idao.haveRelationship(commenter, club))
			throw new UserNotAuthorizedException("User is not authorized to comment if no shared events.");
		
		return cd.createComment(commenter, club, comment);
	}
	
	@Override
	public boolean haveRelationship(final long userid, final long clubid) {
		if(userid <= 0 || clubid <= 0) {
			throw new IllegalArgumentException(NEGATIVE_ID_ERROR);
		}
		
		User user = ud.findById(userid).orElseThrow(NoSuchElementException::new);
		Club club = cd.findById(clubid).orElseThrow(NoSuchElementException::new);
		
		return idao.haveRelationship(user, club);
	}

	@Override
	public List<ClubComment> getCommentsByClub(long clubid, int pageNum) {
		if(clubid <= 0) {
			throw new IllegalArgumentException(NEGATIVE_ID_ERROR);
		}
		if(pageNum <= 0) {
			throw new IllegalArgumentException(NEGATIVE_PAGE_ERROR);
		}
		return cd.getCommentsByClub(clubid, pageNum);
	}

	@Override
	public int countByClubComments(long clubid) {
		if(clubid <= 0) {
			throw new IllegalArgumentException(NEGATIVE_ID_ERROR);
		}
		return cd.countByClubComments(clubid);
	}

	@Override
	public int getCommentsPageInitIndex(int pageNum) {
		if(pageNum <= 0) {
			throw new IllegalArgumentException(NEGATIVE_PAGE_ERROR);
		}
		return cd.getCommentsPageInitIndex(pageNum);
	}

	@Override
	public int getCommentsMaxPage(long clubid) {
		if(clubid <= 0) {
			throw new IllegalArgumentException(NEGATIVE_ID_ERROR);
		}
		return cd.getCommentsMaxPage(clubid);
	}

}
