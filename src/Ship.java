public class Ship {
    // Ship Materials
    int fuel = 100;
    int oxygen = 100;
    int supplies = 100;

    String condition = "Maintained";

    // Maintain the ship, costing a repair kit in exchange for less travel failure risk
    public boolean maintain(Crew crewMember) {
        // If already maintained, don't do anything
        if (this.condition.equals("Maintained")) {
            System.out.println("The ship is already maintained! No worries.");
            return false;
        }

        // If we have no supplies we can't maintain the ship
        if (this.supplies < 15) {
            System.out.println("The ship ran out of supplies, so it cannot be maintained.");
            return false;
        }

        // Can't repair if you haven't had food or water 💀
        if (crewMember.malnourished) {
            System.out.println(crewMember.name + " is malnourished and cannot maintain the ship!");
            return false;
        }

        // A dead person cannot maintain a ship
        if (!crewMember.alive) {
            System.out.println(crewMember.name + " is dead.");
            return false;
        }

        // Maintain the ship at the cost of supplies
        this.supplies -= 15;
        this.condition = "Maintained";

        // Return that it was successful
        return true;
    }

    // Feed a crew member, costing supplies in exchange for enabling Fed status on a crew member
    public boolean feed(Crew crewMember) {
        // Fail feeding if there are no supplies to feed with
        if (this.supplies < 5) {
            System.out.println("The ship ran out of supplies, so " + crewMember.name + " cannot be fed.");
            return false;
        }

        // Remove supplies and feed the crew member, disabling Malnourished status
        this.supplies -= 5;
        crewMember.isFed = true;
        crewMember.malnourished = false;

        // Return that it was successful
        return true;
    }

    // Feed a crew member, costing supplies in exchange for enabling Hydrated status on a crew member
    public boolean quench(Crew crewMember) {
        // Fail feeding if there are no supplies to feed with
        if (this.supplies < 5) {
            System.out.println("The ship ran out of supplies, so " + crewMember.name + " cannot be quenched.");
            return false;
        }

        // Remove supplies and hydrate the crew member, disabling Malnourished status
        this.supplies -= 5;
        crewMember.isHydrated = true;
        crewMember.malnourished = false;

        // Return that it was successful
        return true;
    }
}
