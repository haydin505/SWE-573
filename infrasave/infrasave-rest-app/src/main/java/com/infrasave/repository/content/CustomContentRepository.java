package com.infrasave.repository.content;

import com.infrasave.entity.Content;
import com.infrasave.entity.User;
import com.infrasave.enums.VisibilityLevel;
import java.util.List;

/**
 * @author huseyinaydin
 */
public interface CustomContentRepository {

  List<Content> getByUsersAndVisibilities(List<User> users, List<VisibilityLevel> visibilityLevels);

  List<Content> getByVisibility(List<VisibilityLevel> visibilityLevels);
}
