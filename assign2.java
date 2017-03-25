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
			System.out.println ("Customer id: " + custID);
			System.out.println ("Customer name: " + custName);
			System.out.println ("Customer city: " + custCity);
			System.out.println ();
			fetch_categories();

			System.out.println ();
			System.out.print ("Enter category: ");
			String cat = scan.next();
			System.out.print ("Enter title: ");
			String title = scan.next();
			find_book (title, cat);

		}	

		try {
			conDB.close();
		}
		catch (Exception e) {
			System.out.println (e.toString());
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
					"WHERE cid = ?";

		try {
			querySt = conDB.prepareStatement (queryText);	
			querySt.setInt(1, cid);
			answers = querySt.executeQuery();

			if (answers.next()) {
				inDB = true;
				custID = answers.getInt("cid");
				custName = answers.getString("name");
				custCity = answers.getString("city");
			}
			else {
				inDB = false;
			}
			answers.close();
			querySt.close();
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
			answers.close();
			querySt.close();
		}
		catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	public static void find_book (String t, String c) {
		String queryText = "";
		PreparedStatement querySt = null;
		ResultSet answers = null;

		String title;
		int year;
		String language;
		int weight;

		queryText = 
					"SELECT title, year, language, weight " + 
					"FROM yrb_book " + 
					"WHERE cat = ? and title = ?";

		try {
			querySt = conDB.prepareStatement (queryText);
			querySt.setString(1, c);
			querySt.setString(2, t);
			answers = querySt.executeQuery();

			System.out.println(answers.toString());
			while (answers.next()) {
				title = answers.getString("title");
				year = answers.getInt("year");
				language = answers.getString("language");
				weight = answers.getInt("weight");

				System.out.println (title + " " + year + " " + language + " " + weight);
			}
			answers.close();
			querySt.close();
		}
		catch (Exception e) {
			System.out.println(e.toString());
		}
	}
}
