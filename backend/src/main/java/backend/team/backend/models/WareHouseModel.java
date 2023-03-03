package backend.team.backend.models;

import javax.persistence.*;

@Entity
@Table(name="bodega")
public class WareHouseModel {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;
    @Column(name="nombre")
    private String  nombre;
    @Column(name="capacidad")
    private String capacidad;

    public WareHouseModel() {
    }

    public WareHouseModel(String nombre, String capacidad) {
        this.nombre = nombre;
        this.capacidad = capacidad;
    }

    public Integer getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCapacidad() {
        return capacidad;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCapacidad(String capacidad) {
        this.capacidad = capacidad;
    }
}
