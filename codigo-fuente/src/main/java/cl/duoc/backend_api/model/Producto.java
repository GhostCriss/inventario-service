    package cl.duoc.backend_api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "productos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, name = "codigo_producto")
    private String codigoProducto;

    @Column(nullable = false)
    private String nombre;

    private String categoria;

    @Column(nullable = false)
    private double precio;

    @Column(nullable = false)
    private int stock;

    private boolean activo = true;
}