create table department(
    department_id INT AUTO_INCREMENT PRIMARY KEY,
    department_name VARCHAR(30) NOT NULL
);

create table users(
    users_id INT AUTO_INCREMENT PRIMARY KEY,
    users_name VARCHAR(50) NOT NULL,
    users_email VARCHAR(225),
    users_phone VARCHAR(15),
    users_gender VARCHAR(15),
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
    users_id INT UNIQUE,
    PRIMARY KEY (doctor_id, users_id),
    FOREIGN KEY (doctor_id) REFERENCES doctor(doctor_id),
    FOREIGN KEY (users_id) REFERENCES users(users_id)
);

create table patient(
    patient_id INT AUTO_INCREMENT PRIMARY KEY,
    users_id INT UNIQUE,
    FOREIGN KEY (users_id) REFERENCES users(users_id)
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