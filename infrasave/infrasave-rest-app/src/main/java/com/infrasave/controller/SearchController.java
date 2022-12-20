package com.infrasave.controller;

import com.infrasave.bean.AppResponse;
import com.infrasave.bean.AppResponses;
import com.infrasave.bean.ContentDTO;
import com.infrasave.bean.SearchResponseDTO;
import com.infrasave.bean.TagDTO;
import com.infrasave.bean.UserDTO;
import com.infrasave.config.CustomUserDetails;
import com.infrasave.entity.Content;
import com.infrasave.service.MyContentService;
import com.infrasave.service.SearchService;
import java.util.List;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static java.lang.Boolean.TRUE;

/**
 * @author huseyinaydin
 */
@RestController
@RequestMapping("search")
public class SearchController {

  private final SearchService searchService;

  private final MyContentService myContentService;

  public SearchController(SearchService searchService, MyContentService myContentService) {
    this.searchService = searchService;
    this.myContentService = myContentService;
  }

  @GetMapping
  public AppResponse<SearchResponseDTO> search(@RequestParam(required = false) Boolean user,
                                               @RequestParam(required = false) Boolean tag,
                                               @RequestParam(required = false) Boolean content,
                                               @RequestParam String query) {
    SearchResponseDTO searchResponseDTO = new SearchResponseDTO();
    if (TRUE.equals(user)) {
      List<UserDTO> userDTOs = searchService.getUsers(query);
      searchResponseDTO.setUsers(userDTOs);
    }
    if (TRUE.equals(tag)) {
      List<ContentDTO> contentDTOs = getContentDTOs(searchService.getContentsByTag(query));
      searchResponseDTO.setContentsByTags(contentDTOs);
    }
    if (TRUE.equals(content)) {
      List<ContentDTO> contentDTOs = getContentDTOs(searchService.getContentsByTitleAndDescription(query));
      searchResponseDTO.setContents(contentDTOs);
    }
    return AppResponses.successful(searchResponseDTO);
  }

  private List<ContentDTO> getContentDTOs(List<Content> contents) {
    CustomUserDetails principal = (CustomUserDetails) SecurityContextHolder.getContext()
                                                                           .getAuthentication()
                                                                           .getPrincipal();
    Long userId = principal.getUserId();
    List<Long> myContentContentIds = myContentService.getMyContent().stream().map(ContentDTO::id).toList();
    List<ContentDTO> contentDTOs = contents.stream()
                                           .map(c -> new ContentDTO(c,
                                                                    myContentContentIds.contains(c.getId()),
                                                                    new UserDTO(c.getCreatorId()),
                                                                    c.getTags().stream().map(TagDTO::new).toList()))
                                           .toList();
    return contentDTOs;
  }
}
