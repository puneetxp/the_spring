package com.puneetxp.lib;

import java.util.ArrayList;
import java.util.List;

public class Mail {
    public String subject = "Default Subject";
    public String message;
    public List<String> to = new ArrayList<>();

    public Mail subject(String subject) {
        this.subject = subject;
        return this;
    }

    public Mail message(String message) {
        this.message = message;
        return this;
    }

    public Mail to(String email) {
        this.to.add(email);
        return this;
    }

    public void send() {
        // Placeholder implementation using JavaMailSender (implied)
        System.out.println("Mock Email to " + to + ": " + subject);
    }
}
