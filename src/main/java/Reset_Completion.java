import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/reset")
public class Reset_Completion extends HttpServlet {
    private static final long serialVersionUID = 1L;
    PreparedStatement checkStmt = null;
    ResultSet checkResult = null;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/bus_reservation";
<<<<<<< HEAD
    private static final String DB_USER = "root"; // Replace with your MySQL username
    private static final String DB_PASSWORD = ""; // Replace with your MySQL password
=======
<<<<<<< HEAD
    private static final String DB_USER = "busappuser"; // Replace with your MySQL username
    private static final String DB_PASSWORD = "C-DfK%H^_fv@7zM"; // Replace with your MySQL password
=======
    private static final String DB_USER = "root"; // Replace with your MySQL username
    private static final String DB_PASSWORD = ""; // Replace with your MySQL password
>>>>>>> f060c9a3e93416fae95530a84014f99340188871
>>>>>>> a7d5e10a67eab23be89acb8242e2141d20b65102

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        PrintWriter out = response.getWriter();
        response.setContentType("text/html");
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirm-password");
        if (!password.equals(confirmPassword)) {
            out.println("<p>Passwords do not match.</p>");
            return;
        }

        // Database connection and credential validation
        try {
        	Class.forName("com.mysql.cj.jdbc.Driver");
        	Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            checkStmt = conn.prepareStatement("SELECT * FROM users WHERE username = ? and email = ?");
            checkStmt.setString(1, username);
            checkStmt.setString(2, email);
            checkResult = checkStmt.executeQuery();
            if(checkResult.next()) {
            	String userid = checkResult.getString("user_id");
            	PreparedStatement ps = conn.prepareStatement("UPDATE users SET password = ? WHERE user_id = ?;");
            	ps.setString(1, password);
            	ps.setString(2, userid);
            	ps.executeUpdate();
            	out.print("<p> Password Successfully reset</p>");
            	out.print("<a href=\"index.html\">Log in </a>");
            }
//            response.sendRedirect("index.html");
        } catch (Exception e) {
            e.printStackTrace(out); // Log the error for debugging
        }
    }
}
