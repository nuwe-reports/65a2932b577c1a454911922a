
package com.example.demo;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.assertj.core.api.Assertions.assertThat;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import com.example.demo.controllers.*;
import com.example.demo.repositories.*;
import com.example.demo.entities.*;
import com.fasterxml.jackson.databind.ObjectMapper;


@WebMvcTest(DoctorController.class)
class DoctorControllerUnitTest {

    @MockBean
    private DoctorRepository doctorRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetDoctorsNonEmpty() throws Exception {

        Doctor doctor1 = new Doctor("Juan", "Carlos", 34, "j.carlos@hospital.accwe");
        Doctor doctor2 = new Doctor("Cornelio", "Andrea", 59, "c.andrea@hospital.accwe");
        List<Doctor> doctors = new ArrayList<>();
        doctors.add(doctor1);
        doctors.add(doctor2);

        when(doctorRepository.findAll()).thenReturn(doctors);

        mockMvc.perform(get("/api/doctors"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].firstName").value("Juan"))
                .andExpect(jsonPath("$[1].firstName").value("Cornelio"))
                .andExpect(jsonPath("$[0].lastName").value("Carlos"))
                .andExpect(jsonPath("$[1].lastName").value("Andrea"))
                .andExpect(jsonPath("$[0].age").value(34))
                .andExpect(jsonPath("$[1].age").value(59))
                .andExpect(jsonPath("$[0].email").value("j.carlos@hospital.accwe"))
                .andExpect(jsonPath("$[1].email").value("c.andrea@hospital.accwe"));
    }

    @Test
    void testGetDoctorsEmpty() throws Exception {
        List<Doctor> doctors = new ArrayList<>();
        when(doctorRepository.findAll()).thenReturn(doctors);
        mockMvc.perform(get("/api/doctors"))
                .andExpect(status().isNoContent()).andExpect(result -> {
                    if (result.getResponse().getContentAsString().isEmpty()) {
                        return;
                    }
                    throw new AssertionError("Content type should not be set for empty response");
                }).andExpect(jsonPath("$.length()").doesNotExist());
    }

    @Test
    void testGetDoctorById() throws Exception {
        Doctor searchDoctor = new Doctor("Juan", "Carlos", 34, "j.carlos@hospital.accwe");
        when(doctorRepository.findById(searchDoctor.getId())).thenReturn(Optional.of(searchDoctor));

        mockMvc.perform(get("/api/doctors/{id}", searchDoctor.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Juan"))
                .andExpect(jsonPath("$.lastName").value("Carlos"))
                .andExpect(jsonPath("$.age").value(34))
                .andExpect(jsonPath("$.email").value("j.carlos@hospital.accwe"));

    }

    @Test
    void testGetDoctorByIdIsNotPresent() throws Exception {
        Doctor searchDoctor = new Doctor("Juan", "Carlos", 34, "j.carlos@hospital.accwe");
        if (!doctorRepository.findById(searchDoctor.getId()).isPresent()) {
            mockMvc.perform(get("/api/doctors/{id}", searchDoctor.getId()))
                    .andExpect(status().isNotFound());
        }
    }

    @Test
    void testCreateDoctor() throws Exception {
        Doctor newDoctor = new Doctor("Juan", "Carlos", 34, "j.carlos@hospital.accwe");
        when(doctorRepository.save(any(Doctor.class))).thenReturn(newDoctor);
        mockMvc.perform(post("/api/doctor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newDoctor)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName").value("Juan"))
                .andExpect(jsonPath("$.lastName").value("Carlos"))
                .andExpect(jsonPath("$.age").value(34))
                .andExpect(jsonPath("$.email").value("j.carlos@hospital.accwe"));
    }


    @Test
    void testDeleteDoctor() throws Exception {
        Optional<Doctor> doctor = Optional.of(new Doctor("Juan", "Carlos", 34, "j.carlos@hospital.accwe"));
        assertThat(doctor).isPresent();
        when(doctorRepository.findById(doctor.get().getId())).thenReturn(doctor);
        mockMvc.perform(delete("/api/doctors/" + doctor.get().getId()))
                .andExpect(status().is(HttpStatus.OK.value()));
    }

    @Test
    void testDeleteDoctorIsNotPresent() throws Exception {
        Doctor doctor = new Doctor("Juan", "Carlos", 34, "j.carlos@hospital.accwe");
        if (!doctorRepository.findById(doctor.getId()).isPresent()) {
            mockMvc.perform(delete("/api/doctors/{id}", doctor.getId()))
                    .andExpect(status().isNotFound());
        }
    }

    @Test
    void testDeleteAllDoctors() throws Exception {
        mockMvc.perform(delete("/api/doctors"))
                .andExpect(status().isOk());
    }
}


@WebMvcTest(PatientController.class)
class PatientControllerUnitTest {

    @MockBean
    private PatientRepository patientRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetPatientsNonEmpty() throws Exception {
        Patient patient1 = new Patient("Juan", "Carlos", 34, "j.carlos@hospital.accwe");
        Patient patient2 = new Patient("Cornelio", "Andrea", 59, "c.andrea@hospital.accwe");
        List<Patient> patients = new ArrayList<>();
        patients.add(patient1);
        patients.add(patient2);
        when(patientRepository.findAll()).thenReturn(patients);
        mockMvc.perform(get("/api/patients"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].firstName").value("Juan"))
                .andExpect(jsonPath("$[1].firstName").value("Cornelio"))
                .andExpect(jsonPath("$[0].lastName").value("Carlos"))
                .andExpect(jsonPath("$[1].lastName").value("Andrea"))
                .andExpect(jsonPath("$[0].age").value(34))
                .andExpect(jsonPath("$[1].age").value(59))
                .andExpect(jsonPath("$[0].email").value("j.carlos@hospital.accwe"))
                .andExpect(jsonPath("$[1].email").value("c.andrea@hospital.accwe"));
    }

    @Test
    void testGetPatientsEmpty() throws Exception {
        List<Patient> patients = new ArrayList<>();
        when(patientRepository.findAll()).thenReturn(patients);
        mockMvc.perform(get("/api/patients")).andExpect(status().isNoContent()).andExpect(result -> {
            if (result.getResponse().getContentAsString().isEmpty()) {
                return;
            }
            throw new AssertionError("Content type should not be set for empty response");
        }).andExpect(jsonPath("$.length()").doesNotExist());
    }

    @Test
    void testGetPatientById() throws Exception {
        Patient searchPatient = new Patient("Juan", "Carlos", 34, "j.carlos@hospital.accwe");
        when(patientRepository.findById(searchPatient.getId())).thenReturn(Optional.of(searchPatient));

        mockMvc.perform(get("/api/patients/{id}", searchPatient.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Juan"))
                .andExpect(jsonPath("$.lastName").value("Carlos"))
                .andExpect(jsonPath("$.age").value(34))
                .andExpect(jsonPath("$.email").value("j.carlos@hospital.accwe"));

    }

    @Test
    void testGetPatientByIdIsNotPresent() throws Exception {
        Patient searchPatient = new Patient("Juan", "Carlos", 34, "j.carlos@hospital.accwe");
        if (!patientRepository.findById(searchPatient.getId()).isPresent()) {
            mockMvc.perform(get("/api/patients/{id}", searchPatient.getId()))
                    .andExpect(status().isNotFound());
        }
    }

    @Test
    void testCreatePatient() throws Exception {
        Patient newPatient = new Patient("Juan", "Carlos", 34, "j.carlos@hospital.accwe");
        when(patientRepository.save(any(Patient.class))).thenReturn(newPatient);
        mockMvc.perform(post("/api/patient")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPatient)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName").value("Juan"))
                .andExpect(jsonPath("$.lastName").value("Carlos"))
                .andExpect(jsonPath("$.age").value(34))
                .andExpect(jsonPath("$.email").value("j.carlos@hospital.accwe"));
    }

    @Test
    void testDeletePatient() throws Exception {
        Optional<Patient> patient = Optional.of(new Patient("Juan", "Carlos", 34, "j.carlos@hospital.accwe"));
        assertThat(patient).isPresent();
        when(patientRepository.findById(patient.get().getId())).thenReturn(patient);
        mockMvc.perform(delete("/api/patients/{id}", patient.get().getId()))
                .andExpect(status().is(HttpStatus.OK.value()));
    }

    @Test
    void testDeletePatientIsNotPresent() throws Exception {
        Patient patient = new Patient("Juan", "Carlos", 34, "j.carlos@hospital.accwe");
        if (!patientRepository.findById(patient.getId()).isPresent()) {
            mockMvc.perform(delete("/api/patients/{id}", patient.getId()))
                    .andExpect(status().isNotFound());
        }
    }

    @Test
    void testDeleteAllPatients() throws Exception {
        mockMvc.perform(delete("/api/patients"))
                .andExpect(status().isOk());
    }
}

@WebMvcTest(RoomController.class)
class RoomControllerUnitTest {

    @MockBean
    private RoomRepository roomRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetRoomsNonEmpty() throws Exception {
        Room room1 = new Room("Dermatology");
        Room room2 = new Room("Operations");
        List<Room> rooms = new ArrayList<>();
        rooms.add(room1);
        rooms.add(room2);
        when(roomRepository.findAll()).thenReturn(rooms);
        mockMvc.perform(get("/api/rooms"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].roomName").value("Dermatology"))
                .andExpect(jsonPath("$[1].roomName").value("Operations"));
    }

    @Test
    void testGetRoomsEmpty() throws Exception {
        List<Room> rooms = new ArrayList<>();
        when(roomRepository.findAll()).thenReturn(rooms);
        mockMvc.perform(get("/api/rooms")).andExpect(status().isNoContent()).andExpect(result -> {
            if (result.getResponse().getContentAsString().isEmpty()) {
                return;
            }
            throw new AssertionError("Content type should not be set for empty response");
        }).andExpect(jsonPath("$.length()").doesNotExist());
    }

    @Test
    void testGetRoomByRoomName() throws Exception {
        Room searchRoom = new Room("Dermatology");
        when(roomRepository.findByRoomName(searchRoom.getRoomName())).thenReturn(Optional.of(searchRoom));
        mockMvc.perform(get("/api/rooms/{roomName}", searchRoom.getRoomName()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roomName").value("Dermatology"));
    }

    @Test
    void testGetRoomByRoomNameIsNotPresent() throws Exception {
        Room searchRoom = new Room("Dermatology");
        if (!roomRepository.findByRoomName(searchRoom.getRoomName()).isPresent()) {
            mockMvc.perform(get("/api/rooms/{roomName}", searchRoom.getRoomName()))
                    .andExpect(status().isNotFound());
        }
    }

    @Test
    void testCreateRoom() throws Exception {
        Room newRoom = new Room("Dermatology");
        when(roomRepository.save(any(Room.class))).thenReturn(newRoom);
        mockMvc.perform(post("/api/room")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newRoom)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.roomName").value("Dermatology"));
    }

    @Test
    void testDeleteRoom() throws Exception {
        Optional<Room> room = Optional.of(new Room("Dermatology"));
        assertThat(room).isPresent();
        when(roomRepository.findByRoomName(room.get().getRoomName())).thenReturn(room);
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/rooms/{roomName}", room.get().getRoomName()))
                .andExpect(status().is(HttpStatus.OK.value()));
    }

    @Test
    void testDeleteRoomIsNotPresent() throws Exception {
        Room room = new Room("Dermatology");
        if (!roomRepository.findByRoomName(room.getRoomName()).isPresent()) {
            mockMvc.perform(delete("/api/rooms/{roomName}", room.getRoomName()))
                    .andExpect(status().isNotFound());
        }
    }

    @Test
    void testDeleteAllRooms() throws Exception {
        mockMvc.perform(delete("/api/rooms"))
                .andExpect(status().isOk());
    }

}
