package com.jay.apis.libraryapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class Publisher {
     public String publisherId;
     public String name;
     public String emailId;
     public String phoneNumber;
}
