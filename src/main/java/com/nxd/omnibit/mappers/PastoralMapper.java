package com.nxd.omnibit.mappers;


import org.springframework.stereotype.Component;

import com.nxd.omnibit.DTOs.PastoralDTO;
import com.nxd.omnibit.models.Pastoral;

@Component
public class PastoralMapper {
    
    public Pastoral toPastoral(PastoralDTO pastoralRequest) {
        return new Pastoral( pastoralRequest.nome(), pastoralRequest.descricao());
    }

}
