
package tiendaTech.tienda.service;

import tiendaTech.tienda.domain.Producto;
import tiendaTech.tienda.repository.ProductoRepository;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final FirebaseStorageService firebaseStorageService;

    // Obtiene todos los productos o solamente los activos
    @Transactional(readOnly = true)
    public List<Producto> getProductos(boolean activo) {
        if (activo) {
            return productoRepository.findByActivoTrue();
        }
        return productoRepository.findAll();
    }

    // Busca un producto por su identificador
    @Transactional(readOnly = true)
    public Optional<Producto> getProducto(Integer idProducto) {
        return productoRepository.findById(idProducto);
    }

    // Guarda o actualiza un producto
    @Transactional
    public void save(Producto producto, MultipartFile imagenFile) {
        producto = productoRepository.save(producto);

        if (imagenFile != null && !imagenFile.isEmpty()) {
            try {
                String rutaImagen = firebaseStorageService.uploadImage(
                        imagenFile,
                        "producto",
                        producto.getIdProducto()
                );

                producto.setRutaImagen(rutaImagen);
                productoRepository.save(producto);

            } catch (IOException e) {
                log.warn(
                        "No fue posible almacenar la imagen del producto {}",
                        producto.getIdProducto(),
                        e
                );
            }
        }
    }

    // Elimina un producto
    @Transactional
    public void delete(Integer idProducto) {

        if (!productoRepository.existsById(idProducto)) {
            throw new IllegalArgumentException(
                    "El producto con ID " + idProducto + " no existe."
            );
        }

        try {
            productoRepository.deleteById(idProducto);
            productoRepository.flush();

        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException(
                    "No se puede eliminar el producto. Tiene datos asociados.",
                    e
            );
        }
    }

    // Consulta derivada por rango de precios
    @Transactional(readOnly = true)
    public List<Producto> consultaDerivada(
            double precioInf, double precioSup) {

        BigDecimal inferior = BigDecimal.valueOf(precioInf);
        BigDecimal superior = BigDecimal.valueOf(precioSup);

        return productoRepository.findByPrecioBetweenOrderByPrecioAsc(
                inferior, superior);
    }

    // Consulta utilizando JPQL
    @Transactional(readOnly = true)
    public List<Producto> consultaJPQL(
            double precioInf, double precioSup) {

        BigDecimal inferior = BigDecimal.valueOf(precioInf);
        BigDecimal superior = BigDecimal.valueOf(precioSup);

        return productoRepository.consultaJPQL(
                inferior, superior);
    }

    // Consulta utilizando SQL nativo
    @Transactional(readOnly = true)
    public List<Producto> consultaSQL(
            double precioInf, double precioSup) {

        BigDecimal inferior = BigDecimal.valueOf(precioInf);
        BigDecimal superior = BigDecimal.valueOf(precioSup);

        return productoRepository.consultaSQL(
                inferior, superior);
    }

    // Practica #2: consulta ampliada
    // Filtra productos por rango de precios y categoria
    @Transactional(readOnly = true)
    public List<Producto> consultaAmpliada(
            double precioInf,
            double precioSup,
            Integer idCategoria) {

        BigDecimal inferior = BigDecimal.valueOf(precioInf);
        BigDecimal superior = BigDecimal.valueOf(precioSup);

        return productoRepository.consultaAmpliada(
                inferior,
                superior,
                idCategoria);
    }
}
