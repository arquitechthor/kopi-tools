package com.arquitechthor.kopi.links;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.arquitechthor.kopi.config.TestSecurityConfig;
import org.springframework.context.annotation.Import;

import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestSecurityConfig.class)
@WithMockUser
class LinkControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LinkRepository linkRepository;

    @Test
    void shouldLoadHomePageAndRedirect() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/links"));
    }

    @Test
    void shouldLoadLinksPage() throws Exception {
        mockMvc.perform(get("/links"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Kopi Tools")));
    }

    @Test
    void shouldCreateAndListLink() throws Exception {
        mockMvc.perform(post("/links")
                .with(csrf())
                .param("url", "https://example.com")
                .param("title", "Example Link")
                .param("category", "Test Category")
                .param("description", "A test link"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/links"));

        mockMvc.perform(get("/links"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Example Link")))
                .andExpect(content().string(containsString("Test Category")));
    }

    @Test
    void shouldValidateInput() throws Exception {
        mockMvc.perform(post("/links")
                .with(csrf())
                .param("url", "") // Invalid
                .param("title", "") // Invalid
                .param("category", "")) // Invalid
                .andExpect(status().isOk()) // Returns form with errors
                .andExpect(view().name("form"))
                .andExpect(model().attributeHasFieldErrors("link", "url", "title", "category"));
    }
}
