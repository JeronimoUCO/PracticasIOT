package backend.team.backend.dto;

public class WarehouseDTO {
    private String nombre;
    private Integer capacidad;

    public WarehouseDTO(){
    }

    public WarehouseDTO(String nombre, Integer capacidad) {
        this.nombre = nombre;
        this.capacidad = capacidad;
    }

    public String getNombre() {
        return nombre;
    }

    public Integer getCapacidad() {
        return capacidad;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCapacidad(Integer capacidad) {
        this.capacidad = capacidad;
    }
}
