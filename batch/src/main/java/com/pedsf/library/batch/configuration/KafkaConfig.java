package com.pedsf.library.batch.configuration;

import com.pedsf.library.batch.dto.global.MessageDTO;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

   @Value(value = "${spring.kafka.username}")
   private String username;

   @Value(value = "${spring.kafka.password}")
   private String password;

   @Value(value = "${spring.kafka.bootstrap-servers}")
   private String brokers;

   @Bean
   public Map<String, Object> producerConfig(){
      Map<String, Object> props = new HashMap<>();

      String jaasTemplate = "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"%s\" password=\"%s\";";
      String jaasCfg = String.format(jaasTemplate, username, password);

      props.put("security.protocol", "SASL_SSL");
      props.put(SaslConfigs.SASL_MECHANISM, "SCRAM-SHA-256");
      props.put(SaslConfigs.SASL_JAAS_CONFIG, jaasCfg);

      props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
      props.put(ProducerConfig.RETRIES_CONFIG, 0);
      props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
      props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
      props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);

      props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
      props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,  JsonSerializer.class);

      return props;
   }
   @Bean
   public ProducerFactory<String, MessageDTO> producerFactory() {

      return new DefaultKafkaProducerFactory<>(producerConfig(), new StringSerializer(), new JsonSerializer<>());
   }

   @Bean
   public KafkaTemplate<String, MessageDTO> kafkaTemplate() {
      return new KafkaTemplate<>(producerFactory());
   }
}