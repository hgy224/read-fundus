package cn.hgy.readfundus.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PatientInfoDTO {
    private String patientId;
    private Integer gender;
    private LocalDate birth;
    private LocalDate checkTime;
    private BigDecimal age;
    private BigDecimal bmi;
    private Integer systolicBloodPressure;
    private Integer diastolicPressure;
    private Integer heartRate;
    private String smoke;
    private String drink;
    private String bloodPressureHistory;
    private String circulatoryHistory;
    private String familyHistory;
    private String eatingHabits;
    private String movement;
    private String endocrineSystemHistory;
    private BigDecimal serumTriglycerides;
    private BigDecimal cholesterol;
    private BigDecimal hdl;
    private BigDecimal ldl;
    private BigDecimal egfr;
    private BigDecimal fastingSerumGlucose;
    private BigDecimal glycatedHemoglobin;
    private BigDecimal aspartateAminotransferase;
    private BigDecimal alanineAminotransferase;
    private Integer ggt;
    private BigDecimal bloodSugar;
    private String gpt;
}
