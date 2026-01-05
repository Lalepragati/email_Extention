package com.email.email_writer_sb;


import lombok.Data;
@Data // helps to generate constructor getter setter

public class EmailRequest {
    private String emailcontent;  // content that i drafting for email generation
    private String tone ;   // which type of reply want funny , professional
}
