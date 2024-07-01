package jwt;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        SpringSecurityJwtTestConfig.class
})
@WebAppConfiguration
@Sql(value = {"/sql-for-tests/database-initialization/initialize-database.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/sql-for-tests/database-initialization/clear-database.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@TestPropertySource(locations = "classpath:application-test.properties")
public class BaseTestComponent {
    /*Base classe for IntegrationTests. It is not named like this becase maven integration tests
        plugin, automatically gets all classes with this suffix ;-)
     */

}
