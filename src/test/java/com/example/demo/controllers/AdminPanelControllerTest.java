package com.example.demo.controllers;

import com.example.demo.utils.Roles;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import javax.management.relation.Role;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
public class AdminPanelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(roles = "ROLE_ADMIN")
    void testAdminPanelAccessByAdmin() throws Exception {
        mockMvc.perform(get("/admin/adminPanel"))
                .andExpect(status().isOk())
                .andExpect(view().name("adminPanel"));
    }

    @Test
    @WithMockUser(roles = "ROLE_USER")
    void testAdminPanelAccessByUser() throws Exception {
        mockMvc.perform(get("/admin/adminPanel"))
                .andExpect(status().isForbidden());
    }
}