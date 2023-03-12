package org.example.event.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PassDTO {
    private String passName;
    private String passType;
    private int numberOfPasses;

    @Override
    public String toString() {
        return "PassDTO{" +
                "passName='" + passName + '\'' +
                ", passType='" + passType + '\'' +
                ", numberOfPasses=" + numberOfPasses +
                '}';
    }
}
