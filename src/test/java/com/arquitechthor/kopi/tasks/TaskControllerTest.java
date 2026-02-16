package com.arquitechthor.kopi.tasks;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldCreateAndListTask() throws Exception {
        mockMvc.perform(post("/tasks")
                .param("title", "Test Task")
                .param("description", "Test Description")
                .param("category", "Work")
                .param("tags", "urgent"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tasks"));

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Test Task")))
                .andExpect(content().string(containsString("Work")));
    }

    @Test
    void shouldValidateTaskInput() throws Exception {
        mockMvc.perform(post("/tasks")
                .param("title", "") // Invalid
                .param("description", "") // Invalid
                .param("category", "")) // Invalid
                .andExpect(status().isOk())
                .andExpect(view().name("tasks/form"))
                .andExpect(model().attributeHasFieldErrors("task", "title", "description", "category"));
    }
}
