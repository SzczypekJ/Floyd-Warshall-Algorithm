package com.solvd.controller;

import com.solvd.models.Road;
import com.solvd.models.Station;
import com.solvd.service.RoadService;
import com.solvd.service.RoadServiceImpl;
import com.solvd.service.StationService;
import com.solvd.service.StationServiceImpl;

import java.util.List;
import java.util.Scanner;

public class NavigatorController {
    private final StationService stationService;
    private final RoadService roadService;

    public NavigatorController() {
        this.stationService = new StationServiceImpl();
        this.roadService = new RoadServiceImpl();
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("==== Welcome to the Navigator ====");
        boolean exit = false;

        while (!exit) {
            System.out.println("\n==== Main Menu ====");
            System.out.println("1. Work with the database (stations/roads)");
            System.out.println("2. Work with the Floyd-Warshall Algorithm (placeholder)");
            System.out.println("3. Exit the application");
            System.out.print("Choose an option: ");

            int choiceMain = scanner.nextInt();
            scanner.nextLine();

            switch (choiceMain) {
                case 1:
                    handleDatabaseOperations(scanner);
                    break;
                case 2:
                    handleAlgorithmMenu(scanner);
                    break;
                case 3:
                    exit = true;
                    System.out.println("Exiting the application...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }

    private void handleDatabaseOperations(Scanner scanner) {
        boolean exitDatabase = false;

        while (!exitDatabase) {
            System.out.println("\n==== Database Operations Menu ====");
            System.out.println("1. List all stations");
            System.out.println("2. Get station by ID");
            System.out.println("3. Create new station");
            System.out.println("4. Update station by ID");
            System.out.println("5. Delete station by ID");
            System.out.println("6. List all roads");
            System.out.println("7. Get road by ID");
            System.out.println("8. Get roads by mode");
            System.out.println("9. Create new road");
            System.out.println("10. Update road by ID");
            System.out.println("11. Delete road by ID");
            System.out.println("12. Return to Main Menu");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    listAllStations();
                    break;
                case 2:
                    getStationById(scanner);
                    break;
                case 3:
                    createNewStation(scanner);
                    break;
                case 4:
                    updateStationById(scanner);
                    break;
                case 5:
                    deleteStationById(scanner);
                    break;
                case 6:
                    listAllRoads();
                    break;
                case 7:
                    getRoadById(scanner);
                    break;
                case 8:
                    getRoadByMode(scanner);
                    break;
                case 9:
                    createNewRoad(scanner);
                    break;
                case 10:
                    updateRoadById(scanner);
                    break;
                case 11:
                    deleteRoadById(scanner);
                    break;
                case 12:
                    exitDatabase = true;
                    System.out.println("Returning to Main Menu...");
                    break;
                default:
                    System.out.println("Invalid choice. Please select again.");
                    break;
            }
        }
    }

    private void handleAlgorithmMenu(Scanner scanner) {
        boolean exitAlgorithm = false;

        while (!exitAlgorithm) {
            System.out.println("\n==== Floyd-Warshall Algorithm Menu ====");
            System.out.println("1. Compute shortest path (placeholder)");
            System.out.println("2. Compute alternative path (placeholder)");
            System.out.println("3. Return to Main Menu");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    calculateRoute(scanner, false);
                    break;
                case 2:
                    calculateRoute(scanner, true);
                    break;
                case 3:
                    exitAlgorithm = true;
                    System.out.println("Returning to Main Menu...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }

    private void calculateRoute(Scanner scanner, boolean alternative) {
        System.out.print("Enter start station name: ");
        String startName = scanner.nextLine().trim();

        Station startStation = getStationByName(startName);

        if (startStation == null) {
            System.out.println("Station " + startName + " does not exist.");
            return;
        }

        System.out.print("Enter destination station name: ");
        String endName = scanner.nextLine().trim();

        Station endStation = getStationByName(endName);
        if (endStation == null) {
            System.out.println("Station '" + endName + "' does not exist.");
            return;
        }

        System.out.print("Enter mode of transport (CAR or BUS): ");
        String mode = scanner.nextLine().trim().toUpperCase();

        if (!mode.equals("CAR") && !mode.equals("BUS")) {
            System.out.println("Invalid mode. Please enter CAR or BUS.");
            return;
        }

        computeRoute(startStation.getStationId(), endStation.getStationId(), mode, alternative);
    }

    private Station getStationByName(String name) {

        List<Station> stations = stationService.getAllStations();
        for (Station s : stations) {
            if (s.getName().equalsIgnoreCase(name)) {
                return s;
            }
        }
        return null;
    }

    private void computeRoute(int startStationId, int endStationId, String mode, boolean alternative) {
        if (!alternative) {
            System.out.println("Feature in progress: computing shortest path from "
                    + startStationId + " to " + endStationId + " via " + mode + "...");
        } else {
            System.out.println("Feature in progress: computing alternative path from "
                    + startStationId + " to " + endStationId + " via " + mode + "...");
        }
        // TODO: Integrate real Floyd-Warshall logic here
    }



    private void listAllStations() {
        List<Station> stations = stationService.getAllStations();
        System.out.println("=== All Stations ===");
        stations.forEach(System.out::println);
    }

    private void getStationById(Scanner scanner) {
        System.out.print("Enter station ID: ");
        int stationId = scanner.nextInt();
        scanner.nextLine();

        Station s = stationService.getStationById(stationId);
        if (s != null) {
            System.out.println("Found: " + s);
        } else {
            System.out.println("No station found with ID " + stationId);
        }
    }

    private void createNewStation(Scanner scanner) {
        System.out.print("Enter station name: ");
        String name = scanner.nextLine();
        System.out.print("Enter X coordinate: ");
        double x = scanner.nextDouble();
        System.out.print("Enter Y coordinate: ");
        double y = scanner.nextDouble();
        scanner.nextLine();

        Station station = new Station();
        station.setName(name);
        station.setXCoord(x);
        station.setYCoord(y);

        stationService.createStation(station);
        System.out.println("Station created with ID: " + station.getStationId());
    }

    private void updateStationById(Scanner scanner) {
        System.out.print("Enter the station ID to update: ");
        int stationId = scanner.nextInt();
        scanner.nextLine();

        Station existingStation = stationService.getStationById(stationId);
        if (existingStation == null) {
            System.out.println("No station found with ID " + stationId);
            return;
        }

        System.out.println("Existing Station: " + existingStation);
        System.out.print("Enter new name (press Enter to keep current): ");
        String newName = scanner.nextLine();
        if (!newName.isEmpty()) {
            existingStation.setName(newName);
        }

        System.out.print("Enter new X coordinate (or Enter to keep current): ");
        String newX = scanner.nextLine();
        if (!newX.isEmpty()) {
            existingStation.setXCoord(Double.parseDouble(newX));
        }

        System.out.print("Enter new Y coordinate (or Enter to keep current): ");
        String newY = scanner.nextLine();
        if (!newY.isEmpty()) {
            existingStation.setYCoord(Double.parseDouble(newY));
        }

        stationService.updateStation(stationId, existingStation);
        System.out.println("Station updated successfully. New info: " + stationService.getStationById(stationId));
    }

    private void deleteStationById(Scanner scanner) {
        System.out.print("Enter the station ID to delete: ");
        int stationId = scanner.nextInt();
        scanner.nextLine();

        Station existing = stationService.getStationById(stationId);
        if (existing == null) {
            System.out.println("No station found with ID " + stationId);
            return;
        }

        stationService.deleteStation(stationId);
        System.out.println("Deleted station with ID: " + stationId);
    }

    private void listAllRoads() {
        System.out.println("=== All Roads ===");
        roadService.getAllRoads().forEach(System.out::println);
    }

    private void getRoadById(Scanner scanner) {
        System.out.print("Enter road ID: ");
        int roadId = scanner.nextInt();
        scanner.nextLine();

        Road r = roadService.getRoadById(roadId);
        if (r != null) {
            System.out.println("Found: " + r);
        } else {
            System.out.println("No road found with ID " + roadId);
        }
    }

    private void getRoadByMode(Scanner scanner) {
        System.out.print("Enter road mode: ");
        String mode = scanner.nextLine();

        List<Road> r = roadService.getRoadsByMode(mode);

        if (r != null) {
            System.out.println("Found: " + r);
        } else {
            System.out.println("No roads found with Mode " + mode);
        }

    }

    private void createNewRoad(Scanner scanner) {
        System.out.print("Enter fromStationId: ");
        int fromId = scanner.nextInt();
        System.out.print("Enter toStationId: ");
        int toId = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter mode (CAR or BUS): ");
        String mode = scanner.nextLine();

        System.out.print("Is it one-way? (true/false): ");
        boolean isOneWay = scanner.nextBoolean();
        scanner.nextLine();

        System.out.print("Enter bus color (or press Enter if not a bus): ");
        String busColor = scanner.nextLine();
        if (busColor.isEmpty()) {
            busColor = null;
        }

        Road road = new Road();
        road.setFromStationId(fromId);
        road.setToStationId(toId);
        road.setMode(mode);
        road.setOneWay(isOneWay);
        road.setBusColor(busColor);

        roadService.createRoad(road);
        System.out.println("Road created with ID: " + road.getRoadId());
    }

    private void updateRoadById(Scanner scanner) {
        System.out.print("Enter the road ID to update: ");
        int roadId = scanner.nextInt();
        scanner.nextLine();

        Road existingRoad = roadService.getRoadById(roadId);
        if (existingRoad == null) {
            System.out.println("No road found with ID " + roadId);
            return;
        }

        System.out.println("Existing Road: " + existingRoad);
        System.out.print("Enter new fromStationId (or Enter to keep current): ");
        String fromInput = scanner.nextLine();
        if (!fromInput.isEmpty()) {
            existingRoad.setFromStationId(Integer.parseInt(fromInput));
        }

        System.out.print("Enter new toStationId (or Enter to keep current): ");
        String toInput = scanner.nextLine();
        if (!toInput.isEmpty()) {
            existingRoad.setToStationId(Integer.parseInt(toInput));
        }

        System.out.print("Enter new mode (CAR/BUS or Enter to keep current): ");
        String newMode = scanner.nextLine();
        if (!newMode.isEmpty()) {
            existingRoad.setMode(newMode);
        }

        System.out.print("Is it one-way? (true/false or Enter to keep current): ");
        String oneWayInput = scanner.nextLine();
        if (!oneWayInput.isEmpty()) {
            existingRoad.setOneWay(Boolean.parseBoolean(oneWayInput));
        }

        System.out.print("Enter new bus color (or Enter to keep current): ");
        String newColor = scanner.nextLine();
        if (!newColor.isEmpty()) {
            existingRoad.setBusColor(newColor);
        }

        roadService.updateRoad(roadId, existingRoad);
        System.out.println("Road updated successfully. New info: " + roadService.getRoadById(roadId));
    }

    private void deleteRoadById(Scanner scanner) {
        System.out.print("Enter the road ID to delete: ");
        int roadId = scanner.nextInt();
        scanner.nextLine();

        Road existing = roadService.getRoadById(roadId);
        if (existing == null) {
            System.out.println("No road found with ID " + roadId);
            return;
        }

        roadService.deleteRoad(roadId);
        System.out.println("Deleted road with ID: " + roadId);
    }
}
