package project.vhp.dolarapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import project.vhp.dolarapi.dto.awesome.api.CotacaoInfoDTO;
import project.vhp.dolarapi.dto.response.CotacaoDolarDTO;
import project.vhp.dolarapi.model.CotacaoDolar;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

/**
 * Interface com métodos para o mapeamento entre entidade e DTOs.
 */
@Mapper(componentModel = "spring")
public interface CotacaoDolarMapper {

    CotacaoDolarMapper INSTANCE = Mappers.getMapper(CotacaoDolarMapper.class);

    @Mappings({
            @Mapping(target = "valor", source = "valorCotacao", numberFormat = "#.00"),
            @Mapping(target="data", source = "dataCotacao", dateFormat = "dd/MM/yyyy - HH:mm:ss")
    })
    CotacaoDolarDTO cotacaoDolarToCotacaoDolarDTO(CotacaoDolar cotacaoDolar);

    @Mappings({
            @Mapping(target="valorCotacao", expression = "java(Double.parseDouble(cotacaoInfoDTO.getBid()))"),
            @Mapping(target = "dataCotacao", source = "timestamp", qualifiedByName = "StringToLocalDateTime")
    })
    CotacaoDolar cotacaoInfoDTOToCotacaoDolar(CotacaoInfoDTO cotacaoInfoDTO);

    List<CotacaoDolar> cotacaoInfoDTOListToCotacaoDolarList(List<CotacaoInfoDTO> cotacaoInfoDTOList);

    List<CotacaoDolarDTO> cotacaoDolarListToCotacaoDolarDTOList(List<CotacaoDolar> cotacaoDolarList);


    @Named("StringToLocalDateTime")
    default LocalDateTime extractLocalDateTimeFromString(String string) throws NumberFormatException{
        Instant instant = Instant.ofEpochSecond(Long.parseLong(string));
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    default Page<CotacaoDolarDTO> pageCotacaoDolarToPageCotacaoDolarDTO(Page<CotacaoDolar> page) {
        List<CotacaoDolarDTO> conteudo = INSTANCE.cotacaoDolarListToCotacaoDolarDTOList(page.getContent());
        return new PageImpl<>(conteudo, page.getPageable(), page.getTotalElements());
    }
}
