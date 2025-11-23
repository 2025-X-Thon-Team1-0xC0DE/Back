# 📝 gAIde Backend Server

**"AI는 거들 뿐, 글쓰기의 주체는 당신입니다."**

**gAIde**는 사용자가 AI에 의존하지 않고 능동적인 글쓰기 주체로 성장하도록 돕는 **글쓰기 가이드 플랫폼**입니다.
본 리포지토리는 서비스의 중추적인 역할을 담당하는 **Spring Boot 백엔드 서버**입니다. React(Frontend)와 FastAPI(AI Model) 사이를 연결하는 Bridge 역할을 수행하며, 사용자 데이터와 피드백 기록을 관리합니다.

## 📌 Project Overview

* [cite_start]**Team:** 1조 0xC0DE 
* [cite_start]**Project Name:** gAIde (가이드) 
* [cite_start]**Service Type:** 글쓰기 가이드 및 피드백 플랫폼 
* [cite_start]**Role:** Bridge Server & Data Management 

## 🛠 Tech Stack

| Category | Technology | Description |
| :--- | :--- | :--- |
| **Framework** | Spring Boot 3.x | [cite_start]안정적인 백엔드 서버 구축 및 확장성 확보 [cite: 3] |
| **Language** | Java 17+ | |
| **Database** | PostgreSQL | [cite_start]정형 데이터 및 JSONB를 활용한 비정형 데이터(피드백 로그) 처리 [cite: 3] |
| **Security** | Spring Security, JWT | [cite_start]Stateless 인증 방식 구현 및 데이터 보안 강화 [cite: 3] |
| **Build Tool** | Gradle | |
| **Deployment** | CloudType | [cite_start]CI/CD 파이프라인 구축 및 배포 [cite: 3] |

## 💡 Key Features

### 1. Bridge Architecture (AI-Frontend 연결)
* [cite_start]**중계 역할:** Frontend(React)로부터 전달받은 사용자 텍스트의 유효성을 검증하고 로깅합니다. 
* [cite_start]**비동기 통신:** 검증된 데이터를 FastAPI(AI 서버)로 비동기 전송하여 LLM 분석을 요청합니다. 
* [cite_start]**피드백 전달:** AI가 생성한 분석 결과(논리적 비약 탐지, 근거 보충 제안 등)를 수신하여 Frontend로 전달합니다. [cite: 3]

### 2. Document & Feedback Archiving
* [cite_start]**문서 관리:** 사용자가 작성 중인 자기소개서, 보고서 등의 문서를 실시간으로 DB에 저장합니다. 
* [cite_start]**JSONB 활용:** AI 모델이 반환하는 복잡하고 가변적인 피드백 데이터(Log)를 PostgreSQL의 `JSONB` 타입을 활용하여 효율적으로 저장하고 관리합니다. [cite: 3]

### 3. Security & Authentication
* **JWT (JSON Web Token):** Access Token 기반의 인증 시스템을 구현하여 사용자별 데이터를 안전하게 분리하고 관리합니다.
* [cite_start]**Spring Security:** 인증/인가 필터를 통해 인가되지 않은 사용자의 API 접근을 차단합니다. [cite: 3]

## 📂 System Architecture

```mermaid
graph LR
    Client[User (React)] -- HTTP Request --> Boot[Spring Boot (Backend)]
    Boot -- JDBC --> DB[(PostgreSQL)]
    Boot -- Async Request --> AI[FastAPI (AI Model)]
    AI -- Analysis Result --> Boot
