package my.edu.utar.noiseapp;

public class NoiseLevelData {
    private int noiseLevel;
    private String category;

    public NoiseLevelData() {
        // Default constructor required for Firebase.
    }

    public NoiseLevelData(int noiseLevel, String category) {
        this.noiseLevel = noiseLevel;
        this.category = category;
    }

    public int getNoiseLevel() {
        return noiseLevel;
    }

    public String getCategory() {
        return category;
    }
}




