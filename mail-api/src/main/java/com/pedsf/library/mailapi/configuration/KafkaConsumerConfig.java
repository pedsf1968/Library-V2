package com.pedsf.library.mailapi.configuration;

import com.pedsf.library.mailapi.dto.MessageDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@EnableKafka
@Configuration
public class KafkaConsumerConfig {

   @Value(value = "${spring.kafka.username}")
   private String username;
   @Value(value = "${spring.kafka.password}")
   private String password;
   @Value(value = "${spring.kafka.bootstrap-servers}")
   private String brokers;
   @Value(value = "${spring.kafka.consumer.group-id}")
   private String groupId;

   @Bean
   public Map<String,Object> consumerConfig(){
      Map<String, Object> props = new HashMap<>();
      String jaasTemplate = "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"%s\" password=\"%s\";";

      props.put("security.protocol", "SASL_SSL");
      props.put(SaslConfigs.SASL_MECHANISM, "SCRAM-SHA-256");
      props.put(SaslConfigs.SASL_JAAS_CONFIG, String.format(jaasTemplate, username, password));

      props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
      props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
      props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
      props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
      

      props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
      props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "MessageDTODeserializer");
      props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

      return props;
   }

   @Bean
   public ConsumerFactory<String, MessageDTO> messageDTOConsumerFactory(){
      return new DefaultKafkaConsumerFactory<>(
            consumerConfig(),
            new StringDeserializer(),
            new JsonDeserializer<>(MessageDTO.class, false));
   }


   @Bean
   public ConcurrentKafkaListenerContainerFactory<String, MessageDTO> messageDTOConcurrentKafkaListenerContainerFactory(){

      ConcurrentKafkaListenerContainerFactory<String, MessageDTO> factory = new ConcurrentKafkaListenerContainerFactory<>();
      factory.setConsumerFactory(messageDTOConsumerFactory());

      return factory;
   }

}
