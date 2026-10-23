
package tiendaTech.tienda.service;

import tiendaTech.tienda.domain.Categoria;
import tiendaTech.tienda.repository.CategoriaRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CategoriaService {

    // El repositorio es final para asegurar la inmutabilidad
    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Transactional(readOnly = true)
    public List<Categoria> getCategorias(boolean activo) {
        if (activo) { // Solo activos...
            return categoriaRepository.findByActivoTrue();
        }
        return categoriaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Categoria> getCategoria(Integer idCategoria) {
        return categoriaRepository.findById(idCategoria);
    }

    @Transactional
    public void save(Categoria categoria, MultipartFile imagenFile) {

        // Conserva la imagen existente al actualizar una categoria
        // cuando no se proporciona una nueva ruta de imagen.
        if (categoria.getIdCategoria() != null) {
            categoriaRepository.findById(categoria.getIdCategoria())
                    .ifPresent(existente -> {
                        if (categoria.getRutaImagen() == null
                                || categoria.getRutaImagen().isBlank()) {
                            categoria.setRutaImagen(existente.getRutaImagen());
                        }
                    });
        }

        categoriaRepository.save(categoria);

        // Pendiente: subir imagen a Firebase Storage.
        // Se implementara cuando Storage este habilitado.
    }

    @Transactional
    public void delete(Integer idCategoria) {

        if (!categoriaRepository.existsById(idCategoria)) {
            throw new IllegalArgumentException(
                    "La categoría con ID " + idCategoria + " no existe.");
        }

        try {
            categoriaRepository.deleteById(idCategoria);
            categoriaRepository.flush();

        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException(
                    "No se puede eliminar la categoria. Tiene datos asociados.", e);
        }
    }
}
