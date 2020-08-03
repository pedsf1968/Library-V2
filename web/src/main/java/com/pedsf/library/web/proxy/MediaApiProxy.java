package com.pedsf.library.web.proxy;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "zuul-server")
@RibbonClient(name = "media-api")
public interface MediaApiProxy {
   @GetMapping("/static/images/{imageName}")
   byte[] getImage(@PathVariable("imageName") String imageName);

   @GetMapping("/static/images/{type}/{imageName}")
   byte[] getBusinessImage(@PathVariable(value = "type", required = false) String type, @PathVariable("imageName") String imageName);

}
