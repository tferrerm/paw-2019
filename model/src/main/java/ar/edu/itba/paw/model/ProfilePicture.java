package ar.edu.itba.paw.model;

public class ProfilePicture {
	
	private long profilePictureId;
	private long userid;
	private byte[] data;
	
	public ProfilePicture(long profilePictureId, long userid) {
		this.profilePictureId = profilePictureId;
		this.userid = userid;
	}
	
	public ProfilePicture(long profilePictureId, long userid, byte[] data) {
		this(profilePictureId, userid);
		this.data = data;
	}
	
	@Override
	public String toString() {
		return "Profile Picture id: " + profilePictureId + " User id: " + userid;
	}

	public long getProfilePictureId() {
		return profilePictureId;
	}

	public long getUserid() {
		return userid;
	}

	public byte[] getData() {
		return data;
	}

}
