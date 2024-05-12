package hu.inf.koncertjegy;

public class Concert {
    private String performer;
    private String location;
    private String date;
    private int quantity;
    private float ratedInfo;
    private final int imageResource;

    public Concert(String performer, String location, String date, int quantity, float ratedInfo, int imageResource) {
        this.performer = performer;
        this.location = location;
        this.date = date;
        this.quantity = quantity;
        this.ratedInfo = ratedInfo;
        this.imageResource = imageResource;
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

    public float getRatedInfo() {
        return ratedInfo;
    }

    public int getImageResource() {
        return imageResource;
    }
}
