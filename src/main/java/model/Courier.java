package model;

public class Courier {
    private String login;
    private String password;
    private String firstName;
    private Integer id;  // Добавляем поле id

    public Courier(String login, String password, String firstName) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;

    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public Integer getId() {  // Добавляем метод getId()
        return id;
    }

    public void setId(Integer id) {  // Добавляем setId(), чтобы устанавливать id после логина
        this.id = id;
    }

    @Override
    public String toString() {
        return "Courier{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", id=" + id +
                '}';
    }
}
