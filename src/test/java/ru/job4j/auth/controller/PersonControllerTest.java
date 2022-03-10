package ru.job4j.auth.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import ru.job4j.auth.Main;
import ru.job4j.auth.domain.Person;
import ru.job4j.auth.repository.PersonRepository;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = Main.class)
@AutoConfigureMockMvc
public class PersonControllerTest {
    private static final Gson GSON = new GsonBuilder().create();

    @Autowired
    PersonRepository persons;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void whenFindAll() throws Exception {
        Iterable<Person> allPersons = persons.findAll();
        String rsl = GSON.toJson(allPersons);
        this.mockMvc.perform(get("/person/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(rsl));
    }

    @Test
    public void whenFindById() throws Exception {
        Person person = persons.findById(1).get();
        String rsl = GSON.toJson(person);
        this.mockMvc.perform(get("/person/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(rsl));
    }

    @Test
    public void whenUpdatePerson() throws Exception {
        Person person = new Person(1, "updatedUser", "123");
        String json = GSON.toJson(person);
        this.mockMvc.perform(post("/person/")
                        .contentType("application/json").content(json))
                .andDo(print())
                .andExpect(status().isCreated());
        this.mockMvc.perform(get("/person/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(json));

    }

    @Test
    public void whenAddPerson() throws Exception {
        Person person = new Person("vasya", "123");
        String json = GSON.toJson(person);
        this.mockMvc.perform(post("/person/")
                        .contentType("application/json").content(json))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    public void whenDeletePerson() throws Exception {
        this.mockMvc.perform(delete("/person/4"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}