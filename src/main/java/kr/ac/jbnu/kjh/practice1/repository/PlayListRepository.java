package kr.ac.jbnu.kjh.practice1.repository;

import kr.ac.jbnu.kjh.practice1.dto.PlayListDto;
import org.springframework.stereotype.Repository;

import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class PlayListRepository{
    private final Map<Long, PlayListDto> store = new HashMap<>();
    private Long nextId = 1L;

    //플레이리스트 저장
    public PlayListDto save(PlayListDto playlist){
        if (playlist.getId() == null){
            playlist.setId(nextId++);
        }

        store.put(playlist.getId(), playlist);
        return playlist;
    }
    //전체 플레이리스트 조회
    public List<PlayListDto> findAll(){
        return new ArrayList<>(store.values());
    }

    // 특정 플레이리스트 조회
    public PlayListDto findById(Long id) {
        return store.get(id);
    }

    // 플레이리스트 삭제
    public boolean deleteById(Long id) {
        return store.remove(id) != null;
    }
}

