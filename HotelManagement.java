package MyPackage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class HotelManagement {
	public static void main(String[] args) throws Exception {
		 //built connection
		    Class.forName("com.mysql.cj.jdbc.Driver");
	Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel_db","root","Shreya@2002");
		    
			//System.out.println(e.getMessage());
	          if(con.isClosed()) {
			
		System.out.println("Connection is closed");
	          }
	          else {
	        	  System.out.println("Connection is created"); 
	          }
	          while(true) {
		  			Scanner sc=new Scanner(System.in);
		  			System.out.println("WELCOME TO HOTEL MANAGEMENT SYSTEM: ");
		  			System.out.println("1.Book a Reservation ");
		  			System.out.println("2.view reservstion ");
		  			System.out.println("3.Get room no ");
		  			System.out.println("4.Update reservation");
		  			System.out.println("5.Delete your reservation ");
		  			System.out.println("0.Exit ");
		  			System.out.print("Choose an option: ");
		  			int choice=sc.nextInt();
		  				switch(choice) {
		  				case 1:reserveRoom(con,sc);
		  				      break;
		  				case 2:viewReservation(con);
		  				       break;
		  				case 3:getRoomNo(con,sc);
		  				       break;
		  				case 4:updateReservation(con,sc);
		  				       break;
		  				case 5:deleteReservation(con,sc);
		  				       break;
		  				case 0:
		  					exit();
		  					sc.close();
		  					return;
		  				default:
		                      System.out.println("Invalid choice. Try again.");
		  				
		  			}
		  			}
}
	public static void reserveRoom(Connection con,Scanner sc) {
		try {
			System.out.print("Enter guest name: ");
            String guestName = sc.next();
            sc.nextLine();
            System.out.print("Enter room number: ");
            int roomNumber = sc.nextInt();
            System.out.print("Enter contact number: ");
            String contactNumber = sc.next();
            
            String sql = "INSERT INTO reservations (guest_name, room_number, contact_number) " +
                    "VALUES ('" + guestName + "', " + roomNumber + ", '" + contactNumber + "')";
            
		
		 
		try (Statement statement = con.createStatement()) {
            int affectedRows = statement.executeUpdate(sql);

            if (affectedRows > 0) {
                System.out.println("Reservation successful!");
            } else {
                System.out.println("Reservation failed.");
            }
        }
		}
     catch(SQLException e) {
    	e.printStackTrace();
			
		}
		
	}
	
	public static void viewReservation(Connection con) throws SQLException{
		String sql = "SELECT reservation_id, guest_name, room_number, contact_number, reservation_date FROM reservations";

        try (Statement statement = con.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            System.out.println("Current Reservations:");
            System.out.println("+----------------+-----------------+---------------+----------------------+-------------------------+");
            System.out.println("| Reservation ID | Guest           | Room Number   | Contact Number      | Reservation Date        |");
            System.out.println("+----------------+-----------------+---------------+----------------------+-------------------------+");

            while (resultSet.next()) {
                int reservationId = resultSet.getInt("reservation_id");
                String guestName = resultSet.getString("guest_name");
                int roomNumber = resultSet.getInt("room_number");
                String contactNumber = resultSet.getString("contact_number");
                String reservationDate = resultSet.getTimestamp("reservation_date").toString();

                // Format and display the reservation data in a table-like format
                System.out.printf("| %-14d | %-15s | %-13d | %-20s | %-19s   |\n",
                        reservationId, guestName, roomNumber, contactNumber, reservationDate);
            }

            System.out.println("+----------------+-----------------+---------------+----------------------+-------------------------+");
        }
	}
	public static void  getRoomNo(Connection con,Scanner sc) {
		try {
            System.out.print("Enter reservation ID: ");
            int reservationId = sc.nextInt();
            System.out.print("Enter guest name: ");
            String guestName = sc.next();

            String sql = "SELECT room_number FROM reservations " +
                    "WHERE reservation_id = " + reservationId +
                    " AND guest_name = '" + guestName + "'";

            try (Statement statement = con.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {

                if (resultSet.next()) {
                    int roomNumber = resultSet.getInt("room_number");
                    System.out.println("Room number for Reservation ID " + reservationId +
                            " and Guest " + guestName + " is: " + roomNumber);
                } else {
                    System.out.println("Reservation not found for the given ID and guest name.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
	}
	public static void updateReservation(Connection con,Scanner sc) {
		try {
            System.out.print("Enter reservation ID to update: ");
            int reservationId = sc.nextInt();
            sc.nextLine(); // Consume the newline character

            if (!reservationExists(con, reservationId)) {
                System.out.println("Reservation not found for the given ID.");
                return;
            }

            System.out.print("Enter new guest name: ");
            String newGuestName = sc.nextLine();
            System.out.print("Enter new room number: ");
            int newRoomNumber = sc.nextInt();
            System.out.print("Enter new contact number: ");
            String newContactNumber = sc.next();

            String sql = "UPDATE reservations SET guest_name = '" + newGuestName + "', " +
                    "room_number = " + newRoomNumber + ", " +
                    "contact_number = '" + newContactNumber + "' " +
                    "WHERE reservation_id = " + reservationId;

            try (Statement statement = con.createStatement()) {
                int affectedRows = statement.executeUpdate(sql);

                if (affectedRows > 0) {
                    System.out.println("Reservation updated successfully!");
                } else {
                    System.out.println("Reservation update failed.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
	}
	public static void deleteReservation(Connection con,Scanner sc) {
		  try {
	            System.out.print("Enter reservation ID to delete: ");
	            int reservationId = sc.nextInt();

	            if (!reservationExists(con, reservationId)) {
	                System.out.println("Reservation not found for the given ID.");
	                return;
	            }

	            String sql = "DELETE FROM reservations WHERE reservation_id = " + reservationId;

	            try (Statement statement = con.createStatement()) {
	                int affectedRows = statement.executeUpdate(sql);

	                if (affectedRows > 0) {
	                    System.out.println("Reservation deleted successfully!");
	                } else {
	                    System.out.println("Reservation deletion failed.");
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	}
	 public static boolean reservationExists(Connection connection, int reservationId) {
	        try {
	            String sql = "SELECT reservation_id FROM reservations WHERE reservation_id = " + reservationId;

	            try (Statement statement = connection.createStatement();
	                 ResultSet resultSet = statement.executeQuery(sql)) {

	                return resultSet.next(); // If there's a result, the reservation exists
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	            return false; // Handle database errors as needed
	        }
	    }

	 public static void exit() throws InterruptedException {
		 System.out.print("Exiting System");
	        int i = 5;
	        while(i!=0){
	            System.out.print(".");
	            Thread.sleep(1000);
	            i--;
	        }
	        System.out.println();
	        System.out.println("ThankYou For Using Hotel Reservation System!!!");
	    }
}

