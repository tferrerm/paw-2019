package ar.edu.itba.paw.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "profile_pictures")
public class ProfilePicture {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "profile_pictures_profile_picture_id_seq")
	@SequenceGenerator(sequenceName = "profile_pictures_profile_picture_id_seq", name = "profile_pictures_profile_picture_id_seq", allocationSize = 1)
	@Column(name = "profile_picture_id")
	private long profilePictureId;
	
	@OneToOne(fetch = FetchType.EAGER, optional = false)
	private long userid;
	
	@Column(name = "data", nullable = false)
	private byte[] data;
	
	/*package*/ ProfilePicture() {
		
	}
	
	public ProfilePicture(long profilePictureId, long userid) {
		super();
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
