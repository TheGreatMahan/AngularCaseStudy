package com.info5059.casestudy;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ViewController {

  @RequestMapping(
    { "/home", "/employees", "/expenses", "/generator", "/viewer" }
  )
  public String index() {
    return "forward:/index.html";
  }
}
