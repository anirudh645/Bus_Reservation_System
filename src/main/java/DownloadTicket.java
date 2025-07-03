import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/downloadTicket")
public class DownloadTicket extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private String jdbcURL = "jdbc:mysql://localhost:3306/bus_reservation";
    private String jdbcUsername = "root";
    private String jdbcPassword = ""; // Replace with your actual password

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int ticketId = Integer.parseInt(request.getParameter("ticketId"));

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"ticket_" + ticketId + ".pdf\"");

        try (Connection connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT t.ticket_id, r.source_city, r.destination_city, b.registration_number, t.number_of_tickets, t.fare_price, t.travel_date, t.starting_time, u.email " +
                     "FROM tickets t " +
                     "JOIN routes r ON t.route_id = r.route_id " +
                     "JOIN buses b ON t.bus_id = b.bus_id " +
                     "JOIN users u ON t.user_email = u.email " +
                     "WHERE t.ticket_id = ?")) {
            preparedStatement.setInt(1, ticketId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                // Create a PDF document using iText
                PdfWriter writer = new PdfWriter(response.getOutputStream());
                PdfDocument pdf = new PdfDocument(writer);
                Document document = new Document(pdf);

                document.add(new Paragraph("Ticket ID: " + resultSet.getInt("ticket_id")));
                document.add(new Paragraph("User Email: " + resultSet.getString("email")));
                document.add(new Paragraph("Route: " + resultSet.getString("source_city") + " - " + resultSet.getString("destination_city")));
                document.add(new Paragraph("Bus: " + resultSet.getString("registration_number")));
                document.add(new Paragraph("Number of Tickets: " + resultSet.getInt("number_of_tickets")));
                document.add(new Paragraph("Fare Price: " + resultSet.getDouble("fare_price")));
                 // Format the date and time as needed
                document.add(new Paragraph("Travel Date: " + resultSet.getDate("travel_date").toString()));
                document.add(new Paragraph("Starting Time: " + resultSet.getTime("starting_time").toString()));

                document.close();
            } else {
                response.getWriter().println("Ticket not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().println("Error fetching ticket details.");
        }
    }
}