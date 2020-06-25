package it.polito.tdp.newufosightings.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.newufosightings.db.NewUfoSightingsDAO;

public class Model {
	
	
	
	private NewUfoSightingsDAO dao;
	private SimpleWeightedGraph<State,DefaultWeightedEdge> grafo;
	private List<String> shapes;
	private Map<String, State> idMap;
	private List<Arco> archi;
	private Map<String,Integer> stati;
	private List<Sighting> avvistamenti;
	private Simulator sim;
	
	public Model() {
		this.dao = new NewUfoSightingsDAO();
		
	}
	
	
	public void creaGrafo(int anno, String shape) {
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		this.idMap = new HashMap<>();
		this.dao.loadAllStates(this.idMap);
		
		
		for(String s: this.idMap.keySet()) {
			this.grafo.addVertex(this.idMap.get(s));
		}
		
		this.archi = new ArrayList<>(this.dao.loadAllArchi(this.idMap));
		
		for(Arco a: this.archi) {
			int peso  = this.dao.getPeso(anno, shape, a.getS1(), a.getS2());
			Graphs.addEdgeWithVertices(this.grafo, a.getS1(), a.getS2(), peso);
		}
		
		
		
		
	}


	public List<String> getShapes(int anno) {
		this.shapes = new ArrayList<>(this.dao.loadAllShape(anno));
		return shapes;
	}
	
	public int getNumeroVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int getNumeroarchi() {
		return this.grafo.edgeSet().size();
	}
	
	public Map<String,Integer> getStati(){
		
		this.stati = new TreeMap<>();
		
		for(State s: this.grafo.vertexSet()) {
			this.stati.put(s.getId(), calcolaPeso(s));
		}
		
		
		return stati;
		
	}


	private int calcolaPeso(State s) {
		
		int peso = 0;
		
		for(State st: Graphs.neighborListOf(this.grafo, s)) {
			peso+= this.grafo.getEdgeWeight(this.grafo.getEdge(s, st));
		}
		
		return peso;
	}
	
	public Map<String,Double> simula(int anno, String shape,double prob, int giorni){
		this.sim = new Simulator();
		this.avvistamenti = new ArrayList<>(this.dao.loadAllSightings(anno, shape));
		
		this.sim.run(prob,giorni, this.idMap, this.avvistamenti, this.grafo);
		
		return this.sim.getStati();
	}
	
	

}
