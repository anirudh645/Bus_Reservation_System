import java.text.SimpleDateFormat;

public class TicketInfo {
    private String sourceCity;
    private String destinationCity;
    private String registrationNumber;
    private int numTickets;
    private double fare;
    private String travelDateStr;
    private String startTimeStr;
    private SimpleDateFormat sdf;

    public TicketInfo(String sourceCity, String destinationCity, String registrationNumber, int numTickets, double fare, String travelDateStr, String startTimeStr, SimpleDateFormat sdf) {
        this.sourceCity = sourceCity;
        this.destinationCity = destinationCity;
        this.registrationNumber = registrationNumber;
        this.numTickets = numTickets;
        this.fare = fare;
        this.travelDateStr = travelDateStr;
        this.startTimeStr = startTimeStr;
        this.sdf = sdf;
    }

    // Getter methods
    public String getSourceCity() {
        return sourceCity;
    }

    public String getDestinationCity() {
        return destinationCity;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public int getNumTickets() {
        return numTickets;
    }

    public double getFare() {
        return fare;
    }

    public String getTravelDateStr() {
        return travelDateStr;
    }

    public String getStartTimeStr() {
        return startTimeStr;
    }

    public SimpleDateFormat getSdf() {
        return sdf;
    }
}
