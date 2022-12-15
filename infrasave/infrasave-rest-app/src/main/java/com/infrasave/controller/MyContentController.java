package com.infrasave.controller;

import com.infrasave.bean.AddMyContentRequest;
import com.infrasave.bean.AppResponse;
import com.infrasave.bean.AppResponses;
import com.infrasave.bean.MyContentDTO;
import com.infrasave.service.MyContentService;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author huseyinaydin
 */
@RestController
@RequestMapping("my-content")
public class MyContentController {

  private final MyContentService myContentService;

  public MyContentController(MyContentService myContentService) {
    this.myContentService = myContentService;
  }

  @GetMapping
  public List<MyContentDTO> getMyContent() {
    return myContentService.getMyContent();
  }

  @PostMapping
  public AppResponse addMyContent(@RequestBody AddMyContentRequest request) {
    myContentService.addMyContent(request);
    return AppResponses.successful();
  }

  @DeleteMapping("/{contentId}")
  @ResponseBody
  public AppResponse deleteMyContent(@PathVariable Long contentId) {
    myContentService.deleteMyContent(contentId);
    return AppResponses.successful();
  }
}
