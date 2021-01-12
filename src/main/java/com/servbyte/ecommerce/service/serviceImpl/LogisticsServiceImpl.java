package com.servbyte.ecommerce.service.serviceImpl;

import com.servbyte.ecommerce.dtos.LogisticsDto;
import com.servbyte.ecommerce.entities.Logistics;
import com.servbyte.ecommerce.enums.ApiErrorCodes;
import com.servbyte.ecommerce.exceptions.BadRequestException;
import com.servbyte.ecommerce.repository.LogisticsRepository;
import com.servbyte.ecommerce.service.LogisticsService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class LogisticsServiceImpl implements LogisticsService {

    private final LogisticsRepository logisticsRepository;

    public LogisticsServiceImpl(LogisticsRepository logisticsRepository) {
        this.logisticsRepository = logisticsRepository;
    }

    public void registerLogisticsCompany(LogisticsDto logisticsDto){
        if(logisticsDto != null){
            Logistics logistics = new Logistics();
            BeanUtils.copyProperties(logisticsDto, logistics);
            logisticsRepository.save(logistics);
        }else throw new BadRequestException(ApiErrorCodes.INVALID_REQUEST.getKey(), "User should not be empty");
    }

    public List<Logistics> fetchAllLogisticsCompany(){
        return logisticsRepository.findAll();
    }

}
