public class Crew {
    // Crew Info
    String name;
    String role;
    int health = 100;
    boolean alive = true;

    boolean isFed = true;
    boolean isHydrated = true;
    boolean isResting = false;
    boolean malnourished = false; // Malnourished if the member has not been fed or hydrated

    // Constructor to set up crew member
    public Crew(String chosenName, String crewRole) {
        name = chosenName;
        role = crewRole;
    }

    // Get crew status as string
    public String getStatus() {
        if (!alive) {
            return "Dead";
        } else if (malnourished) {
            return "Malnourished";
        } else if (isHydrated && !isFed) {
            return "Hungry";
        } else if (!isHydrated && isFed) {
            return "Thirsty";
        }

        return "Well";
    }

    // Update crew status when called
    public void update() {
        // Remove 5 health if not fed
        if (!isFed) {
            if (!malnourished) {
                System.out.println(this.name + " is hungry! -5 HP");
            }

            health -= 5;
        }

        // Remove 10 health if not hydrated since water is very important
        if (!isHydrated) {
            if (!malnourished) {
                System.out.println(this.name + " is thirsty! -10 HP");
            }

            health -= 10;
        }

        // One announcement if not fed and hungry
        if (malnourished) {
            System.out.println(this.name + " is MALNOURISHED! -15 HP");
        }

        // Die if you lose all HP
        if (health <= 0) {
            alive = false;
            health = 0;
        }

        // Reset food and health status
        isFed = false;
        isHydrated = false;
        malnourished = true;
    }
}
