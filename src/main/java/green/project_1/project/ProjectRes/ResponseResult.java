package green.project_1.project.ProjectRes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseResult {
    private String code;
    private String message;


    public static ResponseResult success() {
        return new ResponseResult("OK", "성공");
    }

    public static ResponseResult inputError() {
        return new ResponseResult("NN", "입력 오류");
    }

    public static ResponseResult noPermission() {
        return new ResponseResult("NN", "권한 없음");
    }

    public static ResponseResult serverError() {
        return new ResponseResult("NN", "서버 오류");
    }
}