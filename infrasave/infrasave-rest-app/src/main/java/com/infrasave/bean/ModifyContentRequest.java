package com.infrasave.bean;

import com.infrasave.enums.VisibilityLevel;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author huseyinaydin
 */
public record ModifyContentRequest(Long contentId,
                                   @NotNull VisibilityLevel visibilityLevel,
                                   @NotBlank String title,
                                   @NotBlank String url,
                                   String imageUrl,
                                   String description) {

}
