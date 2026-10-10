package kr.ac.jbnu.kjh.practice1.controller;

import kr.ac.jbnu.kjh.practice1.dto.ApiResponse;
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

    //ID로 노래검색
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getSong(@PathVariable Long id){
        SongDto song = store.get(id);
        ApiResponse response = new ApiResponse();
        if (song == null) {
            response.setStatus("error");
            response.setData(Map.of("message", "음악을 찾을 수 없습니다"));

            return ResponseEntity.status(404).body(response);
        }

        response.setStatus("success");
        response.setData(song);
        return ResponseEntity.ok(response);
    }

    //노래 생성
    @PostMapping
    public ResponseEntity<ApiResponse> createSong(@RequestBody SongCreateRequest request){
        SongDto song = new SongDto();
        ApiResponse response = new ApiResponse();
        if (request.getTitle() == null || request.getArtist() == null || request.getTitle().isBlank() || request.getArtist().isBlank()){
            response.setStatus("error");
            response.setData(Map.of("message", "잘못된 입력입니다"));
            return ResponseEntity.status(400).body(response);
        }

        song.setId(nextId++);
        song.setTitle(request.getTitle());
        song.setArtist(request.getArtist());
        store.put(song.getId(), song);

        response.setStatus("success");
        response.setData(song);
        return ResponseEntity.status(201).body(response);
    }

    //노래 정보 수정
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateSong(@PathVariable Long id, @RequestBody SongCreateRequest request){
        SongDto song = store.get(id);
        ApiResponse response = new ApiResponse();
        if (song==null){
            response.setStatus("error");
            response.setData(Map.of("message", "음악을 찾을 수 없습니다"));
           return ResponseEntity.status(404).body(response);
        }

        if (request.getTitle() == null || request.getTitle().isBlank() || request.getArtist() == null || request.getArtist().isBlank()){
            response.setStatus("error");
            response.setData(Map.of("message", "잘못된 입력입니다"));
            return ResponseEntity.status(400).body(response);
        }

        song.setTitle(request.getTitle());
        song.setArtist(request.getArtist());

        response.setStatus("success");
        response.setData(song);
        return ResponseEntity.ok(response);
    }

    //노래 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteSong(@PathVariable Long id){
        SongDto song = store.get(id);
        ApiResponse response = new ApiResponse();
        if (song == null){
            response.setStatus("error");
            response.setData(Map.of("message", "음악을 찾을 수 없습니다"));
            return ResponseEntity.status(404).body(response);
        }

        store.remove(id);
        response.setStatus("success");
        response.setData(Map.of("message", "음악이 삭제되었습니다."));
        return ResponseEntity.ok(response);
    }

}
