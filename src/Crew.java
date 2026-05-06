public class Crew {
    // Crew Info
    String name;
    String role;
    int health = 100;
    boolean alive = true;

    final String[] STATUS_NAMES = {"Healthy", "Malnourished", "Starved", "Dying"};
    int crewStatus = 0;
    boolean isRationing = false;
    boolean isResting = false;

    // Constructor to set up crew member
    public Crew(String chosenName, String crewRole) {
        name = chosenName;
        role = crewRole;
    }

    // Get crew status as string
    public String getStatus() {
        if (!alive) {
            return "Dead";
        } else {
            return STATUS_NAMES[this.crewStatus];
        }
    }

    // Get crew action as string
    public String getAction() {
        if (this.isRationing) {
            return "Rationing";
        } else if (this.isResting) {
            return "Resting";
        } else {
           return "No Action";
        }
    }

    // Update crew status when called
    public void update() {
        // Don't update if the crew member is dead
        if (!this.alive) {
            return;
        }

        if (isResting) {
            // Heal crewmate and restore status by 1
            this.health += 5;
            this.crewStatus = Math.max(this.crewStatus - 1, 0);
        } else if (isRationing) {
            // Lower status by 1
            this.crewStatus = Math.min(this.crewStatus + 1, STATUS_NAMES.length - 1);
        }

        // Remove health on update based on current status
        int healthLoss = (this.crewStatus * 5);
        this.health -= healthLoss;

        // Die if you lose all HP
        if (this.health <= 0) {
            this.alive = false;
            this.health = 0;
        }
    }
}
