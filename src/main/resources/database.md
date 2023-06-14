```shell
DELETE   http://localhost:5001/doctor/delete/1  # 删除readId为1的记录
PUT      http://localhost:5001/dataset/add/data # 添加对应的数据集
nohup java -jar read-fundus-0.0.1-SNAPSHOT.jar > log.log 2>&1 &

http://101.43.32.24/doctor/delete/1
```





| fundus_dataset |           表名           |
| :------------: | :----------------------: |
|       id       |                          |
|  dataset_name  |                          |
|   info_file    | 存储图片名字和meta的文件 |
|      num       |   有多少人或眼睛或图片   |
|      type      | 0：图片，1：眼睛，2：人  |
|     state      |   0表示下线，1表示上线   |

| doctor_read | 表名                 |
| :---------: | -------------------- |
|     id      |                      |
| doctor_name | 医生名字             |
| dataset_id  | 读片的眼底数据集id   |
|   cur_num   | 当前读片的进度0~num  |
|     num     | 有多少人或眼睛或图片 |



每次创建一项docter_read就需要往outcome写入对应dataset的所有信息

|   outcome    | 存储读片结果            |
| :----------: | ----------------------- |
|      id      |                         |
|   read_id    |                         |
|      i       | 方便获取读片的进度1~num |
|  image_name  | 需要读片的眼底          |
|  image_num   | 图片的数量              |
|   info_id    | 用来存储病人信息的id    |
|    choose    | 选择题的答案            |
| choose_time  | 做选择题的时间          |
|   opinion    | 最终的意见              |
| opinion_time | 提出最终意见的时间      |

|  patient   |                                        |
| :--------: | -------------------------------------- |
|     id     |                                        |
| dataset_id | 属于哪个数据集                         |
| image_name | 第一张图片的名字  A.png                |
| image_num  | 图片的数量，后面的名字依次为A_1.png .. |
|  info_id   | 用来存储病人信息的id                   |

|        patient_info        | 存储病人meta信息                                   |
| :------------------------: | -------------------------------------------------- |
|             id             |                                                    |
|         dataset_id         | 需要依靠dataset_id和patient_id来找info_id          |
|         patient_id         | 疗养号                                             |
|           gender           | 男1女0                                             |
|           birth            | 出生日期                                           |
|         check_time         | 检查日期                                           |
|            age             | 年龄                                               |
|            bmi             | BMI                                                |
|  systolic_blood_pressure   | 收缩压                                             |
|     diastolic_pressure     | 舒张压                                             |
|         heart_rate         | 心率                                               |
|           smoke            | 吸烟  text                                         |
|           drink            | 饮酒  text                                         |
|   blood_pressure_history   | 血压史  text                                       |
|    circulatory_history     | 循环系统病史 text                                  |
|       family_history       | 家族史  text                                       |
|       eating_habits        | 饮食习惯 text                                      |
|          movement          | 运动方式 text                                      |
|  endocrine_system_history  | 内分泌系统病史 text                                |
|    serum_triglycerides     | 血清甘油三酯                                       |
|        cholesterol         | 总胆固醇                                           |
|            hdl             | 高密度脂蛋白                                       |
|            ldl             | 低密度脂蛋白                                       |
|            egfr            | eGFR                                               |
|   fasting_serum_glucose    | 空腹血清葡萄糖                                     |
|    glycated_hemoglobin     | 糖化血红蛋白                                       |
| aspartate_aminotransferase | 谷草转氨酶                                         |
|  alanine_aminotransferase  | 谷丙转氨酶                                         |
|            ggt             | r-谷氨酰转移酶 Gamma-glutamyl transpeptidase (GGT) |
|        blood_sugar         | 饭后2小时血糖                                      |
|            gpt             | ChatGPT管理意见                                    |

