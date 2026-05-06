// Name: Christian Francois
// SID: 1371909

import java.util.Random; // For random numbers (used for distance)
import java.util.Scanner; // For reading inputs

public class Mission {
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
    };

    public static void displayCrew() {
        System.out.println("\nCrew Status: ");

        for (Crew member : crewMembers) {
            System.out.println(member.name + " / " + member.role + " / " + member.health + " Health / " + member.getStatus());
        }
    }

    public static void main(String[] args) {
        Random rand = new Random();
        Scanner sc = new Scanner(System.in);

        // Handles mission state
        boolean running = true;
        boolean initialized = false;
        String lastAction;

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
            } else {
                // Run events and stuff
                if (lastAction.equals("Travel")) {
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

            System.out.println("Choose an action:");
            System.out.println("1 - Travel (-FUEL/OXYGEN, +DISTANCE)");
            System.out.println("2 - Maintain Ship (-FUEL/OXYGEN, +DISTANCE)");
            System.out.println("1 - Travel (-FUEL/OXYGEN, +DISTANCE)");
            System.out.println("1 - Travel (-FUEL/OXYGEN, +DISTANCE)");

            running = false;
        }

        sc.close();
    }
}
