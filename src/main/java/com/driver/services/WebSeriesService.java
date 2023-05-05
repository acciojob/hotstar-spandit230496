package com.driver.services;

import com.driver.EntryDto.WebSeriesEntryDto;
import com.driver.model.ProductionHouse;
import com.driver.model.WebSeries;
import com.driver.repository.ProductionHouseRepository;
import com.driver.repository.WebSeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WebSeriesService {

    @Autowired
    WebSeriesRepository webSeriesRepository;

    @Autowired
    ProductionHouseRepository productionHouseRepository;

    public Integer addWebSeries(WebSeriesEntryDto webSeriesEntryDto)throws  Exception{



        WebSeries series = webSeriesRepository.findBySeriesName(webSeriesEntryDto.getSeriesName());
        int updatedrating=0;
        if(series==null) {

            series.setSeriesName(webSeriesEntryDto.getSeriesName());
            series.setAgeLimit(webSeriesEntryDto.getAgeLimit());
            series.setRating(webSeriesEntryDto.getRating());
            series.setSubscriptionType(webSeriesEntryDto.getSubscriptionType());
            try{
                ProductionHouse productionHouse=productionHouseRepository.findById(webSeriesEntryDto.getProductionHouseId()).get();
                productionHouse.getWebSeriesList().add(series);

                List<WebSeries>changedrating=productionHouse.getWebSeriesList();
                for(WebSeries x:changedrating){
                    updatedrating+=x.getRating();
                }
                updatedrating=updatedrating/changedrating.size();
                productionHouse.setRatings(updatedrating);
                series.setProductionHouse(productionHouse);
                webSeriesRepository.save(series);

            }
            catch(Exception e){
                throw new RuntimeException("Production House not found");
            }

        }
        else{
            throw new RuntimeException("Series is already present");
        }


        return 1;
    }

}
