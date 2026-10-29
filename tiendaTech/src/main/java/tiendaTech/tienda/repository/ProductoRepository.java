
package tiendaTech.tienda.repository;

import tiendaTech.tienda.domain.Producto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    // Se crea una consulta derivada para recuperar solo los productos activos
    public List<Producto> findByActivoTrue();

}

