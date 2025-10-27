package imoveis.aluguel.controllers.web;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;

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

@WebMvcTest(UserWebController.class)
@WithMockUser(authorities = "ADMIN")
class UserWebControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    @DisplayName("GET /users - Deve exibir a lista de usuários")
    void list_ShouldReturnListView() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setRole(RoleEnum.ADMIN);

        when(userService.list()).thenReturn(List.of(user));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/list"))
                .andExpect(model().attributeExists("users", "currentPage"))
                .andExpect(model().attribute("users", hasSize(1)))
                .andExpect(model().attribute("currentPage", "users"));
    }

    @Test
    @DisplayName("GET /users/new - Deve exibir o formulário de novo usuário")
    void createForm_ShouldReturnFormView() throws Exception {
        mockMvc.perform(get("/users/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/form"))
                .andExpect(model().attributeExists("user", "currentPage", "roles"))
                .andExpect(model().attribute("user", is(instanceOf(User.class))))
                .andExpect(model().attribute("currentPage", "users"))
                .andExpect(model().attribute("roles", RoleEnum.values()));
    }

    @Test
    @DisplayName("GET /users/edit/{id} - Deve exibir o formulário de edição")
    void editForm_ShouldReturnFormViewWithUser() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setRole(RoleEnum.ADMIN);

        when(userService.findById(1L)).thenReturn(user);

        mockMvc.perform(get("/users/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/form"))
                .andExpect(model().attributeExists("user", "currentPage", "roles"))
                .andExpect(model().attribute("user", is(user)))
                .andExpect(model().attribute("currentPage", "users"))
                .andExpect(model().attribute("roles", RoleEnum.values()));

        verify(userService, times(1)).findById(1L);
    }

    @Test
    @DisplayName("POST /users - Deve criar novo usuário")
    void saveForm_ForCreate_ShouldCallCreateAndRedirect() throws Exception {
        User user = new User();
        user.setUsername("newuser");
        user.setPassword("password123");

        when(userService.create(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/users").with(csrf())
                .param("username", "newuser")
                .param("password", "password123")
                .param("role", "ADMIN"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"));

        verify(userService, times(1)).create(any(User.class));
        verify(userService, never()).update(any(User.class), anyLong());
    }

    @Test
    @DisplayName("POST /users - Deve atualizar usuário existente")
    void saveForm_ForUpdate_ShouldCallUpdateAndRedirect() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername("updateduser");

        doNothing().when(userService).update(any(User.class), anyLong());

        mockMvc.perform(post("/users").with(csrf())
                .param("id", "1")
                .param("username", "updateduser")
                .param("role", "OPERADOR"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"));

        verify(userService, times(1)).update(any(User.class), anyLong());
        verify(userService, never()).create(any(User.class));
    }

    @Test
    @DisplayName("POST /users/delete/{id} - Deve deletar usuário e redirecionar")
    void delete_ShouldCallDeleteAndRedirect() throws Exception {
        doNothing().when(userService).delete(1L);

        mockMvc.perform(post("/users/delete/1").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"));

        verify(userService, times(1)).delete(1L);
    }
}
