package cl.duoc.backend_api.service;

import cl.duoc.backend_api.model.Producto;
import cl.duoc.backend_api.repository.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductoServiceTest {

    @Mock
    private ProductoRepository repository;

    @InjectMocks
    private ProductoService service;

    private Producto productoPrueba;

    @BeforeEach
    void setUp() {
        // Inicializamos un producto de prueba para usarlo en los tests
        productoPrueba = new Producto(1L, "PROD-01", "Laptop Test", "Tecnología", 500000.0, 10, true);
    }

    @Test
    void listarTodosTest() {
        // Simulamos que el repositorio devuelve una lista con nuestro producto
        when(repository.findAll()).thenReturn(Arrays.asList(productoPrueba));

        // Ejecutamos el método del servicio
        List<Producto> resultado = service.listarTodos();

        // Validamos que no venga vacío y traiga los datos correctos
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals("Laptop Test", resultado.get(0).getNombre());
    }

    @Test
    void guardarTest() {
        // Simulamos el guardado en base de datos
        when(repository.save(any(Producto.class))).thenReturn(productoPrueba);

        // Ejecutamos el servicio
        Producto resultado = service.guardar(productoPrueba);

        // Validamos que el producto retornado sea el correcto
        assertNotNull(resultado);
        assertEquals("PROD-01", resultado.getCodigoProducto());
        assertEquals(10, resultado.getStock());
    }

    @Test
    @org.junit.jupiter.api.DisplayName("Debe retornar lista vacía cuando no hay productos")
    void listarTodosVacioTest() {
        // Arrange: Simulamos que el repositorio no tiene productos guardados
        when(repository.findAll()).thenReturn(java.util.Collections.emptyList());

        // Act: Ejecutamos el servicio
        List<Producto> resultado = service.listarTodos();

        // Assert: Validamos que la lista retorne correctamente pero vacía
        assertTrue(resultado.isEmpty());
    }
}