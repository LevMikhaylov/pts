package com.example.demo;
import com.example.orders.Logging.LoginAttemptAspect;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityConfiguratorIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoginAttemptAspect loginAttemptAspect;

    @Test
    @WithAnonymousUser
    public void accessProtectedResource_ShouldRedirectToLogin() throws Exception {
        mockMvc.perform(get("/protected"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void userAccessUserEndpoint_ShouldSucceed() throws Exception {
        mockMvc.perform(get("/user/some-resource"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void userAccessAdminEndpoint_ShouldBeForbidden() throws Exception {
        mockMvc.perform(get("/admin/some-resource"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void adminAccessAdminEndpoint_ShouldSucceed() throws Exception {
        mockMvc.perform(get("/admin/some-resource"))
                .andExpect(status().isOk());
    }

    @Test
    public void loginWithValidCredentials_ShouldSucceed() throws Exception {
        mockMvc.perform(formLogin().user("user").password("password"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"))
                .andExpect(authenticated().withUsername("user"));
    }

    @Test
    public void loginWithInvalidCredentials_ShouldFail() throws Exception {
        mockMvc.perform(formLogin().user("user").password("wrong"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error"))
                .andExpect(unauthenticated());
    }

    @Test
    public void loginWithLockedAccount_ShouldRedirectToLocked() throws Exception {
        Mockito.when(loginAttemptAspect.isLockedOut(anyString())).thenReturn(true);

        mockMvc.perform(formLogin().user("lockedUser").password("password"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error=locked"))
                .andExpect(unauthenticated());
    }

    @Test
    @WithMockUser
    public void logout_ShouldSucceed() throws Exception {
        mockMvc.perform(get("/logout").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?logout"))
                .andExpect(unauthenticated());
    }

    @Test
    @WithMockUser
    public void concurrentSession_ShouldRedirectToExpired() throws Exception {
        // Первая аутентификация
        MockHttpServletRequestBuilder loginRequest = formLogin()
                .user("user")
                .password("password");

        mockMvc.perform(loginRequest);

        // Вторая аутентификация (должна завершить первую)
        mockMvc.perform(loginRequest)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?expired"));
    }
}