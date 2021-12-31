package it.generationitaly.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import it.generationitaly.dao.DAOException;
import it.generationitaly.dao.DBUtil;
import it.generationitaly.dao.UtenteDAO;
import it.generationitaly.model.Utente;

public class UtenteDAOImpl implements UtenteDAO {

	@Override
	public void saveUtente(Connection connection, Utente utente) throws DAOException {
		String sql ="INSERT INTO Utente(nome,cognome,email,telefono,username,password) VALUES(?,?,?,?,?,?)";
		System.out.println(sql);
		PreparedStatement statement = null;
        ResultSet generatedKeys = null;
        try {
            statement = connection.prepareStatement(sql, new String[] { "id" });
            statement.setString(1, utente.getNome());
            statement.setString(2, utente.getCognome());
            statement.setString(3, utente.getEmail());
            statement.setLong(4, utente.getTelefono());
            statement.setString(5, utente.getUsername());
            statement.setString(6, utente.getPassword());
            statement.executeUpdate();
            generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int id = generatedKeys.getInt(1);
                utente.setId(id);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            throw new DAOException(e.getMessage(), e);
        } finally {
            DBUtil.close(generatedKeys);
            DBUtil.close(statement); 
        }
	}

}