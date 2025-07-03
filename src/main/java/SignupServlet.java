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

@WebServlet("/signup")
public class SignupServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try (PrintWriter out = response.getWriter()) { // Use try-with-resources
        	response.setContentType("text/html");
            String username = request.getParameter("username");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String confirmPassword = request.getParameter("confirm-password");
            String phone = request.getParameter("phone");
            String is_admin = request.getParameter("is_admin");

            // Basic validation (improve as needed)
            if (username == null || username.isEmpty()) {
                out.println("<p>Username cannot be empty.</p>");
                return;
            }
            if (email == null || email.isEmpty()) {
                out.println("<p>Email cannot be empty.</p>");
                return;
            }
            if (password == null || password.isEmpty()) {
                out.println("<p>Password cannot be empty.</p>");
                return;
            }
            if (!password.equals(confirmPassword)) {
                out.println("<p>Passwords do not match.</p>");
                return;
            }

            // Replace with your database connection details
            String url = "jdbc:mysql://localhost:3306/bus_reservation";
<<<<<<< HEAD
            String usernameDb = "busappuser";
            String passwordDb = "C-DfK%H^_fv@7zM";
=======
            String usernameDb = "root";
            String passwordDb = "";
>>>>>>> f060c9a3e93416fae95530a84014f99340188871

            Connection connection = null;
            PreparedStatement stmt = null;
            PreparedStatement checkStmt = null;
            ResultSet checkResult = null;

            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(url, usernameDb, passwordDb);

                // Check if username already exists
                checkStmt = connection.prepareStatement("SELECT * FROM users WHERE username = ?");
                checkStmt.setString(1, username);
                checkResult = checkStmt.executeQuery();
                
                boolean admin = false;
                if(is_admin!=null) {
                	admin=true;
                }

                if (checkResult.next()) {
                    out.println("<p>Username already exists. Please choose a different username.</p>");
                    return;
                }

                // Insert user data if username is unique
                stmt = connection.prepareStatement("INSERT INTO users (username, email, password, phone_number,is_admin) VALUES (?, ?, ?, ?,?)");
                stmt.setString(1, username);
                stmt.setString(2, email);
                stmt.setString(3, password); // Consider hashing password before storing
                stmt.setString(4, phone);
                stmt.setBoolean(5, admin);
                stmt.executeUpdate();
                out.println("<p>Registration successful! You can now log in.</p>");
                out.println("<a href=\"index.html\">Log in </a>");
            } catch (Exception e) {
                e.printStackTrace(System.err); // Log the exception for debugging
                out.println("<p>An error occurred during registration. Please try again later.</p>");
            } finally {
                // Close resources
                try {
                    if (checkResult != null) {
                        checkResult.close();
                    }
                    if (stmt != null) {
                        stmt.close();
                    }
                    if (checkStmt != null) {
                        checkStmt.close();
                    }
                    if (connection != null) {
                        connection.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace(System.err); // Log the exception for debugging
                }
            }
        }
    }
}
