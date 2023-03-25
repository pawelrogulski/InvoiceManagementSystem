package progulski.invoicemanagementsystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import progulski.invoicemanagementsystem.domain.Invoice;
import progulski.invoicemanagementsystem.domain.Item;
import progulski.invoicemanagementsystem.service.AppService;

import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AppControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @InjectMocks
    AppController mockedAppController;
    @Mock
    AppService mockedAppService;
    @Autowired
    private AppService appService;
    @Autowired
    private ObjectMapper objectMapper;
    @BeforeAll
    public void addTestUser(){
        Item item = new Item();
        Invoice invoice = new Invoice();
        invoice.setId(1);
        invoice.setItems(Set.of(item));
        appService.addInvoice(invoice);
    }
    @Test
    void processRegisterIsOk() throws Exception {
        mockMvc.perform(get("/api/app/register")).andExpect(status().isOk());
    }

    @Test
    void processRegisterEmployeeWithoutCredentialsIsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/app/registerEmployee")).andExpect(status().isUnauthorized());
    }
    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void processRegisterEmployeeWithEmployeeCredentialsIsOk() throws Exception {
        mockMvc.perform(get("/api/app/registerEmployee")).andExpect(status().isOk());
    }

    @Test
    void addInvoiceWithoutCredentialsIsUnauthorized() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(post("/api/app/addInvoice")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new Invoice())))
        .andExpect(status().isUnauthorized());
    }
    @Test
    @WithMockUser(roles = "CUSTOMER")
    void addInvoiceWithCustomerCredentialsIsOk() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(post("/api/app/addInvoice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new Invoice())))
                .andExpect(status().isOk());
    }
    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void addInvoiceWithEmployeeCredentialsIsOk() throws Exception {
        mockMvc.perform(post("/api/app/addInvoice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new Invoice())))
                .andExpect(status().isOk());
    }
    @Test
    void getInvoiceWithoutCredentialsIsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/app/invoice/1")).andExpect(status().isUnauthorized());
    }
    @Test
    @WithMockUser(roles = "CUSTOMER")
    void getInvoiceWithCustomerCredentialsIsOk() throws Exception {
        mockMvc.perform(get("/api/app/invoice/1")).andExpect(status().isOk());
    }
    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void getInvoiceWithEmployeeCredentialsIsOk() throws Exception {
        mockMvc.perform(get("/api/app/invoice/1")).andExpect(status().isOk());
    }

}