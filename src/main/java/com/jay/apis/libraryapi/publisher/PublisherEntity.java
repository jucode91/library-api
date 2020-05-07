package com.jay.apis.libraryapi.publisher;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "PUBLISHER")
@Getter
@NoArgsConstructor
public class PublisherEntity {

    @Column(name = "publisher_id")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "publisherId_generator")
    @SequenceGenerator(name = "publisherId_generator", sequenceName = "publisher_sequence",allocationSize = 50)
    private int publisherId;

    @Setter
    @Column(name = "Name")
    private String name;

    @Setter
    @Column(name = "email_id")
    private String emailId;

    @Setter
    @Column(name = "phone_number")
    private String phoneNumber;

    public PublisherEntity(String name, String emailId, String phoneNumber) {
        this.name = name;
        this.emailId = emailId;
        this.phoneNumber = phoneNumber;
    }
}
