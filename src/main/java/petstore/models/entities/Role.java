package petstore.models.entities;

import jakarta.persistence.*;
import petstore.models.enums.UserRoles;

@Entity
@Table(name = "roles")
public class Role {
    @Id
    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false)
    private UserRoles name;

    public Role() {

    }

    public Role(UserRoles name) {
        this.name = name;
    }

    public UserRoles getName() {
        return name;
    }

    public void setName(UserRoles name) {
        this.name = name;
    }
}