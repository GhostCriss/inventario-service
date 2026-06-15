package cl.duoc.backend_api.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductoTest {

    @Test
    @DisplayName("Debe instanciar Producto con constructor vacío y mantener campo activo en true")
    void debeCrearProductoConConstructorVacio() {
        // Arrange & Act
        Producto producto = new Producto();

        // Assert
        assertNull(producto.getId());
        assertTrue(producto.isActivo());
    }

    @Test
    @DisplayName("Debe asignar y recuperar los valores correctamente a través de getters y setters")
    void debeAsignarYRecuperarValores() {
        // Arrange
        Producto producto = new Producto();

        // Act
        producto.setCodigoProducto("PROD-001");
        producto.setNombre("Tarjeta Gráfica");
        producto.setPrecio(250000.0);
        producto.setStock(15);

        // Assert
        assertEquals("PROD-001", producto.getCodigoProducto());
        assertEquals("Tarjeta Gráfica", producto.getNombre());
        assertEquals(250000.0, producto.getPrecio());
        assertEquals(15, producto.getStock());
    }

    @Test
    @DisplayName("Debe instanciar Producto correctamente utilizando el constructor con todos los argumentos")
    void debeCrearProductoConConstructorCompleto() {
        // Arrange & Act
        Producto producto = new Producto(1L, "PROD-002", "Monitor", "Periféricos", 150000.0, 10, false);

        // Assert
        assertEquals(1L, producto.getId());
        assertEquals("PROD-002", producto.getCodigoProducto());
        assertFalse(producto.isActivo());
    }
}
