package cl.duoc.backend_api.controller;

import cl.duoc.backend_api.dto.StockRequestDTO;
import cl.duoc.backend_api.dto.StockResponseDTO;
import cl.duoc.backend_api.model.Producto;
import cl.duoc.backend_api.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping
@Tag(name = "Inventario y Productos", description = "Endpoints para la gestión del inventario y control de stock")
public class ProductoController {

    @Autowired
    private ProductoService service;

    @Operation(summary = "Listar todos los productos", description = "Obtiene una lista completa de los productos registrados en el inventario.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de productos obtenida exitosamente")
    })
    @GetMapping("/productos")
    public List<Producto> listar() {
        return service.listarTodos();
    }

    @Operation(summary = "Crear un nuevo producto", description = "Registra un nuevo producto en la base de datos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    @PostMapping("/productos")
    public ResponseEntity<Producto> crear(@Valid @RequestBody Producto producto) {
        return ResponseEntity.ok(service.guardar(producto));
    }

    @Operation(summary = "Actualizar un producto", description = "Modifica los datos de un producto existente buscándolo por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @PutMapping("/productos/{id}")
    public ResponseEntity<Producto> actualizar(@PathVariable Long id, @Valid @RequestBody Producto productoDetalles) {
        return ResponseEntity.ok(service.actualizar(id, productoDetalles));
    }

    @Operation(summary = "Eliminar un producto", description = "Elimina físicamente un producto de la base de datos según su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Producto eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @DeleteMapping("/productos/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Obtener producto por ID", description = "Busca y retorna los detalles de un producto específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto encontrado"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @GetMapping("/productos/{id}")
    public ResponseEntity<Producto> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @Operation(summary = "Descontar stock (Integración)", description = "Endpoint interno utilizado por otros microservicios para reducir el stock al confirmar una compra.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stock descontado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Stock insuficiente o producto no encontrado")
    })
    @PostMapping("/api/inventario/descontar")
    public ResponseEntity<StockResponseDTO> descontar(@Valid @RequestBody StockRequestDTO request) {
        StockResponseDTO respuesta = service.descontarStock(request);
        
        if (!respuesta.isConfirmado()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
        }
        
        return ResponseEntity.ok(respuesta);
    }
}