package com.infrasave.repository.content;

import com.infrasave.entity.Content;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author huseyinaydin
 */
public interface ContentRepository extends JpaRepository<Content, Long>, CustomContentRepository {

}
