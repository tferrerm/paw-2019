package ar.edu.itba.paw.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@SuppressWarnings("serial")
@Embeddable
public class TournamentEventId implements Serializable {
	
	@Column(name = "eventid", nullable = false)
	private Long eventid;
	
	/*package*/ TournamentEventId() {
		
	}
	
	public TournamentEventId(Long eventid) {
		this.eventid = eventid;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == this)
			return true;
		if(!(o instanceof TournamentEventId))
			return false;
		TournamentEventId other = (TournamentEventId) o;
		return this.getEventid().equals(other.getEventid());
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.getEventid());
	}

	public Long getEventid() {
		return eventid;
	}
}
