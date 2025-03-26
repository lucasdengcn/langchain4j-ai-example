package com.example.demo.service;

import java.time.Duration;

import dev.langchain4j.model.openai.OpenAiChatModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import reactor.core.publisher.Flux;

@SpringBootTest
class ChatServiceTest {

  @Mock
  private OpenAiChatModel openAiChatModel;

  @Autowired
  private ProposalChatService proposalChatService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

  }

  @Test
  void test_flux() {
      Flux.interval(Duration.ofSeconds(1)).take(2).map(i -> i.intValue())
                      .doOnNext(System.out::println).subscribe();
      try {
          Thread.sleep(10000);
      } catch (InterruptedException e) {
          throw new RuntimeException(e);
      }
  }

}