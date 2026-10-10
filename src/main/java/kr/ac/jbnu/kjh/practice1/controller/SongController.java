package kr.ac.jbnu.kjh.practice1.controller;

import kr.ac.jbnu.kjh.practice1.dto.SongDto;
import kr.ac.jbnu.kjh.practice1.request.SongCreateRequest;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/songs")
public class SongController {

    public final Map<Long, SongDto> store = new HashMap<>();
    private Long nextId = 1L;

    @GetMapping
    public List<SongDto> getAllSongs(){
        return new ArrayList<>(store.values());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SongDto> getSong(@PathVariable Long id){
        SongDto song = store.get(id);
        if (song == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(song);
    }

    @PostMapping
    public ResponseEntity<SongDto> createSong(@RequestBody SongCreateRequest request){
        SongDto song = new SongDto();
        song.setId(nextId++);
        song.setTitle(request.getTitle());
        song.setArtist(request.getArtist());
        store.put(song.getId(), song);

        return ResponseEntity.ok(song);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SongDto> updateSong(@PathVariable Long id, @RequestBody SongCreateRequest request){
        SongDto song = store.get(id);

        if (song==null){
           return ResponseEntity.notFound().build();
        }

        if (request.getTitle() == null || request.getTitle().isBlank() || request.getArtist() == null || request.getArtist().isBlank()){
            return ResponseEntity.badRequest().build();
        }

        song.setTitle(request.getTitle());
        song.setArtist(request.getArtist());
        return ResponseEntity.ok(song);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSong(@PathVariable Long id){
        SongDto song = store.get(id);

        if (song == null){
            return ResponseEntity.notFound().build();
        }

        store.remove(id);

        return ResponseEntity.ok("음악이 삭제되었습니다");
    }

}
