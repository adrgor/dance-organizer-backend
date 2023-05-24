package com.example.participant.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;
import java.util.Map;

@Document("participant")
@Data
@NoArgsConstructor
public class Participant {

    @Id
    private String participantId;
    private int eventId;
    private String partnerId;
    
    Map<String, List<String>> formInputs;

    private String status = "REGISTERED";

    @Field("Amount paid")
    private float amountPaid = 0;
}
