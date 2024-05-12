package hu.inf.koncertjegy;

public class Ticket {
    private String performer;
    private String location;
    private String date;
    private int quantity;
    private String image;

    public Ticket(String performer, String location, String date, int quantity, String image) {
        this.performer = performer;
        this.location = location;
        this.date = date;
        this.quantity = quantity;
        this.image = image;
    }

    public String getPerformer() {
        return performer;
    }

    public String getLocation() {
        return location;
    }

    public String getDate() {
        return date;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getImage() {
        return image;
    }
}
