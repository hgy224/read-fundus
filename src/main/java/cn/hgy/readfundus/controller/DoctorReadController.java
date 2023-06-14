package cn.hgy.readfundus.controller;

import cn.hgy.readfundus.common.Result;
import cn.hgy.readfundus.dto.DoctorReadDTO;
import cn.hgy.readfundus.entity.DoctorRead;
import cn.hgy.readfundus.entity.Patient;
import cn.hgy.readfundus.service.IOutcomeService;
import cn.hgy.readfundus.service.IPatientService;
import cn.hgy.readfundus.service.impl.DoctorReadService;
import cn.hgy.readfundus.service.impl.FundusDatasetService;
import cn.hutool.core.bean.BeanUtil;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/doctor")
public class DoctorReadController {

    @Resource
    private DoctorReadService doctorReadService;

    @Resource
    private IPatientService patientService;

    @Resource
    private IOutcomeService outcomeService;

    @GetMapping("/list/{datasetId}")
    public Result<List<DoctorReadDTO>> list(@PathVariable Integer datasetId){
        List<DoctorRead> reads = doctorReadService.list(datasetId);
        List<DoctorReadDTO> readDTOS = reads.stream()
                .map(r ->
                        BeanUtil.copyProperties(r, DoctorReadDTO.class))
                .collect(Collectors.toList());
        return Result.success(readDTOS);
    }

    @PostMapping("/add")
    public Result<String> add(@RequestBody DoctorRead doctorRead){
        if (doctorReadService.checkName(doctorRead.getDoctorName(), doctorRead.getDatasetId()) != null){
            return Result.error("医生名字重复!");
        }

        if (!doctorReadService.createLabel(doctorRead)) return Result.error("添加失败!");
        // 获取该数据集所有的患者
        List<Patient> patients = patientService.list(doctorRead.getDatasetId());
        // 打乱患者顺序，确保每次医生创建标签时顺序不固定
        Collections.shuffle(patients);
        // 将这些患者与readId绑定，代表由readId读片
        // 将打乱之后的结果存入outcome，之后按照这个顺序读片，并存入结果
        if (outcomeService.bindPatients(patients, doctorRead.getId())) return Result.success("添加成功!");
        return Result.error("添加失败!");
    }

    @DeleteMapping("/delete/{readId}")
    public Result<String> delete(@PathVariable Integer readId){
        // 删除doctor_read中的记录
        ;
        // 删除outcome中和readId相关的结果
        ;
        if (doctorReadService.removeById(readId) && outcomeService.remove(readId)){
            return Result.success("删除成功!");
        }
        return Result.error("删除失败!");
    }
}
