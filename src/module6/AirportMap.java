package module6;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.data.ShapeFeature;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.marker.SimpleLinesMarker;
import de.fhpotsdam.unfolding.marker.SimplePointMarker;
import de.fhpotsdam.unfolding.providers.Google;
import de.fhpotsdam.unfolding.providers.MBTilesMapProvider;
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.geo.Location;
import parsing.ParseFeed;
import processing.core.PApplet;
import processing.core.PGraphics;

/** An applet that shows airports (and routes)
 * on a world map.  
 * @author Adam Setters and the UC San Diego Intermediate Software Development
 * MOOC team
 *
 */
public class AirportMap extends PApplet {
	
	UnfoldingMap map;
	private List<Marker> airportList;
	List<Marker> myAirports;
	List<Marker> routeList;
	List <String> myCities;
	ArrayList <Marker> airportsTo = new ArrayList <Marker>();

	
	private CommonMarker Selected;
	private CommonMarker Clicked;
	//private CommonMarker routeTo;
	public static String mbTilesString = "blankLight-1-3.mbtiles";
	private static final boolean offline = true;
	
	public void setup() {
		// setting up PAppler
		size(900,700, OPENGL);
		//buffer = createGraphics(900, 700);
		
		// setting up map and default events
		
		
		if (offline) {
		    map = new UnfoldingMap(this, 200, 50, 650, 600, new MBTilesMapProvider(mbTilesString));
		    
		}
		else {
			map = new UnfoldingMap(this, 200, 50, 650, 600, new Google.GoogleMapProvider());
		}
		MapUtils.createDefaultEventDispatcher(this, map);
		
		// get features from airport data
		List<PointFeature> features = ParseFeed.parseAirports(this, "airports.dat");
		
		// list for markers, hashmap for quicker access when matching with routes
		airportList = new ArrayList<Marker>();
		HashMap<Integer, Location> airports = new HashMap<Integer, Location>();
		
		// create markers from features
		for(PointFeature feature : features) {
			AirportMarker m = new AirportMarker(feature);
	
			m.setRadius(5);
			airportList.add(m);
			// put airport in hashmap with OpenFlights unique id for key
			airports.put(Integer.parseInt(feature.getId()), feature.getLocation());
		}
		
		
		// parse route data
		List<ShapeFeature> routes = ParseFeed.parseRoutes(this, "routes.dat");
		routeList = new ArrayList<Marker>();
		
		for(ShapeFeature route : routes) {
			
			// get source and destination airportIds
			int source = Integer.parseInt((String)route.getProperty("source"));
			int dest = Integer.parseInt((String)route.getProperty("destination"));
			
			// get locations for airports on route
			if(airports.containsKey(source) && airports.containsKey(dest)) {
				route.addLocation(airports.get(source));
				route.addLocation(airports.get(dest));
			}
			
			SimpleLinesMarker sl = new SimpleLinesMarker(route.getLocations(), route.getProperties());
		
			//System.out.println(sl.getProperties());
			
			//UNCOMMENT IF YOU WANT TO SEE ALL ROUTES
		routeList.add(sl);
		}
        map.addMarkers(routeList);
        hideRoutes();
		//myCities = new ArrayList<String>();
		myAirports = new ArrayList <Marker>();
		try {
			readMyAirports();
		} catch (FileNotFoundException e) {
			System.out.println("Exception found");
			// TODO Auto-generated catch block
		//	e.printStackTrace();
		}
			
        map.addMarkers(airportList);
        
		
		
	}//end setup
	
	public void readMyAirports() throws FileNotFoundException{
		myCities = new ArrayList<String>();
				
		Scanner scanner = new Scanner(new File("myairports.out.txt"));          
		while (scanner.hasNextLine()) {  
           String line = scanner.nextLine();
           myCities.add(line);
        }
        System.out.println(myCities.toString());
        
        
        myAirports = new ArrayList <Marker>();
        for (Marker airport : airportList) {
           for (String city : myCities) {
        	    if (((AirportMarker) airport).getCity().contains(city)) {
        	    	myAirports.add(airport);
        	    }
        }
        }
       //for (Marker m : myAirports) {
       //System.out.println(((AirportMarker)m).getCity());
	}
	//}
	
	public void draw() {
		background(0);
		map.draw();
		//image(buffer, 0, 0);
	}
		
    public void mouseMoved() {
			// clear the last selection
			if (Selected != null) {
				Selected.setSelected(false);
				Selected = null;
			}
			selectMarkerIfHover(airportList);
		}
		
		// If there is a marker selected 
		
	public void selectMarkerIfHover(List<Marker> markers){
			// Abort if there's already a marker selected
	    if (Selected != null) {
				return;
		}
			
		for (Marker m : markers) {
		    CommonMarker marker = (CommonMarker)m;
			if (marker.isInside(map,  mouseX, mouseY)) {
				Selected = marker;
				marker.setSelected(true);
					return;
			}
		}
	}
		
	public void mouseClicked() {
		if (Clicked != null) {
		    unhideMarkers();
		    Clicked = null;
		}
		else{
			
			checkAirportsForClick();
			}
		
	}
		
	public void checkAirportsForClick() {
		if (Clicked != null) return;
		for (Marker marker : airportList) {
			if (!marker.isHidden() && marker.isInside(map, mouseX, mouseY)) {
				Clicked = (CommonMarker)marker;
				//System.out.println(((AirportMarker) marker).getCode());
				displayRoutes(marker);
				
				}
		}
				
				//display(); //display the info about the cities!!!!!!!
				// Hide all the other airports
		for (Marker mhide : airportList) {
					//for (Marker route : routeList) {
						//if (((AirportMarker) mhide).getCode().equals(route.getProperty("to"))) {
						//	routeTo = (CommonMarker) mhide;
						//}
					    if (mhide != Clicked && !airportsTo.contains(mhide)) {
						    mhide.setHidden(true);
					}
				}
				
				return;
			}
			
	public void displayRoutes(Marker am) {
		for (Marker route : routeList) {
		    if (((AirportMarker) am).getCode().equals(route.getProperty("from"))){
		    	route.setHidden(false);
		    	showDestinations(route);
		    }
		}
	}
	
	public void showDestinations(Marker route) {
		//airportsTo.clear();
		for (Marker airport : airportList) {
			if(route.getProperty("to").equals(((AirportMarker)airport).getCode())) {
			    airportsTo.add(airport);
			    //SET COLOR AND DISPLAY airport;
			}
		}
		
		for (Marker air : airportsTo) {
			System.out.println(((AirportMarker) air).getCode());
			air.setHidden(false);
		}
	}
		              
				   
		    
	    
	
		
		
	private void unhideMarkers() {
		for(Marker marker : airportList) {
			marker.setHidden(false);
		}
	}
	private void hideRoutes() {
		for (Marker route : routeList) {
			route.setHidden(true);
		}
	}
	
		
		//UNCOMMENT IF YOU WANT TO SEE ALL ROUTES
		
	

	
	
	

}
