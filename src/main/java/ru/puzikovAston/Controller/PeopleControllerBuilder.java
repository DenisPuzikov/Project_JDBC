package ru.puzikovAston.Controller;

import ru.puzikovAston.dao.PersonDAO;

public class PeopleControllerBuilder {
    private PersonDAO personDAO;

    public PeopleControllerBuilder setPersonDAO(PersonDAO personDAO) {
        this.personDAO = personDAO;
        return this;
    }

    public PeopleController createPeopleController() {
        return new PeopleController(personDAO);
    }
}