package backend.team.backend.models;

import javax.persistence.*;

@Entity
@Table(name="paquete")
public class PackageModel {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    @ManyToOne //bien
    @JoinColumn(name = "idusersource")
    private UserModel userSource;
    @ManyToOne //bien
    @JoinColumn(name = "iduserdestination")
    private UserModel userDestination;
    @Column(name="description")
    private String description;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserModel getUserSource() {
        return userSource;
    }

    public void setUserSource(UserModel userSource) {
        this.userSource = userSource;
    }

    public UserModel getUserDestination() {
        return userDestination;
    }

    public void setUserDestination(UserModel userDestination) {
        this.userDestination = userDestination;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
