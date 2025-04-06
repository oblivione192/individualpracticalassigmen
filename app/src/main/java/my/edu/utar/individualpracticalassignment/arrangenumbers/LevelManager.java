package my.edu.utar.individualpracticalassignment.arrangenumbers;

import java.util.Arrays;
import java.util.Random;

public class LevelManager {
    private int level;
    private int numberOfSlots;
    private int sortMode;
    private int bound;
    private boolean makeSortModeRandom;

    public LevelManager() {
        this.level = 1;
        this.numberOfSlots = 4; // Remove conflicting assignment
        this.bound = 10;
        this.sortMode = 1;
        this.makeSortModeRandom = false;
    }
    public int getTotalSlots(){
        return numberOfSlots;
    }
    private void shuffleArray(int[] array) {
        //Fisher-Yates algorithm to shuffle the array to ensure further randomness
        Random rand = new Random();
        for (int i = array.length - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            int temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }
    }

    public int[] getNumArray() {
        int[] arr = new int[numberOfSlots];
        Random rand = new Random();

        if (makeSortModeRandom) {
            sortMode = rand.nextBoolean() ? 2 : 1;
        }

        for (int i = 0; i < numberOfSlots; i++) {
            arr[i] = rand.nextInt(bound);
        }

        shuffleArray(arr);
        return arr;
    }
    public String getSortMode(){
        if(sortMode == 1) { return "Ascending";}
        return "Descending";
    }
    public void increaseLevel() {
        if(level < 4) {
            this.level++;
        }
        updateDifficulty();
    }
    private void increaseSlots(){
        if(this.numberOfSlots < 7) {
            this.numberOfSlots++;
        }
    }
    public int getLevel() {
        return this.level;
    }

    public void updateDifficulty() {
        switch (level) {
            case 2:
                this.makeSortModeRandom = true;
                this.bound = 40;
                break;
            case 3:
                this.bound = 350;
                break;
            case 4:
                this.bound = 1100;
                break;
        }
        increaseSlots();
    }

    public boolean questionCompleted(int[] array) {
        if (array.length < 2) return true; // Avoids errors for small arrays

        switch (sortMode) {
            case 1: // Ascending order
                for (int i = 0; i < array.length - 1; i++) {
                    if (array[i] > array[i + 1]) {
                        return false;
                    }
                }
                break;
            case 2: // Descending order
                for (int i = 0; i < array.length - 1; i++) {
                    if (array[i] < array[i + 1]) {
                        return false;
                    }
                }
                break;
        }

        return true;
    }
}
