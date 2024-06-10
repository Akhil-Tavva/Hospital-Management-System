alter table patient
    add constraint fk_med_id FOREIGN KEY (med_id) REFERENCES medicine(med_id);

alter table medicine
    add constraint fk_buyer_of_med FOREIGN KEY (buyer_of_med) REFERENCES patient(patient_id);