package SettingupDB;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DroppingTables {
	public static void main(String[] args) {

		try (BufferedReader in = new BufferedReader(new FileReader("files/DBconnection"))) {
			String url = in.readLine();
			System.out.println(url);
			Connection con = DriverManager.getConnection(url);

			Statement statment = con.createStatement();
			statment.executeUpdate(" DROP TABLE company");
			statment.executeUpdate(" DROP TABLE customer");
			statment.executeUpdate(" DROP TABLE coupon");
			statment.executeUpdate(" DROP TABLE customer_coupon");
			statment.executeUpdate(" DROP TABLE company_coupon");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
