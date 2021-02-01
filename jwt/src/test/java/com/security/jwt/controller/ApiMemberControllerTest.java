package com.security.jwt.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.security.jwt.common.RestDocsConfig;
import com.security.jwt.domain.Member;
import com.security.jwt.domain.dto.RefreshTokenDto;
import com.security.jwt.domain.form.LoginForm;
import com.security.jwt.domain.form.MemberForm;
import com.security.jwt.exception.NewAccessTokenIssuedException;
import com.security.jwt.service.MemberService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfig.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ApiMemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @Order(1)
    public void 회원가입() throws Exception{
        //given
        Member member = Member.builder()
                .username("test")
                .password("1234")
                .name("테스트계정")
                .build();
        MemberForm memberForm = new MemberForm(member);

        //when
        //then
        mockMvc.perform(post("/api/members/new")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON_VALUE)
                .content(objectMapper.writeValueAsString(memberForm)))  //JSON형태로 변환해서 POST요청
                .andDo(print())
                .andExpect(status().isCreated())    //status는 201을 기대
                .andDo(document("signUp",
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        requestFields(
                                fieldWithPath("username").description("아이디"),
                                fieldWithPath("password").description("비밀번호"),
                                fieldWithPath("name").description("이름")
                        ),responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        responseFields(
                                fieldWithPath("result").description("성공 실패 여부"),
                                fieldWithPath("status").description("http status"),
                                fieldWithPath("message").description("서버 전달 메세지")
                        )
                ));
    }

    @Test
    @Order(2)
    public void 로그인() throws Exception{
        //given
        LoginForm loginForm = LoginForm.builder()
                .username("test")
                .password("1234")
                .build();
        //when
        //then
        mockMvc.perform(post("/api/tokens")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON_VALUE)
                .content(objectMapper.writeValueAsString(loginForm)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("login",
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        requestFields(
                                fieldWithPath("username").description("아이디"),
                                fieldWithPath("password").description("비밀번호")
                        ),responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        responseFields(
                                fieldWithPath("result").description("성공 실패 여부"),
                                fieldWithPath("status").description("http status"),
                                fieldWithPath("message").description("서버 전달 메세지"),
                                fieldWithPath("token_type").description("발급 토큰 타입"),
                                fieldWithPath("access_token").description("JWT access_token"),
                                fieldWithPath("refresh_token").description("JWT refresh_token"),
                                fieldWithPath("expire_in").description("JWT access_token 유효시간(초)")
                        )
                ));
    }

    @Test
    @Order(3)
    public void 내정보보기() throws Exception{
        //given
        String access_token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyb2xlXyI6WyJST0xFX1VTRVIiXSwiaXNzIjoibGVlYmVvbWp1biIsImV4cCI6MTYxMzQwNjQ0NSwidXNlcm5hbWUiOiJ1c2VyIn0.HMV3Qgs0q6DCjQhGgwfT_XD5geJO0utVAFP_0IBVSdg";

        //when
        //then
        mockMvc.perform(get("/api/members/me")
                .header("Authorization", "Bearer " + access_token)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("memberInfo",
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("Bearer Token"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        responseFields(
                                fieldWithPath("username").description("아이디"),
                                fieldWithPath("name").description("이름")
                        )
                ));
        }
        
//        @Test
//        public void refreshToken_이용한_accessToken_재발급() throws Exception{
//            //given
//            String refresh_token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyb2xlXyI6WyJST0xFX1VTRVIiXSwiaXNzIjoibGVlYmVvbWp1biIsImV4cCI6MTYxMzQwNjQ0NSwidXNlcm5hbWUiOiJ1c2VyIn0.HMV3Qgs0q6DCjQhGgwfT_XD5geJO0utVAFP_0IBVSdg";
//
//            RefreshTokenDto refreshTokenDto = new RefreshTokenDto();
//            refreshTokenDto.setGrant_type("refresh_token");
//
//            //when
//            //then
//            mockMvc.perform(post("/api/members/me")
//                    .header("Authorization", "Bearer " + refresh_token)
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(objectMapper.writeValueAsString(refreshTokenDto)))
//                    .andDo(print())
//                    .andExpect((result) -> assertTrue(result.getResolvedException().getClass().isAssignableFrom(NewAccessTokenIssuedException.class)))
//                    .andReturn();
//            }
}