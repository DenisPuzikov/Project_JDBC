package ru.puzikovAston.dao;


import org.springframework.stereotype.Component;
import ru.puzikovAston.models.Person;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class PersonDAO {
    /**
     * For connection to DB we should notice URL, USERNAME and Password.
     * Normally this information we notice in properties file.
     */

    private static final String URL= "jdbc:postgresql://localhost:5432/first_db";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "dizonly";

    /**
     * Создаем connection и подгружаем файл с JDBC драйвером.
     */
    private static Connection connection;
     static {
         try {
             Class.forName("org.postgresql.Driver");
         } catch (ClassNotFoundException e) {
             throw new RuntimeException(e);
         }
         try {
             connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
         } catch (SQLException e) {
             throw new RuntimeException(e);
         }
     }

    public List<Person> index() {
        List<Person> people = new ArrayList<>();
        /**
         * Объект statement содержит в себе SQL запрос к БД.
         * Создем тело запроса и затем выполняем его на нашем объекте.
         * ExecuteQuery return us resultSet.
         */
        try {
            Statement statement = connection.createStatement();
            String SQL = "select * from Person";
            ResultSet resultSet = statement.executeQuery(SQL);

            while (resultSet.next()){
                Person person = new Person();
                person.setId(resultSet.getInt("id"));
                person.setName(resultSet.getString("name"));
                person.setAge(resultSet.getInt("age"));
                person.setEmail(resultSet.getString("email"));

                people.add(person);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return people;
    }

        /**
         * Используем PreparedStatement для избежания SQl injections и более быстрой работой с БД
         *
         */

    public void save(Person person) {


        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("insert into Person values (1, ?, ?, ?)");

            preparedStatement.setString(1, person.getName());
            preparedStatement.setInt(2, person.getAge());
            preparedStatement.setString(3, person.getEmail());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Person show(int id) {
         Person person = null;
        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("SELECT * from Person where id=?");

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.next();

            person = new Person();
            person.setId(resultSet.getInt("id"));
            person.setName(resultSet.getString("name"));
            person.setEmail(resultSet.getString("email"));
            person.setAge(resultSet.getInt("age"));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return person;
    }


    public void update(int id, Person updatedPerson) {

        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("update Person set name=?, age=?, email=? where id=?");

            preparedStatement.setString(1, updatedPerson.getName());
            preparedStatement.setInt(2, updatedPerson.getAge());
            preparedStatement.setString(3, updatedPerson.getEmail());
            preparedStatement.setInt(4, id);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void delete(int id) {
        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("delete from Person where id=?");
            preparedStatement.setInt(1,id);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
