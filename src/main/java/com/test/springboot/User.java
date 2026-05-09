package com.test.springboot;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Boolean loggedInFlag = false;

    private LocalDateTime loginTime;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("user")
    private List<Calculations> savedCalc;

    public User() {
    }

    public User(Integer id, String email, String password, List<Calculations> savedCalc) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.savedCalc = savedCalc;
    }

    public Integer getId() {
        return id;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Calculations> getSavedCalc() {
        return savedCalc;
    }

    public Boolean getLoggedInFlag() {
        return loggedInFlag;
    }

    public void setLoggedInFlag(Boolean loggedInFlag) {
        this.loggedInFlag = loggedInFlag;
    }

    public LocalDateTime getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(LocalDateTime loginTime) {
        this.loginTime = loginTime;
    }

    public void addCalc(Calculations calculations){
        this.savedCalc.add(calculations);

        calculations.setUser(this);
    }
}
