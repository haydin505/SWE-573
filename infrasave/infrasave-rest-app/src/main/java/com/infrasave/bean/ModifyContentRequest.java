package com.infrasave.bean;

import com.infrasave.enums.VisibilityLevel;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * @author huseyinaydin
 */
public record ModifyContentRequest(@NotNull @Positive Long id,
                                   @NotNull VisibilityLevel visibilityLevel,
                                   @NotBlank String title,
                                   @NotBlank String url,
                                   String imageUrl,
                                   String description,
                                   List<@NotNull @Positive Long> tags) {

}
