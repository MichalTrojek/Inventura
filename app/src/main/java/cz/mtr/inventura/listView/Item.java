package cz.mtr.inventura.listView;

public class Item {

    private String ean;
    private int amount;
    private String location;


    public Item(String ean, int amount, String location) {
        this.ean = ean;
        this.amount = amount;
        this.location = location;
    }

    public void setEan(String ean) {
        this.ean = ean;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEan() {
        return ean;
    }

    public int getAmount() {
        return amount;
    }

    public String getLocation() {
        return location;
    }
}
