# ✍️ gAIde Backend Server

**"AI는 거들 뿐, 글쓰기의 주체는 당신입니다."**

**gAIde**는 사용자가 AI에 의존하지 않고 **능동적인 글쓰기 주체**로 성장하도록 돕는 **글쓰기 가이드 플랫폼**입니다.  
본 리포지토리는 React(Frontend)와 FastAPI(AI Model) 사이를 연결하는 **Spring Boot 기반 Bridge Server**이며,  
사용자 문서 및 AI 피드백을 안정적으로 저장·관리하는 핵심 백엔드 역할을 수행합니다.

<br>

## 💡 프로젝트 소개 (Project Overview)

- **Team:** 1조 0xC0DE  
- **Project Name:** gAIde (가이드)  
- **Service Type:** 글쓰기 가이드 및 AI 피드백 플랫폼  
- **Backend Role:** Bridge Server & Data Management  

<br>



## 🛠 주요 기능 (Key Features)

### 1. 🔗 Bridge Architecture (Frontend ↔ Backend ↔ AI Server)
- **중계 역할:** Frontend(React)로부터 전달받은 사용자 텍스트의 유효성을 검증하고 로깅합니다.
- **비동기 통신:** 검증된 텍스트를 FastAPI(AI 서버)로 비동기 전송하여 LLM 분석을 요청합니다.
- **피드백 전달:** AI가 생성한 분석 결과(논리적 비약 탐지, 근거 보충 제안 등)를 수신하여 Frontend에 전달합니다.

### 2. 📄 Document & Feedback Archiving
- **문서 관리:** 사용자가 작성 중인 자소서·보고서 등의 문서를 실시간으로 저장합니다.
- **JSONB 기반 피드백 저장:**  
  AI 피드백은 요청마다 형태가 달라질 수 있어, PostgreSQL `JSONB` 타입을 사용하여 유연하게 저장 및 관리합니다.

### 3. 🔐 Security & Authentication
- **JWT 인증:** Access Token 기반 인증 방식을 통해 사용자별 데이터를 안전하게 분리합니다.
- **Spring Security:** 비인가 사용자의 API 접근을 차단하며 모든 요청에 대해 인증/인가 필터를 적용합니다.

<br>



## 🧱 Tech Stack

| Category | Technology | Description |
| :--- | :--- | :--- |
| **Framework** | Spring Boot 3.4 | 안정적인 백엔드 서버 구축 |
| **Language** | Java 17 |  |
| **Database** | PostgreSQL | JSONB 기반 비정형 데이터 처리 |
| **Security** | Spring Security, JWT | Stateless 인증 구조 |
| **Build Tool** | Gradle | 빌드 및 의존성 관리 |
| **AI Server** | FastAPI | LLM 피드백 생성기 |
| **Infra** | CloudType | CI/CD 및 배포 |

<br>

## 🔁 데이터 처리 흐름 (Flow)
1. 사용자가 작성할 글의 종류 선택 및 키워드 작성<br>
2. 사용자가 에디터에서 글 작성<br>
3. BackEnd로 텍스트 전송<br>
4. 유효성 검증 및 DB에 저장<br>
5. FastAPI(AI 서버)로 비동기 요청 전송<br>
6. AI가 논리·구조·문맥을 기반으로 피드백 생성<br>
7. 피드백을 FrontEnd로 전송<br>
8. 사용자는 피드백을 참고하며 글을 작성함<br>

<br>

## 📂 Directory Structure

```plaintext
src/
 ├─ main/
 │   ├─ java/
 │   │   └─ Xthon/
 │   │       └─ gAIde/
 │   │           ├─ controller/    # API endpoint
 │   │           ├─ domain/        
 │   │           │   ├─ dto/
 │   │           │   │   ├─ request/    # requestDto
 │   │           │   │   └─ response/    # responseDto
 │   │           │   └─ entity/    # entity class
 │   │           ├─ exception/    # CustomException
 │   │           ├─ repository/    # JPA Repository
 │   │           ├─ service/    # business logic
 │   │           └─ util/    # Security & JWT config
 │   └─ resources/
 │       └─ application.yml
 └─ test/
```
