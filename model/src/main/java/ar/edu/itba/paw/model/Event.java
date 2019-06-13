package ar.edu.itba.paw.model;

import java.time.Instant;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "events")
public class Event {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "events_eventid_seq")
	@SequenceGenerator(sequenceName = "events_eventid_seq", name = "events_eventid_seq", allocationSize = 1)
	@Column(name = "eventid")
	private long eventid;
	
	@Column(name = "eventname", length = 100, nullable = false)
	private String name;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "userid")
	private User owner;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "pitchid")
	private Pitch pitch;
	
	@Column(length = 500, nullable = true)
	private String description;
	
	@Column(name = "max_participants", nullable = false)
	private int maxParticipants;
	
	@Column(name = "starts_at", nullable = false)
	private Instant startsAt;
	
	@Column(name = "ends_at", nullable = false)
	private Instant endsAt;
	
	@Column(name = "event_created_at", nullable = false)
	private Instant createdAt;
	
	@PrePersist
	protected void onCreate() {
		createdAt = Instant.now();
	}
	
	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "inscriptionEvent")
	private List<Inscription> inscriptions;
	
	/*package*/ Event() {
		
	}
	
	public Event(String name, Pitch pitch, String description,
			int maxParticipants, Instant startsAt, Instant endsAt) {
		super();
		this.name = name;
		this.pitch = pitch;
		this.description = description;
		this.maxParticipants = maxParticipants;
		this.startsAt = startsAt;
		this.endsAt = endsAt;
	}
	
	public Event(String name, User owner, Pitch pitch, String description,
	int maxParticipants, Instant startsAt, Instant endsAt) {
		this(name, pitch, description,
			maxParticipants, startsAt, endsAt);
		this.owner = owner;
	}
	
	public Event(String name, User owner, Pitch pitch, String description, 
			int maxParticipants, Instant startsAt, Instant endsAt, Instant createdAt) {
		this(name, owner, pitch, description, maxParticipants, startsAt,
			endsAt);
		this.createdAt = createdAt;
	}
	
	public Event(String name, Pitch pitch, String description, 
			int maxParticipants, Instant startsAt, Instant endsAt, Instant createdAt) {
		this(name, pitch, description, maxParticipants, startsAt,
			endsAt);
		this.createdAt = createdAt;
	}
	
	public Event(String name, User owner, Pitch pitch, int maxParticipants, Instant startsAt, 
			Instant endsAt) {
		this.name = name;
		this.owner = owner;
		this.pitch = pitch;
		this.maxParticipants = maxParticipants;
		this.startsAt = startsAt;
		this.endsAt = endsAt;
	}
	
	@Override
	public String toString() {
		return "Eventid: " + eventid + " Name: " + name;
	}
	
	public long getEventId() {
		return eventid;
	}

	public String getName() {
		return name;
	}
	
	public User getOwner() {
		return owner;
	}
	
	public Pitch getPitch() {
		return pitch;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public int getMaxParticipants() {
		return maxParticipants;
	}

	public Instant getStartsAt() {
		return startsAt;
	}

	public Instant getEndsAt() {
		return endsAt;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}
	
	public List<Inscription> getInscriptions() {
		return inscriptions;
	}

}
