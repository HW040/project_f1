package green.project_1.project;

import green.project_1.User.UserMapper;
import green.project_1.project.ProjectReq.ProjectDeleteReq;
import green.project_1.project.ProjectReq.ProjectReq;
import green.project_1.project.ProjectRes.ResponseResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor  // Lombok 어노테이션을 사용해 final 필드들의 생성자 주입 처리
public class ProjectController {
    private final UserMapper userMapper;
    private final ProjectService projectService;

    // 프로젝트 생성
    @PostMapping  // "/api/project/project" 경로에 해당
    public ResponseResult createProject(@RequestBody ProjectReq request) {
        log.info("프로젝트 생성 요청: {}", request);

        ResponseResult response = projectService.createProject(request);

        if ("OK".equals(response.getCode())) {
            log.info("프로젝트 생성 성공: {}", request.getProject().getName());
        } else {
            log.error("프로젝트 생성 실패: {}", response.getMessage());
        }

        return response;
    }

    // 프로젝트 삭제
    @DeleteMapping("/{projectNo}") // 프로젝트 번호를 경로에 포함시켜서 삭제
    public ResponseResult deleteProject(@RequestBody ProjectDeleteReq request, @PathVariable long projectNo) {
        log.info("프로젝트 삭제 요청, 프로젝트 번호: {}, 요청자: {}", projectNo, request.getSignedUserNo());

        long signedUserNo = request.getSignedUserNo();  // 요청에서 사용자 번호 (signedUserNo) 가져오기

        // 프로젝트 번호로 리더 번호 가져오기
        long leaderNo = userMapper.leaderNo(projectNo);  // 리더 번호 조회

        // 로그인한 사용자의 번호가 리더와 일치하는지 확인
        if (signedUserNo != leaderNo) {
            log.error("리더 권한 없음. 사용자 번호: {}", signedUserNo);
            return ResponseResult.noPermission();
        }

        // 서비스에서 프로젝트 삭제 처리 (프로젝트 삭제 로직 구현)
        ResponseResult response = deleteProjectLogic(request, projectNo);  // 실제 삭제 로직 호출

        // 서비스 응답 반환
        return response;
    }

    // 실제 프로젝트 삭제 로직
    private ResponseResult deleteProjectLogic(ProjectDeleteReq request, long projectNo) {
        log.info("프로젝트 삭제 중, 프로젝트 번호: {}", projectNo);

        // 삭제할 프로젝트가 존재하는지 확인 (예시: DB에서 프로젝트 삭제)
        ResponseResult result = projectService.deleteProject(request); // 서비스에서 실제 삭제 처리하는 로직

        if ("OK".equals(result.getCode())) {
            log.error("프로젝트 삭제 실패, 프로젝트가 존재하지 않음: {}", projectNo);
            return ResponseResult.serverError();
        }

        // 프로젝트 삭제 성공 메시지 반환
        log.info("프로젝트 삭제 성공: {}", projectNo);
        return ResponseResult.success();
    }
}