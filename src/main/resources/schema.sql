-- Department Table
CREATE TABLE IF NOT EXISTS department(
                                         department_id INT AUTO_INCREMENT PRIMARY KEY,
                                         department_name VARCHAR(30) NOT NULL
);

-- User Table
CREATE TABLE IF NOT EXISTS users(
                                    user_id INT AUTO_INCREMENT PRIMARY KEY,
                                    user_name VARCHAR(200) NOT NULL,
                                    user_email VARCHAR(225) UNIQUE NOT NULL,
                                    user_password VARCHAR(225) NOT NULL,
                                    user_phone VARCHAR(15),
                                    user_address VARCHAR(225),
                                    user_gender VARCHAR(15),
                                    role ENUM('doctor', 'patient', 'admin') NOT NULL,
                                    profile LONGBLOB
);

-- Doctor Table
CREATE TABLE IF NOT EXISTS doctor(
                                     doctor_id INT AUTO_INCREMENT PRIMARY KEY,
                                     experience INT,
                                     specialty VARCHAR(100),
                                     department_id INT,
                                     user_id INT,
                                     FOREIGN KEY (department_id) REFERENCES department(department_id),
                                     FOREIGN KEY (user_id) REFERENCES users(user_id)
);



-- Patient Table
CREATE TABLE IF NOT EXISTS patient(
                                      patient_id INT AUTO_INCREMENT PRIMARY KEY,
                                      user_id INT UNIQUE,
                                      FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- Appointment Table
CREATE TABLE IF NOT EXISTS appointment(
                                          appointment_id INT AUTO_INCREMENT PRIMARY KEY,
                                          appointment_date DATE NOT NULL,
                                          cause VARCHAR(225),
                                          doctor_id INT,
                                          patient_id INT,
                                          time_slot VARCHAR(255),
                                          FOREIGN KEY (doctor_id) REFERENCES doctor(doctor_id),
                                          FOREIGN KEY (patient_id) REFERENCES patient(patient_id)
);

-- Create patient_medical_info table
CREATE TABLE IF NOT EXISTS patient_medical_info (
                                                    medical_info_id INT AUTO_INCREMENT PRIMARY KEY,
                                                    patient_id INT NOT NULL,
                                                    date_of_birth DATE,
                                                    blood_group VARCHAR(5),
                                                    emergency_contact VARCHAR(100),
                                                    allergies TEXT,
                                                    medical_conditions TEXT,
                                                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                                    FOREIGN KEY (patient_id) REFERENCES patient(patient_id)
);

-- Appointment Status Table
CREATE TABLE IF NOT EXISTS appointment_status (
                                                  status_id INT AUTO_INCREMENT PRIMARY KEY,
                                                  appointment_id INT NOT NULL UNIQUE,
                                                  status ENUM('confirmed', 'cancelled', 'completed') NOT NULL,
                                                  updated_by INT NOT NULL,
                                                  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                                  notes TEXT,
                                                  FOREIGN KEY (appointment_id) REFERENCES appointment(appointment_id) ON DELETE CASCADE,
                                                  FOREIGN KEY (updated_by) REFERENCES users(user_id)
);