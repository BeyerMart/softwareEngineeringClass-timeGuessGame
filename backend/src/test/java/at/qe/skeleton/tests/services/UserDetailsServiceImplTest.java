package at.qe.skeleton.tests.services;

import at.qe.skeleton.model.User;
import at.qe.skeleton.model.UserRole;
import at.qe.skeleton.repository.UserRepository;
import at.qe.skeleton.services.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class UserDetailsServiceImplTest {

    @TestConfiguration
    static class UserDetailsServiceImplTestContextConfiguration {
        @Bean
        public UserDetailsService userDetailsService() {
            return new UserDetailsServiceImpl();
        }
    }

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserDetailsService userDetailsService;

    @BeforeEach
    public void setUp() {
        User user1User = new User("test_user1", "passwd","jonass@gmail.com");
        user1User.setId(1000L);
        user1User.setRole(UserRole.ROLE_USER);

        Mockito.when(userRepository.findByUsername(user1User.getUsername())).thenReturn(Optional.of(user1User));
    }

    @Test
    public void testWhenValidNameThenUserShouldBeLoaded() {
        assertThat(userDetailsService).isNotNull();
        String name = "test_user1";
        UserDetails loaded = userDetailsService.loadUserByUsername(name);

        assertThat(loaded.getUsername()).isEqualTo(name);
 }
}
