package ar.edu.itba.paw.webapp.dto;

import java.time.Instant;

import ar.edu.itba.paw.model.TournamentEvent;

public class TournamentEventDto {
	
	private long eventid;
	private String name;
	//private UserDto owner;
	//private String description;
	private int maxParticipants;
	private PitchDto pitch;
	private TournamentTeamDto firstTeam;
	private TournamentTeamDto secondTeam;
	private Integer firstTeamScore; // ANOTATION?
	private Integer secondTeamScore; // ANOTATION?
	//private int inscriptionCount;
	//private Instant inscriptionEnd;
	//private boolean inscriptionSuccess;
	private Instant startsAt;
	private Instant endsAt;
	//private Instant createdAt;
	
	//private boolean isParticipant;
	
	public static TournamentEventDto ofTournamentEvent(TournamentEvent event/*, boolean isParticipant*/) {
		TournamentEventDto dto = new TournamentEventDto();
		dto.eventid = event.getEventId();
		dto.name = event.getName();
		//dto.owner = UserDto.ofUser(event.getOwner());
		//dto.description = event.getDescription();
		dto.maxParticipants = event.getMaxParticipants();
		dto.pitch = PitchDto.ofPitch(event.getPitch());
		dto.firstTeam = TournamentTeamDto.ofTeam(event.getFirstTeam());
		dto.secondTeam = TournamentTeamDto.ofTeam(event.getSecondTeam());
		dto.firstTeamScore = event.getFirstTeamScore();
		dto.secondTeamScore = event.getSecondTeamScore();
		//dto.inscriptionCount = event.getInscriptions().size();
		//dto.inscriptionEnd = event.getEndsInscriptionAt();
		//dto.inscriptionSuccess = event.getInscriptionSuccess();
		dto.startsAt = event.getStartsAt();
		dto.endsAt = event.getEndsAt();
		//dto.createdAt = event.getCreatedAt();
		//dto.isParticipant = isParticipant;

		return dto;
	}

	public long getEventid() {
		return eventid;
	}

	public void setEventid(long eventid) {
		this.eventid = eventid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

//	public UserDto getOwner() {
//		return owner;
//	}
//
//	public void setOwner(UserDto owner) {
//		this.owner = owner;
//	}
//
//	public String getDescription() {
//		return description;
//	}
//
//	public void setDescription(String description) {
//		this.description = description;
//	}

	public int getMaxParticipants() {
		return maxParticipants;
	}

	public void setMaxParticipants(int maxParticipants) {
		this.maxParticipants = maxParticipants;
	}

	public PitchDto getPitch() {
		return pitch;
	}

	public void setPitch(PitchDto pitch) {
		this.pitch = pitch;
	}
	
	public TournamentTeamDto getFirstTeam() {
		return firstTeam;
	}

	public void setFirstTeam(TournamentTeamDto firstTeam) {
		this.firstTeam = firstTeam;
	}

	public TournamentTeamDto getSecondTeam() {
		return secondTeam;
	}

	public void setSecondTeam(TournamentTeamDto secondTeam) {
		this.secondTeam = secondTeam;
	}

	public Integer getFirstTeamScore() {
		return firstTeamScore;
	}

	public void setFirstTeamScore(Integer firstTeamScore) {
		this.firstTeamScore = firstTeamScore;
	}

	public Integer getSecondTeamScore() {
		return secondTeamScore;
	}

	public void setSecondTeamScore(Integer secondTeamScore) {
		this.secondTeamScore = secondTeamScore;
	}

//	public int getInscriptionCount() {
//		return inscriptionCount;
//	}
//
//	public void setInscriptionCount(int inscriptionCount) {
//		this.inscriptionCount = inscriptionCount;
//	}

//	public Instant getInscriptionEnd() {
//		return inscriptionEnd;
//	}
//
//	public void setInscriptionEnd(Instant inscriptionEnd) {
//		this.inscriptionEnd = inscriptionEnd;
//	}

//	public boolean isInscriptionSuccess() {
//		return inscriptionSuccess;
//	}
//
//	public void setInscriptionSuccess(boolean inscriptionSuccess) {
//		this.inscriptionSuccess = inscriptionSuccess;
//	}

	public Instant getStartsAt() {
		return startsAt;
	}

	public void setStartsAt(Instant startsAt) {
		this.startsAt = startsAt;
	}

	public Instant getEndsAt() {
		return endsAt;
	}

	public void setEndsAt(Instant endsAt) {
		this.endsAt = endsAt;
	}

//	public Instant getCreatedAt() {
//		return createdAt;
//	}
//
//	public void setCreatedAt(Instant createdAt) {
//		this.createdAt = createdAt;
//	}

//	public boolean isParticipant() {
//		return isParticipant;
//	}
//
//	public void setParticipant(boolean isParticipant) {
//		this.isParticipant = isParticipant;
//	}

}
