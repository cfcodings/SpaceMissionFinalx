public class Crew {
    // Crew Info
    String name;
    String role;
    int health = 100;
    boolean alive = true;

    // Constructor to set up crew member
    public Crew(String chosenName, String crewRole) {
        name = chosenName;
        role = crewRole;
    }
}
