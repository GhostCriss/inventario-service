package cl.duoc.backend_api.repository;

import cl.duoc.backend_api.model.Producto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) 
public class ProductoRepositoryTest {

    @Autowired
    private ProductoRepository repository;

    @Test
    @DisplayName("Debe guardar un producto en la base de datos")
    void guardarProductoTest() {
        Producto p = new Producto(null, "TEST-123", "Teclado", "Periféricos", 50.0, 20, true);
        Producto guardado = repository.save(p);

        assertNotNull(guardado.getId());
        assertEquals("TEST-123", guardado.getCodigoProducto());
    }

    @Test
    @DisplayName("Debe encontrar un producto por su ID")
    void buscarPorIdTest() {
        Producto p = new Producto(null, "TEST-456", "Mouse", "Periféricos", 30.0, 15, true);
        Producto guardado = repository.save(p);

        Optional<Producto> encontrado = repository.findById(guardado.getId());

        assertTrue(encontrado.isPresent());
        assertEquals("Mouse", encontrado.get().getNombre());
    }

    @Test
    @DisplayName("Debe listar todos los productos")
    void listarTodosTest() {
        Producto p1 = new Producto(null, "TEST-789", "Monitor", "Periféricos", 150.0, 10, true);
        repository.save(p1);

        List<Producto> lista = repository.findAll();

        assertFalse(lista.isEmpty());
    }
}