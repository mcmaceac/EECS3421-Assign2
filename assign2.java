import java.net.*;
import java.sql.*;
import java.util.Scanner;

public class assign2 {
	private static Connection conDB; 	//Connection to the database system.
	private static String url; 		//URL: Which database?

	private static int custID;
	private static String custName;
	private static String custCity;

	public static void main(String[] args) {
		try {
			Class.forName("COM.ibm.db2.jdbc.app.DB2Driver").newInstance();
			url = "jdbc:db2:c3421m";
			conDB = DriverManager.getConnection(url);
		} 
		catch (Exception e) {
			System.out.println(e.toString());
		}
		

		System.out.print ("Enter customer id: ");
		Scanner scan = new Scanner (System.in);
		int cid = scan.nextInt();

		if (!find_customer (cid)) {
			System.out.println ("Error! Customer with id " + cid + " not found in the database");
		}
		else {
			System.out.println ("Customer with id " + cid + " is in the database");
			System.out.println ("Customer id: " + cid);
			System.out.println ("Customer name: " + custName);
			System.out.println ("Customer city: " + custCity);
			System.out.println ();
			fetch_categories();
		}	

	}

	public static boolean find_customer(int cid) {
		String queryText = "";
		PreparedStatement querySt = null;
		ResultSet answers = null;

		boolean inDB = false;

		queryText = 
					"SELECT cid, name, city " +
					"FROM yrb_customer " +
					"WHERE cid = " + cid;

		try {
			querySt = conDB.prepareStatement (queryText);	
			answers = querySt.executeQuery();

			if (answers.next()) {
				inDB = true;
				//custId = answers.getString("cid");
				custName = answers.getString("name");
				custCity = answers.getString("city");
			}
			else {
				inDB = false;
			}
		}
		catch (Exception e) {
			System.out.println(e.toString());
		}
		return inDB;
	}

	public static void fetch_categories () {
		String queryText = "";
		PreparedStatement querySt = null;
		ResultSet answers = null;

		String category;

		queryText = 
					"SELECT cat " +
					"FROM yrb_category";

		try {
			querySt = conDB.prepareStatement (queryText);	
			answers = querySt.executeQuery();

			while (answers.next()) {
				category = answers.getString("cat");
				System.out.println (category);
			}
		}
		catch (Exception e) {
			System.out.println(e.toString());
		}
	}
}
