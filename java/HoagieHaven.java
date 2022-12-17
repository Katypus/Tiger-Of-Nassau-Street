import java.util.Random;

// carry out company related functions (profit, random events) and return important values
public class HoagieHaven {

    // measure quantitative demand
    private double demand;
    // starting demand
    private double STARTING_DEMAND;
    // hoagies sold
    private int quantitySold;
    // price of one hoagie
    private double price;
    // hoagies made
    private int quantitySupplied;
    // money available
    private double fundsAvailable;
    // description of random event
    private String randomEvent;
    // what day it is
    private int day;
    // whether baby tiger has been created
    private boolean babyTiger;

    // Random object for random events
    Random r;

    // create hoagie market class
    public HoagieHaven(double fundsAvailable) {
        this.STARTING_DEMAND = 60;
        this.demand = STARTING_DEMAND;
        this.fundsAvailable = fundsAvailable;
        randomEvent = "";
        r = new Random();
    }

    // calculate the hoagies sold for one day
    public void simulateDay() {

        // relationship between demand the market price and quantity
        double DEMAND_PRICE = 7.5;
        double DEMAND_QUANTITY = 0.8;
        double priceDemanded = demand / DEMAND_PRICE;
        int quantityDemanded = (int) (demand * DEMAND_QUANTITY);

        // penalize player for setting price too high or making too many hoagies
        if (price > priceDemanded) {
            int PROPORTIONAIDTY = 6;
            quantitySold = (int) (quantitySupplied - ((price - priceDemanded) * PROPORTIONAIDTY));
            if (quantitySold < 0) {
                quantitySold = 0;
            }
        } else if (quantitySupplied > quantityDemanded) {
            quantitySold = quantityDemanded;
        } else {
            quantitySold = quantitySupplied;
        }

    }

    // make one hoagie
    public void makeHoagie() {
        quantitySupplied++;
    }

    // return funds
    public double funds() {
        return fundsAvailable;
    }

    // return price
    public double price() {
        return price;
    }

    // increment price
    public void changePrice() {
        price++;
    }

    // calculate and return profits
    public double profits() {
        int VARIABLE_COST = 2;
        int TOTAL_COST = 25;
        double revenue = quantitySold * price;
        double totalCost = (quantitySupplied * VARIABLE_COST) + TOTAL_COST;
        return revenue - totalCost;
    }

    public int hoagies() {
        return quantitySupplied;
    }

    public int getDay() {
        return day;
    }

    // tell them how much they made
    public String profitMessage() {
        if (profits() > 0) {
            return "Congrats! You made " + (int) (profits() * 100) / 100 + " dollars today.";
        } else if (profits() < 0) {
            return "Uh oh, looks like you lost " + (int) (profits() * 100) / 100 + " dollars today.";
        } else {
            return "You broke even today.";
        }
    }

    // add to funds, reset variables, increment day
    public void endDay() {
        day++;
        fundsAvailable += profits();
        quantitySold = 0;
        quantitySupplied = 0;
        price = 0;
    }

    // return the day
    public int day() {
        return day;
    }

    // check if baby tiger is generated
    public boolean babyTiger() {
        return babyTiger;
    }

    // return qualitative measure of demand
    public String qualDemand() {
        String qualDemand;
        int LOW_DEMAND = 50;
        int MED_DEMAND = 65;
        if (demand < LOW_DEMAND) {
            qualDemand = "Low";
        } else if (demand < MED_DEMAND) {
            qualDemand = "Medium";
        } else {
            qualDemand = "High";
        }
        return qualDemand;
    }

    // set up array of random events and generate events
    public void random() {
        int EVENT_NUMBER = 5;
        String[] randomEvents = new String[EVENT_NUMBER];
        randomEvents[0] = "A carb-free diet is sweeping Princeton! \n" +
                "Everyone is eating cabbage rolls instead of hoagies. The demand has decreased.";
        randomEvents[1] = "That new Indian Restaurant on Witherspoon got added to PawPoints. \n" +
                "Less people want hoagies ... they're eating samosas instead. Demand decreases.";
        randomEvents[2] = "A destructive tiger toddler wanders in and breaks your hoagie machine. \n" +
                "You decide to fix the hoagie machine ... and take him in. You lost $350 in funds.";
        randomEvents[3] = "It's the day before deans date and students are stressed. \n" +
                "They need some delicious hoagies to comfort them. Demand has increased.";
        randomEvents[4] = "St. Patty's Day has arrived and every student is partying. \n" +
                "There are flocks of students with late-night munchies and they're heading straight \n" +
                "towards you. Demand has increased.";
        // generate number between one and ten
        int event = r.nextInt(10);
        if (event < EVENT_NUMBER) {
            // assign string description of event
            randomEvent = randomEvents[event];
            // create event effect
            switch (event) {
                case 0:
                    double LOW_D = 0.55;
                    demand *= LOW_D;
                    break;
                case 1:
                    double LOW_ISH_D = 0.75;
                    demand *= LOW_ISH_D;
                    break;
                case 2:
                    // make sure baby tiger only happens once
                    if (!babyTiger) {
                        int CHILD_SUPPORT = 350;
                        fundsAvailable -= CHILD_SUPPORT;
                        babyTiger = true;
                        randomEvents[2] = "";
                    }
                    break;
                case 3:
                    double HIGH_ISH_D = 1.4;
                    demand *= HIGH_ISH_D;
                    break;
                case 4:
                    double HIGH_D = 2;
                    demand *= HIGH_D;
                    break;
            }
        }
    }

    // return string description of event
    public String eventDescription() {
        return randomEvent;
    }


}
