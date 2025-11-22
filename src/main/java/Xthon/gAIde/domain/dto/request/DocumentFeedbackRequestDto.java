package Xthon.gAIde.domain.dto.request;

import Xthon.gAIde.domain.entity.Category;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record DocumentFeedbackRequestDto(
        @JsonProperty("category")
        Category category,

        String title,

        @JsonProperty("keywords")
        List<String> keywords,

        @JsonProperty("description")
        String description,

        @JsonProperty("request_type")
        int requestType,

        @JsonProperty("user_text")
        String userText
) {
}
