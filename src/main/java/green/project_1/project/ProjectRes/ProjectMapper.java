package green.project_1.project.ProjectRes;

import green.project_1.project.ProjectReq.ProjectReq;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProjectMapper {

    int createProject(ProjectReq.ProjectDetails project); // 프로젝트 생성
    int deleteProject(long projectNo); // 프로젝트 삭제

}
