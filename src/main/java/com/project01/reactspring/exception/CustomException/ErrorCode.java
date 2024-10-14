package com.project01.reactspring.exception.CustomException;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

public enum ErrorCode {
    UNAUTHOUCATED(401,"Tài khoản chưa được xác thực,Yêu cầu đăng nhập!"),
    BAD_REQUEST(400,"Dữ liệu chưa hợp lệ!"),
    USER_NOTFOUND(404,"User không tồn tại!"),
    PHONE_EXITSED(400,"SDT này đã tồn tại!"),
    PASSWORD_WRONG(400,"Mật khẩu sai.Hãy nhập lại!"),
    INVALID_TOKEN(401,"Token đã hết hiệu lực hoặc sai!"),
    USER_EXITSED(400,"Username này đã tồn tại.Vui lòng chọn tên đăng nhập khác"),
    UNAUTHOUZATED(403,"Bạn không có quyền truy cập vào tài nguyên này"),
    SEARCH_USERNAME(400,"Field nay chi duoc tim kiem bang username");
    private int code;
    private String message;


    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;

    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
