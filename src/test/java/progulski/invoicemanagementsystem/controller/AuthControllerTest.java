package progulski.invoicemanagementsystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import progulski.invoicemanagementsystem.domain.User;
import progulski.invoicemanagementsystem.repository.UserRepository;
import progulski.invoicemanagementsystem.security.LoginRequest;
import progulski.invoicemanagementsystem.service.AppService;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AuthControllerTest {

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private AppService appService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @BeforeAll
    public void addTestUser(){
        User user = new User(1,"username","password",null);
        appService.addUser(user,"ROLE_CUSTOMER");
    }

    @Test
    void authenticateUserTestWithExistedCredentials()  throws Exception{
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("username");
        loginRequest.setPassword("password");
        mockMvc.perform(post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void authenticateUserTestWithWrongPassword()  throws Exception{
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("username");
        loginRequest.setPassword("wrong_password");
        mockMvc.perform(post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }
    @Test
    void authenticateUserTestWithWrongUsername()  throws Exception{
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("wrong_username");
        loginRequest.setPassword("password");
        mockMvc.perform(post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }
    @Test
    void authenticateUserTestWithWrongUsernameAndPassword()  throws Exception{
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("wrong_username");
        loginRequest.setPassword("wrong_password");
        mockMvc.perform(post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }
}