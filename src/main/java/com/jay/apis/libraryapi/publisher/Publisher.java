package com.jay.apis.libraryapi.publisher;

import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class Publisher {
     @Ignore
     public Integer publisherId;

     public String name;
     public String emailId;
     public String phoneNumber;
}
