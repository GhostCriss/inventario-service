package cl.duoc.backend_api.controller;

import cl.duoc.backend_api.dto.StockRequestDTO;
import cl.duoc.backend_api.dto.StockResponseDTO;
import cl.duoc.backend_api.model.Producto;
import cl.duoc.backend_api.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping
public class ProductoController {

    @Autowired
    private ProductoService service;

    // Endpoints Administrativos Locales
    @GetMapping("/productos")
    public List<Producto> listar() {
        return service.listarTodos();
    }

    @PostMapping("/productos")
    public ResponseEntity<Producto> crear(@Valid @RequestBody Producto producto) {
        return ResponseEntity.ok(service.guardar(producto));
    }

    @PutMapping("/productos/{id}")
    public ResponseEntity<Producto> actualizar(@PathVariable Long id, @Valid @RequestBody Producto productoDetalles) {
        return ResponseEntity.ok(service.actualizar(id, productoDetalles));
    }

    @DeleteMapping("/productos/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/productos/{id}")
    public ResponseEntity<Producto> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    // NUEVO: Endpoint de integración para comunicación entre microservicios (Hito 2)
    @PostMapping("/api/inventario/descontar")
    public ResponseEntity<StockResponseDTO> descontar(@Valid @RequestBody StockRequestDTO request) {
        StockResponseDTO respuesta = service.descontarStock(request);
        
        if (!respuesta.isConfirmado()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
        }
        
        return ResponseEntity.ok(respuesta);
    }
}