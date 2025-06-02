package am.banking.system.common.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Author: Artyom Aroyan
 * Date: 02.06.25
 * Time: 22:47:24
 */
@Component
@RequiredArgsConstructor
public class GenericMapper {
    private final ModelMapper modelMapper;

    public <S, T> T map(S source, Class<T> targetClass) {
        return modelMapper.map(source, targetClass);
    }

    public <S, T> List<T> map(List<S> sourceList, Class<T> targetClass) {
        return sourceList.stream()
                .map(source -> modelMapper.map(source, targetClass))
                .toList();
    }
}