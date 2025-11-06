package ec.edu.monster.controller;

public class LoginController {

    private final String validUser = "MONSTER";
    private final String validPass = "MONSTER9";

    public boolean validate(String user, String pass){
        return user.equals(validUser) && pass.equals(validPass);
    }
}