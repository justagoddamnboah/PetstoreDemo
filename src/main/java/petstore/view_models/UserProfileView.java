package petstore.view_models;

import petstore.models.entities.Pet;
import petstore.models.entities.Role;
import java.util.List;

public class UserProfileView {
    private String username;
    private String email;
    private int age;
    private List<Role> roles;
    private List<Pet> petsInCart;

    public UserProfileView() {
    }

    public UserProfileView(String username, String email, int age,
                           List<Role> roles, List<Pet> petsInCart) {
        this.username = username;
        this.email = email;
        this.age = age;
        this.roles = roles;
        this.petsInCart = petsInCart;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }

    public List<Role> getRoles() {
        return roles;
    }
    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public List<Pet> getPetsInCart() {
        return petsInCart;
    }
    public void setPetsInCart(List<Pet> petsInCart) {
        this.petsInCart = petsInCart;
    }
}