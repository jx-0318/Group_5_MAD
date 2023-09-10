package my.edu.utar.noiseapp;

public class RecordingData {
    private String filePath;
    private long timestamp;

    public RecordingData(String filePath, long timestamp) {
        this.filePath = filePath;
        this.timestamp = timestamp;
    }

    public String getFilePath() {
        return filePath;
    }

    public long getTimestamp() {
        return timestamp;
    }
}


