import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException; // Import SQLException for specific catching
import java.sql.Date;       // Import java.sql.Date for journeyDate
import java.sql.Time;       // Import java.sql.Time for startTime

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/search-buses")
public class BookingServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    // It's generally better to make these constants or load them from a config file
    // For simplicity, they are kept here for now but recommended to externalize
    private static final String DB_URL = "jdbc:mysql://localhost:3306/bus_reservation";
    private static final String DB_USERNAME = "busappuser";
    private static final String DB_PASSWORD = "C-DfK%H^_fv@7zM"; // **WARNING: Hardcoded password is a security risk!**

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(true);
        String userEmail = (String) session.getAttribute("email");

        // Basic validation for userEmail
        if (userEmail == null || userEmail.trim().isEmpty()) {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<p style='color:red;'>User not logged in. Please log in to book tickets.</p>");
            out.println("<a href=\"login.html\">Go to Login</a>"); // Assuming a login page
            return; // Stop processing
        }

        String fromCity = request.getParameter("fromCity");
        String toCity = request.getParameter("toCity");
        String busType = request.getParameter("busType"); // Corrected variable name for clarity
        String journeyDateStr = request.getParameter("journeyDate");
        String timeStr = request.getParameter("startTime");

        int ticketCounter;
        try {
            ticketCounter = Integer.parseInt(request.getParameter("numTickets"));
        } catch (NumberFormatException e) {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<p style='color:red;'>Invalid number of tickets provided.</p>");
            out.println("<a href=\"Home.html\">Go Home</a>");
            e.printStackTrace(); // Log the exception for debugging
            return; // Stop processing
        }

        Connection connection = null;
        PreparedStatement stmt = null;
        PreparedStatement stmt1 = null;
        PreparedStatement stmt2 = null;
        PreparedStatement stmt3 = null;
        ResultSet routeResult = null;
        ResultSet busIdResult = null; // Renamed 'busid' to 'busIdResult' for clarity
        ResultSet bookedTicketResult = null; // Renamed 'bookedticket' for clarity

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        double farePrice = 0; // Initialize farePrice

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

            // 1. Find route ID and fare price
            stmt = connection.prepareStatement("SELECT route_id, fare_price FROM routes WHERE source_city = ? AND destination_city = ?");
            stmt.setString(1, fromCity);
            stmt.setString(2, toCity);
            routeResult = stmt.executeQuery();

            int routeId = 0;
            if (routeResult.next()) {
                routeId = routeResult.getInt("route_id");
                farePrice = routeResult.getDouble("fare_price");
            } else {
                out.println("<p style='color:red;'>No route found for the selected cities.</p>");
                out.println("<a href=\"Home.html\">Go Home</a>");
                return; // Exit if no route found
            }

            // Adjust fare based on bus type using .equals() for string comparison
            if ("Non-AC Seater".equals(busType)) {
                farePrice = farePrice + 0; // No change, but explicit
            } else if ("Non-AC Sleeper".equals(busType)) { // Corrected typo here
                farePrice += 75;
            } else if ("AC Seater".equals(busType)) {
                farePrice += 150;
            } else if ("AC Sleeper".equals(busType)) {
                farePrice += 250;
            }
            // Add an 'else' if there can be other bustypes or a default value
            else {
                out.println("<p style='color:red;'>Invalid bus type selected.</p>");
                out.println("<a href=\"Home.html\">Go Home</a>");
                return; // Exit if invalid bus type
            }

            // 2. Find buses based on route ID, bus type, and starting time
            // route_id is an INT, so use setInt()
            stmt1 = connection.prepareStatement("SELECT bus_id, registration_number, seating_capacity FROM buses WHERE route_id = ? AND bus_type = ? AND starting_time = ?");
            stmt1.setInt(1, routeId); // Changed to setInt
            stmt1.setString(2, busType);
            stmt1.setString(3, timeStr); // Assuming timeStr is in a format compatible with DB TIME type
            busIdResult = stmt1.executeQuery();

            String resultantBusId = "";
            int resSeatingCapacity = 0;
            String busRegistration = ""; // To store registration number

            if (busIdResult.next()) {
                int bus_id_found = busIdResult.getInt("bus_id");
                busRegistration = busIdResult.getString("registration_number");
                resultantBusId = String.valueOf(bus_id_found);
                resSeatingCapacity = busIdResult.getInt("seating_capacity");

                // 3. Check already booked tickets for the chosen bus and date
                stmt2 = connection.prepareStatement("SELECT COUNT(*) as ticketCount FROM tickets WHERE travel_date = ? AND bus_id = ?");
                // Convert String to java.sql.Date for setDate()
                Date sqlJourneyDate = Date.valueOf(journeyDateStr);
                stmt2.setDate(1, sqlJourneyDate); // Using setDate
                stmt2.setInt(2, bus_id_found); // Use the integer bus_id
                bookedTicketResult = stmt2.executeQuery();
                bookedTicketResult.next(); // Move cursor to the first (and only) row
                int alreadyBooked = bookedTicketResult.getInt("ticketCount");

                if ((resSeatingCapacity - alreadyBooked) >= ticketCounter) {
                    farePrice = farePrice * ticketCounter;

                    // 4. Insert into tickets table
                    stmt3 = connection.prepareStatement("INSERT INTO tickets (user_email, route_id, bus_id, number_of_tickets, fare_price, travel_date, starting_time) VALUES (?, ?, ?, ?, ?, ?, ?)");
                    stmt3.setString(1, userEmail);
                    stmt3.setInt(2, routeId);
                    stmt3.setInt(3, bus_id_found); // Use the integer bus_id
                    stmt3.setInt(4, ticketCounter);
                    stmt3.setDouble(5, farePrice);
                    stmt3.setDate(6, sqlJourneyDate); // Using setDate
                    
                    // Convert timeStr to java.sql.Time if necessary, or ensure format matches DB TIME type
                    // Assuming timeStr is in "HH:MM" or "HH:MM:SS" format. Add ":00" if only "HH:MM"
                    Time sqlTime = Time.valueOf(timeStr.length() == 5 ? timeStr + ":00" : timeStr);
                    stmt3.setTime(7, sqlTime);

                    stmt3.executeUpdate();

                    // Display confirmation
                    out.print("<header style='color: blue; text-align:center'><h2>Ticket Confirmed</h2></header>");
                    out.print("<p><strong>User:</strong> " + userEmail + "</p>");
                    out.print("<p><strong>From:</strong> " + fromCity + "</p>");
                    out.print("<p><strong>To:</strong> " + toCity + "</p>");
                    out.print("<p><strong>Bus Registration Number:</strong> " + busRegistration + "</p>");
                    out.print("<p><strong>Travel Date:</strong> " + journeyDateStr + "</p>");
                    out.print("<p><strong>Starting Time:</strong> " + timeStr + "</p>");
                    out.print("<p><strong>Number of Tickets:</strong> " + ticketCounter + "</p>");
                    out.print("<hr>");
                    out.print("<p><strong>Total Fare Price:</strong> " + String.format("%.2f", farePrice) + "</p>"); // Format currency
                    out.print("<input type=\"button\" onclick=\"window.print()\" value=\"Print Ticket\">");
                    out.print("<a href=\"Home.html\" style='margin-left: 10px;'>Home</a>");
                    out.print("<footer style='color: blue; text-align:center'><p>&copy; Easy Bus</p></footer>");

                } else {
                    out.println("<p style='color:red;'>Not enough tickets available for this bus on " + journeyDateStr + ".</p>");
                    out.println("<p>Available seats: " + (resSeatingCapacity - alreadyBooked) + "</p>");
                    out.println("<a href=\"Home.html\">Go Home</a>");
                }
            } else {
                out.println("<p style='color:red;'>No buses found for the selected route, type, and time.</p>");
                out.println("<a href=\"Home.html\">Go Home</a>");
            }

        } catch (ClassNotFoundException e) {
            out.println("<p style='color:red;'>JDBC Driver not found. Please check your setup.</p>");
            e.printStackTrace();
        } catch (SQLException e) {
            out.println("<p style='color:red;'>Database error occurred: " + e.getMessage() + "</p>");
            e.printStackTrace();
        } catch (Exception e) {
            out.println("<p style='color:red;'>An unexpected error occurred. Please try again.</p>");
            e.printStackTrace();
        } finally {
            // Close resources in reverse order of creation
            try {
                if (bookedTicketResult != null) {
                    bookedTicketResult.close();
                }
                if (busIdResult != null) {
                    busIdResult.close();
                }
                if (routeResult != null) {
                    routeResult.close();
                }
                if (stmt3 != null) {
                    stmt3.close();
                }
                if (stmt2 != null) {
                    stmt2.close();
                }
                if (stmt1 != null) {
                    stmt1.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // Log closing errors, don't just print to console in production
                e.printStackTrace();
            }
            out.close(); // Close the PrintWriter
        }
    }
}