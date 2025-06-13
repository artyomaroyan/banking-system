package am.banking.system.user.converter;

import am.banking.system.common.enums.RoleEnum;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

/**
 * Author: Artyom Aroyan
 * Date: 13.06.25
 * Time: 01:34:14
 */
@WritingConverter
public class RoleEnumWriteConverter implements Converter<RoleEnum, String> {
    @Override
    public String convert(RoleEnum source) {
        return source.name();
    }
}