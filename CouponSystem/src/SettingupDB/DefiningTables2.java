package SettingupDB;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DefiningTables2 {
	public static void main(String[] args) {

		try (BufferedReader in = new BufferedReader(new FileReader("files/DBconnection"))) {
			String url = in.readLine();
			System.out.println(url);
			Connection con = DriverManager.getConnection(url);

			Statement statment = con.createStatement();
			statment.executeUpdate(
					"CREATE TABLE company(id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 100, INCREMENT BY 1), comp_name  VARCHAR(30),password VARCHAR(30),email VARCHAR(30))");
			statment.executeUpdate(
					"CREATE TABLE customer(id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 2000, INCREMENT BY 1), cust_name VARCHAR(30),password VARCHAR(30))");
			statment.executeUpdate(
					"CREATE TABLE coupon(id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 50000, INCREMENT BY 1), title VARCHAR(30),start_date DATE,end_date DATE, amount INTEGER, type VARCHAR(30),message VARCHAR(30),price DOUBLE CHECK (price>0), image VARCHAR(35))");

			statment.executeUpdate(
					"CREATE TABLE customer_coupon(cust_id BIGINT, coupon_id BIGINT,PRIMARY KEY(cust_id,coupon_id))");
			statment.executeUpdate(
					"CREATE TABLE company_coupon(comp_id BIGINT, coupon_id BIGINT,PRIMARY KEY(comp_id,coupon_id))");
		} catch (IOException | SQLException e) {
			e.printStackTrace();
		}
		{

		}
	}

}
