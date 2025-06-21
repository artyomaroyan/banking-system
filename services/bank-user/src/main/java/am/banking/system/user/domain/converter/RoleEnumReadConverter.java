package am.banking.system.user.domain.converter;

import am.banking.system.common.shared.enums.RoleEnum;
import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

/**
 * Author: Artyom Aroyan
 * Date: 13.06.25
 * Time: 01:33:07
 */
@ReadingConverter
public class RoleEnumReadConverter implements Converter<String, RoleEnum> {
    @Override
    public RoleEnum convert(@NonNull String source) {
        return RoleEnum.valueOf(source);
    }
}