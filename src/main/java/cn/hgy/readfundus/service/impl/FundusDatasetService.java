package cn.hgy.readfundus.service.impl;

import cn.hgy.readfundus.aggregates.PatientRich;
import cn.hgy.readfundus.common.ResourcePath;
import cn.hgy.readfundus.entity.FundusDataset;
import cn.hgy.readfundus.entity.Patient;
import cn.hgy.readfundus.entity.PatientInfo;
import cn.hgy.readfundus.mapper.FundusDatasetMapper;
import cn.hgy.readfundus.service.IFundusDatasetService;
import cn.hgy.readfundus.service.IPatientInfoService;
import cn.hgy.readfundus.service.IPatientService;
import cn.hutool.core.text.csv.CsvData;
import cn.hutool.core.text.csv.CsvReader;
import cn.hutool.core.text.csv.CsvUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FundusDatasetService extends ServiceImpl<FundusDatasetMapper, FundusDataset> implements IFundusDatasetService {

    @Resource
    private ResourcePath resourcePath;

    @Resource
    private IPatientService patientService;

    @Resource
    private IPatientInfoService patientInfoService;

    /**
     * 查询state=1的dataset
     */
    public List<FundusDataset> usedList(int state) {
        LambdaQueryWrapper<FundusDataset> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FundusDataset::getState, state);
        return this.list(queryWrapper);
    }

    /**
     * 添加眼底数据集
     */
    @Transactional
    public boolean addDataset(String datasetName, String fileName, int type){
        assert fileName.endsWith(".csv"):"信息文件应为csv文件";
        String path = Paths.get(resourcePath.getInfo(), fileName).toString();
        File infoFile = new File(path);
        if (infoFile.exists()){
            CsvReader reader = CsvUtil.getReader();
            reader.setContainsHeader(true);
            CsvData data = reader.read(infoFile, StandardCharsets.UTF_8);
            // 获取数据条数
            int num = data.getRowCount();

            // 从文件中读取每一行到对象中  dataset_id info_id需要填充
            List<PatientRich> patients = null;
            if (type==2){
                patients = data.getRows().stream()
                        .map(patientService::getPatientInfo)
                        .collect(Collectors.toList());
            }else if (type == 1){
                patients = data.getRows().stream().map(patientService::getPatient)
                        .collect(Collectors.toList());
            }

            // 要插入的一个数据集
            FundusDataset fundusDataset = new FundusDataset(datasetName, fileName, type, num);
            if (!save(fundusDataset)){
                return false;
            }
            Integer datasetId = getByName(datasetName).getId();
            // 填充dataset_id
            assert patients != null;
            patients.forEach(patientRich -> {
                patientRich.getPatient().setDatasetId(datasetId);
                if (patientRich.getPatientInfo()!=null){
                    patientRich.getPatientInfo().setDatasetId(datasetId);
                }});

            if (type==1){
                return patientService.saveBatch(patients.stream().map(PatientRich::getPatient).collect(Collectors.toList()));
            }
            // 保存这个数据集所有患者的信息 patientInfo
            List<PatientInfo> patientInfos = patients.stream()
                    .map(PatientRich::getPatientInfo).collect(Collectors.toList());
            if (!patientInfoService.saveBatch(patientInfos)) return false;

            // 查询所有患者的info_id并填充
            List<Patient> patient = patients.stream().map(patientRich -> {
                patientRich.getPatient()
                        .setInfoId(patientInfoService
                                .getIdByDatasetPatient(datasetId, patientRich.getPatientInfo().getPatientId()));
                return patientRich.getPatient();
            }).collect(Collectors.toList());
            return patientService.saveBatch(patient);
        }else {
            log.error(String.format("%s not exist!", path));
        }
        return false;
    }

    /**
     * 根据数据集的名字获取数据集
     */
    public FundusDataset getByName(String datasetName){
        LambdaQueryWrapper<FundusDataset> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(!StringUtils.isBlank(datasetName), FundusDataset::getDatasetName, datasetName);
        return getOne(queryWrapper);
    }

    /**
     * 激活数据集 state=1
     */
    public boolean activateDataset(String datasetName){
        LambdaUpdateWrapper<FundusDataset> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(FundusDataset::getDatasetName, datasetName);
        updateWrapper.set(FundusDataset::getState, 1);
        return update(updateWrapper);
    }

    @Override
    public boolean addGpt(String datasetName, String fileName) {
        assert fileName.endsWith(".csv"):"信息文件应为csv文件";
        // 获取数据集的id
        FundusDataset dataset = getByName(datasetName);
        String path = Paths.get(resourcePath.getInfo(), fileName).toString();
        File infoFile = new File(path);
        if (infoFile.exists()){
            CsvReader reader = CsvUtil.getReader();
            reader.setContainsHeader(true);
            CsvData data = reader.read(infoFile, StandardCharsets.UTF_8);
            // 遍历gpt建议存入数据库
            data.getRows().forEach(row -> patientInfoService.update(row.get(1), dataset.getId(), row.get(0)));
        }
        return true;
    }
}
