// Name: Christian Francois
// SID: 1371909

import java.util.Random; // For random numbers (used for distance)
import java.util.Scanner; // For reading inputs

public class Mission {
    static Random rand = new Random();
    static Scanner sc = new Scanner(System.in);

    static int missionDay = 1; // Current day of the mission
    static int distance = 0; // Distance traveled for the mission
    static String event = "Clear"; // Current event for the day

    static Crew[] crewMembers = new Crew[3]; // 3 crew members per mission
    static Ship flyingShip = new Ship(); // The ship the crew will fly on
    static StringBuilder resourceHistory = new StringBuilder(); // Resource usage history

    static final String[] ROLES = {"Pilot", "Co-Pilot", "Crewmate"}; // Crew roles
    static final String[] SHIP_CONDITIONS = {"Maintained", "Blemished", "Impaired", "Critical"}; // Ship conditions
    static final String[] EVENTS = {"Meteor Shower", "Solar Flare", "Supply Cache"};
    static final int DISTANCE_REQUIRED = 100; // Distance required to win

    // Show the instructions on what to do
    public static void showInstructions() {
        System.out.println("Welcome to Space Mission Control! Manage your crew and limited resources to reach Mars!");
        System.out.println("You begin with 100 fuel, oxygen and supplies, which are used to manage the ship and crew.");
        System.out.println("Every action has a cost, whether it be from the crew or the ship, and daily events also pose a risk.");
        System.out.println("Your crew is at peak health, can you keep it that way?");
    }

    // Score that is calculated after a simulation ends
    public static int calculateScore() {
        int score = 0;

        for (Crew member : crewMembers) {
            if (member.alive) {
                score += 100;
            }
        }

        score += (flyingShip.fuel * 5);
        score += (flyingShip.supplies * 2);
        score += (flyingShip.oxygen * 3);
        score += Math.max(300 - (missionDay * 15), 0);

        return score;
    }

    public static void displayShip() {
        // Use ship info for multiple status strings
        System.out.println("\nShip Status: ");
        System.out.println("Oxygen: " + flyingShip.oxygen);
        System.out.println("Fuel: " + flyingShip.fuel);
        System.out.println("Supplies: " + flyingShip.supplies);
        System.out.println("Condition: " + SHIP_CONDITIONS[flyingShip.condition]);

        // Use mission info for information till completion
        System.out.println("\nDistance to Mars: " + distance + "/" + DISTANCE_REQUIRED + " Mm");
        System.out.println("Mission Day " + missionDay);

        // Display event information
        if (event.equals(EVENTS[0])) {
            // Meteor shower
            System.out.println("A METEOR SHOWER has damaged your ship today!");
        } else if (event.equals(EVENTS[1])) {
            // Solar Flare
            System.out.println("A SOLAR FLARE has harmed your crewmates today!");
        } else if (event.equals(EVENTS[2])) {
            // Supply Cache
            System.out.println("A hidden SUPPLY CACHE has given you extra supplies today!");
        }
    }

