package kr.ac.jbnu.kjh.practice1.request;

public class SongCreateRequest {
    private String title;
    private String artist;

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }
}
