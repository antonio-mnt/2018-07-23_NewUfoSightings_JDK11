package it.polito.tdp.newufosightings;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;

import it.polito.tdp.newufosightings.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

//controller turno A --> switchare al branch master_turnoB per turno B

public class FXMLController {
	
	private Model model;
	private int an;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextArea txtResult;

    @FXML
    private TextField txtAnno;

    @FXML
    private Button btnSelezionaAnno;

    @FXML
    private ComboBox<String> cmbBoxForma;

    @FXML
    private Button btnCreaGrafo;

    @FXML
    private TextField txtT1;

    @FXML
    private TextField txtAlfa;

    @FXML
    private Button btnSimula;

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	
    	String shape = this.cmbBoxForma.getValue();
    	
    	if(shape==null) {
    		this.txtResult.setText("Selezionare una forma");
    		return;
    	}
    	
    	this.model.creaGrafo(this.an, shape);
    	
    	this.txtResult.setText("Grafo creato!\nVertivi: "+this.model.getNumeroVertici()+" archi: "+this.model.getNumeroarchi()+"\n");
    	
    	Map<String,Integer> mappa = new TreeMap<>(this.model.getStati());
    	
    	for(String s: mappa.keySet()) {
    		txtResult.appendText(s+" somma pesi:"+ mappa.get(s)+"\n");
    	}

    }

    @FXML
    void doSelezionaAnno(ActionEvent event) {
    	
    	int anno;
    	
    	try {
    		anno = Integer.parseInt(this.txtAnno.getText());
    	}catch(NumberFormatException ne) {
    		this.txtResult.setText("Scrivere un'anno valido.");
    		return;
    	}
    	
    	if(anno<1910 || anno > 2014) {
    		this.txtResult.setText("Anno non presente nel DB.");
    		return;
    	}
    	
    	this.an = anno;   
    	this.cmbBoxForma.getItems().clear();
    	this.cmbBoxForma.getItems().addAll(this.model.getShapes(anno));

    }

    @FXML
    void doSimula(ActionEvent event) {
    	
    	int giorni;
    	
    	try {
    		giorni = Integer.parseInt(this.txtT1.getText());
    	}catch(NumberFormatException ne) {
    		this.txtResult.setText("Scrivere un numero di gironi valido.");
    		return;
    	}
    	
    	if(giorni<1 || giorni > 365) {
    		this.txtResult.setText("Il numero di giorni deve essere compreso tra 1 e 365");
    		return;
    	}
    	
    	int prob;
    	
    	try {
    		prob = Integer.parseInt(this.txtAlfa.getText());
    	}catch(NumberFormatException ne) {
    		this.txtResult.setText("Scrivere una probabilità valida");
    		return;
    	}
    	
    	if(prob<1 || prob> 100) {
    		this.txtResult.setText("La probabilità deve essere compresa tra 0 e 100");
    		return;
    	}
    	
    	String shape = this.cmbBoxForma.getValue();
    	
    	if(shape==null) {
    		this.txtResult.setText("Selezionare una forma");
    		return;
    	}
    	
    	this.txtResult.clear();
    	Map<String,Double> mappa = new TreeMap<>(this.model.simula(this.an, shape, prob, giorni));
    	
    	for(String s: mappa.keySet()) {
    		txtResult.appendText(String.format("%s  DEFCON: %.2f\n",s, mappa.get(s)));
    	}
    	

    }

    @FXML
    void initialize() {
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
        assert txtAnno != null : "fx:id=\"txtAnno\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
        assert btnSelezionaAnno != null : "fx:id=\"btnSelezionaAnno\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
        assert cmbBoxForma != null : "fx:id=\"cmbBoxForma\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
        assert txtT1 != null : "fx:id=\"txtT1\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
        assert txtAlfa != null : "fx:id=\"txtAlfa\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";

    }

	public void setModel(Model model) {
		this.model = model;
		this.an = 0;
		
	}
}
