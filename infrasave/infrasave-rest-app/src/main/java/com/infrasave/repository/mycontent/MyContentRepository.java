package com.infrasave.repository.mycontent;

import com.infrasave.entity.MyContent;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author huseyinaydin
 */
public interface MyContentRepository extends JpaRepository<MyContent, Long>, CustomMyContentRepository {

}
