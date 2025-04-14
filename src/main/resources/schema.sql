create table department(
    department_id INT AUTO_INCREMENT PRIMARY KEY,
    department_name VARCHAR(30) NOT NULL
);

create table doctor(
    doctor_id INT AUTO_INCREMENT PRIMARY KEY,
    doctor_name VARCHAR(50) NOT NULL,
    doctor_email VARCHAR(225),
    doctor_phone VARCHAR(15),
    doctor_gender VARCHAR(10),
    experience INT,
    department_id INT,
    FOREIGN KEY (department_id) REFERENCES department(department_id)
);

create table patient(
    patient_id INT AUTO_INCREMENT PRIMARY KEY,
    patient_name VARCHAR(225) NOT NULL,
    patient_gender VARCHAR(10),
    patient_phone VARCHAR(15),
    patient_email VARCHAR(225)
);

create table appointment(
    appointment_id INT AUTO_INCREMENT PRIMARY KEY,
    appointment_date DATE NOT NULL,
    doctor_id INT,
    FOREIGN KEY (doctor_id) REFERENCES doctor(doctor_id)

);

create table appointment_patient(
    appointment_id INT,
    patient_id INT,
    PRIMARY KEY (appointment_id, patient_id),
    FOREIGN KEY (appointment_id) REFERENCES appointment(appointment_id),
    FOREIGN KEY (patient_id) REFERENCES patient(patient_id)
);