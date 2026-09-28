package com.example.wallet_management_app.integration;

import com.example.wallet_management_app.repository.UserRepository;
import com.example.wallet_management_app.repository.CategoryRepository;
import com.example.wallet_management_app.repository.PaymentMethodRepository;
import com.example.wallet_management_app.repository.ExpenditureRepository;
import com.example.wallet_management_app.entity.User;
import com.example.wallet_management_app.entity.Category;
import com.example.wallet_management_app.entity.Expenditure;
import com.example.wallet_management_app.entity.PaymentMethod;

import org.springframework.transaction.annotation.Transactional;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.test.web.servlet.MockMvc;

import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;


import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class ExpenditureIntegrationTest {
        
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Autowired
    private ExpenditureRepository expenditureRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MockMvc mockMvc;

    private User user1;
    private User user2;

    private Category category1;
    private Category category2;

    private PaymentMethod paymentMethod1;
    private PaymentMethod paymentMethod2;

    private Expenditure expenditure1;
    private Expenditure expenditure2;


    @BeforeEach
    void setUp() {

        user1 = new User();
        user1.setEmail("test1@example.com");
        user1.setPassword(passwordEncoder.encode("test1Password"));
        userRepository.save(user1);

        user2 = new User();
        user2.setEmail("test2@example.com");
        user2.setPassword("test2Password");
        userRepository.save(user2);

        category1 = new Category();
        category1.setUser(user1);
        category1.setName("testPrivate1");
        categoryRepository.save(category1);

        category2 = new Category();
        category2.setUser(user2);
        category2.setName("testPrivate2");
        categoryRepository.save(category2);

        paymentMethod1 = new PaymentMethod();
        paymentMethod1.setUser(user1);
        paymentMethod1.setName("testCredit1");
        paymentMethodRepository.save(paymentMethod1);
        
        paymentMethod2 = new PaymentMethod();
        paymentMethod2.setUser(user2);
        paymentMethod2.setName("testCredit2");
        paymentMethodRepository.save(paymentMethod2);

        expenditure1 = new Expenditure();
        expenditure1.setName("testExpenditure1");
        expenditure1.setUser(user1);
        expenditure1.setCategory(category1);
        expenditure1.setPaymentMethod(paymentMethod1);
        BigDecimal amount1 = BigDecimal.valueOf(5000);
        LocalDate expenditureDate1 = LocalDate.of(2026, 9, 27);
        expenditure1.setAmount(amount1);
        expenditure1.setExpenditureDate(expenditureDate1);
        expenditureRepository.save(expenditure1);

        expenditure2 = new Expenditure();
        expenditure2.setName("testExpenditure2");
        expenditure2.setUser(user2);
        expenditure2.setCategory(category2);
        expenditure2.setPaymentMethod(paymentMethod2);
        BigDecimal amount2 = BigDecimal.valueOf(5000);
        LocalDate expenditureDate2 = LocalDate.of(2026, 9, 27);
        expenditure2.setAmount(amount2);
        expenditure2.setExpenditureDate(expenditureDate2);
        expenditureRepository.save(expenditure2);
    }

    @Test
    @WithUserDetails(
        value = "test1@example.com",
        setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    void shouldDisplayOnlyAuthenticatedUsersExpenditures() throws Exception {

        mockMvc
            .perform(get("/expenditures").param("targetMonth", "2026-09"))
                .andExpect(status().isOk())
                .andExpect(view().name("expenditureList"))
                .andExpect(content().string(
                    containsString("testExpenditure1")))
                .andExpect(content().string(
                    not(containsString("testExpenditure2"))
                ));
    }

    @Test
    @WithUserDetails(
        value = "test2@example.com",
        setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    void shouldRejectEditAccessWhenExpenditureBelongsToAnotherUser() throws Exception {

        mockMvc
            .perform(get("/expenditures/edit/{expenditureId}", expenditure1.getId()))
            .andExpect(status().is4xxClientError());
    }

    @Test
    @WithUserDetails(
        value = "test2@example.com",
        setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    void shouldCreateExpenditureOwnedByAuthenticatedUser() throws Exception{

        mockMvc
            .perform(
                post("/expenditures")
                    .param("name", "newExpenditureByUser2")
                    .param("amount", "3000")
                    .param("expenditureDate", "2026-09-28")
                    .param("categoryId", category2.getId().toString())
                    .param("paymentMethodId", paymentMethod2.getId().toString())
                    .param("memo", "Integration test")
                    .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/expenditures"));

        Expenditure createExpenditure =
                expenditureRepository.findAll()
                        .stream()
                        .filter(expenditure ->
                                expenditure.getName().equals("newExpenditureByUser2"))
                        .findFirst()
                        .orElseThrow();

        assertEquals(user2.getId(), createExpenditure.getUser().getId());
    }

    @Test
    void shouldAuthenticatedUserAndRedirectToDashboardWhenCredentialsAreValid() throws Exception {
    
        mockMvc
            .perform(
                post("/login")
                .param("email", "test1@example.com")
                .param("password" ,"test1Password")
                .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(authenticated().withUsername("test1@example.com"))
            .andExpect(redirectedUrl("/dashboard"));
    }
}