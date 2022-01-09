package it.generationitaly.control;

import java.io.IOException;

import it.generationitaly.model.Messaggio;
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
 * Servlet implementation class InviaMessaggioServlet
 */
@WebServlet("/invia-messaggio")
public class InviaMessaggioServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	UtenteService utenteService = new UtenteServiceImpl();

	/**
	 * Default constructor.
	 */
	public InviaMessaggioServlet() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// il mittente sar� poi l'utente loggato in sessione
		// int mittente_id = Integer.parseInt(request.getParameter("mittenteId"));
		if(request.getSession().getAttribute("username") == null) {
			response.sendRedirect("index.jsp");
			return;
		}
		
		String username = (String) request.getSession().getAttribute("username");
		// il destinatario � colui che ha pubblicato l'annuncio su cui siamo noi
		int idDestinatario = Integer.parseInt(request.getParameter("idDestinatario"));
		String message = request.getParameter("message");
		Utente mittente = null;
		Utente destinatario = null;
		Messaggio messaggio = new Messaggio();
		try {
			mittente = utenteService.findByUsername(username);
			destinatario = utenteService.findById(idDestinatario);
			if(mittente.getId() == destinatario.getId()) {
				//errore 500 da inserire
				response.sendRedirect("404.jsp");
				return;
			} else {
				messaggio.setMittente(mittente);
				messaggio.setDestinatario(destinatario);
				messaggio.setCorpoMessaggio(message);
				utenteService.saveMessaggio(messaggio);
				response.sendRedirect("listing-detail.jsp");
			}
		} catch (ServiceException e) {
			System.err.println(e.getMessage());
		}

	}

}
