package com.william.foro.config.email;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class EmailController {

    private final EmailServiceImpl emailService;

    @PostMapping("/send-email")
    String sendEmail(@RequestParam String to, @RequestParam String subject, @RequestParam String text, Authentication authentication) {
        emailService.sendSimpleMessage(to, subject, text);
        log.info(authentication.getAuthorities().toString());
        log.info(authentication.getPrincipal().toString());
        log.info(authentication.getName());
        log.info(authentication.isAuthenticated() + "");
        return "Email sent successfully";
    }

}
