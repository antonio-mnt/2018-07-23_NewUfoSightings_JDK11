package it.polito.tdp.newufosightings.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.TreeMap;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.newufosightings.model.Event.EventType;


public class Simulator {
	
	private PriorityQueue<Event> queue = new PriorityQueue<>();
	
	private double probabilità = 0.5;
	private long tempoGiorno = 24*60*60;
	private final double defconMin = 1;
	private final double defconMax = 5;
	private final double valoreMax = 1;
	private final double valoreMin = 0.5;
	private boolean allerta = true;
	private boolean flag = true;
	
	private SimpleWeightedGraph<State,DefaultWeightedEdge> grafo;
	
	private Map<String, Double> stati;
	
	
	
	
	public Map<String, Double> getStati() {
		return stati;
	}



	public void run(double prob, int giorni,Map<String,State> idMap, List<Sighting> avvistamenti, SimpleWeightedGraph<State,DefaultWeightedEdge> g) {
		
		this.flag = true;
		this.allerta = true;
		this.probabilità = prob/100;
		this.tempoGiorno = this.tempoGiorno*giorni;
		this.grafo = g;
		
		this.stati = new TreeMap<>();
		
		for(State s: this.grafo.vertexSet()) {
			this.stati.put(s.getId(), 5.0);
		}
		
		this.queue.clear();
		
		for(Sighting si: avvistamenti) {
			Event e = new Event(EventType.NUOVO_EVENTO,si.getDatetime(),idMap.get(si.getState().toUpperCase()));
			this.queue.add(e);
		}
		
		while(!this.queue.isEmpty()) {
			Event e = this.queue.poll();
			System.out.println(e+"\n");
			processEvent(e);
		}
		
		
	}



	private void processEvent(Event e) {
		
		if(this.flag==true) {
			LocalDateTime tempo = e.getData().plusSeconds(this.tempoGiorno);
			Event ev  = new Event(EventType.CESSATA_ALLERTA,tempo,null);
			this.queue.add(ev);
			this.flag=false;
		}
		
		switch(e.getType()) {
		
		case NUOVO_EVENTO:
			
			double p = Math.random();
			
			
			if(this.allerta==true) {
				this.stati.put(e.getStato().getId(), this.stati.get(e.getStato().getId())-this.valoreMax);
				if(this.stati.get(e.getStato().getId())<1) {
					this.stati.put(e.getStato().getId(), 1.0);
				}
			}else {
				this.stati.put(e.getStato().getId(), this.stati.get(e.getStato().getId())+this.valoreMax);
				if(this.stati.get(e.getStato().getId())>5) {
					this.stati.put(e.getStato().getId(), 5.0);
				}
			}
			
	
			if(p<this.probabilità) {
				modificaValoreVicini(e.getStato());
			}
			
			
			
			
			break;
			
		case CESSATA_ALLERTA:
			
			this.allerta = false;
			
			break;
		
		
		
		
		
		
		}
		
	}



	private void modificaValoreVicini(State stato) {
		
		List<State> vicini = new ArrayList<>(Graphs.neighborListOf(this.grafo, stato));
		
		for(State s: vicini) {
			if(this.allerta==true) {
				
				if(this.stati.get(s.getId())>1) {
					this.stati.put(s.getId(), (this.stati.get(s.getId())-this.valoreMin));
				}
			}else {
				if(this.stati.get(s.getId())<5) {
					this.stati.put(s.getId(), (this.stati.get(s.getId())+this.valoreMin));
				}
			}
			
		}


		
		
	}
	

}
