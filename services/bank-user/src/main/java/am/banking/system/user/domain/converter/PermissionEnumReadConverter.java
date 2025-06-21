package am.banking.system.user.domain.converter;

import am.banking.system.common.shared.enums.PermissionEnum;
import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

/**
 * Author: Artyom Aroyan
 * Date: 11.06.25
 * Time: 15:04:02
 */
@ReadingConverter
public class PermissionEnumReadConverter implements Converter<String, PermissionEnum> {
    @Override
    public PermissionEnum convert(@NonNull String source) {
        return PermissionEnum.valueOf(source);
    }
}