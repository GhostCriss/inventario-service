package cl.duoc.backend_api.controller;

import cl.duoc.backend_api.model.Producto;
import cl.duoc.backend_api.service.ProductoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(MockitoExtension.class)
public class ProductoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProductoService service;

    @InjectMocks
    private ProductoController controller;

    @BeforeEach
    void setUp() {
        // Configuramos el entorno falso para peticiones web
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("Debe retornar 200 OK y la lista de productos")
    void listarProductosTest() throws Exception {
        Producto p = new Producto(1L, "PROD-01", "PC Gamer", "Tech", 1000.0, 5, true);
        when(service.listarTodos()).thenReturn(Arrays.asList(p));

        mockMvc.perform(get("/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("PC Gamer"));
    }

    @Test
    @DisplayName("Debe retornar 404 si la ruta no existe")
    void rutaInvalidaTest() throws Exception {
        mockMvc.perform(get("/ruta-inventada-que-no-existe"))
                .andExpect(status().isNotFound());
    }
    
    @Test
    @DisplayName("Debe retornar 200 OK y una lista vacía si no hay registros")
    void listarProductosVacioTest() throws Exception {
        when(service.listarTodos()).thenReturn(java.util.Collections.emptyList());

        mockMvc.perform(get("/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }
}