    public static void displayCrew() {
        System.out.println("\nCrew Status: ");

        // Use crew info to make a status string
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
            // Line break for start of action tree
            System.out.println("-----------------------------------------------");

            // Print instructions on initialization
            if (!initialized) {
                initialized = true;
                showInstructions();
                resourceHistory.append("DAY 1:");
            } else {
                if (distance >= DISTANCE_REQUIRED) {
                    // You won due to distance! Nice
                    int crewAlive = 0;

                    for (Crew member : crewMembers) {
                        if (member.alive) {
                            crewAlive += 1;
                        }
                    }

                    System.out.println("You won! You made it to mars!");
                    System.out.println("Crew Members left: " + crewAlive);
                    System.out.println("Days taken: " + missionDay);
                    System.out.println("Score: " + calculateScore());

                    running = false;
                    break;
                } else {
                    // Assume everyone died
                    boolean isEveryoneDead = true;

                    // If someone's alive, stop!
                    for (Crew member : crewMembers) {
                        if (member.alive) {
                            isEveryoneDead = false;
                            break;
                        }
                    }

                    if (flyingShip.oxygen <= 0 || flyingShip.fuel <= 0 || isEveryoneDead) {
                        System.out.println("You lost :(");
                        System.out.println("You couldn't make it to Mars, unfortunately.");
                        System.out.println("Total Days: " + missionDay);
                        System.out.println("Fuel left: " + flyingShip.fuel);
                        System.out.println("Oxygen left: " + flyingShip.oxygen);
                        System.out.println("Did everyone die? " + isEveryoneDead);
                        System.out.println("Score: " + calculateScore());

                        running = false;
                        break;
                    }
                }

                // Run events and stuff if traveling
                if (flyingShip.isTravelling) {
                    // Disable travel status and continue with journey
                    flyingShip.isTravelling = false;
                    missionDay += 1;
                    distance += rand.nextInt(6) + 5;
                    event = "Clear";

                    resourceHistory.append("\nDAY ").append(missionDay).append(":");

                    // Base % event chance
                    int eventChance = 50;

                    // Add 20% chance if not maintained
                    if (!SHIP_CONDITIONS[flyingShip.condition].equals("Maintained")) {
                        eventChance += 20;
                    }

                    // Choose event if chance is rolled
                    int rolledChance = rand.nextInt(99) + 1;
                    if (rolledChance <= eventChance && event.equals("Clear")) {
                        int chosenEvent = rand.nextInt(EVENTS.length);

                        if (chosenEvent == 0) {
                            // Meteor shower - worsen ship
                            flyingShip.condition = Math.min(flyingShip.condition + 1, SHIP_CONDITIONS.length - 1);
                        } else if (chosenEvent == 1) {
                            // Solar Flare - harm crewmates
                            for (Crew mate : crewMembers) {
                                if (mate.alive) {
                                    mate.health = Math.max(mate.health - 5, 5);
                                    mate.crewStatus = Math.min(mate.crewStatus + 1, mate.STATUS_NAMES.length - 1);
                                }
                            }
                        } else {
                            // Supply Cache - give supplies
                            flyingShip.supplies += 10;
                        }

                        // Set event to chosen event for information purposes
                        event = EVENTS[chosenEvent];
                    }

                    // Worsen ship condition
                    flyingShip.condition = Math.min(flyingShip.condition + 1, SHIP_CONDITIONS.length - 1);
                }
            }

            // Display crew information
            displayCrew();

            // Display ship information
            displayShip();

            // Action menu
            System.out.println("\nChoose an action:");
            System.out.println("1 - Travel (-FUEL/OXYGEN, +DISTANCE)");
            System.out.println("2 - Maintain Ship (-SUPPLIES, +SHIP STATUS/FUEL)");
            System.out.println("3 - Ration (-CREW HEALTH)");
            System.out.println("4 - Rest (+CREW HEALTH, -SUPPLIES)");
            System.out.println("5 - View Resource History");

            System.out.print("\nYour choice: ");
            int choice = sc.nextInt();

            // Using switch instead of if-else because it's easier
            int memberInt;
            switch (choice) {
                case 1:
                    // Attempt to travel
                    flyingShip.travel(crewMembers, resourceHistory);

                    break;
                case 2:
                    // Choose who to maintain ship and attempt it
                    System.out.println("Which crew member will maintain the ship?");

                    for (int i = 0; i < crewMembers.length; i++) {
                        Crew member = crewMembers[i];

                        System.out.println(i + " - " + member.name + " / " + member.getAction());
                    }

                    System.out.print("Choice: ");

                    memberInt = Math.clamp(sc.nextInt(), 0, crewMembers.length - 1);
                    flyingShip.maintain(crewMembers[memberInt], resourceHistory);

                    break;
                case 3:
                    // Choose who is rationing for next day
                    System.out.println("Which crew member will be rationing?");

                    for (int i = 0; i < crewMembers.length; i++) {
                        Crew member = crewMembers[i];

                        System.out.println(i + " - " + member.name + " / " + member.getAction());
                    }
                    System.out.println(crewMembers.length + " - All members");

                    System.out.print("Choice: ");
                    memberInt = Math.clamp(sc.nextInt(), 0, crewMembers.length);

                    if (memberInt == crewMembers.length) {
                        for (Crew member : crewMembers) {
                            flyingShip.ration(member);
                        }
                    } else {
                        flyingShip.ration(crewMembers[memberInt]);
                    }

                    break;
                case 4:
                    // Choose who is resting for next day
                    System.out.println("Which crew member will be resting?");

                    for (int i = 0; i < crewMembers.length; i++) {
                        Crew member = crewMembers[i];

                        System.out.println(i + " - " + member.name + " / " + member.getAction());
                    }
                    System.out.println(crewMembers.length + " - All members");

                    System.out.print("Choice: ");
                    memberInt = Math.clamp(sc.nextInt(), 0, crewMembers.length);

                    if (memberInt == crewMembers.length) {
                        for (Crew member : crewMembers) {
                            flyingShip.rest(member);
                        }
                    } else {
                        flyingShip.rest(crewMembers[memberInt]);
                    }

                    break;
                case 5:
                    System.out.println("\n" + resourceHistory.toString());
            }
        }

        System.out.println("\nThanks for enjoying the mission!");
        sc.close();
    }
}
