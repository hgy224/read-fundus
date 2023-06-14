package cn.hgy.readfundus.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OutcomeDTO {
    private String imageName;
    private String choose;
    private BigDecimal chooseTime;
    private String opinion;
    private BigDecimal opinionTime;

    public OutcomeDTO() {
    }

    public OutcomeDTO(String imageName, String choose, BigDecimal chooseTime, String opinion, BigDecimal opinionTime) {
        this.imageName = imageName;
        this.choose = choose;
        this.chooseTime = chooseTime;
        this.opinion = opinion;
        this.opinionTime = opinionTime;
    }
}
