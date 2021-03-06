package com.pedsf.library.dto.global;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

@Slf4j
public class MessageDTODeserializer implements Deserializer<MessageDTO> {

@Override
public MessageDTO deserialize(String arg0, byte[] devBytes) {
      ObjectMapper mapper = new ObjectMapper();
   MessageDTO messageDTO = null;
      try {
         messageDTO = mapper.readValue(devBytes, MessageDTO.class);
      } catch (Exception e) {
         log.error("ERROR MessageDTO deserialize : " + e.getMessage());
      }
      return messageDTO;
      }

@Override
public void close() {
         // Do nothing because nothing to close
      }

@Override
public void configure(Map<String, ?> arg0, boolean arg1) {
         // Do nothing because nothing to configure
      }
}
