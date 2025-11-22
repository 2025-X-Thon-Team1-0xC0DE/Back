package Xthon.gAIde.util.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.io.IOException;

@Converter
public class JsonConverter implements AttributeConverter<Object, String> {

    // Jackson 라이브러리의 핵심 객체 (JSON 변환기)
    private final ObjectMapper objectMapper = new ObjectMapper();

    // 1. 자바 객체(List, Map 등) -> DB(JSON 문자열)로 변환
    @Override
    public String convertToDatabaseColumn(Object attribute) {
        if (attribute == null) return null;
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON writing error", e);
        }
    }

    // 2. DB(JSON 문자열) -> 자바 객체로 변환
    @Override
    public Object convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        try {
            return objectMapper.readValue(dbData, Object.class);
        } catch (IOException e) {
            throw new RuntimeException("JSON reading error", e);
        }
    }
}