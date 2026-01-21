package com.email.email_writer_sb;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
//Annotation
@RestController
@AllArgsConstructor
@CrossOrigin(origins="*")
@RequestMapping("/api/email")
public class EmailGeneratController {
    private final EmailGeneratorService emailGeneratorService;
     @PostMapping("/generate")
      public ResponseEntity<String>generateEmail(@RequestBody EmailRequest emailRquest ){
        String response = emailGeneratorService.GenerateEmailReply(emailRquest);
       return ResponseEntity.ok(response);
       /*go to postman and follow steps
       1 creaate blank collection for email writer
       post req http://localhost:8080/api/email/generate
       -> body -> raw data :-  need 2 thing 1. tone 2 . req body {emailContent}
       */

   }
}
