package main.java.de.dhbw.ledcontroller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

	@RequestMapping("/")
    public String home(){
        return "LED-Controller";
    }
	
}
