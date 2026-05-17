package cl.duoc.backend_api.service;

import cl.duoc.backend_api.dto.ItemDescuentoDTO;
import cl.duoc.backend_api.dto.StockRequestDTO;
import cl.duoc.backend_api.dto.StockResponseDTO;
import cl.duoc.backend_api.model.Producto;
import cl.duoc.backend_api.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository repository;

    public List<Producto> listarTodos() {
        return repository.findAll();
    }

    public Producto guardar(Producto producto) {
        return repository.save(producto);
    }
    
    public void desactivarProducto(Long id) {
        repository.findById(id).ifPresent(p -> {
            p.setActivo(false);
            repository.save(p);
        });
    }

    public Producto actualizar(Long id, Producto detallesProducto) {
        Producto producto = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con el id: " + id));

        producto.setCodigoProducto(detallesProducto.getCodigoProducto());
        producto.setNombre(detallesProducto.getNombre());
        producto.setCategoria(detallesProducto.getCategoria());
        producto.setPrecio(detallesProducto.getPrecio());
        producto.setStock(detallesProducto.getStock());
        producto.setActivo(detallesProducto.isActivo());

        return repository.save(producto);
    }

    public void eliminar(Long id) {
        Producto producto = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con el id: " + id));
        
        repository.delete(producto);
    }

    public Producto buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se encontró el producto con ID: " + id));
    }

    // NUEVO: Lógica transaccional síncrona para comunicación entre microservicios
    @Transactional
    public StockResponseDTO descontarStock(StockRequestDTO request) {
        // Primera fase: Validar consistencia de existencias de todo el lote
        for (ItemDescuentoDTO item : request.getItems()) {
            Optional<Producto> productoOpt = repository.findById(item.getProductoId());
            
            if (productoOpt.isEmpty()) {
                return new StockResponseDTO(false, "PRODUCTO_NO_ENCONTRADO_ID_" + item.getProductoId());
            }
            
            Producto producto = productoOpt.get();
            if (producto.getStock() < item.getCantidad()) {
                return new StockResponseDTO(false, "STOCK_INSUFICIENTE_PRODUCTO_" + producto.getCodigoProducto());
            }
        }

        // Segunda fase: Deducción física si el lote completo es válido
        for (ItemDescuentoDTO item : request.getItems()) {
            Producto producto = repository.findById(item.getProductoId()).get();
            producto.setStock(producto.getStock() - item.getCantidad());
            repository.save(producto);
        }

        return new StockResponseDTO(true, null);
    }
}