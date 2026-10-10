# Music Playlist REST API

Spring Boot를 활용하여 구현한 음악 및 플레이리스트 관리 REST API 프로젝트입니다.

음악과 플레이리스트의 생성, 조회, 수정, 삭제 기능을 제공하며,
HTTP 상태 코드 처리, 공통 JSON 응답 형식, 미들웨어를 구현했습니다.

## 1. 기술 스택

- Language: Java 21
- Framework: Spring Boot
- Build Tool: Gradle
- API Testing: Postman
- Data Storage: HashMap

## 2. 주요 기능

### 음악 관리

- 음악 등록 (POST)
- 특정 음악 조회 (GET)
- 음악 정보 수정 (PUT)
- 음악 삭제 (DELETE)

### 플레이리스트 관리

- 플레이리스트 생성 (POST)
- 특정 음악이 포함된 플레이리스트 검색 (GET)
- 플레이리스트 수정 (PUT)
- 플레이리스트 삭제 (DELETE)

### 추가 구현 기능

- HTTP 요청 및 응답 로깅 (미들웨어 구현)
- HTTP 상태 코드에 따른 응답 처리
- 공통 JSON 응답 형식 적용
- 전역 예외 처리 (500 에러 처리)
- 서버 점검 모드 (503 에러 처리)

## 3. API 명세

### Song API

| Method | Endpoint | Description | Success |
| --- | --- | --- | --- |
| GET | `/api/v1/songs/{id}` | 특정 음악 조회 | 200 |
| POST | `/api/v1/songs` | 음악 등록 | 201 |
| PUT | `/api/v1/songs/{id}` | 음악 정보 수정 | 200 |
| DELETE | `/api/v1/songs/{id}` | 음악 삭제 | 200 |

### Playlist API

| Method | Endpoint | Description | Success |
| --- | --- | --- | --- |
| GET | `/api/v1/playlists/search?songId={id}` | 특정 음악이 포함된 플레이리스트 검색 | 200 |
| POST | `/api/v1/playlists` | 플레이리스트 생성 | 201 |
| PUT | `/api/v1/playlists/{id}` | 플레이리스트 수정 | 200 |
| DELETE | `/api/v1/playlists/{id}` | 플레이리스트 삭제 | 200 |

총 8개의 REST API를 구현했습니다.

## 4. API 요청 예시

### 음악 등록

**POST** `/api/v1/songs`

Request Body:

```json
{
  "title": "비",
  "artist": "폴킴"
}
```

Response (201 Created):

```json
{
  "status": "success",
  "data": {
    "id": 1,
    "title": "비",
    "artist": "폴킴",
  }
}
```

### 음악 정보 수정

**PUT** `/api/v1/songs/1`

Request Body:

```json
{
  "title": "비 (2026)",
  "artist": "폴킴",
}
```

### 플레이리스트 생성

**POST** `/api/v1/playlist`

Request Body:

```json
{
  "name": "좋아하는 노래",
  "description": "자주 듣는 음악 모음",
  "songIDs": [1, 2]
}
```

### 플레이리스트 검색

**GET** `/api/v1/playlists/search?songId=1`

특정 음악 ID가 포함된 플레이리스트 목록을 반환합니다.

## 5. 공통 응답 형식

API 응답의 일관성을 유지하기 위해 `ApiResponse` 클래스를 구현했습니다.

모든 직접 처리한 API 응답은 다음 구조를 사용합니다.

```json
{
  "status": "success",
  "data": {}
}
```

- `status`: 요청 처리 결과 (`success` 또는 `error`)
- `data`: 요청 결과 데이터 또는 오류 메시지

### 성공 응답 예시

```json
{
  "status": "success",
  "data": {
    "id": 1,
    "title": "비",
    "artist": "폴킴"
  }
}
```

### 실패 응답 예시

```json
{
  "status": "error",
  "data": {
    "message": "음악을 찾을 수 없습니다."
  }
}
```

## 6. HTTP 상태 코드

요청 처리 결과에 따라 적절한 HTTP 상태 코드를 반환하도록 구현했습니다.

| Category | Status Code | Description |
| --- | --- | --- |
| 2xx | 200 OK | 조회, 수정, 삭제 성공 |
| 2xx | 201 Created | 리소스 생성 성공 |
| 4xx | 400 Bad Request | 잘못된 입력값 |
| 4xx | 404 Not Found | 존재하지 않는 리소스 |
| 5xx | 500 Internal Server Error | 서버 내부 예외 발생 |
| 5xx | 503 Service Unavailable | 서버 점검 중 |

각 범주별로 2종류 이상의 HTTP 상태 코드를 구현했습니다.

## 7. 미들웨어 구현

Spring MVC의 `HandlerInterceptor`를 활용하여
HTTP 요청 및 응답 정보를 기록하는 미들웨어를 구현했습니다.

### LoggingInterceptor

- `preHandle()`: HTTP 요청 메서드 및 URI 기록
- `afterCompletion()`: HTTP 응답 상태 코드 기록
- 서버 점검 상태 확인 및 요청 차단

로그 출력 예시:

```
[LoggingInterceptor] 요청 시작: GET /api/v1/songs
[LoggingInterceptor] 요청 완료, status=200
```

### 서버 점검 모드

`application.properties`에서 서버 점검 모드를 설정할 수 있습니다.

```
app.maintenance=false
```

- `false`: 정상적인 API 요청 처리
- `true`: API 요청을 차단하고 503 상태 코드 반환

점검 모드에서는 다음과 같은 응답을 반환합니다.

```json
{
  "status": "error",
  "data": {
    "message": "현재 서버 점검 중입니다."
  }
}
```

## 8. 전역 예외 처리

`@RestControllerAdvice`를 활용하여
Controller에서 처리하지 못한 예외를 공통으로 처리했습니다.

예상하지 못한 서버 내부 오류 발생 시
HTTP 500 상태 코드와 공통 JSON 응답을 반환합니다.

```json
{
  "status": "error",
  "data": {
    "message": "서버 내부 오류가 발생했습니다."
  }
}
```

## 9. 실행 방법

### API 테스트

서버 실행 후 Postman에서 다음 주소로 요청을 전송합니다.

```
<http://localhost:8080>
```

예시:

```
GET <http://localhost:8080/api/v1/songs>
```

### 참고 사항

현재 프로젝트는 별도의 데이터베이스 없이
HashMap을 활용하여 데이터를 저장합니다.

따라서 서버를 종료하거나 재시작하면 저장된 데이터가 초기화됩니다.

## 10. 테스트 결과

Postman을 활용하여 API별 요청 및 응답을 테스트합니다.

주요 검증 항목:

- GET, POST, PUT, DELETE 정상 동작
- 잘못된 입력값에 대한 400 응답
- 존재하지 않는 리소스에 대한 404 응답
- 서버 예외 발생 시 500 응답
- 점검 모드 활성화 시 503 응답
- 미들웨어 로그 출력
- 공통 JSON 응답 형식 확인

## 11. 프로젝트 구조

```
src/main/
├── java/
│   └── ...
│       ├── controller/
│       │   ├── SongController.java
│       │   └── PlaylistController.java
│       │
│       ├── dto/
│       │   ├── SongDto.java
│       │   ├── PlayListDto.java
│       │   └── ApiResponse.java
│       │
│       ├── config/
│       │   ├── LoggingInterceptor.java
│       │   └── WebConfig.java
│       │
│       └── exception/
│           └── GlobalExceptionHandler.java
│
└── resources/
    └── application.properties
```