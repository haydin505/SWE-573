package com.infrasave.bean;

import com.infrasave.enums.VisibilityLevel;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author huseyinaydin
 */
public record AddContentRequest(@NotNull VisibilityLevel visibilityLevel,
                                @NotBlank String title,
                                @NotBlank String url,
                                String imageUrl,
                                String description) {

}
