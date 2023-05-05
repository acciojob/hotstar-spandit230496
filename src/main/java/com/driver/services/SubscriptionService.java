package com.driver.services;


import com.driver.EntryDto.SubscriptionEntryDto;
import com.driver.model.Subscription;
import com.driver.model.SubscriptionType;
import com.driver.model.User;
import com.driver.repository.SubscriptionRepository;
import com.driver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;



@Service
public class SubscriptionService {

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    UserRepository userRepository;
    public Integer buySubscription(SubscriptionEntryDto subscriptionEntryDto){

        //Save The subscription Object into the Db and return the total Amount that user has to pay
        Subscription subscription = new Subscription();
        int totalPrice=0;
        try {
            User user = userRepository.findById(subscriptionEntryDto.getUserId()).get();
         subscription.setSubscriptionType(subscriptionEntryDto.getSubscriptionType());
         subscription.setNoOfScreensSubscribed(subscriptionEntryDto.getNoOfScreensRequired());
         subscription.setStartSubscriptionDate(new Date());

         String Value = String.valueOf(subscriptionEntryDto.getSubscriptionType());
         int noOfScreen=subscriptionEntryDto.getNoOfScreensRequired();

         if(Value.equals(SubscriptionType.BASIC))
             totalPrice=500+noOfScreen*200;
         else if(Value.equals(SubscriptionType.PRO))
                totalPrice=800+noOfScreen*250;
         else
                totalPrice=1000+noOfScreen*250;

        }
        catch(Exception e){throw new RuntimeException("user not found");}

        subscriptionRepository.save(subscription);

  return totalPrice;

    }

    public Integer upgradeSubscription(Integer userId)throws Exception{

        //If you are already at an ElITE subscription : then throw Exception ("Already the best Subscription")
        //In all other cases just try to upgrade the subscription and tell the difference of price that user has to pay
        //update the subscription in the repository
        User user = userRepository.findById(userId).get();
        Subscription subscription;
        int upPrice = 0;
        if(user.getSubscription().getSubscriptionType().equals(SubscriptionType.ELITE)){
            throw new Exception("Already the best Subscription");
        }
        else if(user.getSubscription().getSubscriptionType().equals(SubscriptionType.BASIC)) {
            user.getSubscription().setSubscriptionType(SubscriptionType.ELITE);
//           int updatePrice = 1000+user.getSubscription().getNoOfScreensSubscribed()*350 - (500+user.getSubscription().getNoOfScreensSubscribed()*200);
            upPrice  = (1000-500)+user.getSubscription().getNoOfScreensSubscribed()*(350-200);
        }
        else if(user.getSubscription().getSubscriptionType().equals(SubscriptionType.PRO)) {

            user.getSubscription().setSubscriptionType(SubscriptionType.ELITE);
            upPrice  = (1000-800)+user.getSubscription().getNoOfScreensSubscribed()*(350-250);
        }
        userRepository.save(user);
        return upPrice;
    }

    public Integer calculateTotalRevenueOfHotstar(){

        //We need to find out total Revenue of hotstar : from all the subscriptions combined
        //Hint is to use findAll function from the SubscriptionDb
        int revenue=0;
       List<Subscription>list=subscriptionRepository.findAll();
       for(Subscription x:list)
           revenue+=x.getTotalAmountPaid();
        return revenue;
    }

}
