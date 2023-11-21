// COPYRIGHTS.© ABANOB AYOUB
// Description: This system code is typped on VSCode IDE; Don't Forget to add the the JDBC conntector .jar file

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {

    String url = "jdbc:mysql://localhost:3306/cardealership"; //DBname
    String username = "root"; //ADD YOUR USERNAME
    String password = "PASSWORD"; //ADD YOUR Password


       System.out.print("\n\n" + "Choose one of the following options:\n" +
       "1- Display the schedule of all trips\n" +
       "2- Edit the schedule\n" +
       "3- Display the stops of a given trip\n" +
       "4- Display the weekly schedule of a given driver and date\n" +
       "5- Add a drive\n" +
       "6- Add a bus\n" +
       "7- Delete a bus\n" +
       "8- Record (insert) the actual data of a given trip offering specified by its key\n" +
       "==============================================\n" +
       "Your Choice: ");

       // Creating a Scanner object to receive user input
        Scanner scanner = new Scanner(System.in);

        // Reading the user's choice
        int userChoice = scanner.nextInt();

        scanner.nextLine(); //For using .nextLine


        // Choose a task to perform
        if (userChoice == 1){       // ------------------------------------Task 1
            System.out.println("==============================================\n");

            // Get trip(s) info from the user
            System.out.print("Enter Start Location: ");
            String startLocationName = scanner.nextLine();

            System.out.print("Enter Destination: ");
            String destinationName = scanner.nextLine();

            System.out.print("Enter Date (YYYY-MM-DD): ");
            String date = scanner.nextLine();

            // JDBC variables
            Connection connection = null;
            PreparedStatement preparedStatement = null;
            ResultSet resultSet = null;

            try {
            // Establish the database connection
            connection = DriverManager.getConnection(url, username, password);

            // SQL query
            String query1 = "SELECT TOF.TripNumber, TOF.Date, TOF.ScheduledStartTime, TOF.ScheduledArrivalTime, " +
                         "TOF.DriverName AS DriverID, TOF.BusID, T.StartLocationName, T.DestinationName " +
                         "FROM TripOffering TOF " +
                         "JOIN Trip T ON TOF.TripNumber = T.TripNumber " +
                         "WHERE T.StartLocationName = ? AND T.DestinationName = ? AND TOF.Date = ?";

            // Prepare the statement
            preparedStatement = connection.prepareStatement(query1);
            preparedStatement.setString(1, startLocationName);
            preparedStatement.setString(2, destinationName);
            preparedStatement.setString(3, date);

            // Execute the query
            resultSet = preparedStatement.executeQuery();

            // Process the result set
            while (resultSet.next()) {
                System.out.println("==============================================\n");
                // Retrieve the data
                int tripNumber = resultSet.getInt("TripNumber");
                String tripDate = resultSet.getString("Date");
                String startTime = resultSet.getString("ScheduledStartTime");
                String arrivalTime = resultSet.getString("ScheduledArrivalTime");
                String driverID = resultSet.getString("DriverID");
                int busID = resultSet.getInt("BusID");
                String startLocation = resultSet.getString("StartLocationName");
                String destination = resultSet.getString("DestinationName");

                // Display the results (you can modify this part based on your needs)
                System.out.println("Trip Number: " + tripNumber);
                System.out.println("Date: " + tripDate);
                System.out.println("Start Time: " + startTime);
                System.out.println("Arrival Time: " + arrivalTime);
                System.out.println("Driver ID: " + driverID);
                System.out.println("Bus ID: " + busID);
                System.out.println("Start Location: " + startLocation);
                System.out.println("Destination: " + destination);
                System.out.println("\n==============================================");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close resources in a finally block to ensure they are always closed
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        }else if (userChoice == 2){     // ------------------------------------Task 2
            System.out.println("==============================================\n");
            System.out.print("Choose the edit task you want to perform: \n" +
            "1- Delete a trip\n" +
            "2- Add a set of trip\n" +
            "3- Change the driver of a specific trip offering\n" +
            "4- Change the bus of a specific trip offering\n" +
            "YOUR CHOICE: ");

            int editChoice = scanner.nextInt();

            scanner.nextLine(); //For using .nextLine

            // Inner (IF-Else) statement for the Edit task that a user want to perform
            if (editChoice == 1){
                try {
                    // Establish the database connection
                    Connection connection = DriverManager.getConnection(url, username, password);

                    // Prompt user for input
                    System.out.print("Enter Trip Number: ");
                    int tripNumber = scanner.nextInt();
                    System.out.print("Enter Date (YYYY-MM-DD): ");
                    String date = scanner.next();
                    System.out.print("Enter Scheduled Start Time (HH:mm:ss): ");
                    String scheduledStartTime = scanner.next();
                    // SQL query to delete the trip offering
            String deleteQuery = "DELETE FROM TripOffering WHERE TripNumber = ? AND Date = ? AND ScheduledStartTime = ?";

            // Prepare the statement
            PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
            deleteStatement.setInt(1, tripNumber);
            deleteStatement.setString(2, date);
            deleteStatement.setString(3, scheduledStartTime);

            // Execute the delete statement
            int rowsAffected = deleteStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("==============================================\n");
                System.out.println("Trip offering deleted successfully!");
            } else {
                System.out.println("==============================================\n");
                System.out.println("No matching trip offering found for deletion.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close resources
            scanner.close();
            System.out.println("\n==============================================");
        }
            }else if(editChoice == 2){

                try {
                    // Establish the database connection
                    Connection connection = DriverManager.getConnection(url, username, password);

                    // Loop to add trip offerings
                    while (true) {
                        System.out.print("Enter Trip Number: ");
                        int tripNumber = scanner.nextInt();
                        System.out.print("Enter Date (YYYY-MM-DD): ");
                        String date = scanner.next();
                        System.out.print("Enter Scheduled Start Time (HH:mm:ss): ");
                        String scheduledStartTime = scanner.next();
                        System.out.print("Enter Scheduled Arrival Time (HH:mm:ss): ");
                        String scheduledArrivalTime = scanner.next();
                        System.out.print("Enter Driver Name: ");
                        String driverName = scanner.next();  // Assuming DriverName in TripOffering corresponds to DriverID
                        System.out.print("Enter Bus ID: ");
                        int busID = scanner.nextInt();

                        // SQL query to insert a new trip offering
                        String insertQuery = "INSERT INTO TripOffering (TripNumber, Date, ScheduledStartTime, ScheduledArrivalTime, DriverName, BusID) VALUES (?, ?, ?, ?, ?, ?)";

                        // Prepare the statement
                        PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
                        insertStatement.setInt(1, tripNumber);
                        insertStatement.setString(2, date);
                        insertStatement.setString(3, scheduledStartTime);
                        insertStatement.setString(4, scheduledArrivalTime);
                        insertStatement.setString(5, driverName);
                        insertStatement.setInt(6, busID);

                      try{  // Execute the insert statement
                        int rowsAffected = insertStatement.executeUpdate();

                        if (rowsAffected > 0) {
                            System.out.println("==============================================");
                            System.out.println("Trip offering added successfully!");
                            System.out.println("==============================================");
                        } else {
                            System.out.println("==============================================");
                            System.out.println("Failed to add trip offering.");
                            System.out.println("==============================================");
                         }
                        }catch (SQLException e){
                            e.printStackTrace();
                            System.out.println("==============================================");
                            System.out.println("Failed to add trip offering.");
                            System.out.println("==============================================");
                        }
                        // Ask the user if they have more trips to enter
                        System.out.print("Do you have more trips to enter? (yes/no): ");
                        String moreTrips = scanner.next().toLowerCase();
                        if (!moreTrips.equalsIgnoreCase("yes")) {
                            break;
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    // Close resources
                    scanner.close();
                }
            }else if(editChoice == 3){
                try {
                    // Establish the database connection
                    Connection connection = DriverManager.getConnection(url, username, password);

                    // Prompt user for input
                    System.out.print("Enter Trip Number: ");
                    int tripNumber = scanner.nextInt();
                    System.out.print("Enter Date (YYYY-MM-DD): ");
                    String date = scanner.next();
                    System.out.print("Enter Scheduled Start Time (HH:mm:ss): ");
                    String scheduledStartTime = scanner.next();
                    System.out.print("Enter New Driver Name: ");
                    String newDriverName = scanner.next();

                    // SQL query to update the driver for a given trip offering
                    String updateQuery = "UPDATE TripOffering SET DriverName = ? WHERE TripNumber = ? AND Date = ? AND ScheduledStartTime = ?";

                    // Prepare the statement
                    PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                    updateStatement.setString(1, newDriverName);
                    updateStatement.setInt(2, tripNumber);
                    updateStatement.setString(3, date);
                    updateStatement.setString(4, scheduledStartTime);

                  try{
                    // Execute the update statement
                    int rowsAffected = updateStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        System.out.println("==============================================\n" +
                        "Driver's Name is updated successfully!\n" +
                        "==============================================\n");
                    } else {    // This else statement assuming the user enter valid data format but it's not found in the db
                        System.out.println("==============================================\n" +
                            "No matching trip offering found for driver update." +
                            "==============================================\n");
                    }
                }catch (SQLException e){    // This catch statement assuming the user enter non valid data-type/format
                            e.printStackTrace();
                        System.out.println("==============================================\n" +
                            "No matching trip offering found for driver update." +
                            "==============================================\n");
                        }
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    // Close resources
                    scanner.close();
                }
            }else if(editChoice == 4){
                try {
                    // Establish the database connection
                    Connection connection = DriverManager.getConnection(url, username, password);

                    // Prompt user for input
                    System.out.print("Enter Trip Number: ");
                    int tripNumber = scanner.nextInt();
                    System.out.print("Enter Date (YYYY-MM-DD): ");
                    String date = scanner.next();
                    System.out.print("Enter Scheduled Start Time (HH:mm:ss): ");
                    String scheduledStartTime = scanner.next();
                    System.out.print("Enter New Bus ID: ");
                    int newBusID = scanner.nextInt();

                    // SQL query to update the bus for a given trip offering
                    String updateQuery = "UPDATE TripOffering SET BusID = ? WHERE TripNumber = ? AND Date = ? AND ScheduledStartTime = ?";

                    // Prepare the statement
                    PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                    updateStatement.setInt(1, newBusID);
                    updateStatement.setInt(2, tripNumber);
                    updateStatement.setString(3, date);
                    updateStatement.setString(4, scheduledStartTime);

                try{
                    // Execute the update statement
                    int rowsAffected = updateStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        System.out.println("==============================================\n" +
                            "Bus for the trip offering updated successfully!\n" +
                            "==============================================\n");
                    } else {
                        System.out.println("==============================================\n" +
                        "No matching trip offering found for bus update\n" +
                        "==============================================\n");
                    }
                }catch(SQLException e){
                     e.printStackTrace();
                        System.out.println("==============================================\n" +
                        "No matching trip offering found for bus update\n" +
                        "==============================================\n");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    // Close resources
                    scanner.close();
                }
            }else{
                System.out.println("==============================================\n" +
                "Error! Invalid Choice For Task 2\n" +
                "==============================================\n");
            }

        }else if (userChoice == 3){     // ------------------------------------Task 3
            System.out.print("Enter Trip Number: ");
            int tripNumber = scanner.nextInt();

            Connection connection = DriverManager.getConnection(url, username, password);

            String query3 = "SELECT * FROM TripStopInfo WHERE TripNumber = ?";

            // Prepare the statement
            PreparedStatement preparedStatement = connection.prepareStatement(query3);
            preparedStatement.setInt(1, tripNumber);

            System.out.println("==============================================\n");
            System.out.println("TripNumber\tStopNumber\tSequenceNumber\tDrivingTime");
            System.out.println("-------------------------------------------------------------");

            try {
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            String tripData = "";
        for (int i = 1; i <= 4; i++) {
            tripData += rs.getString(i) + "\t\t";
        }
        System.out.println(tripData);
    }
        System.out.println("\n==============================================\n");

        // Close the ResultSet and PreparedStatement here
        rs.close();
        preparedStatement.close();
        connection.close();
    } catch (Exception e) {
        e.printStackTrace();
        System.out.println("==============================================\n" +
        "No Stops Found for the given trip\n" +
        "==============================================\n");
    }

        }else if (userChoice == 4){     // ------------------------------------Task 4
            try {
                // Establish the database connection
                Connection connection = DriverManager.getConnection(url, username, password);

                // Prompt user for input
                System.out.print("Enter Driver Name: ");
                String driverName = scanner.next();
                System.out.print("Enter Date (YYYY-MM-DD): ");
                String inputDate = scanner.next();

                // Parse the input date to a java.sql.Date object
                java.sql.Date userDate = java.sql.Date.valueOf(inputDate);

                // SQL query to retrieve the weekly schedule for a given driver
                String selectQuery = "SELECT * FROM TripOffering WHERE DriverName = ? AND Date BETWEEN ? AND ?";

                // Calculate the start and end dates of the week
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                java.sql.Date startDate = java.sql.Date.valueOf(dateFormat.format(userDate));
                java.sql.Date endDate = new java.sql.Date(userDate.getTime() + (6 - userDate.getDay()) * 24 * 60 * 60 * 1000);

                // Prepare the statement
                PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
                selectStatement.setString(1, driverName);
                selectStatement.setDate(2, startDate);
                selectStatement.setDate(3, endDate);

                // Execute the query
                ResultSet resultSet = selectStatement.executeQuery();

                // Display the results
                System.out.println("==============================================\n");
                System.out.println("TripNumber\tDate\t\tScheduledStartTime\tScheduledArrivalTime\tBusID");
                System.out.println("----------------------------------------------------------------------------------");

                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                while (resultSet.next()) {
                    int tripNumber = resultSet.getInt("TripNumber");
                    java.sql.Date tripDate = resultSet.getDate("Date");
                    String startTime = timeFormat.format(resultSet.getTime("ScheduledStartTime"));
                    String arrivalTime = timeFormat.format(resultSet.getTime("ScheduledArrivalTime"));
                    int busID = resultSet.getInt("BusID");

                    System.out.println(tripNumber + "\t\t" + tripDate + "\t" + startTime + "\t\t" + arrivalTime + "\t\t" + busID);
                }
                System.out.println("==============================================\n");
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                // Close resources
                scanner.close();
            }

        }else if (userChoice == 5){     // ------------------------------------Task 5
            try {
                // Establish the database connection
                Connection connection = DriverManager.getConnection(url, username, password);

                // Prompt user for input
                System.out.print("Enter Driver Name: ");
                String driverName = scanner.next();
                System.out.print("Enter Driver Telephone Number: ");
                String driverTelephoneNumber = scanner.next();

                // SQL query to insert a new driver
                String insertQuery = "INSERT INTO Driver (DriverName, DriverTelephoneNumber) VALUES (?, ?)";

                // Prepare the statement
                PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
                insertStatement.setString(1, driverName);
                insertStatement.setString(2, driverTelephoneNumber);

                // Execute the insert statement
                int rowsAffected = insertStatement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("==============================================\n" +
                    "Driver added successfully!\n" +
                    "==============================================\n");
                } else {
                    System.out.println("==============================================\n" +
                    "Failed to add driver.\n" +
                    "==============================================\n");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("==============================================\n" +
                "Failed to add driver.\n" +
                "==============================================\n");
            } finally {
                // Close resources
                scanner.close();
            }

        }else if (userChoice == 6){     // ------------------------------------Task 6
            try {
                // Establish the database connection
                Connection connection = DriverManager.getConnection(url, username, password);

                // Prompt user for input
                System.out.print("Enter Bus ID: ");
                int busID = scanner.nextInt();
                System.out.print("Enter Bus Model: ");
                String model = scanner.next();
                System.out.print("Enter Bus Year: ");
                int year = scanner.nextInt();

                // SQL query to insert a new bus
                String insertQuery = "INSERT INTO Bus (BusID, Model, Year) VALUES (?, ?, ?)";

                // Prepare the statement
                PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
                insertStatement.setInt(1, busID);
                insertStatement.setString(2, model);
                insertStatement.setInt(3, year);

                // Execute the insert statement
                int rowsAffected = insertStatement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("==============================================\n" +
                    "Bus added successfully!\n" +
                    "==============================================\n");
                } else {
                    System.out.println("==============================================\n" +
                "Failed to add bus.\n" +
                "==============================================\n");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("==============================================\n" +
                "Failed to add bus.\n" +
                "==============================================\n");
            } finally {
                // Close resources
                scanner.close();
            }

        }else if (userChoice == 7){     // ------------------------------------Task 7
            try {
                // Establish the database connection
                Connection connection = DriverManager.getConnection(url, username, password);

                // Prompt user for input
                System.out.print("Enter Bus ID to delete: ");
                int busIDToDelete = scanner.nextInt();

                // SQL query to delete a bus
                String deleteQuery = "DELETE FROM Bus WHERE BusID = ?";

                // Prepare the statement
                PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
                deleteStatement.setInt(1, busIDToDelete);

                // Execute the delete statement
                int rowsAffected = deleteStatement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("==============================================\n" +
                        "Bus deleted successfully!\n" +
                        "==============================================\n");
                } else {
                    System.out.println("==============================================\n" +
                        "No matching bus found for deletion.\n" +
                        "==============================================\n");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("==============================================\n" +
                "No matching bus found for deletion.\n" +
                "==============================================\n");
            } finally {
                // Close resources
                scanner.close();
            }

        }else if (userChoice == 8){     // ------------------------------------Task 8
            try {
                // Establish the database connection
                Connection connection = DriverManager.getConnection(url, username, password);

                // Prompt user for input
                System.out.print("Enter Trip Number: ");
                int tripNumber = scanner.nextInt();
                System.out.print("Enter Date (YYYY-MM-DD): ");
                String date = scanner.next();
                System.out.print("Enter Scheduled Start Time (HH:mm:ss): ");
                String scheduledStartTime = scanner.next();
                System.out.print("Enter Stop Number: ");
                int stopNumber = scanner.nextInt();
                System.out.print("Enter Scheduled Arrival Time (HH:mm:ss): ");
                String scheduledArrivalTime = scanner.next();
                System.out.print("Enter Actual Start Time (HH:mm:ss): ");
                String actualStartTime = scanner.next();
                System.out.print("Enter Actual Arrival Time (HH:mm:ss): ");
                String actualArrivalTime = scanner.next();
                System.out.print("Enter Number of Passengers In: ");
                int numberOfPassengersIn = scanner.nextInt();
                System.out.print("Enter Number of Passengers Out: ");
                int numberOfPassengersOut = scanner.nextInt();

                // SQL query to insert actual trip data
                String insertQuery = "INSERT INTO ActualTripStopInfo (TripNumber, Date, ScheduledStartTime, StopNumber, ScheduledArrivalTime, ActualStartTime, ActualArrivalTime, NumberOfPassengerIn, NumberOfPassengerOut) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

                // Prepare the statement
                PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
                insertStatement.setInt(1, tripNumber);
                insertStatement.setString(2, date);
                insertStatement.setString(3, scheduledStartTime);
                insertStatement.setInt(4, stopNumber);
                insertStatement.setString(5, scheduledArrivalTime);
                insertStatement.setString(6, actualStartTime);
                insertStatement.setString(7, actualArrivalTime);
                insertStatement.setInt(8, numberOfPassengersIn);
                insertStatement.setInt(9, numberOfPassengersOut);

                // Execute the insert statement
                int rowsAffected = insertStatement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("==============================================\n" +
                    "Actual trip data recorded successfully!\n" +
                    "==============================================\n");
                } else {
                    System.out.println("==============================================\n" +
                    "Failed to record actual trip data.\n" +
                    "==============================================\n");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("==============================================\n" +
                "Failed to record actual trip data.\n" +
                "==============================================\n");
            } finally {
                // Close resources
                scanner.close();
            }
        } else {
            System.out.println("==============================================");
            System.out.println("Error! Invalid Choice.");
            System.out.println("==============================================");
        }
    }
}
// COPYRIGHTS.© ABANOB AYOUB
// COPYRIGHTS.© ABANOB AYOUB
