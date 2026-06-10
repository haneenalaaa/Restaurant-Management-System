package advancedproj;

public class Reservation implements Reportable {
    private String phone;
    private String name;
    private String tableId;
    private String time;
    private String guestNum;
    private String date;

    public Reservation(String phone, String name, String tableId, String time, String guestNum, String date) {
        this.phone = phone;
        this.name = name;
        this.tableId = tableId;
        this.time = time;
        this.guestNum = guestNum;
        this.date = date;
    }

    
    public String getPhone() { return phone; }
    public String getName() { return name; }
    public String getTableId() { return tableId; }
    public String getTime() { return time; }
    public String getGuestNum() { return guestNum; }
    public String getDate() { return date; }
    
      @Override
    public String generateReport() {
        return "--------------------------\n" +
               "   RESERVATION CONFIRMED  \n" +
               "--------------------------\n" +
               "Report Issued: " + getFormattedDate() + "\n" +
               "Guest Name: " + name + "\n" +
               "Table: " + tableId + " | Guests: " + guestNum + "\n" +
               "Booking: " + date + " at " + time + "\n" +
               "--------------------------";
    }}