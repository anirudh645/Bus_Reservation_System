import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import java.text.SimpleDateFormat;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/alltickets")
public class View_All_Tickets extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try (PrintWriter out = response.getWriter()) { // Use try-with-resources

            // Get user email from HttpSession
            HttpSession session = request.getSession(false);
            String userEmail = null;
            if (session != null) {
                userEmail = (String) session.getAttribute("email");
            }

            if (userEmail == null) {
                out.println("<p>You are not logged in. Please login to view tickets.</p>");
                out.println("<a href=\"index.html\" > Login</a>");
                return;
            }

            Connection connection = null;
            PreparedStatement stmt = null;
            PreparedStatement routeStmt = null;
            PreparedStatement busStmt = null;
            ResultSet ticketResult = null;
            ResultSet routeResult = null;
            ResultSet busResult = null;
            String url = "jdbc:mysql://localhost:3306/bus_reservation";
<<<<<<< HEAD
            String username = "root";
            String password = "";
=======
<<<<<<< HEAD
            String username = "busappuser";
            String password = "C-DfK%H^_fv@7zM";
=======
            String username = "root";
            String password = "";
>>>>>>> f060c9a3e93416fae95530a84014f99340188871
>>>>>>> a7d5e10a67eab23be89acb8242e2141d20b65102

            try {
                // Replace with your database connection details
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(url, username, password);

                // Prepare statement to retrieve tickets based on user email
                stmt = connection.prepareStatement("SELECT * FROM tickets");
           
                ticketResult = stmt.executeQuery();

                out.println("<h1>Your Tickets</h1>");
                out.println("<table border='1'>");
                out.println("<tr>");
                out.println("<th>Route</th><th>Bus Registration Number</th><th>Number of Tickets</th><th>Fare Price</th><th>Travel Date</th><th>Starting Time</th><th>User email</th>");
                out.println("</tr>");

                // Create a SimpleDateFormat object for date formatting
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                // Display tickets with additional information
                while (ticketResult.next()) {
                	String userEmail1 = ticketResult.getString("user_email");
                    int routeId = ticketResult.getInt("route_id");
                    int busId = ticketResult.getInt("bus_id");
                    int numTickets = ticketResult.getInt("number_of_tickets");
                    double fare = ticketResult.getDouble("fare_price");
                    String travelDateStr = ticketResult.getDate("travel_date").toString();
                    String startTimeStr = ticketResult.getTime("starting_time").toString();

                    // Retrieve source and destination city using route ID
                    routeStmt = connection.prepareStatement("SELECT source_city, destination_city FROM routes WHERE route_id = ?");
                    routeStmt.setInt(1, routeId);
                    routeResult = routeStmt.executeQuery();
                    String sourceCity = "";
                    String destinationCity = "";
                    if (routeResult.next()) {
                        sourceCity = routeResult.getString("source_city");
                        destinationCity = routeResult.getString("destination_city");
                    }

                    // Retrieve bus registration number using bus ID
                    busStmt = connection.prepareStatement("SELECT registration_number FROM buses WHERE bus_id = ?");
                    busStmt.setInt(1, busId);
                    busResult = busStmt.executeQuery();
                    String registrationNumber = "";
                    if (busResult.next()) {
                        registrationNumber = busResult.getString("registration_number");
                    }

                    // Display ticket details with formatted date
                    out.println("<tr>");
                    out.println("<td>  " + sourceCity + " - " + destinationCity + "   </td>");
                    out.println("<td>" + registrationNumber + "</td>");
                    out.println("<td>" + numTickets + "</td>");
                    out.println("<td>" + fare + "</td>");
                    out.println("<td>" + sdf.format(sdf.parse(travelDateStr)) + "</td>"); // Formatted date
                    out.println("<td>" + startTimeStr + "</td>");
                    out.println("<td>"+userEmail1+"</td>");
                    out.println("</tr>");
                }
                out.println("</table>");

            } catch (Exception e) {
                // Improved error handling
                e.printStackTrace(System.err); // Log the exception for debugging
                out.println("<p>An error occurred while retrieving your tickets. Please try again later.</p>");
            } finally {
                // Close resources
                try {
                    if (ticketResult != null) {
                        ticketResult.close();
                    }
                    if (routeResult != null) {
                        routeResult.close();
                    }
                    if (busResult != null) {
                        busResult.close();
                    }
                    if (stmt != null) {
                        stmt.close();
                    }
                    if (routeStmt != null) {
                        routeStmt.close();
                    }
                    if (busStmt != null) {
                        busStmt.close();
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
