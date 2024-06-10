alter table patient
    drop foreign key fk_med_id;

alter table medicine
    drop foreign key fk_buyer_of_med;

drop table medicine;
drop table patient;
drop table hospital_staff;
drop table super_admin;

drop database Hospital;