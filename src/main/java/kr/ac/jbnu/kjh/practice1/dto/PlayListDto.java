package kr.ac.jbnu.kjh.practice1.dto;

import java.util.List;

public class PlayListDto {
    private Long id;
    private String name;        //플레이리스트 이름
    private String description;     //플레이리스트 설명
    private List<Long>  songIDs;    //포함된 음악 ID

    //Getter
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
    public List<Long> getSongIDs(){
        return songIDs;
    }

    //Setter
    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSongIDs(List<Long> songIDs) {
        this.songIDs = songIDs;
    }
}
