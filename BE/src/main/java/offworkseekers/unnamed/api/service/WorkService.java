package offworkseekers.unnamed.api.service;

import lombok.RequiredArgsConstructor;
import offworkseekers.unnamed.api.response.GetWorkResponse;
import offworkseekers.unnamed.api.response.StoriesOfWork;
import offworkseekers.unnamed.api.response.WorkListWithTotalCountResponse;
import offworkseekers.unnamed.api.response.WorkOrderByRandomResponse;
import offworkseekers.unnamed.db.repository.WorkRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WorkService {

    private final WorkRepository workRepository;


    public List<WorkOrderByRandomResponse> getWorkListRandom(int pageNum) {

        List<WorkOrderByRandomResponse> works = workRepository.getWorkListRandom(pageNum);
        Collections.shuffle(works);
        return works;
    }

    public WorkListWithTotalCountResponse getWorkSearchList(String keyword, Long categoryId, int pageNum){
        return workRepository.getWorkSearchList(keyword, categoryId, pageNum);
    }

    public GetWorkResponse getWork(Long workId){
        return workRepository.getWork(workId);
    }

    public List<StoriesOfWork> getStoriesByWorkId(Long workId){
        return workRepository.getStoriesByWorkId(workId);
    }
}
