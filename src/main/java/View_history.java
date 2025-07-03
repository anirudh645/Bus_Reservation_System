import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/viewTickets")
public class View_history extends HttpServlet {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Perform any necessary processing based on user interaction
        // (e.g., retrieve data from database, session, etc.)

        // Forward request to a JSP for displaying content
        request.getRequestDispatcher("view_history.jsp").forward(request, response);
    }
}
