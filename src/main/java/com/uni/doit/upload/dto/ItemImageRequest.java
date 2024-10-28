package com.uni.doit.upload.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemImageRequest {

    @JsonProperty("item_id")
    private Long itemId;

    @JsonProperty("image_description")
    private String imageDescription;

    @JsonProperty("is_primary")
    private boolean isPrimary;
}
