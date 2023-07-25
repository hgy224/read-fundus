package cn.hgy.readfundus.service.impl;

import cn.hgy.readfundus.aggregates.PatientRich;
import cn.hgy.readfundus.dto.LabelDTO;
import cn.hgy.readfundus.entity.Patient;
import cn.hgy.readfundus.entity.PatientInfo;
import cn.hgy.readfundus.mapper.PatientMapper;
import cn.hgy.readfundus.service.IPatientService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.text.csv.CsvRow;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PatientService extends ServiceImpl<PatientMapper, Patient> implements IPatientService {

    /**
     * 从csv的一行中获取所有的信息
     */
    @Override
    public PatientRich getPatientInfo(CsvRow row){
        PatientRich patientRich = new PatientRich();

        Patient patient = new Patient();
        PatientInfo patientInfo = new PatientInfo();

        patient.setImageName(row.get(28));
        patient.setImageNum(Integer.valueOf(row.get(29)));

        if (StringUtils.isNotBlank(row.get(0))) patientInfo.setPatientId(row.get(0));
        if (StringUtils.isNotBlank(row.get(1))){
            Integer gender = null;
            if ("男".equals(row.get(1))){
                gender = 1;
            }else if ("女".equals(row.get(1))){
                gender = 0;
            }
            patientInfo.setGender(gender);
        }
        if (StringUtils.isNotBlank(row.get(2))) patientInfo.setBirth(LocalDate.parse(row.get(2), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        if (StringUtils.isNotBlank(row.get(3))) patientInfo.setCheckTime(LocalDate.parse(row.get(3), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        if (StringUtils.isNotBlank(row.get(4))) patientInfo.setAge(new BigDecimal(row.get(4)));
        if (StringUtils.isNotBlank(row.get(5))) patientInfo.setBmi(new BigDecimal(row.get(5)));
        if (StringUtils.isNotBlank(row.get(6))) patientInfo.setSystolicBloodPressure(Integer.valueOf(row.get(6)));
        if (StringUtils.isNotBlank(row.get(7))) patientInfo.setDiastolicPressure(Integer.valueOf(row.get(7)));
        if (StringUtils.isNotBlank(row.get(8))) patientInfo.setHeartRate(Integer.valueOf(row.get(8)));
        if (StringUtils.isNotBlank(row.get(9))) patientInfo.setSmoke(row.get(9));
        if (StringUtils.isNotBlank(row.get(10))) patientInfo.setDrink(row.get(10));
        if (StringUtils.isNotBlank(row.get(11))) patientInfo.setBloodPressureHistory(row.get(11));
        if (StringUtils.isNotBlank(row.get(12))) patientInfo.setCirculatoryHistory(row.get(12));
        if (StringUtils.isNotBlank(row.get(13))) patientInfo.setFamilyHistory(row.get(13));
        if (StringUtils.isNotBlank(row.get(14))) patientInfo.setEatingHabits(row.get(14));
        if (StringUtils.isNotBlank(row.get(15))) patientInfo.setMovement(row.get(15));
        if (StringUtils.isNotBlank(row.get(16))) patientInfo.setEndocrineSystemHistory(row.get(16));
        if (StringUtils.isNotBlank(row.get(17))) patientInfo.setSerumTriglycerides(new BigDecimal(row.get(17)));
        if (StringUtils.isNotBlank(row.get(18))) patientInfo.setCholesterol(new BigDecimal(row.get(18)));
        if (StringUtils.isNotBlank(row.get(19))) patientInfo.setHdl(new BigDecimal(row.get(19)));
        if (StringUtils.isNotBlank(row.get(20))) patientInfo.setLdl(new BigDecimal(row.get(20)));
        if (StringUtils.isNotBlank(row.get(21))) patientInfo.setEgfr(new BigDecimal(row.get(21)));
        if (StringUtils.isNotBlank(row.get(22))) patientInfo.setFastingSerumGlucose(new BigDecimal(row.get(22)));
        if (StringUtils.isNotBlank(row.get(23))) patientInfo.setGlycatedHemoglobin(new BigDecimal(row.get(23)));
        if (StringUtils.isNotBlank(row.get(24))) patientInfo.setAspartateAminotransferase(new BigDecimal(row.get(24)));
        if (StringUtils.isNotBlank(row.get(25))) patientInfo.setAlanineAminotransferase(new BigDecimal(row.get(25)));
        if (StringUtils.isNotBlank(row.get(26))) patientInfo.setGgt(Integer.valueOf(row.get(26)));
        if (StringUtils.isNotBlank(row.get(27))) patientInfo.setBloodSugar(new BigDecimal(row.get(27)));

        patientRich.setPatient(patient);
        patientRich.setPatientInfo(patientInfo);
        return patientRich;
    }

    @Override
    public PatientRich getPatient(CsvRow row) {
        PatientRich patientRich = new PatientRich();

        Patient patient = new Patient();
        patient.setImageName(row.get(0));
        patient.setImageNum(Integer.valueOf(row.get(row.size()-1)));

        LabelDTO label = new LabelDTO();
        if (row.size()==9){
            if (StringUtils.isNotBlank(row.get(1))) label.setDr((Double.valueOf(row.get(1)).intValue()));
            if (StringUtils.isNotBlank(row.get(2))) label.setDme(Double.valueOf(row.get(2)).intValue());
            if (StringUtils.isNotBlank(row.get(3))) label.setWxgl(Double.valueOf(row.get(3)).intValue());
            if (StringUtils.isNotBlank(row.get(4))) label.setMxb(Double.valueOf(row.get(4)).intValue());
            if (StringUtils.isNotBlank(row.get(5))) label.setYxsc(Double.valueOf(row.get(5)).intValue());
            if (StringUtils.isNotBlank(row.get(6))) label.setCx(Double.valueOf(row.get(6)).intValue());
            if (StringUtils.isNotBlank(row.get(7))) label.setRdr(Double.valueOf(row.get(7)).intValue());
        }else if (row.size()==5){
            if (StringUtils.isNotBlank(row.get(1))) label.setDr((Double.valueOf(row.get(1)).intValue()));
            if (StringUtils.isNotBlank(row.get(2))) label.setDme(Double.valueOf(row.get(2)).intValue());
            if (StringUtils.isNotBlank(row.get(3))) label.setRdr(Double.valueOf(row.get(3)).intValue());
            label.setWxgl(-1);
            label.setMxb(-1);
            label.setYxsc(-1);
            label.setCx(-1);
        }

        patient.setInfo(JSONUtil.toJsonStr(label));

        patientRich.setPatient(patient);
        return patientRich;
    }

    /**
     * 获取某个数据集中所有的患者
     */
    @Override
    public List<Patient> list(Integer datasetId) {
        LambdaQueryWrapper<Patient> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Patient::getDatasetId, datasetId);
        return list(queryWrapper);
    }
}
