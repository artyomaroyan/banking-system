package am.banking.system.user.infrastructure.adapter.out.persistence.converter;

import am.banking.system.common.shared.enums.PermissionEnum;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

/**
 * Author: Artyom Aroyan
 * Date: 11.06.25
 * Time: 15:06:17
 */
@WritingConverter
public class PermissionEnumWriteConverter implements Converter<PermissionEnum, String> {
    @Override
    public String convert(PermissionEnum source) {
        return source.name();
    }
}