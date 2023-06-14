use fundus;
create table doctor_read
(
    id int auto_increment
        primary key,
    doctor_name varchar(64) not null,
    dataset_id int not null,
    cur_num int default 0 null,
    num int not null
);

create table fundus_dataset
(
    id int auto_increment
        primary key,
    dataset_name varchar(20) not null,
    info_file varchar(20) not null,
    type tinyint not null,
    num int not null,
    state tinyint default 1 not null,
    constraint fundus_dataset_dataset_name_uindex
        unique (dataset_name)
);

create table outcome
(
    id int auto_increment
        primary key,
    read_id int not null,
    i int not null,
    image_name varchar(64) not null,
    image_num int not null,
    info_id int not null,
    choose text null,
    choose_time decimal(10,2) null,
    opinion text null,
    opinion_time decimal(10,2) null
);

create table patient
(
    id int auto_increment
        primary key,
    dataset_id int not null,
    image_name varchar(64) not null,
    image_num int not null,
    info_id int not null
);

create table patient_info
(
    id int auto_increment
        primary key,
    dataset_id int not null,
    patient_id varchar(20) not null,
    gender tinyint null,
    birth datetime null,
    check_time datetime null,
    age decimal(5,2) null,
    bmi decimal(5,2) null,
    systolic_blood_pressure int null comment '收缩压',
    diastolic_pressure int null comment '舒张压',
    heart_rate int null comment '心率',
    smoke text null,
    drink text null,
    blood_pressure_history text null,
    circulatory_history text null,
    family_history text null,
    eating_habits text null,
    movement text null,
    endocrine_system_history text null,
    serum_triglycerides decimal(5,2) null,
    cholesterol decimal(5,2) null,
    hdl decimal(5,2) null,
    ldl decimal(5,2) null,
    egfr decimal(6,3) null,
    fasting_serum_glucose decimal(5,2) null,
    glycated_hemoglobin decimal(5,1) null,
    aspartate_aminotransferase decimal(5,1) null,
    alanine_aminotransferase decimal(5,1) null,
    ggt int null,
    blood_sugar decimal(5,2) null,
    gpt text null
);

