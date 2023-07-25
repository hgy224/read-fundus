package cn.hgy.readfundus.controller;

import cn.hgy.readfundus.common.Result;
import cn.hgy.readfundus.dto.DoctorReadDTO;
import cn.hgy.readfundus.entity.DoctorRead;
import cn.hgy.readfundus.entity.Patient;
import cn.hgy.readfundus.service.IDoctorReadService;
import cn.hgy.readfundus.service.IOutcomeService;
import cn.hgy.readfundus.service.IPatientService;
import cn.hgy.readfundus.service.impl.DoctorReadService;
import cn.hgy.readfundus.service.impl.FundusDatasetService;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.UUID;
import jakarta.annotation.Resource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/doctor")
public class DoctorReadController {

    @Resource
    private IDoctorReadService doctorReadService;

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

    @GetMapping("/generate")
    public Result<String> generate(){
//        int[] num = new int[]{41,42,42,41,42,42,41,42,42,41,42,42};
        int[] num = new int[]{46,46,46,46,46,45,46,46,46,46,46,45};
        Integer datasetId = 7;
        List<Patient> patients = patientService.list(datasetId);
        int start = 0, end = num[0];
        for (int i = 1; i <= 12; i++) {
            DoctorRead doctorRead = new DoctorRead(Integer.toString(i), datasetId, num[i - 1]);
            doctorReadService.createLabel(doctorRead);
            outcomeService.bindPatients(patients.subList(start, end), doctorRead.getId());
            start += num[i-1];
            if (i<=11){
                end += num[i];
            }
        }
        return Result.success("成功!");
    }

    @PostMapping("/add")
    @Transactional
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

    @PostMapping("/login")
    public Result<String> login(@RequestBody DoctorRead doctorRead){
        if (!doctorReadService.checkPassword(doctorRead.getId(), doctorRead.getPassword())){
            return Result.error("password error!");
        }
        String token = doctorReadService.login(doctorRead.getId());
        return Result.success(token);
    }

    @DeleteMapping("/delete/{readId}")
    public Result<String> delete(@PathVariable Integer readId){
        // 删除doctor_read中的记录
        // 删除outcome中和readId相关的结果
        if (doctorReadService.removeById(readId) && outcomeService.remove(readId)){
            return Result.success("删除成功!");
        }
        return Result.error("删除失败!");
    }
}
