package com.infrasave.bean;

import javax.validation.constraints.NotBlank;

/**
 * @author huseyinaydin
 */
public record AddTagRequest(@NotBlank String name, String description, @NotBlank String color) {

}
