package my.edu.utar.individualpracticalassignment.comparenumbers;

public class LevelManager {
    private int level;
    private int largestBound;

    public LevelManager(){
       this.level = 1;
       this.largestBound = 10;
    }
    public void setLevel(int level){
        this.level =  level;
    }
    public void increaseLevelAndUpdateDifficulty(){
        this.level++;
        switch(this.level){
            case 2:
                this.largestBound = 100;
                break;
            case 3:
                this.largestBound = 999;
                break;
            default:
                this.largestBound = 10;
                break;
        }
    }
    public int getLargestBound(){
        return this.largestBound;
    }
    public int getLevel(){
        return this.level;
    }
}
