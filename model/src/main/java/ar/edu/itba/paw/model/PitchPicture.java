package ar.edu.itba.paw.model;

public class PitchPicture {
	
	private long pitchPictureId;
	private long pitchid;
	private byte[] data;
	
	public PitchPicture(long pitchPictureId, long pitchid) {
		this.pitchPictureId = pitchPictureId;
		this.pitchid = pitchid;
	}
	
	public PitchPicture(long pitchPictureId, long pitchid, byte[] data) {
		this(pitchPictureId, pitchid);
		this.data = data;
	}
	
	@Override
	public String toString() {
		return "Pitch Picture id: " + pitchPictureId + " Pitch id: " + pitchid;
	}

	public long getPitchPictureId() {
		return pitchPictureId;
	}

	public long getPitchid() {
		return pitchid;
	}

	public byte[] getData() {
		return data;
	}

}
