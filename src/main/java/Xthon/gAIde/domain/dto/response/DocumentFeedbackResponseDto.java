package Xthon.gAIde.domain.dto.response;

import Xthon.gAIde.domain.entity.Category;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record DocumentFeedbackResponseDto(
        List<String> feedback,
        String msg
) {
}
