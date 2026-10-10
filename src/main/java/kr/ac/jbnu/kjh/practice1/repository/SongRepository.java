package kr.ac.jbnu.kjh.practice1.repository;

import kr.ac.jbnu.kjh.practice1.dto.SongDto;
import org.springframework.stereotype.Repository;

import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*노래 저장 공간*/
@Repository
public class SongRepository {
    private final Map<Long, SongDto> store = new HashMap<>();
    private Long nextId = 1L;

    //음악 저장 하기
    public SongDto save(SongDto song){
        if (song.getId()==null){
            song.setId(nextId++);
        }

        store.put(song.getId(), song);
        return song;
    }

    //전체 음악 조회
    public List<SongDto> findAll(){
        return new ArrayList<>(store.values());
    }

    //개별 음악 조회
    public SongDto findById(Long id){
        return store.get(id);
    }

    //음악 삭제
    public boolean deleteById(Long id){
        return store.remove(id) != null;
    }
}
