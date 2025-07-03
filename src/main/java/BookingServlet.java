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

@WebServlet("/search-buses")
public class BookingServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

		HttpSession session = request.getSession(true); // Create session if not already exists
		String userEmail = (String) session.getAttribute("email");
        String fromCity = request.getParameter("fromCity");
        String toCity = request.getParameter("toCity");
        String bustype = request.getParameter("busType");
        int ticketCounter = Integer.parseInt(request.getParameter("numTickets"));
        String journeyDateStr = request.getParameter("journeyDate");
        String timeStr = request.getParameter("startTime");
        // Replace with your actual database connection details
        String url = "jdbc:mysql://localhost:3306/bus_reservation";
        String username = "root";
        String password = "";
        String resultantbus_id = "";
        int resseatingcapacity = 0;

        Connection connection = null;
        PreparedStatement stmt = null;
        PreparedStatement stmt1 = null;
        PreparedStatement stmt2 = null;
        PreparedStatement stmt3 = null;
        ResultSet routeResult = null;
        ResultSet busid = null;
        ResultSet bookedticket = null;
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        PreparedStatement busStmt = null;
        ResultSet busResult = null;
        double farePrice = 0;

        try {
            // Connect to the database
        	
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, username, password);

            // Find route ID and fare price based on source and destination cities
            stmt = connection.prepareStatement("SELECT route_id, fare_price FROM routes WHERE source_city = ? AND destination_city = ?");
            stmt.setString(1, fromCity);
            stmt.setString(2, toCity);
            routeResult = stmt.executeQuery();
            int routeId = 0;
            if (routeResult.next()) {
                routeId = routeResult.getInt("route_id");
                farePrice = routeResult.getDouble("fare_price");
            }
            if (bustype == "Non-AC Seater") {
            	farePrice = farePrice+0;
            }
            else if (bustype == "Non-AC Sleepe") {
            	farePrice+= 75;
            }
            else if (bustype == "AC Seater") {
            	farePrice+=150;
            }
            else if (bustype == "AC Sleeper") {
            	farePrice+=250;
            }
            // Find buses based on the retrieved route ID (if any)
            if (routeId != 0) {
            	stmt1 = connection.prepareStatement("select * from buses where route_id = ? and bus_type = ? and starting_time = ?");
                stmt1.setString(1,String.valueOf(routeId));
                stmt1.setString(2, bustype);
                stmt1.setString(3,timeStr);
                busid = stmt1.executeQuery();
                if(busid.next()) {
                	int bus_id1 = busid.getInt("bus_id");
                	String bus_registration = busid.getString("registration_number");
                	resultantbus_id = String.valueOf(bus_id1);
                	resseatingcapacity = busid.getInt("seating_capacity");
                	stmt2 = connection.prepareStatement("SELECT COUNT(*) as ticketCount FROM tickets WHERE STR_TO_DATE(travel_date, '%Y-%m-%d') = ? AND bus_id = ?;");
                	stmt2.setString(1, journeyDateStr);
                	stmt2.setString(2, resultantbus_id);
                	bookedticket = stmt2.executeQuery();
                	bookedticket.next();
                	int alreadybooked = bookedticket.getInt("ticketCount");
                	if ((resseatingcapacity - alreadybooked) >= ticketCounter  ) {
                		farePrice = farePrice*ticketCounter;
            			stmt3 = connection.prepareStatement("INSERT INTO tickets (user_email, route_id, bus_id, number_of_tickets, fare_price, travel_date,starting_time) VALUES (?, ?, ?, ?, ?, ?, ?)");
            			stmt3.setString(1, userEmail);
            			stmt3.setInt(2, routeId);
            			stmt3.setInt(3, bus_id1);
            			stmt3.setInt(4, ticketCounter);
            			stmt3.setDouble(5, farePrice);  // Use setBigDecimal for decimal values
            			stmt3.setString(6, journeyDateStr);  // Assuming travelDate is a java.util.Date object
            			stmt3.setString(7, timeStr);
            			stmt3.executeUpdate();
            			out.print("<header style= 'color: blue; text-align:center'>Ticket Confirmed</header>");
            			out.print("<p>User: </p>"+userEmail);
            			out.print("<p>From: </p>"+fromCity+"<br>");
            			out.print("<p>To: </p>"+toCity+"<br>");
            			out.print("<p>Bus Registration number : "+bus_registration+"</p><br>");
            			out.print("<p>Travel Date: "+journeyDateStr+"</p><br>");
            			out.print("<p>Starting Time: "+timeStr+"<br>");
            			out.print("<p>Number of Tickets: "+ticketCounter+"<br><hr>");
            			out.print("<p>Total Fare price : "+farePrice+"</p><br>");
            			out.print("<input type=\"button\" onclick=\"window.print()\" value=\"Print\">");
            			out.print("<a href= \"Home.html\" >Home </a>");
            			out.print("<footer style= 'color: blue; text-align:center'>@Easy Bus </footer>");
                	}
                	else {
                		out.print("<p>Not Enough Tickets</p>");
                	}
                	
                }
                
            } else {
                out.print("<p>No buses found for the selected route.</p>");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close resources
            try {
                if (routeResult != null) {
                    routeResult.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (busResult != null) {
                    busResult.close();
                }
                if (busStmt != null) {
                    busStmt.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } 
	}
}