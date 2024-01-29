package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

import com.example.demo.entities.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
@TestInstance(Lifecycle.PER_CLASS)
class EntityUnitTest {

	@Autowired
	private TestEntityManager entityManager;

    @Test
    public void testDoctor() {
        Doctor doctor = new Doctor("Juan","Carlos", 34, "j.carlos@hospital.accwe");
        entityManager.persistAndFlush(doctor);
        Doctor retrievedDoctor = entityManager.find(Doctor.class, doctor.getId());
        assertThat(retrievedDoctor.getFirstName()).isEqualTo(doctor.getFirstName());
        assertThat(retrievedDoctor.getLastName()).isEqualTo(doctor.getLastName());
        assertThat(retrievedDoctor.getEmail()).isEqualTo(doctor.getEmail());
        assertThat(retrievedDoctor.getAge()).isEqualTo(doctor.getAge());
        assertThat(retrievedDoctor.getId()).isEqualTo(doctor.getId());
    }

    @Test
    public void testPatient() {
        Patient patient = new Patient("Cornelio","Andrea", 59, "c.andrea@hospital.accwe");
        entityManager.persistAndFlush(patient);
        Patient retrievedPatient = entityManager.find(Patient.class, patient.getId());
        assertThat(retrievedPatient.getFirstName()).isEqualTo(patient.getFirstName());
        assertThat(retrievedPatient.getLastName()).isEqualTo(patient.getLastName());
        assertThat(retrievedPatient.getEmail()).isEqualTo(patient.getEmail());
        assertThat(retrievedPatient.getAge()).isEqualTo(patient.getAge());
        assertThat(retrievedPatient.getId()).isEqualTo(patient.getId());
    }

    @Test
    public void testRoom() {
        Room room = new Room("Dermatology");
        entityManager.persistAndFlush(room);
        Room retrievedRoom = entityManager.find(Room.class, room.getRoomName());
        assertThat(retrievedRoom.getRoomName()).isEqualTo(room.getRoomName());
    }

    @Test
    public void testAppointment() {
        Patient patient = new Patient("Cornelio","Andrea", 59, "c.andrea@hospital.accwe");
        Doctor doctor = new Doctor("Juan","Carlos", 34, "j.carlos@hospital.accwe");
        Room room = new Room("Dermatology");
        LocalDateTime startsAt = LocalDateTime.now();
        LocalDateTime finishesAt = LocalDateTime.now();
        Appointment appointment = new Appointment(patient, doctor, room, startsAt, finishesAt);
        entityManager.persistAndFlush(appointment);
        Appointment retrievedAppointment = entityManager.find(Appointment.class, appointment.getId());
        assertThat(retrievedAppointment.getId()).isEqualTo(appointment.getId());
        assertThat(retrievedAppointment.getDoctor().getId()).isEqualTo(doctor.getId());
        assertThat(retrievedAppointment.getPatient().getId()).isEqualTo(patient.getId());
        assertThat(retrievedAppointment.getStartsAt()).isEqualTo(appointment.getStartsAt());
        assertThat(retrievedAppointment.getFinishesAt()).isEqualTo(appointment.getFinishesAt());
        assertThat(retrievedAppointment.getRoom().getRoomName()).isEqualTo(room.getRoomName());

    }
}



