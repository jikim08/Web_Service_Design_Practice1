package kr.ac.jbnu.kjh.practice1.controller;

import kr.ac.jbnu.kjh.practice1.dto.ApiResponse;
import kr.ac.jbnu.kjh.practice1.dto.PlayListDto;
import kr.ac.jbnu.kjh.practice1.request.PlayListCreateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/playlist")
public class PlaylistController {
    private final Map<Long, PlayListDto> store = new HashMap<>();
    private Long nextId = 1L;

    // n번 ID의 노래가 포함된 플레이리스트 검색
    @GetMapping("/search")
    public ResponseEntity<ApiResponse> searchPlaylist(@RequestParam Long songId){
        ApiResponse response = new ApiResponse();

        List<PlayListDto> result = store.values().stream()
                .filter(playlist -> playlist.getSongIDs() != null && playlist.getSongIDs().contains(songId)).toList();

        response.setStatus("success");
        response.setData(result);
        return ResponseEntity.ok(response);
    }

    //플레이리스트 생성
    @PostMapping
    public ResponseEntity<ApiResponse> createPlaylist(@RequestBody PlayListCreateRequest request){
        ApiResponse response = new ApiResponse();
        //이름이 이상하면 badRequest보내기
        if (request.getName() == null || request.getName().isBlank() || request.getDescription() == null || request.getSongIDs() == null){
            response.setStatus("error");
            response.setData(Map.of("message", "잘못된 입력입니다"));
            return ResponseEntity.status(400).body(response);
        }
        PlayListDto playList = new PlayListDto();

        //Id 자동으로 할당
        playList.setId(nextId++);
        // 목록 초기화
        if (playList.getSongIDs() == null) {
            playList.setSongIDs(new ArrayList<>());
        }
        playList.setName(request.getName());
        playList.setDescription(request.getDescription());
        playList.setSongIDs(request.getSongIDs());

        response.setStatus("success");
        response.setData(playList);
        //저장
        store.put(playList.getId(), playList);
        return ResponseEntity.status(200).body(response);
    }

    //플레이리스트 수정
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updatePlaylist(@PathVariable Long id, @RequestBody PlayListCreateRequest request){
        ApiResponse response = new ApiResponse();
        PlayListDto playlist = store.get(id);

        if (playlist == null){
            response.setStatus("error");
            response.setData(Map.of("message", "플레이리스트를 찾을 수 없습니다"));

            return ResponseEntity.status(404).body(response);
        }
        if (request.getName() == null || request.getName().isBlank() ||
                request.getDescription() == null ||
                request.getSongIDs() == null
        ){

            response.setStatus("error");
            response.setData(Map.of("message", "잘못된 입력입니다"));
            return ResponseEntity.status(400).body(response);
        }

        playlist.setName(request.getName());
        playlist.setDescription(request.getDescription());
        playlist.setSongIDs(request.getSongIDs());
        response.setStatus("success");
        response.setData(playlist);

        return ResponseEntity.ok(response);
    }

    //플레이리스트 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deletePlaylist(@PathVariable Long id){
        ApiResponse response = new ApiResponse();
        PlayListDto playlist = store.get(id);
        if (playlist == null){
            response.setStatus("error");
            response.setData(Map.of("message", "플레이리스트를 찾을 수 없습니다."));

            return ResponseEntity.status(404).body(response);
        }
        store.remove(id);

        response.setStatus("success");
        response.setData(Map.of("message", "플레이리스트가 삭제되었습니다"));
        return ResponseEntity.ok(response);
    }
}
