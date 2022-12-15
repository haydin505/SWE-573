package com.infrasave.repository.tag;

import com.infrasave.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author huseyinaydin
 */
public interface TagRepository extends JpaRepository<Tag, Long>, CustomTagRepository {

}
