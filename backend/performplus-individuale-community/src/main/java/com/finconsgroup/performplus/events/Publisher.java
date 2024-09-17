package com.finconsgroup.performplus.events;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class Publisher {
  
  private final ApplicationEventPublisher publisher;
    
    Publisher(ApplicationEventPublisher publisher) {
      this.publisher = publisher;
    }

}