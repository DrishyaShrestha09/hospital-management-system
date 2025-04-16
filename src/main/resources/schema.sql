create table department(
    department_id INT AUTO_INCREMENT PRIMARY KEY,
    department_name VARCHAR(30) NOT NULL
);

create table user(
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    user_name VARCHAR(50) NOT NULL,
    user_email VARCHAR(225),
    user_phone VARCHAR(15),
    user_gender VARCHAR(15),
    role ENUM('doctor', 'patient', 'admin') NOT NULL
);

create table doctor(
    doctor_id INT AUTO_INCREMENT PRIMARY KEY,
    experience INT,
    department_id INT,
    FOREIGN KEY (department_id) REFERENCES department(department_id)
);

create table doctor_user(
    doctor_id INT UNIQUE,
    user_id INT UNIQUE,
    PRIMARY KEY (doctor_id, user_id),
    FOREIGN KEY (doctor_id) REFERENCES doctor(doctor_id),
    FOREIGN KEY (user_id) REFERENCES user(user_id)
);

create table patient(
    patient_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT UNIQUE,
    FOREIGN KEY (user_id) REFERENCES user(user_id)
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