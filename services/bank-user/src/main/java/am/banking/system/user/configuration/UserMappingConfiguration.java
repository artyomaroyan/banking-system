package am.banking.system.user.configuration;

import am.banking.system.user.api.dto.UserResponse;
import am.banking.system.user.domain.entity.User;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Configuration;

/**
 * Author: Artyom Aroyan
 * Date: 13.07.25
 * Time: 23:33:28
 */
@Configuration
@RequiredArgsConstructor
public class UserMappingConfiguration {
    private final ModelMapper modelMapper;

    @PostConstruct
    public void setupMapping() {
        modelMapper.createTypeMap(User.class, UserResponse.class)
                .setProvider(ctx -> {
                    User user = (User) ctx.getSource();
                    return new UserResponse(
                            user.getId(),
                            user.getCreatedAt(),
                            user.getUpdatedAt(),
                            user.getUsername(),
                            user.getFirstName(),
                            user.getLastName(),
                            user.getEmail(),
                            user.getPassword(),
                            user.getPhone(),
                            user.getAge(),
                            user.getAccountState()
                    );
                });
    }
}