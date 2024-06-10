create database Hospital;
use Hospital;


create table patient(
    patient_name varchar(30) not null,
    patient_id varchar(30) not null,
    med_id varchar(30),
    constraint pk_patient PRIMARY KEY (patient_id)
);

create table medicine(
    med_id varchar(30) not null,
    med_name varchar(30) not null,
    which_disease varchar(30) not null,
    med_year integer not null,
    buyer_of_med varchar(30),
    constraint pk_medicine PRIMARY KEY (med_id)
);

create table hospital_staff(
    hospital_staff_id varchar(30) not null,
    hospital_staff_name varchar(30) not null,
    hospital_staff_password varchar(30) not null,
    constraint pk_hospital_staff PRIMARY KEY (hospital_staff_id)
);

create table super_admin(
    super_admin_id varchar(30) not null,
    super_admin_name varchar(30) not null,
    super_admin_password varchar(30) not null,
    constraint pk_super_admin PRIMARY KEY (super_admin_id)
); 