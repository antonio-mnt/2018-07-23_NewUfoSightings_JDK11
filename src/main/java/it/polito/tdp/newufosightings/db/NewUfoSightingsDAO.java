package it.polito.tdp.newufosightings.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.newufosightings.model.Arco;
import it.polito.tdp.newufosightings.model.Sighting;
import it.polito.tdp.newufosightings.model.State;

public class NewUfoSightingsDAO {

	public List<Sighting> loadAllSightings(int anno, String shape) {
		String sql = "SELECT * " + 
				"FROM sighting " + 
				"WHERE YEAR(DATETIME) = ? AND shape = ? ";
		List<Sighting> list = new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			st.setString(2, shape);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				list.add(new Sighting(res.getInt("id"), res.getTimestamp("datetime").toLocalDateTime(),
						res.getString("city"), res.getString("state"), res.getString("country"), res.getString("shape"),
						res.getInt("duration"), res.getString("duration_hm"), res.getString("comments"),
						res.getDate("date_posted").toLocalDate(), res.getDouble("latitude"),
						res.getDouble("longitude")));
			}

			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}

		return list;
	}

	public void loadAllStates(Map<String,State> idMap) {
		String sql = "SELECT * FROM state";

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				State state = new State(rs.getString("id"), rs.getString("Name"), rs.getString("Capital"),
						rs.getDouble("Lat"), rs.getDouble("Lng"), rs.getInt("Area"), rs.getInt("Population"),
						rs.getString("Neighbors"));
				idMap.put(rs.getString("id"), state);
				
			}

			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	
	
	public List<Arco> loadAllArchi(Map<String,State> idMap) {
		String sql = "SELECT n.state1, n.state2 " + 
				"FROM neighbor AS n " + 
				"WHERE n.state1 > n.state2";
		
		List<Arco> list = new ArrayList<>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				String s1 = rs.getString("n.state1");
				String s2 = rs.getString("n.state2");
				
				Arco a = new Arco(idMap.get(s1),idMap.get(s2));
				list.add(a);
				
			}

			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
		
		return list;
	}
	
	public List<String> loadAllShape(int anno) {
		String sql = "SELECT DISTINCT shape " + 
				"FROM sighting " + 
				"WHERE shape != \"\" AND YEAR(DATETIME) = ? " + 
				"ORDER BY shape ";
		List<String> list = new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);	
			st.setInt(1, anno);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				list.add(res.getString("shape"));
			}

			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}

		return list;
	}
	
	
	public int getPeso(int anno, String shape, State s1, State s2) {
		String sql = "SELECT COUNT(*) AS peso " + 
				"FROM sighting AS s1, sighting as s2 " + 
				"WHERE s1.state = ? AND s2.state  = ? " + 
				"AND YEAR(s1.datetime) = ? AND YEAR(s2.datetime) = ? " + 
				"AND s1.shape = ?  AND s2.shape = ? " + 
				"GROUP BY s1.id , s2.id";
		Integer peso = 0;
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, s1.getId().toLowerCase());
			st.setString(2, s2.getId().toLowerCase());
			st.setInt(3, anno);
			st.setInt(4, anno);
			st.setString(5, shape);
			st.setString(6, shape);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				peso = res.getInt("peso");
				if(peso==null) {
					peso = 0;
				}
			}

			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}

		return peso;
	}

}

