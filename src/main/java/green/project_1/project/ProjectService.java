package green.project_1.project;

import green.project_1.User.UserMapper;
import green.project_1.project.ProjectReq.ProjectDeleteReq;
import green.project_1.project.ProjectReq.ProjectReq;       // ProjectReq import
import green.project_1.project.ProjectRes.ProjectMapper;
import green.project_1.project.ProjectRes.ResponseResult;       // Response import
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@RequiredArgsConstructor //final 로 된 애들의 생성자를 자동으로 만들어주는 역할
@Service
public class ProjectService {

    private final UserMapper userMapper;
    private final ProjectMapper projectMapper;

    private static List<ProjectReq.ProjectDetails> projectList = new ArrayList<>(); // 임시 메모리 저장소


    // 프로젝트 생성
    public ResponseResult createProject(ProjectReq request) {
        // 필수 값 검증 (누락된 값이 있으면 에러 반환)
        if (request == null || request.getProject() == null ||
                request.getProject().getName() == null ||
                request.getProject().getWriterUserNo() == null ||
                request.getProject().getDescription() == null ||
                request.getProject().getStartAt() == null ||
                request.getProject().getCreatedAt() == null ||
                request.getProject().getDeadLine() == null) {
            return new ResponseResult("NN", "입력을 확인해주세요.");
        }

        // 프로젝트 정보를 DB에 저장
        int result = projectMapper.createProject(request.getProject());

        if (result == 0) {
            return ResponseResult.serverError();  // DB 저장 실패
        }

        // 성공 응답 반환
        return ResponseResult.success();
    }

    // 프로젝트 삭제 처리
    public ResponseResult deleteProject(ProjectDeleteReq request) {
        // 프로젝트 번호로 리더 번호 가져오기
        long leaderNo = userMapper.leaderNo(request.getProjectNo());  // 리더 번호 조회

        // 요청 보낸 사용자가 리더인지 확인
        if (request.getSignedUserNo() != leaderNo) {
            // 리더가 아니면 삭제를 할 수 없음
            return ResponseResult.noPermission();
        }

        // 프로젝트 삭제 처리 (예: DB에서 프로젝트 삭제)
        int result=projectMapper.deleteProject(request.getProjectNo());
        // 프로젝트가 존재하는지 확인하고 삭제 로직 처리

        if (result==0) {
            // 프로젝트가 존재하지 않으면 NN 반환
            return ResponseResult.serverError();
        }

        // 프로젝트 삭제 성공 응답 반환
        return ResponseResult.success();
    }
}