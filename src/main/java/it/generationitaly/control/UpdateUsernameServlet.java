package it.generationitaly.control;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import it.generationitaly.model.Utente;
import it.generationitaly.service.ServiceException;
import it.generationitaly.service.UtenteService;
import it.generationitaly.service.impl.UtenteServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class UpdateUsernameServlet
 */
@WebServlet("/update-username")
public class UpdateUsernameServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private UtenteService utenteService = new UtenteServiceImpl();
	private List<Utente> utenti = new ArrayList<Utente>();

	/**
	 * Default constructor.
	 */
	public UpdateUsernameServlet() {
		// TODO Auto-generated constructor stub
	}
//da correggere
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (request.getSession().getAttribute("username") == null) {
			response.sendRedirect("index.jsp?hasErrorsLogin");
			return;
		}

	//	String username = request.getParameter("username");
		String newUsername = request.getParameter("newUsername");
		String confirmUsername = request.getParameter("confirmUsername");
		
		try {
			Utente utente = utenteService.findByUsername((String) (request.getSession().getAttribute("username")));
			//Utente utente = utenteService.findByUsername(username);
			utenti = utenteService.findAllUtenti();			
			if (newUsername.equals(confirmUsername)) {
				System.out.println("sono entrato nel primo if");
				utente.setUsername(newUsername);
				request.setAttribute("utente", utente);
			} else {	
				request.setAttribute("utente", utente);
				request.getRequestDispatcher("profile-settings?usernameDiversi").forward(request, response);
				return;
				}
				for (Utente utente2 : utenti) {
				//	if (utente.getUsername().equals(utente2.getUsername())) {
					if (utente.getUsername().equals(utente2.getUsername())) {
						System.out.println("Username non cambiato");
						request.getRequestDispatcher("profile-settings.jsp?usernameDuplicato").forward(request, response);	
						return;
					} 					
				}
				utenteService.updateUsername(utente);
				System.out.println("Username cambiato merda");
				request.getSession().invalidate();
				request.getSession().setAttribute("username", utente.getUsername());
				request.getRequestDispatcher("profile-settings.jsp?usernameModificato").forward(request, response);						
				return;					
		} catch (ServiceException e) {
			System.err.println(e.getMessage());
		}

	}

}
