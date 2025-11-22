package Xthon.gAIde.util.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.List;

@Converter
public class StringListConverter implements AttributeConverter<List<String>, String> {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        // DB에 저장할 때: List -> JSON String
        if (attribute == null) return null;
        try {
            return mapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON writing error", e);
        }
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        // DB에서 읽어올 때: JSON String -> List
        if (dbData == null) return null;
        try {
            // 여기가 핵심! TypeReference를 써서 List<String>임을 명시해야 에러가 안 남
            return mapper.readValue(dbData, new TypeReference<List<String>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON reading error", e);
        }
    }
}