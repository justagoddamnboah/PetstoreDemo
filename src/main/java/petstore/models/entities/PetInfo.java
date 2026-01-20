package petstore.models.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "pet_info")
public class PetInfo {
    @Id
    @Column(name = "photo_name", nullable = true, unique = true)
    private String photoName;

    @Column(name = "description", nullable = true)
    private String description;

    @OneToOne
    @JoinColumn(name = "pets_id", nullable = false)
    private Pet pet;

    public String getPhotoName() {
        return photoName;
    }
    public void setPhotoName(String photoName) {
        this.photoName = photoName;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public Pet getPet() {
        return pet;
    }
    public void setPet(Pet pet) {
        this.pet = pet;
    }
}