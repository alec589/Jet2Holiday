package Graph;

import java.util.ArrayList;
import java.util.List;

public class DestinationData {
	private List<Destination> destinations;

    public DestinationData() {
        destinations = new ArrayList<>();
        initializeDestinations();
    }

    private void initializeDestinations() {
    	destinations.add(new Destination("BackBay Mall", "BackBay", "BackBayMall"));
    	destinations.add(new Destination("Prudential Center", "BackBay", "PrudentialCenter"));
    	destinations.add(new Destination("Copley Square", "BackBay", "CopleySquare"));
    	destinations.add(new Destination("Newbury Street", "BackBay", "NewburyStreet"));
    	
    	destinations.add(new Destination("Fenway Park", "Fenway", "FenwayPark"));
    	destinations.add(new Destination("MFA Museum", "Fenway", "MFAMuseum"));
    	destinations.add(new Destination("Northeastern University", "Fenway", "NEU"));
    	destinations.add(new Destination("Kenmore Square", "Fenway", "KenmoreSquare"));
    	
    	destinations.add(new Destination("Downtown Office", "Downtown", "DowntownOffice"));
    	destinations.add(new Destination("City Hall", "Downtown", "CityHall"));
    	destinations.add(new Destination("Financial District", "Downtown", "FinancialDistrict"));
    	destinations.add(new Destination("Boston Common", "Downtown", "BostonCommon"));
    	
    	destinations.add(new Destination("Seaport Pier", "Seaport", "SeaportPier"));
    	destinations.add(new Destination("Convention Center", "Seaport", "ConventionCenter"));
    	destinations.add(new Destination("Harbor Walk", "Seaport", "HarborWalk"));
    	destinations.add(new Destination("Seaport Plaza", "Seaport", "SeaportPlaza"));
    	
    	destinations.add(new Destination("Newton Center", "Newton", "NewtonCenter"));
    	destinations.add(new Destination("Newton Library", "Newton", "NewtonLibrary"));
    	destinations.add(new Destination("Newton High School", "Newton", "NewtonHigh"));
    	destinations.add(new Destination("Newton Park", "Newton", "NewtonPark"));
    }

    public List<Destination> getAllDestinations() {
        return destinations;
    }

    public Destination getByName(String name) {
        for (Destination d : destinations) {
            if (d.getName().equals(name)) {
                return d;
            }
        }
        return null;
    }
}
