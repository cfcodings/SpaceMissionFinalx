// Name: Christian Francois
// SID: 1371909

import java.util.Random; // For random numbers (used for distance)
import java.util.Scanner; // For reading inputs

public class Mission {
    static Random rand = new Random();
    static Scanner sc = new Scanner(System.in);

    static int missionDay = 1; // Current day of the mission
    static int distance = 0; // Distance traveled for the mission

    static Crew[] crewMembers = new Crew[3]; // 3 crew members per mission
    static Ship flyingShip = new Ship(); // The ship the crew will fly on
    static StringBuilder resourceHistory = new StringBuilder(); // Resource usage history

    static final String[] ROLES = {"Pilot", "Co-Pilot", "Crewmate"}; // Crew roles
    static final String[] SHIP_CONDITIONS = {"Maintained", "Blemished", "Impaired", "Critical"}; // Ship conditions
    static final int DISTANCE_REQUIRED = 100; // Distance required to win

    // Show the instructions on what to do
    public static void showInstructions() {
        System.out.println("\nWelcome to Space Mission Control! Manage your crew and limited resources to reach Mars!");
        System.out.println("You begin with 100 fuel, oxygen and supplies, which are used to manage the ship and crew.");
        System.out.println("Every action has a cost, whether it be from the crew or the ship, and daily events also pose a risk.");
        System.out.println("Your crew is at peak health, can you keep it that way?");
    }

    public static void displayShip() {
        System.out.println("\nShip Status: ");
        System.out.println("Oxygen: " + flyingShip.oxygen);
        System.out.println("Fuel: " + flyingShip.fuel);
        System.out.println("Supplies: " + flyingShip.supplies);
        System.out.println("Condition: " + flyingShip.condition);
    }

    public static void displayCrew() {
        System.out.println("\nCrew Status: ");

        for (Crew member : crewMembers) {
            System.out.println(member.name + " / " + member.role + " / " + member.health + " Health / " + member.getStatus() + " / " + member.getAction());
        }
    }

    public static void main(String[] args) {
        // Handles mission state
        boolean running = true;
        boolean initialized = false;

        // Allow players to customize the ship name
        for (int i = 0; i < crewMembers.length; i++) {
            String roleName = ROLES[i];

            System.out.print("Name your " + roleName + ": ");
            String memberName = sc.nextLine();

            Crew member = new Crew(memberName, roleName);
            crewMembers[i] = member;
        }

        while (running) {
            // Print instructions on initialization
            if (!initialized) {
                initialized = true;
                showInstructions();
                resourceHistory.append("DAY 1:");
            } else {
                // Run events and stuff if traveling
                if (flyingShip.isTravelling) {
                    // Disable travel status and continue with journey
                    flyingShip.isTravelling = false;
                    missionDay += 1;
                    distance += rand.nextInt(6) + 5;

                    resourceHistory.append("\nDAY ").append(missionDay).append(":");

                    // Base % event chance
                    int eventChance = 50;

                    // Add 20% chance if not maintained
                    if (!flyingShip.condition.equals("Maintained")) {
                        eventChance += 20;
                    }

                    int rolledChance = rand.nextInt(99) + 1;
                    if (rolledChance <= eventChance) {
                        // TODO: choose weather
                    }
                }
            }

            // Display crew information
            displayCrew();

            // Display ship information
            displayShip();

            // Action menu
            System.out.println("\nChoose an action:");
            System.out.println("1 - Travel (-FUEL/OXYGEN, +DISTANCE)");
            System.out.println("2 - Maintain Ship (-SUPPLIES, +SHIP STATUS)");
            System.out.println("3 - Ration (-CREW HEALTH)");
            System.out.println("4 - Rest (+CREW HEALTH, -SUPPLIES)");
            System.out.println("5 - View Resource History");

            System.out.print("\nYour choice: ");
            int choice = sc.nextInt();

            // Using switch instead of if-else because it's easier
            int memberInt = 0;
            switch (choice) {
                case 1:
                    flyingShip.travel(crewMembers, resourceHistory);

                    break;
                case 2:
                    System.out.println("Which crew member will maintain the ship?");

                    for (int i = 0; i < crewMembers.length; i++) {
                        Crew member = crewMembers[i];

                        System.out.println(i + " - " + member.name + " / " + member.getAction());
                    }

                    System.out.print("Choice: ");

                    memberInt = Math.clamp(sc.nextInt(), 0, crewMembers.length - 1);
                    flyingShip.maintain(crewMembers[memberInt]);

                    break;
                case 3:
                    System.out.println("Which crew member will be rationing?");

                    for (int i = 0; i < crewMembers.length; i++) {
                        Crew member = crewMembers[i];

                        System.out.println(i + " - " + member.name + " / " + member.getAction());
                    }

                    System.out.print("Choice: ");

                    memberInt = Math.clamp(sc.nextInt(), 0, crewMembers.length - 1);
                    flyingShip.ration(crewMembers[memberInt]);

                    break;
                case 4:
                    System.out.println("Which crew member will be resting?");

                    for (int i = 0; i < crewMembers.length; i++) {
                        Crew member = crewMembers[i];

                        System.out.println(i + " - " + member.name + " / " + member.getAction());
                    }

                    System.out.print("Choice: ");

                    memberInt = Math.clamp(sc.nextInt(), 0, crewMembers.length - 1);
                    flyingShip.rest(crewMembers[memberInt]);

                    break;
                case 5:
                    System.out.println("\n" + resourceHistory.toString());
            }
        }

        sc.close();
    }
}
