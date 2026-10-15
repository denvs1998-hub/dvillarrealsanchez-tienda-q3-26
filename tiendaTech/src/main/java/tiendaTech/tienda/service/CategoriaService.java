
package tiendaTech.tienda.service;

import tiendaTech.tienda.domain.Categoria;
import tiendaTech.tienda.repository.CategoriaRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    // El repositorio es final para asegurar la inmutabilidad
    private final CategoriaRepository categoriaRepository;

    @Transactional(readOnly = true)
    public List<Categoria> getCategorias(boolean activo) {
        if (activo) { // Solo activos...
            return categoriaRepository.findByActivoTrue();
        }
        return categoriaRepository.findAll();
    }
}
