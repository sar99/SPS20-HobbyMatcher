package com.sps.hobbymatcher;

import java.util.List;
import java.time.LocalDate;
import java.util.Date;

import com.google.common.collect.Lists;
import com.sps.hobbymatcher.repository.UserRepository;
import com.sps.hobbymatcher.domain.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
@SpringBootApplication
public class DemoApplication {
    @Autowired
    UserRepository userRepository;

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}