package kr.ac.jbnu.kjh.practice1.controller;

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
    public ResponseEntity<List<PlayListDto>> searchPlaylist(@RequestParam Long songId){
        List<PlayListDto> result = store.values().stream()
                .filter(playlist -> playlist.getSongIDs() != null && playlist.getSongIDs().contains(songId)).toList();

        return ResponseEntity.ok(result);
    }

    //플레이리스트 생성
    @PostMapping
    public ResponseEntity<PlayListDto> createPlaylist(@RequestBody PlayListCreateRequest request){
        //이름이 이상하면 badRequest보내기
        if (request.getName() == null || request.getName().isBlank()){
            return ResponseEntity.badRequest().build();
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
        //저장
        store.put(playList.getId(), playList);
        return ResponseEntity.ok(playList);
    }

    //플레이리스트 수정
    @PutMapping("/{id}")
    public ResponseEntity<PlayListDto> updatePlaylist(@PathVariable Long id, @RequestBody PlayListCreateRequest request){
        PlayListDto playlist = store.get(id);

        if (playlist == null){
            return ResponseEntity.notFound().build();
        }
        if (request.getName() == null || request.getName().isBlank() ||
                request.getDescription() == null ||
                request.getSongIDs() == null
        ){
            return ResponseEntity.badRequest().build();
        }

        playlist.setName(request.getName());
        playlist.setDescription(request.getDescription());
        playlist.setSongIDs(request.getSongIDs());

        return ResponseEntity.ok(playlist);
    }

    //플레이리스트 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePlaylist(@PathVariable Long id){
        PlayListDto playlist = store.get(id);
        if (playlist == null){
            return ResponseEntity.notFound().build();
        }
        store.remove(id);
        return ResponseEntity.ok("플레이리스트가 삭제되었습니다.");
    }
}
