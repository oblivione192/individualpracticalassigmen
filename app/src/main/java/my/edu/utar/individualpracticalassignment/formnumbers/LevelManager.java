package my.edu.utar.individualpracticalassignment.formnumbers;

import java.util.HashMap;
import java.util.Random;

public class LevelManager {

    private int level;
    private HashMap<String, Integer> priceList;

    public LevelManager() {
        this.level = 1;
        priceList = new HashMap<>();

        priceList.put("🍬", 1);
        priceList.put("🍭", 3);
        priceList.put("🎂", 5);
    }

    public int getLevel() {
        return this.level;
    }

    public int getPriceOf(String candy) {
        int price = priceList.get(candy);
        return price;
    }

    private void updateDifficulty() {
        Random rand = new Random();

        int sweetPrice = 0;
        int lollipopPrice = 0;
        int cupcakePrice = 0;

        switch (this.level) {
            case 2:
                sweetPrice = 5;
                lollipopPrice = rand.nextInt(4) * 10;
                cupcakePrice = rand.nextInt(10) * 5;
                break;
            case 3:
                sweetPrice = 10 + rand.nextInt(10);
                lollipopPrice = 10 + rand.nextInt(8) * 5;
                cupcakePrice = 10 + rand.nextInt(9) * 10;
                break;
            case 4:
                sweetPrice = 20 + rand.nextInt(10);
                lollipopPrice = 15 + rand.nextInt(12) * 5;
                cupcakePrice = 20 + rand.nextInt(10) * 10;
                break;
        }

        priceList.put("🍬", sweetPrice);
        priceList.put("🍭", lollipopPrice);
        priceList.put("🎂", cupcakePrice);
    }

    public void reset() {
        this.level = 1;
        priceList = new HashMap<>();

        priceList.put("🍬", 1);
        priceList.put("🍭", 3);
        priceList.put("🎂", 5);
    }

    public int getTarget() {
        int sweet = priceList.get("🍬");
        int lollipop = priceList.get("🍭");
        int cupcake = priceList.get("🎂");

        Random rand = new Random();
        int sweet_bound_coefficient, lollipop_bound_coefficient, cupcake_bound_coefficient;

        // Define difficulty scaling based on level
        sweet_bound_coefficient = (int) (Math.log(level + 1) * 3) + rand.nextInt(3); // Scaling using log
        lollipop_bound_coefficient = (int) (Math.log(level + 2) * 2) + rand.nextInt(3);
        cupcake_bound_coefficient = (int) (Math.log(level + 3) * 1.5) + rand.nextInt(2);

        // New difficulty scaling for level 4
        if (level >= 4) {
            sweet_bound_coefficient += rand.nextInt(2); // Adding a small scaling factor
            lollipop_bound_coefficient += rand.nextInt(2);
            cupcake_bound_coefficient += rand.nextInt(2);
        }

        int targetValue = (sweet * sweet_bound_coefficient) +
                (lollipop * lollipop_bound_coefficient) +
                (cupcake * cupcake_bound_coefficient);

        return targetValue;
    }

    public void increaseLevel() {
        this.level++;
        updateDifficulty();
    }
}
