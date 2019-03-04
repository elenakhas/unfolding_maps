package module6;

import java.util.List;

import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.marker.SimpleLinesMarker;
import processing.core.PConstants;
import processing.core.PGraphics;

/** 
 * A class to represent AirportMarkers on a world map.
 *   
 * @author Adam Setters and the UC San Diego Intermediate Software Development
 * MOOC team
 *
 */
public class AirportMarker extends CommonMarker {
	public static List<SimpleLinesMarker> routes;
	public AirportMarker(Feature city) {
		super(((PointFeature)city).getLocation(), city.getProperties());
	
	}
	
	@Override
	public void drawMarker(PGraphics pg, float x, float y) {
		//buffer.beginDraw();
		pg.fill(0, 0, 250);
		pg.ellipse(x, y, 10, 10);
		//buffer.endDraw();
	}


	
	
	

	@Override
	public void showTitle(PGraphics pg, float x, float y) {
		// show rectangle with title
		String name = getName() + ", " + getCode();
		String location = getCity() + ", " + getCountry();
		
		pg.pushStyle();
		pg.rectMode(PConstants.CORNER);
		pg.stroke(110);
		pg.fill(255,255,255);
		pg.rect(x, y+18, pg.textWidth(location) + 6, 30, 5);
		pg.textAlign(PConstants.LEFT, PConstants.TOP);
		pg.fill(0);
		pg.text(name, x + 3 , y +18);
		pg.textAlign(PConstants.LEFT, PConstants.TOP);
		pg.fill(0);
		pg.text(location, x + 3 , y +28);
		
		pg.popStyle();
		
	}
		
		// show routes
	
	
	

		
		
		
		
	public String getName() {
		return (String) getProperty("name");
	}
	
	public String getCity() {
		return (String) getProperty("city");
	}
	
	public String getCountry() {
		return (String) getProperty("country");
	}
		
    public String getCode() {
			return (String) getProperty("code");
	}		
	
}
