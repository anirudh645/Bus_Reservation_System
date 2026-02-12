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
import javax.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginVerification extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Assuming XAMPP MySQL is running on localhost port 3306 with database name
    // bus_reservation
    // Replace with your actual database connection details
    private static final String DB_URL = "jdbc:mysql://bus_reservation_db:3306/bus_reservation";
    private static final String DB_USER = "bus_user"; // Replace with your MySQL username
    private static final String DB_PASSWORD = "bus_password"; // Replace with your MySQL password

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("Email");
        String password = request.getParameter("Password");

        PrintWriter out = response.getWriter();

        // Database connection and credential validation
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE email = ? AND password = ?");
            ps.setString(1, email);
            ps.setString(2, password); // Hash password before storing in DB - see previous security note

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // Login successful, create session and store username and email
                HttpSession session = request.getSession(true); // Create session if not already exists
                session.setAttribute("username", rs.getString("username")); // Get username from DB result
                session.setAttribute("email", email);

                // Redirect to home page after successful login
                if(rs.getBoolean("is_admin")) {
                	response.sendRedirect("adminhome.html");
                }
                else {
                	response.sendRedirect("Home.html");
                }
            } else {
                // Login failed, set error message as response
                response.sendRedirect("index.html");
            }
        } catch (Exception e) {
            e.printStackTrace(out); // Log the error for debugging
        }
    }
}
