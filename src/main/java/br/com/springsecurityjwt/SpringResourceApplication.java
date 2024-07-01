package br.com.springsecurityjwt;

import br.com.springsecurityjwt.resource.model.TBUser;
import br.com.springsecurityjwt.resource.repository.UserRepository;
import java.util.Optional;
import org.springdoc.core.SpringDocUIConfiguration;
import org.springdoc.core.configuration.SpringDocConfiguration;
import org.springdoc.core.SwaggerUiConfigProperties;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.providers.ObjectMapperProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication(
        scanBasePackages = {
                "br.com.springsecurityjwt"
        }
)
@EnableJpaAuditing
public class SpringResourceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringResourceApplication.class, args);
    }

    // Encripted password for user using BCrypt encoder
    @Bean
    ApplicationRunner runner(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        final Optional<TBUser> user = userRepository.findByUsername("username");
        if (user.isEmpty()) {
            final TBUser defaultUser = new TBUser();
            defaultUser.setUsername("username");
            defaultUser.setPassword(passwordEncoder.encode("password"));
            userRepository.save(defaultUser);
        }
        return args -> System.out.println(passwordEncoder.encode("password"));
    }
    /*
    TODO: Configurar //https://github.com/songrgg/swaggerdemo/blob/master/pom.xml

     */
    //incompatibility do spring.core 6.1 because LocalVariableTableParameterNameDiscoverer was removed replaced by StandardReflectionParameterNameDiscoverer
    @Bean
    SpringDocConfiguration springDocConfiguration() {
        return new SpringDocConfiguration();
    }

    @Bean
    SpringDocConfigProperties springDocConfigProperties() {
        return new SpringDocConfigProperties();
    }

    @Bean
    ObjectMapperProvider objectMapperProvider(SpringDocConfigProperties springDocConfigProperties) {

        return new ObjectMapperProvider(springDocConfigProperties);
    }

    @Bean
    SpringDocUIConfiguration SpringDocUIConfiguration(Optional<SwaggerUiConfigProperties> optionalSwaggerUiConfigProperties){
        return new SpringDocUIConfiguration(optionalSwaggerUiConfigProperties);
    }

}
