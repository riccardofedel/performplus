package com.finconsgroup.performplus.events;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
class AsyncListener {

  @Async
  @EventListener
  void handleAsyncEvent(String event) {
    // handle event
  }
}