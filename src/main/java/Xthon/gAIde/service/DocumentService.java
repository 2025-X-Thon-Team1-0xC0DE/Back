package Xthon.gAIde.service;

import Xthon.gAIde.domain.dto.request.AI.EvaluationRequest;
import Xthon.gAIde.domain.dto.request.DocumentFeedbackRequestDto;
import Xthon.gAIde.domain.dto.response.AI.EvaluationResponse;
import Xthon.gAIde.domain.dto.response.AI.FeedbackResponseDto;
import Xthon.gAIde.domain.dto.response.DocumentFeedbackResponseDto;
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
import org.springframework.boot.ApplicationArguments;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import Xthon.gAIde.domain.dto.response.DocumentListResponse; // ⬅️ import 추가
import java.util.List; // ⬅️ import 추가

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 기본적으로 읽기 전용
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final RestClient restClient = RestClient.create();
    private final ApplicationArguments applicationArguments;

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

        String eval = "ai 서버 연결 실패";

        try {
            // 요청 DTO 생성
            EvaluationRequest aiRequest = new EvaluationRequest(
                    req.category().name(),// Enum이라면 name() 변환 필요
                    req.keywords(),
                    req.description(),
                    req.userText()
            );

            // API 호출 (Synchronous)
            EvaluationResponse aiResponse = restClient.post()
                    .uri("https://port-0-gaide-mhvvbjymeac0f465.sel3.cloudtype.app/api/evaluation")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(aiRequest)
                    .retrieve()
                    .body(EvaluationResponse.class); // 응답을 객체로 변환

            if (aiResponse != null && aiResponse.eval() != null) {
                eval = aiResponse.eval();
            }

        } catch (Exception e) {
            log.error("AI 서버 호출 중 오류 발생: {}", e.getMessage());
            eval = "AI 분석을 완료할 수 없습니다: " + e.getMessage();
        }


        log.info("문서 저장 완료: docId={}, length={}", docId, req.userText().length());

        // 3. 응답 반환 (이미지 명세에 맞춘 더미 평가 데이터)
        return new DocumentSaveResponse(
                eval, // eval: 나중에 AI 분석 결과로 대체될 부분
                "글 저장 성공"         // msg
        );

    }
    public List<DocumentListResponse> getMyDocuments(Long memberId) {
        // 1. DB에서 내 글 다 가져오기 (최신순)
        List<Document> documents = documentRepository.findAllByMemberIdOrderByIdDesc(memberId);

        // 2. DTO로 변환해서 반환
        return documents.stream()
                .map(DocumentListResponse::from)
                .toList();
    }

    // AI에 피드백 요청
    @Transactional
    public DocumentFeedbackResponseDto feedbackDocument(
            Long docId, DocumentFeedbackRequestDto feedbackDto
    ) {
        DocumentUpdateRequest updateRequestDto = new DocumentUpdateRequest(
                docId,
                feedbackDto.category(),
                feedbackDto.keywords(),
                feedbackDto.description(),
                feedbackDto.userText()
        );

        // 피드백 요청 전 먼저 저장
        updateDocument(docId, updateRequestDto);

        List<String> feedback = List.of("ai 서버 연결 실패");

        try {

            // AI에게 피드백 요청
            FeedbackResponseDto feedbackResponseDto = restClient.post()
                    .uri("https://port-0-gaide-mhvvbjymeac0f465.sel3.cloudtype.app/api/evaluation")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(feedbackDto)
                    .retrieve()
                    .body(FeedbackResponseDto.class);

            System.out.println(feedbackResponseDto);

            System.out.println(feedbackResponseDto.feedback());

            if (feedbackResponseDto != null && feedbackResponseDto.feedback() != null) {
                feedback = feedbackResponseDto.feedback();
                System.out.println("if문 test");
            }
        } catch (Exception e) {
            log.error("AI 서버 호출 중 오류 발생: {}", e.getMessage());
            feedback = List.of("AI 분석을 완료할 수 없습니다: " + e.getMessage());
        }

        return new DocumentFeedbackResponseDto(
                feedback,
                "글 저장 성공"
        );


    }
}