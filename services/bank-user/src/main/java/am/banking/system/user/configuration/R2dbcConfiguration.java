package am.banking.system.user.configuration;

import am.banking.system.user.converter.PermissionEnumReadConverter;
import am.banking.system.user.converter.PermissionEnumWriteConverter;
import am.banking.system.user.converter.RoleEnumReadConverter;
import am.banking.system.user.converter.RoleEnumWriteConverter;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import io.r2dbc.spi.Option;
import lombok.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.convert.CustomConversions;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

import java.util.List;

import static io.r2dbc.spi.ConnectionFactoryOptions.*;

/**
 * Author: Artyom Aroyan
 * Date: 11.06.25
 * Time: 15:08:41
 */
@Configuration
@EnableR2dbcRepositories
public class R2dbcConfiguration extends AbstractR2dbcConfiguration {

    @Bean
    @NonNull
    @Override
    public ConnectionFactory connectionFactory() {
        return ConnectionFactories.get(ConnectionFactoryOptions.builder()
                .option(DRIVER, "postgresql")
                .option(HOST, "localhost")
                .option(PORT, 5432)
                .option(DATABASE, "user_db")
                .option(USER, "user_owner")
                .option(PASSWORD, "user_owner")
                .option(Option.valueOf("schema"), "usr")
                .build());
    }

    @Bean
    @NonNull
    @Override
    public R2dbcCustomConversions r2dbcCustomConversions() {
        return new R2dbcCustomConversions(
                CustomConversions.StoreConversions.NONE,
                List.of(new PermissionEnumWriteConverter(), new PermissionEnumReadConverter(),
                        new RoleEnumWriteConverter(), new RoleEnumReadConverter()));
    }
}