package kr.ac.jbnu.kjh.practice1.exception;


import kr.ac.jbnu.kjh.practice1.dto.ApiResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // 예상하지 못한 서버 오류 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleServerError(Exception ex) {

        ApiResponse response = new ApiResponse();
        response.setStatus("error");
        response.setData(Map.of("message", "서버 내부 오류가 발생했습니다"));

        return ResponseEntity.status(500).body(response);
    }

    // Spring이 자체적으로 처리하는 오류도 공통 JSON 형식으로 반환
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex,
            Object body,
            HttpHeaders headers,
            HttpStatusCode statusCode,
            WebRequest request
    ) {
        ApiResponse response = new ApiResponse();
        response.setStatus("error");

        if (statusCode.is4xxClientError()) {
            response.setData(Map.of("message", "잘못된 요청입니다."));
        } else {
            response.setData(Map.of("message", "서버 내부 오류가 발생했습니다."));
        }

        return new ResponseEntity<>(response, headers, statusCode);
    }
}