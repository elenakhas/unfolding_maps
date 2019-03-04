package module6;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Tester {

public static void main (String [] args){
	
	try {
		readMyAirports();
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
}
	


public static void readMyAirports() throws FileNotFoundException{
ArrayList myCities = new ArrayList<String>();
	Scanner scanner = new Scanner(new File("data/myairports.out.txt"));  
    while (scanner.hasNextLine()) {  
       String line = scanner.nextLine();
       myCities.add(line);
    }
    System.out.println(myCities.toString());
}
}