package org.example.registrationdashboardservice.model.ticket;

import lombok.Data;

@Data
public class Ticket {
    private String name;
    private String type;
    private Float price;
    private String currency;
    private Integer numberOfTickets;
}