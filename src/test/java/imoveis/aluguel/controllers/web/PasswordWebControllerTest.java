package imoveis.aluguel.controllers.web;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import imoveis.aluguel.entities.User;
import imoveis.aluguel.enums.RoleEnum;
import imoveis.aluguel.services.UserService;

@WebMvcTest(PasswordWebController.class)
@WithMockUser(username = "testuser")
class PasswordWebControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    @DisplayName("GET /password/form - Deve exibir o formulário de alteração de senha")
    void form_ShouldReturnFormView() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setRole(RoleEnum.ADMIN);

        when(userService.findByUsername("testuser")).thenReturn(user);

        mockMvc.perform(get("/password/form"))
                .andExpect(status().isOk())
                .andExpect(view().name("password/form"))
                .andExpect(model().attributeExists("id"))
                .andExpect(model().attribute("id", 1L));
    }

    @Test
    @DisplayName("POST /password/update - Deve atualizar senha com sucesso")
    void update_ShouldUpdatePasswordSuccessfully() throws Exception {
        doNothing().when(userService).updatePassword(1L, "newPassword123");

        mockMvc.perform(post("/password/update").with(csrf())
                .param("id", "1")
                .param("password", "newPassword123")
                .param("confirmPassword", "newPassword123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(flash().attributeExists("successMessage"));
    }

    @Test
    @DisplayName("POST /password/update - Deve falhar quando senhas não coincidem")
    void update_ShouldFailWhenPasswordsDoNotMatch() throws Exception {
        mockMvc.perform(post("/password/update").with(csrf())
                .param("id", "1")
                .param("password", "newPassword123")
                .param("confirmPassword", "differentPassword"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/password/form"))
                .andExpect(flash().attributeExists("errorMessage"));
    }

    @Test
    @DisplayName("POST /password/update - Deve exibir erro quando ocorrer exceção")
    void update_ShouldShowErrorWhenExceptionOccurs() throws Exception {
        doThrow(new RuntimeException("Erro ao alterar senha"))
                .when(userService).updatePassword(1L, "newPassword123");

        mockMvc.perform(post("/password/update").with(csrf())
                .param("id", "1")
                .param("password", "newPassword123")
                .param("confirmPassword", "newPassword123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(flash().attributeExists("errorMessage"));
    }
}
