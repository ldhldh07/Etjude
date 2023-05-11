package offworkseekers.unnamed.api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import offworkseekers.unnamed.api.response.*;
import offworkseekers.unnamed.db.entity.Likes;
import offworkseekers.unnamed.db.entity.Role;
import offworkseekers.unnamed.db.entity.Story;
import offworkseekers.unnamed.db.entity.User;
import offworkseekers.unnamed.db.repository.LikesRepository;
import offworkseekers.unnamed.db.repository.RoleRepository;
import offworkseekers.unnamed.db.repository.StoryRepository;
import offworkseekers.unnamed.db.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class StoryService {

    private final StoryRepository storyRepository;
    private final LikesRepository likesRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    public List<StoryListResponse> storyListRecommendedByLike(int pageNum) {
        List<StoryListResponse> storyListRecommendedByLike = storyRepository.getStoryListRecommendedByLike(pageNum);
        
        return storyListRecommendedByLike;
    }

    public StoryDetailResponse storyDetailResponse(Long storyId, String userId) {
        StoryDetailResponse storyDetail = storyRepository.getStoryDetail(storyId, userId);
        return storyDetail;
    }

    public String storyDescResponse(Long storyId) {
        Optional<Story> story = storyRepository.findById(storyId);
        return story.get().getStoryDesc();
    }

    public List<StoryRoleResponse> storyRoleList(Long storyId) {
        List<Role> roles = roleRepository.getStoryRoleList(storyId);

        List<StoryRoleResponse> roleList = new ArrayList<>();
        for (Role role : roles) {
            roleList.add(StoryRoleResponse.builder()
                    .roleId(role.getRoleId())
                    .roleName(role.getRoleName())
                    .roleDesc(role.getRoleDesc())
                    .rolePhotoUrl(role.getRolePhotoUrl())
                    .build());
        }

        return roleList;
    }

    public List<RoleWithLineOfSceneResponse> roleWithLineOfSceneResponseList(Long storyId) {
        List<RoleWithLineOfSceneResponse> roleWithLines = roleRepository.getRoleWithLines(storyId);
        return roleWithLines;
    }

    public StoryListWithTotalCountResponse storySearchList(String keyword, Long categoryId, int pageNum) {
        StoryListWithTotalCountResponse storySearchList = storyRepository.getStorySearchList(keyword, categoryId, pageNum);
        return storySearchList;
    }

    @Transactional
    public void editStoryLike(int storyId, int division, String userId) {
        Optional<Likes> likes = getStoryLikeStatus(storyId, division, userId);
        if (likes.isEmpty()) {
            User user = userRepository.findById(userId).orElse(null);
            Likes build = Likes.builder()
                    .articleStoryId(storyId)
                    .division(division)
                    .user(user)
                    .build();
            likesRepository.save(build);
        }

    }
    @Transactional
    public void deleteStoryLike(int storyId, int division, String userId) {
        Optional<Likes> likes = getStoryLikeStatus(storyId, division, userId);
        likesRepository.deleteById(likes.get().getLikesId());
    }

    public Optional<Likes> getStoryLikeStatus(int storyId, int division, String userId){
        return likesRepository.findLikeConnection(storyId, division, userId);
    }

}
