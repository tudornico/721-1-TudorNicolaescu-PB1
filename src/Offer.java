public class Offer {
    private int ID;
    private String name;
    private double price;
    private double VAT;
    private String address;
    private Place place;

    @Override
    public String toString() {
        return "ID: " + ID + "\nName: " + name + "\nPrice: " + price + "\nVAT: " + VAT + "\nAddress: " + address
                + "\nPlace: " + place + "\n";
    }

    public Offer(String line){
        // we split the file
        String[] fields = line.split("&");
        if (fields.length != 6)
            throw new RuntimeException("Invalid Line!");
        ID = Integer.parseInt(fields[0]);
        name = fields[1];
        price = Double.parseDouble(fields[2]);
        VAT = Double.parseDouble(fields[3]);
        address = fields[4];
        //and we create the switch case we need
        switch (fields[5]){
            case "Zurich" -> place = Place.ZURICH;
            case "St. Gallen" -> place = Place.ST_GALLEN;
            case "Thurgau" -> place = Place.THURGAU;
            default -> {
                throw new RuntimeException("Invalid address!" + fields[5].length());
            }
        }
    }

    public double getFinalPrice(){
        return price + price * VAT / 100;
    } // calculate the price with the TVA

    public String getFileString(){
        String placeString = "";
        switch (place)
        {
            case ZURICH -> placeString = "Zurich\n";
            case THURGAU -> placeString = "Thurgau\n";
            case ST_GALLEN -> placeString = "St. Gallen\n";
        }
        return ID + "&" + name + "&" + price + "&" + VAT + "&" + address + "&" + placeString;
    }

    public Place getPlace(){
        return place;
    }
}
