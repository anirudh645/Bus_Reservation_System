import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
<<<<<<< HEAD

=======
<<<<<<< HEAD
import io.github.cdimascio.dotenv.Dotenv;
=======

>>>>>>> f060c9a3e93416fae95530a84014f99340188871
>>>>>>> a7d5e10a67eab23be89acb8242e2141d20b65102
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginVerification extends HttpServlet {
<<<<<<< HEAD
=======
<<<<<<< HEAD
    private static final long serialVersionUID = 1L;    
    String DB_URL = System.getenv("DB_URL");
    String DB_USER = System.getenv("DB_USER");
    String DB_PASSWORD = System.getenv("DB_PASSWORD");


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
=======
>>>>>>> a7d5e10a67eab23be89acb8242e2141d20b65102
    private static final long serialVersionUID = 1L;

    // Assuming XAMPP MySQL is running on localhost port 3306 with database name
    // bus_reservation
    // Replace with your actual database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/bus_reservation";
    private static final String DB_USER = "root"; // Replace with your MySQL username
    private static final String DB_PASSWORD = ""; // Replace with your MySQL password

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
<<<<<<< HEAD
=======
>>>>>>> f060c9a3e93416fae95530a84014f99340188871
>>>>>>> a7d5e10a67eab23be89acb8242e2141d20b65102

        String email = request.getParameter("Email");
        String password = request.getParameter("Password");

        PrintWriter out = response.getWriter();

        // Database connection and credential validation
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
<<<<<<< HEAD
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM Users WHERE email = ? AND password = ?");
=======
<<<<<<< HEAD
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE email = ? AND password = ?");
=======
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM Users WHERE email = ? AND password = ?");
>>>>>>> f060c9a3e93416fae95530a84014f99340188871
>>>>>>> a7d5e10a67eab23be89acb8242e2141d20b65102
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
<<<<<<< HEAD
=======
<<<<<<< HEAD
}
=======
>>>>>>> f060c9a3e93416fae95530a84014f99340188871
>>>>>>> a7d5e10a67eab23be89acb8242e2141d20b65102
