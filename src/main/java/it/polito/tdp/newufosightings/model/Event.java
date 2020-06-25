package it.polito.tdp.newufosightings.model;

import java.time.LocalDateTime;

public class Event implements Comparable<Event>{
	
	
	public enum EventType{
		NUOVO_EVENTO,
		CESSATA_ALLERTA,
	}
	
	private EventType type;
	private LocalDateTime data;
	private State stato;
	public Event(EventType type, LocalDateTime data, State stato) {
		super();
		this.type = type;
		this.data = data;
		this.stato = stato;
	}
	public EventType getType() {
		return type;
	}
	public void setType(EventType type) {
		this.type = type;
	}
	public LocalDateTime getData() {
		return data;
	}
	public void setData(LocalDateTime data) {
		this.data = data;
	}
	public State getStato() {
		return stato;
	}
	public void setStato(State stato) {
		this.stato = stato;
	}
	@Override
	public String toString() {
		return "Event [type=" + type + ", data=" + data + ", stato=" + stato + "]";
	}
	@Override
	public int compareTo(Event o) {
		return this.data.compareTo(o.data);
	}
	
	

}
