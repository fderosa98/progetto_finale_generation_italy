package it.generationitaly.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.generationitaly.dao.AnnuncioDAO;
import it.generationitaly.dao.DAOException;
import it.generationitaly.dao.DBUtil;
import it.generationitaly.model.Annuncio;
import it.generationitaly.model.Automobile;
import it.generationitaly.model.Carburante;
import it.generationitaly.model.Foto;
import it.generationitaly.model.Indirizzo;
import it.generationitaly.model.NumeroPorte;
import it.generationitaly.model.Utente;

public class AnnuncioDAOImpl implements AnnuncioDAO {

	@Override
	public List<Annuncio> findAll(Connection connection) throws DAOException {
		List<Annuncio> annunci = new ArrayList<Annuncio>();
		int index = 0;
		System.out.println("QUERY DI FIND ALL");
		String sql = "SELECT * FROM annuncio JOIN automobile ON annuncio.automobile_id = automobile.id JOIN indirizzo ON annuncio.indirizzo_id = indirizzo.id JOIN utente on utente.id = annuncio.utente_id join foto ON annuncio.id = foto.annuncio_id";
		System.out.println(sql);
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.prepareStatement(sql);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Annuncio annuncio = new Annuncio();
				if (index != resultSet.getInt(1)) {
					index = resultSet.getInt(1);
					annuncio.setId(resultSet.getInt(1));
					annuncio.setTitolo(resultSet.getString(2));
					annuncio.setDescrizione(resultSet.getString(3));

					// System.out.println(annuncio);

					Automobile automobile = new Automobile();
					automobile.setId(resultSet.getInt(5));
					automobile.setMarca(resultSet.getString(8));
					automobile.setModello(resultSet.getString(9));
					automobile.setAnno(resultSet.getInt(10));
					automobile.setPrezzo(resultSet.getInt(11));
					automobile.setKm(resultSet.getInt(12));
					automobile.setCarburante(Carburante.fromValue(resultSet.getString(13)));
					automobile.setNumeroPorte(NumeroPorte.fromValue(resultSet.getInt(14)));
					
					// System.out.println(automobile);
					annuncio.setAutomobile(automobile);
					automobile.setAnnuncio(annuncio);
					
					
					Indirizzo indirizzo = new Indirizzo();
					indirizzo.setId(resultSet.getInt(15));
					indirizzo.setCitta(resultSet.getString(16));
					indirizzo.setProvincia(resultSet.getString(17));
					
					annuncio.setIndirizzo(indirizzo);
					indirizzo.setAnnuncio(annuncio);
					
					Utente utente = new Utente();
					utente.setId(resultSet.getInt(18));
					utente.setNome(resultSet.getString(19));
					utente.setCognome(resultSet.getString(20));
					utente.setEmail(resultSet.getString(21));
					utente.setTelefono(resultSet.getLong(22));
					utente.setUsername(resultSet.getString(23));
					utente.setPassword(resultSet.getString(24));
					
					annuncio.setUtente(utente);
					
					annunci.add(annuncio);
				}

				if (annuncio.getId() == resultSet.getInt(28)) {
					Foto foto = new Foto();
					foto.setId(resultSet.getInt(25));
					foto.setUrl(resultSet.getString(26)); 
					foto.setPrincipale(resultSet.getBoolean(27));
					foto.setAnnuncio(annuncio);
					annuncio.getFoto().add(foto);
				}
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			throw new DAOException(e.getMessage(), e);
		} finally {
			DBUtil.close(resultSet);
			DBUtil.close(statement);
		}
		return annunci;
	}
	
	@Override
	public List<Annuncio> findAllLimited(Connection connection) throws DAOException {
		List<Annuncio> annunci = new ArrayList<Annuncio>();
		int index = 0;
		System.out.println("QUERY DI FIND ALL LIMITED");
		String sql = "SELECT * FROM annuncio JOIN automobile ON annuncio.automobile_id = automobile.id JOIN indirizzo ON annuncio.indirizzo_id = indirizzo.id JOIN foto ON annuncio.id = foto.annuncio_id WHERE principale=1 LIMIT 4";
		System.out.println(sql);
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.prepareStatement(sql);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Annuncio annuncio = new Annuncio();
				if (index != resultSet.getInt(1)) {
					index = resultSet.getInt(1);
					annuncio.setId(resultSet.getInt(1));
					annuncio.setTitolo(resultSet.getString(2));
					annuncio.setDescrizione(resultSet.getString(3));

					// System.out.println(annuncio);

					Automobile automobile = new Automobile();
					automobile.setId(resultSet.getInt(5));
					automobile.setMarca(resultSet.getString(8));
					automobile.setModello(resultSet.getString(9));
					automobile.setAnno(resultSet.getInt(10));
					automobile.setPrezzo(resultSet.getInt(11));
					automobile.setKm(resultSet.getInt(12));
					automobile.setCarburante(Carburante.fromValue(resultSet.getString(13)));
					automobile.setNumeroPorte(NumeroPorte.fromValue(resultSet.getInt(14)));
					
					// System.out.println(automobile);
					annuncio.setAutomobile(automobile);
					automobile.setAnnuncio(annuncio);
					
					
					Indirizzo indirizzo = new Indirizzo();
					indirizzo.setId(resultSet.getInt(15));
					indirizzo.setCitta(resultSet.getString(16));
					indirizzo.setProvincia(resultSet.getString(17));
					
					annuncio.setIndirizzo(indirizzo);
					indirizzo.setAnnuncio(annuncio);
					
					annunci.add(annuncio);
				}

				if (annuncio.getId() == resultSet.getInt(21)) {
					Foto foto = new Foto();
					foto.setId(resultSet.getInt(18));
					foto.setUrl(resultSet.getString(19)); 
					foto.setPrincipale(resultSet.getBoolean(20));
					foto.setAnnuncio(annuncio);
					annuncio.getFoto().add(foto);
				}
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			throw new DAOException(e.getMessage(), e);
		} finally {
			DBUtil.close(resultSet);
			DBUtil.close(statement);
		}
		return annunci;
	}

	@Override
	public List<Annuncio> findFiltered(Connection connection, String marca, String modello, int prezzoMin,
			int prezzoMax, String orderBy) throws DAOException {
		String sql = "SELECT * FROM annuncio JOIN automobile ON annuncio.automobile_id = automobile.id JOIN indirizzo ON annuncio.indirizzo_id = indirizzo.id JOIN foto ON annuncio.id = foto.annuncio_id";
		List<Annuncio> annunci = new ArrayList<Annuncio>();
		int index = 0;
		if (marca != "" || prezzoMin > 0 || prezzoMax > 0) {
			sql += " WHERE";
			if (marca != "") {
				sql += " marca LIKE ?";
			}
			if (modello != "") {
				sql += " AND modello LIKE ?";
			}
			if (prezzoMin > 0 && prezzoMax == 0) {
				if (marca != "") {
					sql += " AND prezzo>=?";
				} else {
					sql += " prezzo>=?";
				}
			}
			if (prezzoMin == 0 && prezzoMax > 0) {
				if (marca != "") {
					sql += " AND prezzo<=?";
				} else {
					sql += " prezzo<=?";
				}
			}
			if (prezzoMin > 0 && prezzoMax > 0) {
				if (marca != "") {
					sql += " AND prezzo BETWEEN ? AND ?";
				} else {
					sql += " prezzo BETWEEN ? AND ?";
				}
			}
		}
		if (orderBy != null) {
			sql += " ORDER BY " + orderBy;
			System.out.println(orderBy);
		}
		System.out.println(sql);
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.prepareStatement(sql);
			if (marca != "") {
				statement.setString(1, "%" + marca + "%");
				if (modello != "") {
					statement.setString(2, "%" + modello + "%");
					if (prezzoMin > 0 && prezzoMax == 0) {
						statement.setInt(3, prezzoMin);
					} else if (prezzoMin == 0 && prezzoMax > 0) {
						statement.setInt(3, prezzoMax);
					} else if (prezzoMin > 0 && prezzoMax > 0) {
						statement.setInt(3, prezzoMin);
						statement.setInt(4, prezzoMax);
					}
				}
				if (modello == "") {
					if (prezzoMin > 0 && prezzoMax == 0) {
						statement.setInt(2, prezzoMin);
					} else if (prezzoMin == 0 && prezzoMax > 0) {
						statement.setInt(2, prezzoMax);
					} else if (prezzoMin > 0 && prezzoMax > 0) {
						statement.setInt(2, prezzoMin);
						statement.setInt(3, prezzoMax);
					}
				}
			} else {
				if (prezzoMin > 0 && prezzoMax == 0) {
					statement.setInt(1, prezzoMin);
				} else if (prezzoMin == 0 && prezzoMax > 0) {
					statement.setInt(1, prezzoMax);
				} else if (prezzoMin > 0 && prezzoMax > 0) {
					statement.setInt(1, prezzoMin);
					statement.setInt(2, prezzoMax);
				}
			}

			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Annuncio annuncio = new Annuncio();
				if (index != resultSet.getInt(1)) {
					index = resultSet.getInt(1);
					annuncio.setId(resultSet.getInt(1));
					annuncio.setTitolo(resultSet.getString(2));
					annuncio.setDescrizione(resultSet.getString(3));

					// System.out.println(annuncio);

					Automobile automobile = new Automobile();
					automobile.setId(resultSet.getInt(5));
					automobile.setMarca(resultSet.getString(8));
					automobile.setModello(resultSet.getString(9));
					automobile.setAnno(resultSet.getInt(10));
					automobile.setPrezzo(resultSet.getInt(11));
					automobile.setKm(resultSet.getInt(12));
					automobile.setCarburante(Carburante.fromValue(resultSet.getString(13)));
					automobile.setNumeroPorte(NumeroPorte.fromValue(resultSet.getInt(14)));
					
					// System.out.println(automobile);
					annuncio.setAutomobile(automobile);
					automobile.setAnnuncio(annuncio);
					
					
					Indirizzo indirizzo = new Indirizzo();
					indirizzo.setId(resultSet.getInt(15));
					indirizzo.setCitta(resultSet.getString(16));
					indirizzo.setProvincia(resultSet.getString(17));
					
					annuncio.setIndirizzo(indirizzo);
					indirizzo.setAnnuncio(annuncio);
					
					annunci.add(annuncio);
				}

				if (annuncio.getId() == resultSet.getInt(21)) {
					Foto foto = new Foto();
					foto.setId(resultSet.getInt(18));
					foto.setUrl(resultSet.getString(19)); 
					foto.setPrincipale(resultSet.getBoolean(20));
					foto.setAnnuncio(annuncio);
					annuncio.getFoto().add(foto);
				}
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			throw new DAOException(e.getMessage(), e);
		} finally {
			DBUtil.close(statement);
			DBUtil.close(resultSet);
		}
		return annunci;
	}

	@Override
	public Annuncio findById(Connection connection, int id) throws DAOException {
		String sql = "SELECT * FROM annuncio WHERE id=?";
		System.out.println(sql);
		Annuncio annuncio = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.prepareStatement(sql);
			statement.setInt(1, id);
			resultSet = statement.executeQuery();
			if (resultSet.next()) {
				annuncio = new Annuncio();
				annuncio.setId(resultSet.getInt(1));
				annuncio.setTitolo(resultSet.getString(2));
				annuncio.setDescrizione(resultSet.getString(3));
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			throw new DAOException(e.getMessage(), e);
		} finally {
			DBUtil.close(statement);
			DBUtil.close(resultSet);
		}
		return annuncio;
	}

	@Override
	public void saveAnnuncio(Connection connection, Annuncio annuncio) throws DAOException {
		String sql = "INSERT INTO annuncio(titolo,descrizione,utente_id,automobile_id,indirizzo_id) VALUES(?,?,?,?,?)";
		System.out.println(sql);
		PreparedStatement statement = null;
		ResultSet generatedKeys = null;
		try {
			statement = connection.prepareStatement(sql, new String[] { "id" });
			statement.setString(1, annuncio.getTitolo());
			statement.setString(2, annuncio.getDescrizione());
			statement.setInt(3, annuncio.getUtente().getId());
			statement.setInt(4, annuncio.getAutomobile().getId());
			statement.setInt(5, annuncio.getIndirizzo().getId());
			statement.executeUpdate();
			generatedKeys = statement.getGeneratedKeys();
			if (generatedKeys.next()) {
				int id = generatedKeys.getInt(1);
				annuncio.setId(id);
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			throw new DAOException(e.getMessage(), e);
		} finally {
			DBUtil.close(generatedKeys);
			DBUtil.close(statement);
		}
	}

	@Override
	public Annuncio findDettaglioById(Connection connection, int id) throws DAOException {
		String sql = "SELECT * FROM annuncio JOIN indirizzo ON annuncio.indirizzo_id = indirizzo.id JOIN utente ON annuncio.utente_id = utente.id JOIN automobile ON automobile.id = annuncio.automobile_id JOIN foto ON annuncio.id = foto.annuncio_id WHERE annuncio.id=?";
		System.out.println("QUERY DI FIND DETTAGLIO BY ID");
		System.out.println(sql);
		Annuncio annuncio = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.prepareStatement(sql);
			statement.setInt(1, id);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				if (annuncio == null) {
					annuncio = new Annuncio();
					annuncio.setId(resultSet.getInt(1));
					annuncio.setTitolo(resultSet.getString(2));
					annuncio.setDescrizione(resultSet.getString(3));

					Utente utente = new Utente();
					utente.setId(resultSet.getInt(10));
					utente.setNome(resultSet.getString(11));
					utente.setCognome(resultSet.getString(12));
					utente.setEmail(resultSet.getString(13));
					utente.setTelefono(resultSet.getLong(14));

					Indirizzo indirizzo = new Indirizzo();
					indirizzo.setId(resultSet.getInt(7));
					indirizzo.setCitta(resultSet.getString(8));
					indirizzo.setProvincia(resultSet.getString(9));

					Automobile automobile = new Automobile();
					automobile.setId(resultSet.getInt(17));
					automobile.setMarca(resultSet.getString(18));
					automobile.setModello(resultSet.getString(19));
					automobile.setAnno(resultSet.getInt(20));
					automobile.setPrezzo(resultSet.getInt(21));
					automobile.setKm(resultSet.getInt(22));
					automobile.setCarburante(Carburante.fromValue(resultSet.getString(23)));
					automobile.setNumeroPorte(NumeroPorte.fromValue(resultSet.getInt(24)));

					annuncio.setAutomobile(automobile);
					annuncio.setUtente(utente);
					annuncio.setIndirizzo(indirizzo);

					automobile.setAnnuncio(annuncio);
					indirizzo.setAnnuncio(annuncio);
				}

				Foto foto = new Foto();
				foto.setId(resultSet.getInt(25));
				foto.setUrl(resultSet.getString(26));
				foto.setPrincipale(resultSet.getBoolean(27));

				foto.setAnnuncio(annuncio);

				annuncio.getFoto().add(foto);

			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			throw new DAOException(e.getMessage(), e);
		} finally {
			DBUtil.close(resultSet);
			DBUtil.close(statement);
		}
		return annuncio;
	}
	
	public void deleteAnnuncio(Connection connection, Annuncio annuncio) throws DAOException {
		String sql = "DELETE FROM annuncio WHERE id=?";
		System.out.println(sql);
		// Statement statement = null;
		PreparedStatement statement = null;
		try {
			// statement = connection.createStatement();
			statement = connection.prepareStatement(sql);
			statement.setInt(1, annuncio.getId());
			// statement.executeUpdate(sql);
			statement.executeUpdate();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			throw new DAOException(e.getMessage(), e);
		} finally {
			DBUtil.close(statement);
		}
		
	}

}