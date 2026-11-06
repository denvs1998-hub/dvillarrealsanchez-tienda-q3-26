
package tiendaTech.tienda.repository;

import tiendaTech.tienda.domain.Producto;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    // Recupera solamente los productos activos
    public List<Producto> findByActivoTrue();

    // Consulta derivada ordenada por precio ascendente
    public List<Producto> findByPrecioBetweenOrderByPrecioAsc(
            BigDecimal precioInf,
            BigDecimal precioSup);

    // Consulta JPQL ordenada por precio ascendente
    @Query("SELECT p FROM Producto p "
            + "WHERE p.precio BETWEEN :precioInf AND :precioSup "
            + "ORDER BY p.precio ASC")
    public List<Producto> consultaJPQL(
            @Param("precioInf") BigDecimal precioInf,
            @Param("precioSup") BigDecimal precioSup);

    // Consulta SQL nativa ordenada por precio ascendente
    @Query(value = "SELECT * FROM producto "
            + "WHERE precio BETWEEN :precioInf AND :precioSup "
            + "ORDER BY precio ASC",
            nativeQuery = true)
    public List<Producto> consultaSQL(
            @Param("precioInf") BigDecimal precioInf,
            @Param("precioSup") BigDecimal precioSup);

    // Practica #2: consulta ampliada
    // Filtra por rango de precios y categoria
    @Query(value = "SELECT * FROM producto "
            + "WHERE precio BETWEEN :precioInf AND :precioSup "
            + "AND (:idCategoria IS NULL "
            + "OR id_categoria = :idCategoria) "
            + "ORDER BY precio ASC",
            nativeQuery = true)
    public List<Producto> consultaAmpliada(
            @Param("precioInf") BigDecimal precioInf,
            @Param("precioSup") BigDecimal precioSup,
            @Param("idCategoria") Integer idCategoria);
}
