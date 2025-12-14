package com.fresco.Visa;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.json.JSONObject;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fresco.Visa.Auth.AuthenticationRequest;
import com.fresco.Visa.Dto.UpdateDto;
import com.fresco.Visa.Entities.Visa;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class VisaApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // Static variables to persist state between ordered tests
    private static String OFFICER_TOKEN;
    private static String USER_TOKEN;
    private static String USER2_TOKEN;
    private static int CREATED_VISA_ID;
    private static String CREATED_APP_ID = "AID9999";

    // ==================================================
    // 1. LOGIN TESTS (Green Endpoint)
    // ==================================================

    @Test
    @Order(1)
    public void testLogin_Officer_Success() throws Exception {
        // Daniel is OFFICER
        AuthenticationRequest request = new AuthenticationRequest("daniel123@gmail.com", "pass1");
        
        MvcResult result = mockMvc.perform(post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andReturn();
        
        String response = result.getResponse().getContentAsString();
        JSONObject json = new JSONObject(response);
        OFFICER_TOKEN = json.getString("token");
        assertNotNull(OFFICER_TOKEN);
    }

    @Test
    @Order(2)
    public void testLogin_User_Success() throws Exception {
        // Alice is USER
        AuthenticationRequest request = new AuthenticationRequest("alice456@gmail.com", "pass2");
        
        MvcResult result = mockMvc.perform(post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();
        
        String response = result.getResponse().getContentAsString();
        JSONObject json = new JSONObject(response);
        USER_TOKEN = json.getString("token");
        assertNotNull(USER_TOKEN);
    }
    
    @Test
    @Order(3)
    public void testLogin_User2_Success() throws Exception {
        // Jack is another USER - Used to test ownership permissions for delete
        AuthenticationRequest request = new AuthenticationRequest("jack789@gmail.com", "pass3");
        
        MvcResult result = mockMvc.perform(post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();
        
        String response = result.getResponse().getContentAsString();
        JSONObject json = new JSONObject(response);
        USER2_TOKEN = json.getString("token");
    }

    @Test
    @Order(4)
    public void testLogin_InvalidCredentials() throws Exception {
        // Wrong password scenario
        AuthenticationRequest request = new AuthenticationRequest("alice456@gmail.com", "wrongpass");
        
        mockMvc.perform(post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    // ==================================================
    // 2. ADD VISA TESTS (Red Endpoint)
    // ==================================================

    @Test
    @Order(5)
    public void testAddVisa_Success_User() throws Exception {
        // Authenticated USER adds visa
        Visa visa = new Visa();
        visa.setApplicationId(CREATED_APP_ID);
        visa.setCountry("USA");
        visa.setVisaType("Tourist");
        visa.setDuration(3.0f);
        visa.setNationality("Indian");
        visa.setPassportNumber("X1234567");
        visa.setPhoneNumber("9876543210");

        MvcResult result = mockMvc.perform(post("/visa/add")
                .header("Authorization", "Bearer " + USER_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(visa)))
                .andExpect(status().isCreated()) // 201
                .andExpect(jsonPath("$.status").value("approval pending")) // Default status
                .andReturn();

        String response = result.getResponse().getContentAsString();
        JSONObject json = new JSONObject(response);
        CREATED_VISA_ID = json.getInt("id");
    }

    @Test
    @Order(6)
    public void testAddVisa_DuplicateId_Failure() throws Exception {
        // Duplicate Application ID check
        Visa visa = new Visa();
        visa.setApplicationId(CREATED_APP_ID); // Same ID as above
        visa.setCountry("UK");

        mockMvc.perform(post("/visa/add")
                .header("Authorization", "Bearer " + USER_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(visa)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(7)
    public void testAddVisa_Officer_Forbidden() throws Exception {
        Visa visa = new Visa();
        visa.setApplicationId("AID_OFFICER");
        
        // Officer tries to add visa (Not allowed, must be USER role)
        mockMvc.perform(post("/visa/add")
                .header("Authorization", "Bearer " + OFFICER_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(visa)))
                .andExpect(status().isForbidden());
    }

    // ==================================================
    // 3. LIST VISA TESTS (Orange Endpoint)
    // ==================================================

    @Test
    @Order(8)
    public void testListVisa_Success() throws Exception {
        // List by ID
        mockMvc.perform(get("/visa/list")
                .param("applicationId", CREATED_APP_ID)
                .header("Authorization", "Bearer " + OFFICER_TOKEN)) // Any role can access
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.country").value("USA"));
    }

    @Test
    @Order(9)
    public void testListVisa_MissingParam_Failure() throws Exception {
        // Missing Query Param
        mockMvc.perform(get("/visa/list")
                .header("Authorization", "Bearer " + USER_TOKEN))
                .andExpect(status().isBadRequest());
    }

    // ==================================================
    // 4. UPDATE VISA TESTS (Blue Endpoint)
    // ==================================================

    @Test
    @Order(10)
    public void testUpdateVisa_Officer_Success() throws Exception {
        // Officer updates status
        UpdateDto updateDto = new UpdateDto("Approved");

        mockMvc.perform(patch("/visa/update/" + CREATED_VISA_ID)
                .header("Authorization", "Bearer " + OFFICER_TOKEN) 
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("Approved"));
    }

    @Test
    @Order(11)
    public void testUpdateVisa_User_Forbidden() throws Exception {
        UpdateDto updateDto = new UpdateDto("Rejected");

        // User tries to update status (Forbidden)
        mockMvc.perform(patch("/visa/update/" + CREATED_VISA_ID)
                .header("Authorization", "Bearer " + USER_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isForbidden());
    }

    // ==================================================
    // 5. DELETE VISA TESTS (Red Endpoint)
    // ==================================================

    @Test
    @Order(12)
    public void testDeleteVisa_NonCreator_Forbidden() throws Exception {
        // Jack (USER2) tries to delete Alice's application
        mockMvc.perform(delete("/visa/delete/" + CREATED_VISA_ID)
                .header("Authorization", "Bearer " + USER2_TOKEN))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(13)
    public void testDeleteVisa_Creator_Success() throws Exception {
        // Alice (Creator) deletes application
        mockMvc.perform(delete("/visa/delete/" + CREATED_VISA_ID)
                .header("Authorization", "Bearer " + USER_TOKEN))
                .andExpect(status().isNoContent()); // 204
    }
}