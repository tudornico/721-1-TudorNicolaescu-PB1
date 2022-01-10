import java.io.*;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args){

        // read from "offerten.txt" and save everything into offerList
        BufferedReader reader;
        List<Offer> offerList = new LinkedList<>();
        try{
            reader = new BufferedReader(new FileReader("offerten.txt"));
            String line = reader.readLine();
            while (line != null){
                Offer offer = new Offer(line);
                offerList.add(offer);
                line = reader.readLine();
            }
            reader.close();
        }
        catch (IOException exception){
            exception.printStackTrace();
            return;
        }

        // print everything out to see if it's ok
        for (Offer offer : offerList)
            System.out.println(offer);

        // sort the list of offers by their final price
        offerList.sort(
                (Offer o1, Offer o2) -> Double.compare(o2.getFinalPrice(), o1.getFinalPrice())
        );


        System.out.println("\n\n\nSORTED:\n");
        // print everything out to see if it's sorted
        for (Offer offer : offerList) {
            double finalPrice = offer.getFinalPrice();
            String finalString = offer + "FinalPrice: " + finalPrice + "\n";
            System.out.println(finalString);
        }

        // write everything to "offertensortiert.txt"
        BufferedWriter writer;
        try{
            writer = new BufferedWriter(new FileWriter("offertensortiert.txt "));
            for (Offer offer : offerList) {
                writer.write(offer.getFileString());
            }
            writer.close();
        }
        catch (IOException exception){
            exception.printStackTrace();
            return;
        }

        // for each place, we compute the average offer of the place
        // VAT included
        // if there are no offers from there, to avoid division by zero,
        // we say the average is 0
        // we use a HashMap to store that
        HashMap<Place, Double> avgOfferPerPlace = new HashMap<>();
        for (Place place: Place.values()) {
            double totalPrice = offerList.stream()
                    .filter(offer -> (offer.getPlace() == place))
                    .map(Offer::getFinalPrice)
                    .reduce((double) 0, Double::sum);
            int numberOfOffers = (int) offerList.stream()
                    .filter(offer -> (offer.getPlace() == place))
                    .count();
            double averagePrice;
            if (numberOfOffers == 0)
                averagePrice = 0;
            else
                averagePrice = totalPrice / numberOfOffers;
            avgOfferPerPlace.put(place, averagePrice);
        }

        // we sort the average offer per place descendingly
        var offersPerPlaceSorted = avgOfferPerPlace.entrySet().stream()
                .sorted((o1, o2) -> (Double.compare(o2.getValue(), o1.getValue())))
                .collect(Collectors.toList());

        // we write everything to "statistik.txt"
        try{
            writer = new BufferedWriter(new FileWriter("statistik.txt"));
            for (var place: offersPerPlaceSorted){
                String string = place.getKey() + ":" + place.getValue() + "\n";
                writer.write(string);
            }
            writer.close();
        }
        catch (IOException exception){
            exception.printStackTrace();
        }
    }
}
