package it.generationitaly.service;

import java.util.List;

import it.generationitaly.model.Annuncio;
import it.generationitaly.model.Automobile;
import it.generationitaly.model.Indirizzo;
import it.generationitaly.model.Utente;

public interface AnnuncioService {
	
	List<Annuncio> findAll() throws ServiceException;
	
	List<Annuncio> findFiltered(String marca, String modello, int prezzo, String orderBy) throws ServiceException;
	
	Annuncio findById(int id) throws ServiceException;
	
	List<Indirizzo> findAllIndirizzi() throws ServiceException;
	
	void saveAnnuncio(Annuncio annuncio) throws ServiceException;
	
	Automobile carFindById(int id) throws ServiceException;
	
	List<Automobile> carFindAll() throws ServiceException;
	
	Annuncio findDettaglioById(int id) throws ServiceException;
}
