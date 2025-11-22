package Xthon.gAIde.service;

import Xthon.gAIde.domain.dto.response.DocumentResponse;
import Xthon.gAIde.domain.dto.response.DocumentSaveResponse;
import Xthon.gAIde.domain.dto.request.DocumentCreateRequest;
import Xthon.gAIde.domain.dto.request.DocumentUpdateRequest;
import Xthon.gAIde.domain.entity.Document;
import Xthon.gAIde.exception.CustomException;
import Xthon.gAIde.exception.ErrorCode;
import Xthon.gAIde.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 기본적으로 읽기 전용
public class DocumentService {

    private final DocumentRepository documentRepository;

    // [문서 생성]
    @Transactional // 쓰기 권한 필요
    public Long createDocument(Long memberId, DocumentCreateRequest req) {
        Document document = Document.builder()
                .memberId(memberId)
                .title(req.title())
                .category(req.category())
                .keywords(req.keywords())
                .topicDesc(req.description())
                .build();

        documentRepository.save(document);
        return document.getId();
    }

    // [문서 조회]GET
    public DocumentResponse getDocument(Long docId) {
        Document document = documentRepository.findById(docId)
                .orElseThrow(() -> new CustomException(ErrorCode.DOCUMENT_NOT_FOUND));

        // Entity -> DTO 변환하여 반환
        return DocumentResponse.from(document);
    }

    // [문서 저장/수정] (PATCH)

    @Transactional
    public DocumentSaveResponse updateDocument(Long docId, DocumentUpdateRequest req) {
        // 1. 문서 조회 (없으면 예외 발생)
        Document document = documentRepository.findById(docId)
                .orElseThrow(() -> new CustomException(ErrorCode.DOCUMENT_NOT_FOUND));

        // 2. 내용 업데이트 (Entity 내부 메서드 사용 or Setter)
        document.updateCategory(req.category());
        document.updateKeywords(req.keywords());
        document.updateContent(req.userText());

        log.info("문서 저장 완료: docId={}, length={}", docId, req.userText().length());

        // 3. 응답 반환 (이미지 명세에 맞춘 더미 평가 데이터)
        return new DocumentSaveResponse(
                "나쁘지 않은 글 이네여", // eval: 나중에 AI 분석 결과로 대체될 부분
                "글 저장 성공"         // msg
        );
    }
}