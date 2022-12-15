package com.infrasave.repository.mycontent;

import com.infrasave.entity.Content;
import com.infrasave.entity.MyContent;
import com.infrasave.entity.User;
import java.util.List;

/**
 * @author huseyinaydin
 */
public interface CustomMyContentRepository {

  List<MyContent> getMyContent(User user);

  MyContent getMyContentByUserAndContent(User user, Content content);
}
