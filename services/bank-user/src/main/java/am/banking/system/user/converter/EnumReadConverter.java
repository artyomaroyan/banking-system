package am.banking.system.user.converter;

import am.banking.system.common.enums.PermissionEnum;
import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

/**
 * Author: Artyom Aroyan
 * Date: 11.06.25
 * Time: 15:04:02
 */
@ReadingConverter
public class EnumReadConverter implements Converter<String, PermissionEnum> {
    @Override
    public PermissionEnum convert(@NonNull String source) {
        return PermissionEnum.valueOf(source);
    }
}