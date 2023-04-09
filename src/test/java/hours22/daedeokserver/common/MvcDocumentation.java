package hours22.daedeokserver.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import hours22.daedeokserver.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@Import(SecurityDocumentationConfig.class)
@ExtendWith(RestDocumentationExtension.class)
public class MvcDocumentation {
    @Value("${test.token}")
    protected String accessToken;
    protected MockMvc mockMvc;
    protected ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    protected UserRepository userRepository;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext,
               RestDocumentationContextProvider restDocumentationContextProvider) {

        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .apply(documentationConfiguration(restDocumentationContextProvider))
                .apply(springSecurity())
                .build();
    }
}
