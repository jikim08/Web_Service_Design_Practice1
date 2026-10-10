package kr.ac.jbnu.kjh.practice1.config;

import kr.ac.jbnu.kjh.practice1.dto.ApiResponse;
import tools.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

@Component
public class LoggingInterceptor implements HandlerInterceptor {

    @Value("${app.maintenance:false}")
    private boolean maintenanceMode;

    private final ObjectMapper objectMapper;

    public LoggingInterceptor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) throws Exception {

        String method = request.getMethod();
        String uri = request.getRequestURI();
        String query = request.getQueryString();

        System.out.println("[LoggingInterceptor] 요청 시작: "
                + method + " " + uri
                + (query != null ? "?" + query : ""));

        // 서버 점검 중이라면 503 반환
        if (maintenanceMode) {

            ApiResponse apiResponse = new ApiResponse();
            apiResponse.setStatus("error");
            apiResponse.setData(Map.of("message", "현재 서버 점검 중입니다."));

            response.setStatus(503);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            objectMapper.writeValue(response.getWriter(), apiResponse);

            System.out.println("[LoggingInterceptor] 요청 차단, status=503");

            return false;
        }

        return true;
    }

    @Override
    public void afterCompletion(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            Exception ex
    ) {

        System.out.println("[LoggingInterceptor] 요청 완료, status="
                + response.getStatus());
    }
}