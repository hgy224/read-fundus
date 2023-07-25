package cn.hgy.readfundus.service.impl;

import cn.hgy.readfundus.dto.*;
import cn.hgy.readfundus.entity.DoctorRead;
import cn.hgy.readfundus.entity.Outcome;
import cn.hgy.readfundus.entity.Patient;
import cn.hgy.readfundus.entity.PatientInfo;
import cn.hgy.readfundus.mapper.OutcomeMapper;
import cn.hgy.readfundus.service.IDoctorReadService;
import cn.hgy.readfundus.service.IOutcomeService;
import cn.hgy.readfundus.service.IPatientInfoService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class OutcomeService extends ServiceImpl<OutcomeMapper, Outcome> implements IOutcomeService {

    @Resource
    private IPatientInfoService patientInfoService;

    @Resource
    private IDoctorReadService doctorReadService;

    /**
     * 将一个数据集的所有患者与readId绑定存入outcome
     */
    @Override
    public boolean bindPatients(List<Patient> patients, Integer readId){
        int patientNum = patients.size();
        List<Outcome> outcomes = new ArrayList<>(patientNum);
        for (int i = 0; i < patientNum; i++) {
            Outcome outcome = new Outcome();
            outcome.setReadId(readId);
            outcome.setI(i);
            outcome.setImageName(patients.get(i).getImageName());
            outcome.setImageNum(patients.get(i).getImageNum());
            outcome.setInfoId(patients.get(i).getInfoId());
            // 如果info不为空，表示有label需要显示出来，存在opinion
            if (patients.get(i).getInfo()!=null){
                outcome.setOpinion(patients.get(i).getInfo());
            }
            outcomes.add(outcome);
        }
        return saveBatch(outcomes);
    }

    /**
     * 根据readId和curNum确定应该对哪个病人进行诊断
     */
    @Override
    public InfoDTO getPatient(DoctorRead read) {
        LambdaQueryWrapper<Outcome> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Outcome::getReadId, read.getId());
        queryWrapper.eq(Outcome::getI, read.getCurNum());
        // 获取此次要读的眼底和对应患者的infoId
        Outcome patient = getOne(queryWrapper);

        // 根据患者的infoId获取患者的所有meta信息
        PatientInfo patientInfo = patientInfoService.getById(patient.getInfoId());

        // 将info和img封装进行合并
        InfoDTO info = new InfoDTO();
        info.setImage(BeanUtil.copyProperties(patient, ImageDTO.class));
        info.setPatientInfo(BeanUtil.copyProperties(patientInfo, PatientInfoDTO.class));
        return info;
    }

    @Override
    public InfoLabelDTO getPatientLabel(DoctorRead read) {
        LambdaQueryWrapper<Outcome> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Outcome::getReadId, read.getId());
        queryWrapper.eq(Outcome::getI, read.getCurNum());
        // 获取此次要读的眼底和对应患者的infoId
        Outcome patient = getOne(queryWrapper);

        LabelDTO label = JSONUtil.toBean(patient.getOpinion(), LabelDTO.class);
        if (label.getCx() == null) label.setCx(0);
        if (label.getDme() == null) label.setDme(0);
        if (label.getDr() == null) label.setDr(0);
        if (label.getMxb() == null) label.setMxb(0);
        if (label.getRdr() == null) label.setRdr(0);
        if (label.getYxsc() == null) label.setYxsc(0);
        if (label.getWxgl() == null) label.setWxgl(0);
        // 将info和img封装进行合并
        InfoLabelDTO info = new InfoLabelDTO();
        info.setImage(BeanUtil.copyProperties(patient, ImageDTO.class));
        info.setLabel(label);
        return info;
    }

    /**
     * 根据readId和i去更新对应的choose choose_time
     */
    public boolean updateChoose(Outcome outcome){
        LambdaUpdateWrapper<Outcome> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(Outcome::getChoose, outcome.getChoose());
        updateWrapper.set(Outcome::getChooseTime, outcome.getChooseTime());
        updateWrapper.eq(Outcome::getReadId, outcome.getReadId());
        updateWrapper.eq(Outcome::getI, outcome.getI());
        return update(updateWrapper);
    }

    /**
     * 根据readId和i去更新对应的opinion 并将curNum+1
     */
    @Transactional
    public boolean updateOpinion(Outcome outcome){
        LambdaUpdateWrapper<Outcome> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(Outcome::getOpinion, outcome.getOpinion());
        updateWrapper.set(Outcome::getOpinionTime, outcome.getOpinionTime());
        updateWrapper.eq(Outcome::getReadId, outcome.getReadId());
        updateWrapper.eq(Outcome::getI, outcome.getI());
        return update(updateWrapper) && doctorReadService.addCurNum(outcome.getReadId());
    }

    @Override
    public boolean remove(Integer readId) {
        LambdaQueryWrapper<Outcome> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Outcome::getReadId, readId);
        return remove(queryWrapper);
    }
}
