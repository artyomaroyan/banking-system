package am.banking.system.user.converter;

import am.banking.system.common.enums.PermissionEnum;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

/**
 * Author: Artyom Aroyan
 * Date: 11.06.25
 * Time: 15:06:17
 */
@WritingConverter
public class EnumWriteConverter implements Converter<PermissionEnum, String> {
    @Override
    public String convert(PermissionEnum source) {
        return source.name();
    }
}