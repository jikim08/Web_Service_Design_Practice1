package kr.ac.jbnu.kjh.practice1.dto;

public class ApiResponse {
    private String status;
    private Object data;

    public String getStatus() {
        return status;
    }

    public Object getData() {
        return data;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
