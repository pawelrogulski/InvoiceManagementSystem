package progulski.invoicemanagementsystem.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import progulski.invoicemanagementsystem.domain.Invoice;
import progulski.invoicemanagementsystem.domain.Item;
import progulski.invoicemanagementsystem.domain.User;
import progulski.invoicemanagementsystem.repository.UserRepository;
import progulski.invoicemanagementsystem.service.AppService;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ReportControllerTest {
    @Autowired
    private AppService appService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @BeforeAll
    public void prepareData(){
        Item item1 = new Item("item1",1f,334,0);
        Item item2 = new Item("item2",23f,32,8);
        Item item3 = new Item("item3",345f,2,8);
        Item item4 = new Item("item4",4f,16,23);
        Item item5 = new Item("item5",324f,4,0);
        Item item6 = new Item("item6",34f,43,23);
        Item item7 = new Item("item7",34543f,1,23);
        Item item8 = new Item("item8",4f,44,0);
        Item item9 = new Item("item9",234f,10,8);
        Item item10 = new Item("item10",3423f,2,23);
        appService.addInvoice(new Invoice(LocalDate.now(),"Berlin","",1, Set.of(item1,item8,item5)));
        appService.addInvoice(new Invoice(LocalDate.now().withDayOfMonth(5),"Warsaw","",3, Set.of(item2,item10)));
        appService.addInvoice(new Invoice(LocalDate.now().withMonth(1),"Vienna","",2, Set.of(item5,item9)));
        appService.addInvoice(new Invoice(LocalDate.now().withDayOfMonth(1),"Berlin","",4, Set.of(item4,item10,item3)));
        appService.addInvoice(new Invoice(LocalDate.now().withMonth(1).withDayOfMonth(30),"Vienna","",3, Set.of(item7,item1)));
        appService.addInvoice(new Invoice(LocalDate.now(),"Warsaw","",5, Set.of(item10,item3,item7)));
        appService.addInvoice(new Invoice(LocalDate.now().withYear(2022),"Berlin","",3, Set.of(item2,item6,item10)));
        appService.addInvoice(new Invoice(LocalDate.now().withDayOfMonth(29),"Warsaw","",2, Set.of(item1,item4)));
        appService.addInvoice(new Invoice(LocalDate.now().withMonth(1).withDayOfMonth(1),"Vienna","",5, Set.of(item8,item2,item7,item5)));
        appService.addInvoice(new Invoice(LocalDate.now(),"Berlin","",1, Set.of(item5,item8,item2,item7,item10)));
    }
    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void getInvoicesFromCurrentMonthEqualsWithCurrentMonth() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/app/invoices/currentMonth")).andReturn();
        String content = result.getResponse().getContentAsString();
        List<Invoice> invoices = objectMapper.readValue(content, new TypeReference<List<Invoice>>() {});
        for(Invoice invoice : invoices){
            assertEquals(invoice.getDate().getMonth(),LocalDate.now().getMonth());
        }
    }
    @Test
    void getInvoicesFromCurrentMonthWithoutCredentialsIsUnauthorized() throws Exception{
        mockMvc.perform(get("/api/app/invoices/currentMonth")).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void groupByNipOderByOccurrencesIsInCorrectOrder() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/app/invoices/groupByNipOderByOccurrences")).andReturn();
        String content = result.getResponse().getContentAsString();
        assertEquals(content,"[[3,3],[1,2],[2,2],[5,2],[4,1]]");

    }
    @Test
    void groupByNipOderByOccurrencesWithoutCredentialsIsUnauthorized() throws Exception{
        mockMvc.perform(get("/api/app/invoices/groupByNipOderByOccurrences")).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void groupByNipOderByTotalPrice() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/app/invoices/groupByNipOderByTotalPrice")).andReturn();
        String content = result.getResponse().getContentAsString();
        assertEquals(content,"[[1,53175.348],[5,51653.67],[3,42821.89],[4,9244.5],[2,3823.2]]");
    }
    @Test
    void groupByNipOderByTotalPriceWithoutCredentialsIsUnauthorized() throws Exception{
        mockMvc.perform(get("/api/app/invoices/groupByNipOderByTotalPrice")).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void sortByDate() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/app/invoices/sortByDate")).andReturn();
        String content = result.getResponse().getContentAsString();
        List<Invoice> invoices = objectMapper.readValue(content, new TypeReference<List<Invoice>>() {});
        for(int i=0;i<invoices.size()-1;i++){
            assertFalse(invoices.get(i+1).getDate().isAfter(invoices.get(i).getDate()));
        }
    }
    @Test
    void sortByDateWithoutCredentialsIsUnauthorized() throws Exception{
        mockMvc.perform(get("/api/app/invoices/sortByDate")).andExpect(status().isUnauthorized());
    }
    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void sortByNIP() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/app/invoices/sortByNIP")).andReturn();
        String content = result.getResponse().getContentAsString();
        List<Invoice> invoices = objectMapper.readValue(content, new TypeReference<List<Invoice>>() {});
        for(int i=0;i<invoices.size()-1;i++){
            assertTrue(invoices.get(i).getNIP()<=invoices.get(i+1).getNIP());
        }
    }
    @Test
    void sortByNIPWithoutCredentialsIsUnauthorized() throws Exception{
        mockMvc.perform(get("/api/app/invoices/sortByNIP")).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void sortByTotalPrice() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/app/invoices/sortByTotalPrice")).andReturn();
        String content = result.getResponse().getContentAsString();
        List<Invoice> invoices = objectMapper.readValue(content, new TypeReference<List<Invoice>>() {});
        for(int i=0;i<invoices.size()-1;i++){
            assertTrue(invoices.get(i).getTotalPrice()>=invoices.get(i+1).getTotalPrice());
        }
    }
    @Test
    void sortByTotalPriceWithoutCredentialsIsUnauthorized() throws Exception{
        mockMvc.perform(get("/api/app/invoices/sortByTotalPrice")).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void countByCity() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/app/invoices/countByCity")).andReturn();
        String content = result.getResponse().getContentAsString();
        assertEquals(content,"[[\"Berlin\",4],[\"Vienna\",3],[\"Warsaw\",3]]");
    }
    @Test
    void countByCityWithoutCredentialsIsUnauthorized() throws Exception{
        mockMvc.perform(get("/api/app/invoices/countByCity")).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void sumByCity() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/app/invoices/sumByCity")).andReturn();
        String content = result.getResponse().getContentAsString();
        assertEquals(content,"[[\"Berlin\",4],[\"Vienna\",3],[\"Warsaw\",3]]");
    }
    @Test
    void sumByCityWithoutCredentialsisUnauthorized() throws Exception{
        mockMvc.perform(get("/api/app/invoices/sumByCity")).andExpect(status().isUnauthorized());
    }
    @Test
    void groupByItemsWithoutCredentialsisUnauthorized() throws Exception{
        mockMvc.perform(get("/api/app/invoices/groupByItems")).andExpect(status().isUnauthorized());
    }
}