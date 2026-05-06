public class Ship {
    // Ship Materials
    int fuel = 100;
    int oxygen = 100;
    int supplies = 100;

    String condition = "Maintained";
    boolean isTravelling = false;

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

        // Can't repair if you're resting, you need your sleep
        if (crewMember.isResting) {
            System.out.println(crewMember.name + " is resting and cannot maintain the ship!");
            return false;
        }

        // A dead person cannot maintain a ship
        if (!crewMember.alive) {
            System.out.println(crewMember.name + " is dead. They cannot maintain anything.");
            return false;
        }

        // Maintain the ship at the cost of supplies
        this.supplies -= 15;
        this.condition = "Maintained";

        // Return that it was successful
        return true;
    }

    // Rest crew member, which will heal them and take resources next travel
    public boolean rest(Crew crewMember) {
        // Dead
        if (!crewMember.alive) {
            System.out.println(crewMember.name + " is permanently at rest. This won't help.");
            return false;
        }

        // Fail resting if there are no supplies to rest with
        if (this.supplies < 5) {
            System.out.println("The ship ran out of supplies, so " + crewMember.name + " cannot rest.");
            return false;
        }

        // Allow member to rest, removing rationing status.
        crewMember.isResting = true;
        crewMember.isRationing = false;

        // Return that it was successful
        return true;
    }

    // Ration supplies, which will damage them and save resources next travel
    public boolean ration(Crew crewMember) {
        // Dead, so cannot ration
        if (!crewMember.alive) {
            System.out.println(crewMember.name + " is dead. There's nothing to save.");
            return false;
        }

        // Allow member to ration supplies, removing Rest status
        crewMember.isResting = false;
        crewMember.isRationing = true;

        // Return that it was successful
        return true;
    }

    // Continue ship travel, adjusting status and is further used in Mission class
    public boolean travel(Crew[] members, StringBuilder resourceHistory) {
        // Check if members are resting or rationing
        boolean membersHaveActions = true;
        for (Crew member : members) {
            if (member.alive && (!member.isRationing && !member.isResting)) {
                membersHaveActions = false;
                break;
            }
        }

        // Fail to travel if members haven't been assigned an action
        if (!membersHaveActions) {
            System.out.println("All members have not been assigned an action!");
            return false;
        }

        // Update all crew members for next day & lose 5 oxygen per non rationing member
        int oxygenLoss = 0;
        int supplyLoss = 0;
        for (Crew member : members) {
            if (member.alive) {
                if (!member.isRationing) {
                    oxygenLoss += 5;
                }

                if (member.isResting) {
                    supplyLoss += 5;
                }

                member.update();
            }
        }

        // Lower fuel, oxygen & supplies during travel
        this.fuel = Math.max(this.fuel - 10, 0);
        this.oxygen = Math.max(this.oxygen - oxygenLoss, 0);
        this.supplies = Math.max(this.supplies - supplyLoss, 0);
        this.isTravelling = true;

        // Add information to resource log
        resourceHistory.append("\n- 10 fuel was consumed during travelling.");
        resourceHistory.append("\n- ").append(oxygenLoss).append(" oxygen was consumed during travelling.");
        resourceHistory.append("\n- ").append(supplyLoss).append(" supplies were consumed during travelling.");

        // Return a successful travel
        return true;
    }
}
