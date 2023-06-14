package cn.hgy.readfundus.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Outcome {
    private Integer id;
    private Integer readId;
    private Integer i;
    private String imageName;
    private Integer imageNum;
    private Integer infoId;
    private String choose;
    private BigDecimal chooseTime;
    private String opinion;
    private BigDecimal opinionTime;

}
