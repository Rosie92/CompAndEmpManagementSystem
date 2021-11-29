package jcpmv2.jkcho.Mapper;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class QsolModelMapper {
    private static ModelMapper modelMapper;
    private QsolModelMapper() {
    }
    private synchronized static ModelMapper getInstance() {
        if (modelMapper == null) {
            modelMapper = new ModelMapper();
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

			/*
			modelMapper.createTypeMap(CodeCategory.class, SelectOption.class)
				.addMapping(CodeCategory::getCategory, SelectOption::setValue)
				.addMapping(CodeCategory::getCategoryName, SelectOption::setLabel);
			 */
        /*    TypeMap<CodeCategory, SelectOptionDto> typeMap = modelMapper.createTypeMap(CodeCategory.class,
                    SelectOptionDto.class);
            typeMap.addMappings(mapper -> {
                mapper.map(src -> src.getCategory(), SelectOptionDto::setValue);
                mapper.map(src -> src.getCategoryName(), SelectOptionDto::setLabel);
            });*/
        }


        return modelMapper;
    }

    public static <S, T> T map(S source, Class<T> destinationType) {
        return getInstance().map(source, destinationType);
    }

    public static <S, T> List<T> map(List<S> source, Class<T> destinationType) {
        List<T> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(source)) {
            source.forEach(item -> {
                list.add(getInstance().map(item, destinationType));
            });
        }
        return list;
    }

    public static <S, T> void map(S source, T destination) {
        getInstance().map(source, destination);
    }
}
